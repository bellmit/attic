package ca.grimoire.formtree;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.junit.Test;

public class FormDecoderTest {
    private static <T> FormReceiver<T> prepareReceiver(T result) {
        @SuppressWarnings("unchecked")
        FormReceiver<T> receiver = mock(FormReceiver.class);
        when(receiver.finished()).thenReturn(result);
        return receiver;
    }

    @Test
    public void decodesNestedListForm() {
        FormDecoder decoder = new FormDecoder();

        Object result = new Object();
        FormReceiver<Object> receiver = prepareReceiver(result);

        FormElementReceiver nested = mock(FormElementReceiver.class);
        attachKey(receiver, "nested", nested);

        FormElementReceiver nestedZero = mock(FormElementReceiver.class);
        attachIndex(nested, 0, nestedZero);

        FormElementReceiver nestedOne = mock(FormElementReceiver.class);
        attachIndex(nested, 1, nestedOne);

        FormElementReceiver one = mock(FormElementReceiver.class);
        attachKey(nestedZero, "one", one);

        FormElementReceiver two = mock(FormElementReceiver.class);
        attachKey(nestedOne, "two", two);

        FormAdapter formAdapter = mock(FormAdapter.class);
        when(formAdapter.getFields()).thenReturn(Arrays.asList("nested[0].one",
                "nested[1].two"));
        when(formAdapter.getValues("nested[0].one"))
                .thenReturn(Arrays.asList("value one"));
        when(formAdapter.getValues("nested[1].two"))
                .thenReturn(Arrays.asList("value two"));

        assertSame(result, decoder.decode(formAdapter, receiver));
        verify(one).values(Arrays.asList("value one"));
        verify(two).values(Arrays.asList("value two"));
    }

    @Test
    public void decodesNestedSimpleForm() {
        FormDecoder decoder = new FormDecoder();

        Object result = new Object();
        FormReceiver<Object> receiver = prepareReceiver(result);

        FormElementReceiver nested = mock(FormElementReceiver.class);
        attachKey(receiver, "nested", nested);

        FormElementReceiver one = mock(FormElementReceiver.class);
        attachKey(nested, "one", one);

        FormElementReceiver two = mock(FormElementReceiver.class);
        attachKey(nested, "two", two);

        FormAdapter formAdapter = mock(FormAdapter.class);
        when(formAdapter.getFields()).thenReturn(Arrays.asList("nested.one",
                "nested.two"));
        when(formAdapter.getValues("nested.one"))
                .thenReturn(Arrays.asList("value one"));
        when(formAdapter.getValues("nested.two"))
                .thenReturn(Arrays.asList("value two"));

        assertSame(result, decoder.decode(formAdapter, receiver));
        verify(one).values(Arrays.asList("value one"));
        verify(two).values(Arrays.asList("value two"));
    }

    @Test
    public void decodesLists() {
        FormDecoder decoder = new FormDecoder();

        Object result = new Object();
        FormReceiver<Object> receiver = prepareReceiver(result);

        FormElementReceiver foo = mock(FormElementReceiver.class);
        attachKey(receiver, "foo", foo);

        FormElementReceiver elementZero = mock(FormElementReceiver.class);
        attachIndex(foo, 0, elementZero);

        FormElementReceiver elementOne = mock(FormElementReceiver.class);
        attachIndex(foo, 1, elementOne);

        FormAdapter formAdapter = mock(FormAdapter.class);
        // Intentionally backwards.
        when(formAdapter.getFields()).thenReturn(Arrays.asList("foo[1]",
                "foo[0]"));
        when(formAdapter.getValues("foo[0]"))
                .thenReturn(Arrays.asList("value one"));
        when(formAdapter.getValues("foo[1]"))
                .thenReturn(Arrays.asList("value two"));

        assertSame(result, decoder.decode(formAdapter, receiver));
        verify(elementZero).values(Arrays.asList("value one"));
        verify(elementOne).values(Arrays.asList("value two"));
    }

    @Test
    public void decodesSimpleForm() {
        FormDecoder decoder = new FormDecoder();

        Object result = new Object();
        FormReceiver<Object> receiver = prepareReceiver(result);

        FormElementReceiver one = mock(FormElementReceiver.class);
        attachKey(receiver, "one", one);

        FormElementReceiver two = mock(FormElementReceiver.class);
        attachKey(receiver, "two", two);

        FormAdapter formAdapter = mock(FormAdapter.class);
        when(formAdapter.getFields()).thenReturn(Arrays.asList("one", "two"));
        when(formAdapter.getValues("one"))
                .thenReturn(Arrays.asList("value one"));
        when(formAdapter.getValues("two"))
                .thenReturn(Arrays.asList("value two"));

        assertSame(result, decoder.decode(formAdapter, receiver));
        verify(one).values(Arrays.asList("value one"));
        verify(two).values(Arrays.asList("value two"));
    }

    private void attachKey(FormElementReceiver parent,
            String key,
            FormElementReceiver child) {
        when(parent.key(key)).thenReturn(child);
    }

    private void attachIndex(FormElementReceiver parent,
            int index,
            FormElementReceiver child) {
        when(parent.index(index)).thenReturn(child);
    }
}
