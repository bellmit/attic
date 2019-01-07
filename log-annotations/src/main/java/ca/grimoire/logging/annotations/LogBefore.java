package ca.grimoire.logging.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates a log message to emit before a method call. The {@link #value()}
 * field will be used as an slf4j logging format string, allowing method
 * parameters to be substituted in.
 * <p>
 * By default, messages from this annotation will be logged at the
 * {@link Severity#INFO} severity. The severity can be set to any
 * {@link Severity}.
 * 
 * @see org.slf4j.helpers.MessageFormatter
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogBefore {
	/**
	 * The log message template. The method call's arguments will be substituted
	 * into the template in order.
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
