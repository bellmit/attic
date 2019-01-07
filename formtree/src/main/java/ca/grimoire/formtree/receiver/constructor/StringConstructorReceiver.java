package ca.grimoire.formtree.receiver.constructor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

import ca.grimoire.formtree.FormElementReceiver;
import ca.grimoire.formtree.FormReceiver;
import ca.grimoire.formtree.receiver.IgnoreReceiver;

/**
 * A form receiver that uses constructors with the signature (String). The first
 * value passed to {@link #values(Iterable)} will be used to populate the field;
 * other values will be ignored.
 * 
 * @param <T>
 *            the type of the object to create.
 */
public class StringConstructorReceiver<T> implements FormReceiver<T> {
    /**
     * Checkes whether {@link StringConstructorReceiver} is able to construct
     * instances of a given class. {@link StringConstructorReceiver} can
     * construct any class that has a constructor with the signature (String).
     * 
     * @param formClass
     *            the class to interrogate.
     * @return true if {@link StringConstructorReceiver} can create
     *         <var>formClass</var>.
     */
    public static boolean accepts(Class<?> formClass) {
        try {
            formClass.getConstructor(String.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }

    /**
     * A type-inferring alternative to calling
     * {@link #StringConstructorReceiver(Class)}.
     * 
     * @param <T>
     *            the type of the value to construct.
     * @param parameterType
     *            the class of &lt;T&gt;
     * @return a new {@link StringConstructorReceiver}.
     */
    public static <T> StringConstructorReceiver<T> create(Class<T> parameterType) {
        return new StringConstructorReceiver<T>(parameterType);
    }

    private final Constructor<T> constructor;
    private String value = null;

    /**
     * Creates a StringConstructorReceiver that creates instances of
     * <var>valueClass</var>. This class must have a constructor whose signature
     * is (String).
     * 
     * @param valueClass
     *            the class of the product to create.
     */
    public StringConstructorReceiver(Class<T> valueClass) {
        try {
            this.constructor = valueClass.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException(
                    String.format("Class %s has no constructor accepting a single String.",
                            valueClass), e);
        }
    }

    @Override
    public T finished() {
        try {
            if (value != null)
                return constructor.newInstance(value);
            return null;
        } catch (InstantiationException e) {
            throw new IllegalStateException(
                    String.format("Unable to create instances of %s.",
                            constructor.getDeclaringClass()), e);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(
                    String.format("Unable to create instances of %s.",
                            constructor.getDeclaringClass()), e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException(
                    String.format("Unable to create instances of %s.",
                            constructor.getDeclaringClass()), e);
        }
    }

    @Override
    public FormElementReceiver key(String field) {
        return IgnoreReceiver.IGNORE;
    }

    @Override
    public FormElementReceiver index(int index) {
        return IgnoreReceiver.IGNORE;
    }

    @Override
    public void values(Iterable<String> values) {
        Iterator<String> iterator = values.iterator();
        if (iterator.hasNext())
            this.value = iterator.next();
    }
}
