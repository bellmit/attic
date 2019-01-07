package ca.grimoire.critical;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

/**
 * Checks for the presence of {@link BadIdea}, {@link FixMe} and {@link Todo}
 * annotations and warns about them. By default, the warning is at the WARNING
 * severity level, but any severity can be used by setting the 'severity'
 * parameter (for javac, this is '-Aseverity=ERROR') to the name of the level.
 * Valid levels are the same as the enum constants for {@link Kind}.
 * <p>
 * Setting the severity to ERROR will cause detected annotations to trigger
 * compilation failures.
 */
@SupportedAnnotationTypes( { "ca.grimoire.critical.BadIdea",
		"ca.grimoire.critical.FixMe", "ca.grimoire.critical.Todo" })
@SupportedSourceVersion(SourceVersion.RELEASE_6)
@FixMe("No support for different error priorities by annotation.")
public class TaskTagProcessor extends AbstractProcessor {

	private static final Kind DEFAULT_SEVERITY = Kind.WARNING;
	private Kind messageSeverity;

	private Messager messager;
	private Types typeUtils;
	private Elements elementUtils;

	private Kind getCriticismSeverity(Map<String, String> options) {
		String messageSeverity = options.get("severity");
		try {
			if (messageSeverity != null)
				return Kind.valueOf(messageSeverity);
			return DEFAULT_SEVERITY;
		} catch (IllegalArgumentException iae) {
			throw new IllegalArgumentException(String.format(
					"No severity level named %1$s. Valid levels are %2$s",
					messageSeverity, Arrays.toString(Kind.values())));
		}
	}

	/**
	 * In addition to the initialization done by {@link AbstractProcessor}, this
	 * also checks for and initializes the warning level based on processor
	 * options.
	 * 
	 * @see javax.annotation.processing.AbstractProcessor#init(javax.annotation.processing.ProcessingEnvironment)
	 */
	@Override
	public void init(ProcessingEnvironment env) {
		super.init(env);

		this.elementUtils = processingEnv.getElementUtils();
		this.typeUtils = processingEnv.getTypeUtils();
		this.messager = processingEnv.getMessager();

		this.messageFactory = new MessageFactory(elementUtils);

		Map<String, String> options = env.getOptions();
		this.messageSeverity = getCriticismSeverity(options);
	}

	/**
	 * Scans all elements in the processing round for any annotations passed in.
	 * If annotations are found, they're noted at the configured warning level.
	 * <p>
	 * Annotations with value() members will have the value included in the
	 * output.
	 * 
	 * @see javax.annotation.processing.AbstractProcessor#process(java.util.Set,
	 *      javax.annotation.processing.RoundEnvironment)
	 */
	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		for (TypeElement annotation : annotations) {
			processAnnotation(annotation, roundEnv);
		}

		return false;
	}

	private void processAnnotation(TypeElement annotation,
			RoundEnvironment roundEnv) {
		Set<? extends Element> elements = roundEnv
				.getElementsAnnotatedWith(annotation);

		for (Element element : elements) {
			processElement(element, annotation);
		}
	}

	private void processElement(Element element, TypeElement annotation) {
		TypeMirror expectedAnnotationType = annotation.asType();

		for (AnnotationMirror annotationMirror : elementUtils
				.getAllAnnotationMirrors(element)) {
			TypeMirror annotationType = annotationMirror.getAnnotationType();

			if (typeUtils.isAssignable(annotationType, expectedAnnotationType)) {
				String message = messageFactory
						.createMessageForAnnotation(annotationMirror);
				messager.printMessage(messageSeverity, message, element,
						annotationMirror);
			}
		}
	}

	private MessageFactory messageFactory;
}
