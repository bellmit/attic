package com.example.onepointeight;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface ReadableCallback {
    public void dataRead(ByteBuffer data) throws IOException;
}
