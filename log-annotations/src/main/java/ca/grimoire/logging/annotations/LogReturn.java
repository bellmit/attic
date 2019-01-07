package ca.grimoire.logging.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a log message to emit after a method call successfully returns. The
 * {@link #value()} field will be used as an slf4j logging format string,
 * allowing the return value to be substituted in.
 * <p>
 * By default, messages from this annotation will be logged at the
 * {@link Severity#INFO} severity. The severity can be set to any
 * {@link Severity}.
 * 
 * @see org.slf4j.helpers.MessageFormatter
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogReturn {
	/**
	 * The log message template. The return value will be substituted in as the
	 * only formatting parameter.
	 * 
	 * @return the log message template.
	 */
	public String value();

	/**
	 * The logging severity for the message.
	 * 
	 * @return the log severity for the message.
	 */
	public Severity severity() default Severity.INFO;
}
