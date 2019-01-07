package ca.grimoire.formtree.receiver.constructor;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ca.grimoire.formtree.receiver.constructor.ConstructorReceiverTest.SimpleFields;

public class ListReceiverTest {
    @Test
    public void listOfStringsAsValues() {
        ListReceiver<String> r = ListReceiver
                .create(String.class);

        r.values(Arrays.asList("one", "two"));

        assertEquals(Arrays.asList("one", "two"), r.finished());
    }

    @Test
    public void listOfSimpleObjectsAsValues() {
        ListReceiver<SimpleFields> r = ListReceiver
                .create(SimpleFields.class);

        r.index(0).key("x")
                .values(Arrays.asList("x value zero"));
        r.index(0).key("y")
                .values(Arrays.asList("y value zero"));
        r.index(1).key("x")
                .values(Arrays.asList("x value one"));
        r.index(1).key("y")
                .values(Arrays.asList("y value one"));

        List<SimpleFields> form = r.finished();
        assertEquals(2, form.size());

        SimpleFields zero = form.get(0);
        assertEquals("x value zero", zero.x);
        assertEquals("y value zero", zero.y);

        SimpleFields one = form.get(1);
        assertEquals("x value one", one.x);
        assertEquals("y value one", one.y);
    }
}
