package ca.grimoire.formtree.adapter.servlet;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Vector;

import javax.servlet.ServletRequest;

import org.junit.Test;

public class ServletFormTest {
    @Test
    public void simpleServlet() {
        ServletRequest request = mock(ServletRequest.class);
        Vector<String> parameters = new Vector<String>(Arrays.asList("one",
                "two"));
        when(request.getParameterNames()).thenReturn(parameters.elements());
        when(request.getParameterValues("one"))
                .thenReturn(new String[] { "hi" });
        when(request.getParameterValues("two")).thenReturn(new String[] {
                "two", "values" });

        ServletForm form = new ServletForm(request);

        // Cheating slightly here. We don't actually promise that the return
        // values will be *List*s, just that they'll be iterable, but it's a
        // hugely convenient way to write the test. Blame ArrayList not having
        // an Iterable-taking constructor.
        assertEquals(Arrays.asList("one", "two"), form.getFields());

        assertEquals(Arrays.asList("hi"), form.getValues("one"));
        assertEquals(Arrays.asList("two", "values"), form.getValues("two"));
    }
}
