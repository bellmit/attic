package ca.grimoire.critical;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Code around the annotation site needs fixing.
 */
@Retention(RetentionPolicy.SOURCE)
public @interface FixMe {
	public String value() default "";
}
