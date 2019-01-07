package ca.grimoire.formtree;

import ca.grimoire.formtree.receiver.IgnoreReceiver;

/**
 * Intermediate receivers for form elements (along with the final receiver) must
 * implement this to provide {@link FormDecoder} with a way to navigate to the
 * relevant part of the data model for each field.
 * <p>
 * The navigation methods (which return {@link FormElementReceiver}) must not
 * return null when asked for an unsupported key. Implementations may ignore
 * data; the easiest way is to return {@link IgnoreReceiver#IGNORE}. Similarly,
 * the {@link #values(Iterable)} method must silently ignore unexpected values.
 * 
 * @see FormDecoder
 * @see FormElementReceiver
 */
public interface FormElementReceiver {
    /**
     * Add a terminal value to the decoded form.
     * 
     * @param values
     *            the values to store.
     */
    public void values(Iterable<String> values);

    /**
     * Adds an index to the decoded form.
     * 
     * @param index
     *            the index to decode.
     * @return a form receiver for the index's contents.
     */
    public FormElementReceiver index(int index);

    /**
     * Adds a key to the decoded form.
     * 
     * @param field
     *            the field to decode.
     * @return a form receiver for the key's contents.
     */
    public FormElementReceiver key(String field);
}
