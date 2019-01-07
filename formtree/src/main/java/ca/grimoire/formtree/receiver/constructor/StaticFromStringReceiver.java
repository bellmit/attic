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
 * <code>T fromString(String)</code>. The first value passed to
 * {@link #values(Iterable)} will be used to populate the field; other values
 * will be ignored.
 * 
 * @param <T>
 *            the type of the object to create.
 */
public class StaticFromStringReceiver<T> implements FormReceiver<T> {
	/**
	 * Checkes whether {@link StaticFromStringReceiver} is able to construct
	 * instances of a given class. {@link StaticFromStringReceiver} can
	 * construct any class that has a static <code>fromString</code> method that
	 * accepts a String and returns an instance of the class.
	 * 
	 * @param formClass
	 *            the class to interrogate.
	 * @return true if {@link StaticFromStringReceiver} can create
	 *         <var>formClass</var>.
	 */
	public static boolean accepts(Class<?> formClass) {
		try {
			Method fromString = formClass.getMethod("fromString", String.class);
			if ((fromString.getModifiers() & Modifier.STATIC) == 0)
				return false; // not static, doesn't count.
			if (!formClass.isAssignableFrom(fromString.getReturnType()))
				return false;
			return true;
		} catch (NoSuchMethodException e) {
			return false;
		}
	}

	/**
	 * A type-inferring alternative to calling
	 * {@link #StaticFromStringReceiver(Class)}.
	 * 
	 * @param <T>
	 *            the type of the value to construct.
	 * @param parameterType
	 *            the class of &lt;T&gt;
	 * @return a new {@link StaticFromStringReceiver}.
	 */
	public static <T> StaticFromStringReceiver<T> create(Class<T> parameterType) {
		return new StaticFromStringReceiver<T>(parameterType);
	}

	private final Method fromString;
	private String value = null;

	/**
	 * Creates a StaticFromStringReceiver that creates instances of
	 * <var>valueClass</var>. This class must have a static <code>fromString</code>
	 * method that accepts a String and returns an instance of the class.
	 * 
	 * @param valueClass
	 *            the class of the product to create.
	 */
	public StaticFromStringReceiver(Class<T> valueClass) {
		try {
			this.fromString = valueClass.getMethod("fromString", String.class);
			if ((fromString.getModifiers() & Modifier.STATIC) == 0)
				throw new IllegalArgumentException(String.format(
						"Class %s's fromString method is not static.", valueClass));
			if (!valueClass.isAssignableFrom(fromString.getReturnType()))
				throw new IllegalArgumentException(
						String.format(
								"Class %s's fromString method returns the wrong type (%s).",
								valueClass, fromString.getReturnType()));
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
					String.format(
							"Class %s has no fromString method accepting a single String.",
							valueClass), e);
		}
	}

	@Override
	public T finished() {
		try {
			if (value != null)
				return (T) fromString.invoke(null, value);
			return null;
		} catch (IllegalAccessException e) {
			throw new IllegalStateException(String.format(
					"Unable to create instances of %s.",
					fromString.getDeclaringClass()), e);
		} catch (InvocationTargetException e) {
			throw new IllegalStateException(String.format(
					"Unable to create instances of %s.",
					fromString.getDeclaringClass()), e);
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
