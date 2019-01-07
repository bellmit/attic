package ca.grimoire.critical;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Code around the annotation site looks like a bad idea. This annotation is as
 * whiny as possible: it appears in Javadoc output and in the resulting classes'
 * runtime annotations. If you want something for internal use, see
 * {@link FixMe}.
 */
@Documented
@Retention(RetentionPolicy.SOURCE)
public @interface BadIdea {
	public String value() default "";
}
