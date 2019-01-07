package ca.grimoire.logging.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a log message to emit after a method call raises an exception. The
 * {@link #value()} field will be passed to the underlying log framework as-is,
 * along with the exception.
 * <p>
 * By default, messages from this annotation will be logged at the
 * {@link Severity#ERROR} severity. The severity can be set to any
 * {@link Severity}.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogException {
	/**
	 * The log message. No argument substitution will be applied to this
	 * message.
	 * 
	 * @return the log message.
	 */
	public String value();

	/**
	 * The logging severity for the message.
	 * 
	 * @return the log severity for the message.
	 */
	public Severity severity() default Severity.ERROR;
}
