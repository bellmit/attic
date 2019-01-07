package ca.grimoire.formtree.receiver.constructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ca.grimoire.formtree.FormElementReceiver;
import ca.grimoire.formtree.FormReceiver;
import ca.grimoire.formtree.receiver.IgnoreReceiver;
import ca.grimoire.formtree.receiver.annotations.FormField;

/**
 * A {@link FormReceiver} which populates arbitrary object graphs, rooted at an
 * instance of a specific class. This strategy uses constructor invocation to
 * create each node in the object graph.
 * 
 * @param <T>
 *            the type to return from form parsing.
 */
public class ConstructorReceiver<T> implements FormReceiver<T> {
    /**
     * Checkes whether {@link ConstructorReceiver} is able to construct forms of
     * a given class. {@link ConstructorReceiver} can construct any class that
     * has at least one form constructor (any constructor where all parameters
     * are annotated with {@link FormField}).
     * 
     * @param formClass
     *            the class to interrogate.
     * @return true if {@link ConstructorReceiver} can populate
     *         <var>formClass</var>.
     */
    public static boolean accepts(Class<?> formClass) {
        return findFormConstructor(formClass) != null;
    }

    /**
     * A type-inferring alternative to {@link #ConstructorReceiver(Class)}.
     * 
     * @param <T>
     *            the type of the created form element.
     * @param elementClass
     *            the class of &lt;T&gt;
     * @return a new {@link ConstructorReceiver}.
     */
    public static <T> ConstructorReceiver<T> create(Class<T> elementClass) {
        return new ConstructorReceiver<T>(elementClass);
    }

