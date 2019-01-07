package ca.grimoire.formtree.receiver.constructor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Iterator;

import ca.grimoire.formtree.FormElementReceiver;
import ca.grimoire.formtree.FormReceiver;
import ca.grimoire.formtree.receiver.IgnoreReceiver;

/**
 * A form receiver that uses static methods with the signature
 * <code>T valueOf(String)</code>. The first value passed to
 * {@link #values(Iterable)} will be used to populate the field; other values
 * will be ignored.
 * 
 * @param <T>
 *            the type of the object to create.
 */
public class StaticValueOfReceiver<T> implements FormReceiver<T> {
	/**
	 * Checkes whether {@link StaticValueOfReceiver} is able to construct
	 * instances of a given class. {@link StaticValueOfReceiver} can construct
	 * any class that has a static <code>valueOf</code> method that accepts a
	 * String and returns an instance of the class.
	 * 
	 * @param formClass
	 *            the class to interrogate.
	 * @return true if {@link StaticValueOfReceiver} can create
	 *         <var>formClass</var>.
	 */
	public static boolean accepts(Class<?> formClass) {
		try {
			Method valueOf = formClass.getMethod("valueOf", String.class);
			if ((valueOf.getModifiers() & Modifier.STATIC) == 0)
				return false; // not static, doesn't count.
			if (!formClass.isAssignableFrom(valueOf.getReturnType()))
				return false;
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	/**
	 * A type-inferring alternative to calling
	 * {@link #StaticValueOfReceiver(Class)}.
	 * 
	 * @param <T>
	 *            the type of the value to construct.
	 * @param parameterType
	 *            the class of &lt;T&gt;
	 * @return a new {@link StaticValueOfReceiver}.
	 */
	public static <T> StaticValueOfReceiver<T> create(Class<T> parameterType) {
		return new StaticValueOfReceiver<T>(parameterType);
	}

	private final Method valueOf;
	private String value = null;

	/**
	 * Creates a StaticValueOfReceiver that creates instances of
	 * <var>valueClass</var>. This class must have a static <code>valueOf</code>
	 * method that accepts a String and returns an instance of the class.
	 * 
	 * @param valueClass
	 *            the class of the product to create.
	 */
	public StaticValueOfReceiver(Class<T> valueClass) {
		try {
			this.valueOf = valueClass.getMethod("valueOf", String.class);
			if ((valueOf.getModifiers() & Modifier.STATIC) == 0)
				throw new IllegalArgumentException(String.format(
						"Class %s's valueOf method is not static.", valueClass));
			if (!valueClass.isAssignableFrom(valueOf.getReturnType()))
				throw new IllegalArgumentException(
						String.format(
								"Class %s's valueOf method returns the wrong type (%s).",
								valueClass, valueOf.getReturnType()));
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
					String.format(
							"Class %s has no valueOf method accepting a single String.",
							valueClass), e);
		}
	}

	@Override
	public T finished() {
		try {
			if (value != null)
				return (T) valueOf.invoke(null, value);
			return null;
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(String.format(
					"Unable to create instances of %s.",
					valueOf.getDeclaringClass()), e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(String.format(
					"Unable to create instances of %s.",
					valueOf.getDeclaringClass()), e);
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
