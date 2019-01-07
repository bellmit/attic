package ca.grimoire.formtree.receiver.constructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Receives form elements into an {@link List} of some constructible class. The
 * array may be dense (if handled via the {@link #values(Iterable)} method) or
 * sparse (if handled via the {@link #key(String)} method).
 * 
 * @param <T>
 *            the class of the array elements.
 * @see ConstructorReceiver
 */
public class ListReceiver<T> extends CollectionReceiver<T, List<T>> {

    /**
     * Determines if some {@link Type} represents a list of constructible
     * elements.
     * 
     * @param type
     *            the {@link Type} to examine.
     * @return the {@link Class} of the elements of the list, or
     *         <code>null</code> if <var>type</var> is not the type of a list of
     *         constructible types.
     */
    public static Class<?> acceptableListType(Type type) {
        try {
            ParameterizedType listType = (ParameterizedType) type;
            if (listType.getRawType() != List.class)
                return null;

            return acceptableElementClass(listType);
        } catch (ClassCastException cce) {
            return null;
        }
    }

    /**
     * A type-inferring alternative to {@link #ListReceiver(Class)}.
     * 
     * @param <T>
     *            the class of the array elements.
     * @param elementClass
     *            the {@link Class} of &lt;T&gt;
     * @return a new {@link ListReceiver}.
     */
    public static <T> ListReceiver<T> create(Class<T> elementClass) {
        return new ListReceiver<T>(elementClass);
    }

    /**
     * Creates a receiver for a given element class.
     * 
     * @param elementClass
     *            the {@link Class} of &lt;T&gt;
     */
    public ListReceiver(Class<T> elementClass) {
        super(elementClass);
    }

    /**
     * Returns <var>elements</var> unchanged - it's already the right type.
     * 
     * @see ca.grimoire.formtree.receiver.constructor.CollectionReceiver#createCollection(java.util.List)
     */
    @Override
    protected List<T> createCollection(List<T> elements) {
        return elements;
    }
}
