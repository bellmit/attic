package com.example.onepointeight;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Invoked when a new connection is available on a channel.
 */
public interface AcceptCallback {
    public void accepted(SocketChannel channel) throws IOException;
}
