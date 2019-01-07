package ca.grimoire.formtree.adapter.jaxrs;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashSet;

import javax.ws.rs.core.MultivaluedMap;

import org.junit.Test;

public class MultivaluedMapFormTest {
    @Test
    public void simpleMap() {
        MultivaluedMap<String, String> form = mock(MultivaluedMap.class);
        when(form.keySet()).thenReturn(new HashSet<String>(Arrays.asList("one",
                "two")));
        when(form.get("one")).thenReturn(Arrays.asList("hi"));
        when(form.get("two")).thenReturn(Arrays.asList("two", "values"));

        MultivaluedMapForm adapter = new MultivaluedMapForm(form);

        // More cheating (as with ServletForm). We don't promise to return sets,
        // but it again makes the tests way more convenient to write.
        assertEquals(new HashSet<String>(Arrays.asList("one", "two")),
                adapter.getFields());
        assertEquals(Arrays.asList("hi"), adapter.getValues("one"));
        assertEquals(Arrays.asList("two", "values"), adapter.getValues("two"));

    }
}
