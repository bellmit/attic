package ca.grimoire.critical;

import java.util.Map;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.Elements;
import javax.lang.model.util.SimpleAnnotationValueVisitor6;

/**
 * Creates log messages to be printed during annotation processing for bad
 * ideas.
 * 
 * @see TaskTagProcessor
 */
public class MessageFactory {

	private static final class StringConvertingVisitor extends
			SimpleAnnotationValueVisitor6<String, Void> {
		@Override
		protected String defaultAction(Object o, Void p) {
			return String.valueOf(o);
		}
	}

	private final Elements elementUtils;

	public MessageFactory(Elements elementUtils) {
		this.elementUtils = elementUtils;
	}

	private String convertToString(AnnotationValue annotationValue) {
		return annotationValue.accept(new StringConvertingVisitor(), null);
	}

	public String createMessageForAnnotation(AnnotationMirror annotationMirror) {
		String prefix = getPrefixFor(annotationMirror);
		String value = getValueFor(annotationMirror);

		if (isMessage(value))
			return String.format("%1$s: %2$s", prefix, value);
		return String.format("%1$s.", prefix);
	}

	private boolean isMessage(String value) {
		return value != null && value.length() > 0;
	}

	private String getPrefixFor(AnnotationMirror annotationMirror) {
		DeclaredType annotationType = annotationMirror.getAnnotationType();
		String typeName = annotationType.toString();
		return typeName.substring(typeName.lastIndexOf('.') + 1) + " found";
	}

	private String getValueFor(AnnotationMirror annotationMirror) {
		Map<? extends ExecutableElement, ? extends AnnotationValue> elementValues = annotationMirror
				.getElementValues();

		for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : elementValues
				.entrySet()) {

			ExecutableElement element = entry.getKey();
			if (isNamed(element, "value")) {
				return convertToString(entry.getValue());
			}
		}
		return null;
	}

	private boolean isNamed(ExecutableElement element, String name) {
		Name simpleName = element.getSimpleName();
		Name expectedName = elementUtils.getName(name);
		return expectedName.equals(simpleName);
	}
}
