package ca.grimoire.formtree.receiver.constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

public class StringConstructorReceiverTest {
    @Test
    public void capturesStringValues() {
        StringConstructorReceiver<String> receiver = StringConstructorReceiver
                .create(String.class);

        receiver.values(Arrays.asList("hello, world!"));
        assertEquals("hello, world!", receiver.finished());
    }

    @Test
    public void ignoresLaterStringValues() {
        StringConstructorReceiver<String> receiver = StringConstructorReceiver
                .create(String.class);

        receiver.values(Arrays.asList("hello, world!",
                "I am a second value to be ignored."));
        assertEquals("hello, world!", receiver.finished());
    }

    @Test
    public void returnsNullIfNotSet() {
        StringConstructorReceiver<String> receiver = StringConstructorReceiver
                .create(String.class);

        assertNull(receiver.finished());
    }
}
