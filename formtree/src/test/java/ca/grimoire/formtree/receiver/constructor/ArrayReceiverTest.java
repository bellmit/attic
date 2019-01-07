package ca.grimoire.formtree.receiver.constructor;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.Test;

import ca.grimoire.formtree.receiver.constructor.SetReceiverTest.Form;

public class ArrayReceiverTest {
    @Test
    public void listOfStringsAsValues() {
        ArrayReceiver<String> r = ArrayReceiver.create(String.class);

        r.values(Arrays.asList("one", "two"));

        assertArrayEquals(new String[] { "one", "two" }, r.finished());
    }

    @Test
    public void listOfSimpleObjectsAsValues() {
        ArrayReceiver<Form> r = ArrayReceiver.create(Form.class);

        r.index(0).key("name").values(Arrays.asList("alice"));
        r.index(1).key("name").values(Arrays.asList("bob"));

        assertArrayEquals(new Form[] { new Form("alice"), new Form("bob") },
                r.finished());
    }
}
