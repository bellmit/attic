package io.github.unacceptable.alias;

import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class UUIDGeneratorTest {

    @Test
    public void defaultGenerator() {
        UUID uuid = UUIDGenerator.defaultGenerate("foo");
        assertNotNull(uuid);
    }

    @Test
    public void generator() {
        UUID uuid = new UUIDGenerator().generate("foo");
        assertNotNull(uuid);
    }

    @Test
    public void absent() {
        UUID uuid = new UUIDGenerator().generate("ABSENT");
        assertNull(uuid);
    }

}