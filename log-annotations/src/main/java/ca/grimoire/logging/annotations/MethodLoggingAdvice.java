package ca.grimoire.logging.annotations;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intercepts method calls and logs the approprate messages for each of the
 * logging annotations in this package.
 * 
 * @see LogBefore
 * @see LogReturn
 * @see LogException
 */
public class MethodLoggingAdvice {
	/**
	 * Emits the log message from a {@link LogBefore} annotation, using the
	 * method call's parameter list as the formatting parameters.
	 * 
	 * @param call
	 *            the method call being intercepted.
	 * @param logAnnotation
	 *            the log annotation.
	 */
	public void logBefore(JoinPoint call, LogBefore logAnnotation) {
		emit(call, logAnnotation.value(), logAnnotation.severity(),
				call.getArgs());
	}

	/**
	 * Emits the log message from a {@link LogReturn} annotation, using the
	 * method's return value as the (sole) formatting parameter.
	 * 
	 * @param call
	 *            the method call being intercepted.
	 * @param logAnnotation
	 *            the log annotation.
	 */
	public void logReturn(JoinPoint call, LogReturn logAnnotation,
			Object returnValue) {

		emit(call, logAnnotation.value(), logAnnotation.severity(), returnValue);
	}

	/**
	 * Emits the log message from a {@link LogException} annotation, including
	 * the method call's thrown exception.
	 * 
	 * @param call
	 *            the method call being intercepted.
	 * @param logAnnotation
	 *            the log annotation.
	 */
	public void logException(JoinPoint call, LogException logAnnotation,
			Throwable exception) {

		emitException(call, logAnnotation.value(), logAnnotation.severity(),
				exception);
	}

	private void emit(JoinPoint call, String message, Severity severity,
			Object... values) {
		Logger logger = extractLogger(call);
		severity.log(logger, message, values);
	}

	private void emitException(JoinPoint call, String message,
			Severity severity, Throwable exception) {
		Logger logger = extractLogger(call);
		severity.logException(logger, message, exception);
	}

	private Logger extractLogger(JoinPoint call) {
		Signature signature = call.getSignature();
		Class<?> declaringType = signature.getDeclaringType();
		Logger logger = LoggerFactory.getLogger(declaringType);
		return logger;
	}
}
