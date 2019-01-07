package ca.grimoire.formtree.receiver.constructor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import ca.grimoire.formtree.receiver.annotations.FormField;

public class SetReceiverTest {
    public static class Form {
        public final String name;

        public Form(@FormField("name") String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object other) {
            if (other == null)
                return false;
            if (other.getClass() != getClass())
                return false;

            Form o = (Form) other;
            return (name == null && o.name == null)
                    || (name != null && name.equals(o.name));
        }

        @Override
        public int hashCode() {
            return name == null ? 0 : name.hashCode();
        }
    }

    @Test
    public void setOfStringsAsValues() {
        SetReceiver<String> r = SetReceiver.create(String.class);

        r.values(Arrays.asList("one", "two"));

        assertEquals(new HashSet<String>(Arrays.asList("one", "two")),
                r.finished());
    }

    @Test
    public void listOfSimpleObjectsAsValues() {
        SetReceiver<Form> r = SetReceiver.create(Form.class);

        r.index(0).key("name").values(Arrays.asList("alice"));
        r.index(1).key("name").values(Arrays.asList("bob"));

        Set<Form> form = r.finished();
        assertEquals(2, form.size());
        assertTrue(form.contains(new Form("alice")));
        assertTrue(form.contains(new Form("bob")));
    }
}