    private static String fieldNameFromAnnotations(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType() == FormField.class) {
                FormField formFieldAnnotation = (FormField) annotation;
                return formFieldAnnotation.value();
            }
        }
        throw new IllegalArgumentException(
                "Annotation list does not contain FormField annotation.");
    }

    private static <T> Constructor<T> findFormConstructor(Class<T> elementClass) {
        for (Constructor<?> constructor : elementClass.getConstructors()) {
            Annotation[][] parametersAnnotations = constructor
                    .getParameterAnnotations();
            boolean isFormConstructor = true;
            for (Annotation[] parameterAnnotations : parametersAnnotations) {
                boolean hasFormFieldAnnotation = false;
                for (Annotation annotation : parameterAnnotations) {
                    if (annotation.annotationType() == FormField.class)
                        hasFormFieldAnnotation = true;
                }
                isFormConstructor &= hasFormFieldAnnotation;
            }
            // Zero-argument constructors are not form constructors. They just
            // happen to have... get your negative logic hats on, folks ...no
            // non-annotated parameters.
            isFormConstructor &= parametersAnnotations.length > 0;

            if (isFormConstructor) {
                // Given the provenance of constructor, the type parameter is
                // correct. However, getConstructors() strips it because arrays
                // of generic types are painful.
                @SuppressWarnings("unchecked")
                Constructor<T> typeCorrectedConstructor = (Constructor<T>) constructor;
                return typeCorrectedConstructor;
            }
        }
        return null;
    }

    private final Constructor<T> constructor;
    private final LinkedHashMap<String, FormReceiver<?>> receivers = new LinkedHashMap<String, FormReceiver<?>>();

    /**
     * Prepares to construct an instance of <var>formClass</var> using form
     * data. The class must have at least one form constructor (any constructor
     * where all parameters are annotated with {@link FormField}).
     * <p>
     * If the class has multiple form constructors, the constructor used is
     * unspecified.
     * <p>
     * Any fields in the form that cannot be mapped to the chosen form
     * constructor will be ignored.
     * 
     * @param formClass
     *            the class to construct.
     * @throws IllegalArgumentException
     *             if the class has no form constructors.
     */
    public ConstructorReceiver(Class<T> formClass) {
        // Internally: we identify which constructor we're going to use, and
        // then we prepare receivers for each constructor. The result is a tree
        // of receivers that match the expected form fields and types. Anything
        // that falls outside of that tree will be silently ignored.
        this.constructor = findFormConstructor(formClass);
        if (this.constructor == null)
            throw new IllegalArgumentException(
                    String.format("The class %s has no suitable form constructor."));
        prepareReceivers();
    }

    /**
     * Condense the collected form data from this receiver and all receivers
     * created from it into an instance of &lt;T&gt;.
     * 
     * @see ca.grimoire.formtree.FormReceiver#finished()
     */
    @Override
    public T finished() {
        List<Object> parameters = new ArrayList<Object>();
        for (Map.Entry<String, FormReceiver<?>> receiver : receivers.entrySet()) {
            parameters.add(receiver.getValue().finished());
        }

        try {
            return constructor.newInstance(parameters.toArray());
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

    /**
     * Returns a {@link FormElementReceiver} appropriate for the named field.
     * Fields that don't map to constructor parameters will be silently ignored.
     * 
     * @see ca.grimoire.formtree.FormElementReceiver#key(java.lang.String)
     */
    @Override
    public FormElementReceiver key(String field) {
        FormReceiver<?> receiver = receivers.get(field);
        if (receiver != null)
            return receiver;
        return IgnoreReceiver.IGNORE;
    }

    /**
     * Ignores index access entirely.
     * 
     * @see ca.grimoire.formtree.FormElementReceiver#index(int)
     */
    @Override
    public FormElementReceiver index(int index) {
        return IgnoreReceiver.IGNORE;
    }

    /**
     * Ignores attempts to assign immediate values to a constructor.
     * 
     * @see ca.grimoire.formtree.FormElementReceiver#values(java.lang.Iterable)
     */
    @Override
    public void values(Iterable<String> values) {
        // Ignore immediate values.
    }

    private void prepareReceivers() {
        Annotation[][] parameterAnnotationLists = constructor
                .getParameterAnnotations();
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Type[] parameterGenericTypes = constructor.getGenericParameterTypes();

        assert parameterTypes.length == parameterAnnotationLists.length;
        assert parameterTypes.length == parameterGenericTypes.length;

        for (int i = 0; i < parameterTypes.length; ++i) {
            String fieldName = fieldNameFromAnnotations(parameterAnnotationLists[i]);
            Class<?> parameterType = parameterTypes[i];
            Type parameterGenericType = parameterGenericTypes[i];

            FormReceiver<?> receiver = prepareConstructorReceiver(parameterType);
            if (receiver == null)
                receiver = prepareListReceiver(parameterGenericType);
            if (receiver == null)
                receiver = prepareSetReceiver(parameterGenericType);
            if (receiver == null)
                receiver = prepareArrayReceiver(parameterType);
            if (receiver == null)
                receiver = prepareStringReceiver(parameterType);
            if (receiver == null)
            	receiver = prepareStaticValueOfReceiver(parameterType);
            if (receiver == null)
            	receiver = prepareStaticFromStringReceiver(parameterType);
            if (receiver == null)
                throw new IllegalArgumentException(
                        String.format("Unable to determine form data bindings for all parameters of %s (failed at parameter %d).",
                                constructor,
                                i));

            receivers.put(fieldName, receiver);
        }
    }

    private ArrayReceiver<?> prepareArrayReceiver(Class<?> parameterType) {
        Class<?> elementClass = ArrayReceiver
                .acceptableArrayClass(parameterType);
        if (elementClass != null)
            return ArrayReceiver.create(elementClass);
        return null;
    }

    private ConstructorReceiver<?> prepareConstructorReceiver(Class<?> parameterType) {
        if (accepts(parameterType))
            return create(parameterType);
        return null;
    }

    private ListReceiver<?> prepareListReceiver(Type parameterGenericType) {
        Class<?> elementClass = ListReceiver
                .acceptableListType(parameterGenericType);
        if (elementClass != null)
            return ListReceiver.create(elementClass);
        return null;
    }

    private SetReceiver<?> prepareSetReceiver(Type parameterGenericType) {
        Class<?> elementClass = SetReceiver
                .acceptableSetType(parameterGenericType);
        if (elementClass != null)
            return SetReceiver.create(elementClass);
        return null;
    }

    private StringConstructorReceiver<?> prepareStringReceiver(Class<?> parameterType) {
        if (StringConstructorReceiver.accepts(parameterType))
            return StringConstructorReceiver.create(parameterType);
        return null;
    }

    private StaticValueOfReceiver<?> prepareStaticValueOfReceiver(Class<?> parameterType) {
        if (StaticValueOfReceiver.accepts(parameterType))
            return StaticValueOfReceiver.create(parameterType);
        return null;
    }

    private StaticFromStringReceiver<?> prepareStaticFromStringReceiver(Class<?> parameterType) {
        if (StaticFromStringReceiver.accepts(parameterType))
            return StaticFromStringReceiver.create(parameterType);
        return null;
    }
}
