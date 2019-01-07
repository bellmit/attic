package ca.grimoire.formtree.receiver.constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

public class StaticValueOfReceiverTest {
	private enum Example {
		ONE, TWO;
	}

	@Test
	public void capturesStringValues() {
		StaticValueOfReceiver<Example> receiver = StaticValueOfReceiver
				.create(Example.class);

		receiver.values(Arrays.asList("ONE"));
		assertEquals(Example.ONE, receiver.finished());
	}

	@Test
	public void ignoresLaterStringValues() {
		StaticValueOfReceiver<Example> receiver = StaticValueOfReceiver
				.create(Example.class);

		receiver.values(Arrays.asList("ONE", "TWO"));
		assertEquals(Example.ONE, receiver.finished());
	}

	@Test
	public void returnsNullIfNotSet() {
		StringConstructorReceiver<String> receiver = StringConstructorReceiver
				.create(String.class);

		assertNull(receiver.finished());
	}
}
