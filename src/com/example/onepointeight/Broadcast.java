package com.example.onepointeight;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

public class Broadcast {
    public static void main(String[] args) throws IOException {
        List<SocketChannel> clients = new ArrayList<>();
        Reactor.run(reactor ->
                reactor.listen(3000, client -> {
                    clients.add(client);
                    reactor.whenClosed(client, (channel) -> {
                        clients.remove(client);
                    });
                    reactor.read(client, data -> {
                        data.flip();
                        int position = data.position();
                        int limit = data.limit();
                        for (SocketChannel peer : clients) {
                            data.position(position);
                            data.limit(limit);
                            reactor.write(peer, data);
                        }
                    });
                })
        );
    }

}
