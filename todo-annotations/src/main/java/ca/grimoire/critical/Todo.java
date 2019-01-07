package ca.grimoire.critical;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Code around the annotation site has work that needs to be done.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface Todo {
	public String value() default "";
}
