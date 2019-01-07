package ca.grimoire.formtree.receiver.constructor;

import java.lang.reflect.Array;
import java.util.List;

/**
 * Receives form elements into an array of some constructible class. The array
 * may be dense (if handled via the {@link #values(Iterable)} method) or sparse
 * (if handled via the {@link #key(String)} method).
 * 
 * @param <T>
 *            the class of the array elements.
 * @see ConstructorReceiver
 */
public class ArrayReceiver<T> extends CollectionReceiver<T, T[]> {
    /**
     * Determines if a {@link Class} representing an array type can be processed
     * using this receiver. The elements of the array must be constructible
     * (either via {@link ConstructorReceiver} or via
     * {@link StringConstructorReceiver}).
     * 
     * @param type
     *            the class to test.
     * @return the class of the array elements being held, or <code>null</code>
     *         if <var>type</var> cannot be used to hold form data.
     */
    public static Class<?> acceptableArrayClass(Class<?> type) {
        if (!type.isArray())
            return null;

        return acceptableElementClass(type.getComponentType());
    }

    /**
     * A type-inferring alternative to {@link #ArrayReceiver(Class)}.
     * 
     * @param <T>
     *            the class of the array elements.
     * @param elementClass
     *            the {@link Class} of &lt;T&gt;
     * @return a new {@link ArrayReceiver}.
     */
    public static <T> ArrayReceiver<T> create(Class<T> elementClass) {
        return new ArrayReceiver<T>(elementClass);
    }

    /**
     * Creates an {@link ArrayReceiver} that will create arrays of
     * <var>elementClass</var>.
     * 
     * @param elementClass
     *            the {@link Class} of &lt;T&gt;
     */
    public ArrayReceiver(Class<T> elementClass) {
        super(elementClass);
    }

    /**
     * Builds an array out of the passed element list. The array will have
     * component type &lt;T&gt;.
     * 
     * @see ca.grimoire.formtree.receiver.constructor.CollectionReceiver#createCollection(java.util.List)
     */
    @Override
    protected T[] createCollection(List<T> elements) {
        // elementClass is the class of T.
        T[] array = (T[]) Array.newInstance(elementClass, elements.size());
        return elements.toArray(array);
    }
}
