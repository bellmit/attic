package ca.grimoire.formtree;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Decodes tree-shaped form data into a receiver. Form keys are parsed and used
 * to locate specific nodes in the receiver to use with the field's value.
 * <p>
 * Grammar:
 * 
 * <pre>
 * path ::= [pathElement '.'] pathElement
 * pathElement ::= keyPathElement | listPathElement
 * keyPathElement ::= NAME
 * listPathElement ::= NAME '[' DIGITS ']'
 * 
 * DIGITS ::= ('0'..'9')*
 * NAMEINITIAL ::= ('-'|'_'|'a'..'z'|'A'..Z')
 * NAMETRAILING ::= (NAMEINITIAL|DIGITS)
 * NAME ::= NAMEINITIAL NAMETRAILING*
 * </pre>
 * <p>
 * Example field names:
 * <ul>
 * <li><kbd>foo.bar.baz</kbd></li>
 * <li><kbd>foo[5]</kbd></li>
 * <li><kbd>foo.bar[5].baz</kbd></li>
 * </ul>
 */
public class FormDecoder {

    private final Pattern LIST_KEY = Pattern
            .compile("([-A-Za-z_][-A-Za-z_0-9]*)\\[(\\d+)\\]");

    /**
     * Decodes a form, populating the receiver with values.
     * 
     * @param <T>
     *            the expected form decoding result type.
     * @param form
     *            an adapter over the form to decode.
     * @param receiver
     *            an adapter to receive the decoded values.
     * @return the form encoding result, from the <var>receiver</var>.
     */
    public <T> T decode(FormAdapter form, FormReceiver<T> receiver) {
        for (String field : form.getFields()) {
            FormElementReceiver fieldReceiver = findFieldReceiver(receiver,
                    field);

            fieldReceiver.values(form.getValues(field));
        }

        return receiver.finished();
    }

    private <T> FormElementReceiver findFieldReceiver(FormReceiver<T> receiver,
            String field) {
        String[] path = field.split("\\.");
        FormElementReceiver fieldReceiver = receiver;
        for (String element : path)
            fieldReceiver = findPathElementReceiver(element, fieldReceiver);
        return fieldReceiver;
    }

    private final FormElementReceiver findPathElementReceiver(String pathElement,
            FormElementReceiver receiver) {
        Matcher listKeyMatcher = LIST_KEY.matcher(pathElement);
        if (listKeyMatcher.matches()) {
            String listName = listKeyMatcher.group(1);
            int index = Integer.parseInt(listKeyMatcher.group(2));

            return receiver.key(listName).index(index);
        }

        return receiver.key(pathElement);
    }
}
