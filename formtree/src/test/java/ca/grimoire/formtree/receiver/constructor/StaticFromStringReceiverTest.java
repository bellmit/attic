package ca.grimoire.formtree.receiver.constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Arrays;

import org.junit.Test;

public class StaticFromStringReceiverTest {
	private static class Example {
		public final String value;

		private Example(String value) {
			this.value = value;
		}

		@SuppressWarnings("unused")
		public static Example fromString(String value) {
			return new Example(value);
		}
	}

	@Test
	public void capturesStringValues() {
		StaticFromStringReceiver<Example> receiver = StaticFromStringReceiver
				.create(Example.class);

		receiver.values(Arrays.asList("ONE"));
		assertEquals("ONE", receiver.finished().value);
	}

	@Test
	public void ignoresLaterStringValues() {
		StaticFromStringReceiver<Example> receiver = StaticFromStringReceiver
				.create(Example.class);

		receiver.values(Arrays.asList("ONE", "TWO"));
		assertEquals("ONE", receiver.finished().value);
	}

	@Test
	public void returnsNullIfNotSet() {
		StaticFromStringReceiver<Example> receiver = StaticFromStringReceiver
				.create(Example.class);

		assertNull(receiver.finished());
	}
}
