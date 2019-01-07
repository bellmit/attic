package ca.grimoire.formtree.receiver.constructor;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import ca.grimoire.formtree.FormElementReceiver;
import ca.grimoire.formtree.FormReceiver;
import ca.grimoire.formtree.receiver.IgnoreReceiver;

/**
 * Supporting behaviour allowing for form receivers that produce collections.
 * Manages collating and populating the individual elements of the collection.
 * Subclasses are responsible for turning the elements into a finished
 * collection.
 * 
 * @param <E>
 *            the type of the elements of the resulting collection.
 * @param <T>
 *            the type of the resulting collection. This doesn't have to be a
 *            subclass of {@link Collection} - other collection types, such as
 *            arrays, are possible.
 * @see ArrayReceiver
 * @see ListReceiver
 * @see SetReceiver
 */
public abstract class CollectionReceiver<E, T> implements FormReceiver<T> {

    private static class ConstructorReceiverFactory<E> implements
            ElementReceiverFactory<E> {
        private final Class<E> elementClass;

        public ConstructorReceiverFactory(Class<E> elementClass) {
            this.elementClass = elementClass;
        }

        @Override
        public FormReceiver<E> create() {
            return new ConstructorReceiver<E>(elementClass);
        }
    }

    private static interface ElementReceiverFactory<E> {
        public FormReceiver<E> create();
    }

    private static class StringConstructorReceiverFactory<E> implements
            ElementReceiverFactory<E> {
        private final Class<E> elementClass;

        public StringConstructorReceiverFactory(Class<E> elementClass) {
            this.elementClass = elementClass;
        }

        @Override
        public FormReceiver<E> create() {
            return new StringConstructorReceiver<E>(elementClass);
        }
    }

    /**
     * Given a {@link ParameterizedType}, this method examines its parameter to
     * determine if it's a constructible type (either via
     * {@link ConstructorReceiver} or via {@link StringConstructorReceiver}).
     * 
     * @param collectionType
     *            the ParameterizedType representing the full collection type to
     *            create.
     * @return the {@link Class} of the elements of the collection, or
     *         <code>null</code> if the collection would not contain
     *         constructible elements.
     */
    public static Class<?> acceptableElementClass(ParameterizedType collectionType) {
        try {
            Class<?> listElementClass = extractSingleParameterType(collectionType);
            return acceptableElementClass(listElementClass);
        } catch (ClassCastException cce) {
            return null;
        }
    }

    /**
     * Given a {@link Class} representing the elements of some collection, this
     * method determines whether it counts as constructible (either via
     * {@link ConstructorReceiver} or via {@link StringConstructorReceiver}).
     * 
     * @param elementClass
     *            the element class to examine.
     * @return <var>elementClass</var>, or <code>null</code> if
     *         <var>elementClass</var> is not a constructible class.
     */
    public static Class<?> acceptableElementClass(Class<?> elementClass) {
        if (!ConstructorReceiver.accepts(elementClass)
                && !StringConstructorReceiver.accepts(elementClass))
            return null;

        return elementClass;
    }

    private static Class<?> extractSingleParameterType(ParameterizedType pt) {
        Type[] typeArguments = pt.getActualTypeArguments();
        assert typeArguments.length == 1;
        Class<?> listElementClass = (Class<?>) typeArguments[0];
        return listElementClass;
    }

    /** The {@link Class} of the elements of the collection. */
    protected final Class<E> elementClass;
    private ArrayList<FormReceiver<E>> elementReceivers = reset();
    private final ElementReceiverFactory<E> elementReceiverFactory;

    /**
     * Creates a {@link CollectionReceiver} for some constructible element
     * class.
     * 
     * @param elementClass
     *            the class of the elements of the collection.
     */
    public CollectionReceiver(Class<E> elementClass) {
        this.elementClass = elementClass;
        if (ConstructorReceiver.accepts(elementClass))
            this.elementReceiverFactory = new ConstructorReceiverFactory<E>(
                    elementClass);
        else if (StringConstructorReceiver.accepts(elementClass))
            this.elementReceiverFactory = new StringConstructorReceiverFactory<E>(
                    elementClass);
        else
            throw new IllegalArgumentException(
                    String.format("%s is not an acceptable list element.",
                            elementClass));
    }

    /**
     * Returns an appropriate {@link FormElementReceiver} for the
     * <var>index</var>'th element of the collection, creating it if necessary.
     * This method can create "sparse" collections: for lists and other
     * sequential collection types, any indices not given values will be left
     * null.
     * 
     * @see ca.grimoire.formtree.FormElementReceiver#index(int)
     */
    @Override
    public FormElementReceiver index(int index) {
        growToIndex(index);
        return elementReceivers.get(index);
    }

    /**
     * Ignores keys - the collection itself has no key fields.
     * 
     * @see ca.grimoire.formtree.FormElementReceiver#key(java.lang.String)
     */
    @Override
    public FormElementReceiver key(String field) {
        return IgnoreReceiver.IGNORE;
    }

    /**
     * Applies a value sequence to the collection. This method creates a
     * non-sparse collection: the only elements are exactly those in the
     * <var>values</var> sequence.
     * 
     * @see ca.grimoire.formtree.FormElementReceiver#values(java.lang.Iterable)
     */
    @Override
    public void values(Iterable<String> values) {
        this.elementReceivers = reset();
        for (String value : values) {
            FormReceiver<E> elementReceiver = elementReceiverFactory.create();
            elementReceiver.values(Arrays.asList(value));
            this.elementReceivers.add(elementReceiver);
        }
    }

    /**
     * Converts all of the received form data into the appropriate product type.
     * Subclasses must override {@link #createCollection(List)} to convert the
     * received collection into the target type.
     * 
     * @see ca.grimoire.formtree.FormReceiver#finished()
     */
    @Override
    public T finished() {
        List<E> elements = new ArrayList<E>();
        for (FormReceiver<E> elementReceiver : elementReceivers)
            if (elementReceiver != null)
                elements.add(elementReceiver.finished());

        return createCollection(elements);
    }

    /**
     * Converts the raw sequence of form objects to a collection. The
     * <var>elements</var> list will either be in index order (for collections
     * populated via {@link #index(int)}) or in iteration order (for
     * collections populated via {@link #values(Iterable)}.
     * 
     * @param elements
     *            the collection contents.
     * @return the corresponding collection.
     */
    protected abstract T createCollection(List<E> elements);

    private void growToIndex(int index) {
        elementReceivers.ensureCapacity(index + 1);
        while (elementReceivers.size() <= index)
            elementReceivers.add(elementReceiverFactory.create());
    }

    private ArrayList<FormReceiver<E>> reset() {
        return new ArrayList<FormReceiver<E>>();
    }

}
