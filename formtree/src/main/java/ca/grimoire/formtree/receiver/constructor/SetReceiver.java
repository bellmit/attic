package ca.grimoire.formtree.receiver.constructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Receives constructible elements into a {@link Set}.
 * 
 * @param <T>
 *            the type of the elements in the set.
 */
public class SetReceiver<T> extends CollectionReceiver<T, Set<T>> {

    /**
     * Determines whether some {@link Type} represents a set of constructible
     * elements.
     * 
     * @param type
     *            the {@link Type} to examine.
     * @return the {@link Class} of the elements of the set, or
     *         <code>null</code> if <var>type</var> does not represent a set of
     *         constructible elements.
     */
    public static Class<?> acceptableSetType(Type type) {
        try {
            ParameterizedType listType = (ParameterizedType) type;
            if (listType.getRawType() != Set.class)
                return null;

            return acceptableElementClass(listType);
        } catch (ClassCastException cce) {
            return null;
        }
    }

    /**
     * A type-inferring alternative to {@link #SetReceiver(Class)}.
     * 
     * @param <T>
     *            the type of the elements in the set.
     * @param elementClass
     *            the {@link Class} of &lt;T&gt;
     * @return a new {@link SetReceiver}.
     */
    public static <T> SetReceiver<T> create(Class<T> elementClass) {
        return new SetReceiver<T>(elementClass);
    }

    /**
     * Creates a new receiver for a set of some constructible element type.
     * 
     * @param elementClass
     *            the {@link Class} of &lt;T&gt;.
     */
    public SetReceiver(Class<T> elementClass) {
        super(elementClass);
    }

    /**
     * Converts the passed list of elements into a {@link Set}.
     * 
     * @see ca.grimoire.formtree.receiver.constructor.CollectionReceiver#createCollection(java.util.List)
     */
    @Override
    protected Set<T> createCollection(List<T> elements) {
        return new HashSet<T>(elements);
    }

}
