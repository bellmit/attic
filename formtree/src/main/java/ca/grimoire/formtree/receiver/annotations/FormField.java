package ca.grimoire.formtree.receiver.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The annotated symbol will be used to receive the form field whose name is
 * given by this annotation's value.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.PARAMETER, ElementType.METHOD })
public @interface FormField {
    /**
     * @return The name of the form field to assign to this symbol.
     */
    public String value();
}
