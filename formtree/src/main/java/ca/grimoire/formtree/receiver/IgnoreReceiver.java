package ca.grimoire.formtree.receiver;

import ca.grimoire.formtree.FormElementReceiver;

/**
 * A form element receiver that acts as a black hole, ignoring all values passed
 * into it.
 */
public class IgnoreReceiver implements FormElementReceiver {

    /** A convenience instance of IgnoreReceiver. */
    public static final IgnoreReceiver IGNORE = new IgnoreReceiver();

    /**
     * Ignores the passed value entirely.
     * 
     * @see ca.grimoire.formtree.FormElementReceiver#values(java.lang.Iterable)
     */
    @Override
    public void values(Iterable<String> values) {
        // Ignore the values.
    }

    /**
     * Ignores a key element.
     * 
     * @see ca.grimoire.formtree.FormElementReceiver#key(java.lang.String)
     */
    @Override
    public FormElementReceiver key(String field) {
        return this;
    }

    /**
     * Ignores an index element.
     * 
     * @see ca.grimoire.formtree.FormElementReceiver#index(int)
     */
    @Override
    public FormElementReceiver index(int index) {
        return this;
    }
}
