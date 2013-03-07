package com.example.onepointeight;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Reactor {
    private final Map<SelectableChannel, SelectionKey> keys = new HashMap<>();
    private final Map<SelectionKey, AcceptCallback> listeners = new HashMap<>();
    private final Map<SelectionKey, ReadableCallback> readers = new HashMap<>();
    private final Map<SelectionKey, ByteBuffer> pendingOutput = new HashMap<>();
    private final Map<SelectionKey, DisconnectCallback> disconnectors = new HashMap<>();
    private final Selector selector;

    public static void run(ReactorCallback program) throws IOException {
        Reactor r = new Reactor();
        r.start(program);
    }

    public void write(SocketChannel client, ByteBuffer data) throws IOException {
        // Buffering when we don't need to is dumb. On the other hand, this simplifies the control flow considerably.
        SelectionKey key = keys.get(client);
        addInterest(key, SelectionKey.OP_WRITE);
        enqueueWrite(key, data);
    }

    public void read(SocketChannel client, ReadableCallback reader) {
        SelectionKey key = keys.get(client);
        addInterest(key, SelectionKey.OP_READ);
        readers.put(key, reader);
    }

    public void listen(int port, AcceptCallback acceptor) throws IOException {
        ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.bind(new InetSocketAddress(port));
        SelectionKey listenKey = server.register(selector, SelectionKey.OP_ACCEPT);
        listeners.put(listenKey, acceptor);
        keys.put(server, listenKey);
    }

    public void whenClosed(SocketChannel channel, DisconnectCallback callback) {
        SelectionKey key = keys.get(channel);
        disconnectors.put(key, callback);
    }

    public void close(SocketChannel channel) throws IOException {
        SelectionKey key = keys.get(channel);
        if (key != null) {
            DisconnectCallback callback = disconnectors.get(key);
            if (callback != null)
                callback.disconnected(channel);

            listeners.remove(key);
            readers.remove(key);
            pendingOutput.remove(key);
            disconnectors.remove(key);
            keys.remove(channel);
            key.cancel();
            channel.close();
        } else
            throw new IllegalArgumentException();
    }

    private Reactor() throws IOException {
        selector = Selector.open();
    }

    private void start(ReactorCallback bootstrap) throws IOException {
        bootstrap.invoke(this);

        while (!keys.isEmpty())
            react();
    }

    private void react() throws IOException {
        selector.select();
        for (Iterator<SelectionKey> keysIter = selector.selectedKeys().iterator();
             keysIter.hasNext(); ) {
            SelectionKey key = keysIter.next();
            keysIter.remove();
            if (maybeWrite(key))
                if (maybeAccept(key))
                    maybeRead(key);
        }
    }

    private void addInterest(SelectionKey key, int interest) {
        int interests = key.interestOps();
        key.interestOps(interests | interest);
    }

    private void removeInterest(SelectionKey key, int interest) {
        int interests = key.interestOps();
        key.interestOps(interests & ~interest);
    }

    private void enqueueWrite(SelectionKey key, ByteBuffer data) {
        ByteBuffer outputBuffer = prepareOutputBuffer(key);
        outputBuffer.put(data);
        outputBuffer.flip();
    }

    private ByteBuffer prepareOutputBuffer(SelectionKey key) {
        if (!pendingOutput.containsKey(key)) {
            ByteBuffer newBuffer = ByteBuffer.allocate(131072);
            newBuffer.limit(0);
            pendingOutput.put(key, newBuffer);
        }

        ByteBuffer outputBuffer = pendingOutput.get(key);
        outputBuffer.position(outputBuffer.limit());
        outputBuffer.limit(outputBuffer.capacity());
        return outputBuffer;
    }

    private boolean maybeWrite(SelectionKey key) throws IOException {
        ByteBuffer outputBuffer = pendingOutput.get(key);
        if (key.isWritable() && outputBuffer != null && outputBuffer.hasRemaining()) {
            SocketChannel channel = (SocketChannel) key.channel();
            channel.write(outputBuffer);
            int length = outputBuffer.remaining();
            outputBuffer.compact();
            outputBuffer.limit(length); // WHO EVER WANTED TO NEED THIS.
            if (!outputBuffer.hasRemaining())
                removeInterest(key, SelectionKey.OP_WRITE);
        }
        return true;
    }

    private boolean maybeRead(SelectionKey key) throws IOException {
        ReadableCallback readableCallback = readers.get(key);
        if (key.isReadable() && readableCallback != null) {
            SocketChannel channel = (SocketChannel) key.channel();
            ByteBuffer readBuffer = ByteBuffer.allocate(4096);
            int bytesRead = channel.read(readBuffer);
            if (bytesRead >= 0) {
                readableCallback.dataRead(readBuffer);
            } else {
                close(channel);
                return false;
            }
        }
        return true;
    }

    private boolean maybeAccept(SelectionKey key) throws IOException {
        AcceptCallback acceptCallback = listeners.get(key);
        if (key.isAcceptable() && acceptCallback != null) {
            SocketChannel accepted = ((ServerSocketChannel) key.channel()).accept();
            accepted.configureBlocking(false);
            SelectionKey acceptedKey = accepted.register(selector, 0);
            keys.put(accepted, acceptedKey);

            acceptCallback.accepted(accepted);
        }
        return true;
    }
}
