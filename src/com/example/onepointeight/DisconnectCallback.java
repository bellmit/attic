package com.example.onepointeight;

import java.io.IOException;
import java.nio.channels.SocketChannel;

public interface DisconnectCallback {
    public void disconnected(SocketChannel channel) throws IOException;
}
