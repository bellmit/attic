package ca.grimoire.formtree;

/**
 * Provides form data. This is intended to generalize over various form data
 * sources: HttpServletRequest's parameter methods, Jersey's MultivaluedMap, and
 * other similar constructs.
 */
public interface FormAdapter {

    /**
     * Enumerates the associated form's fields.
     * 
     * @return a sequence of field names, in any order.
     */
    public Iterable<String> getFields();

    /**
     * Returns the value of a single form field. This must only be called using
     * field names obtained from {@link #getFields()} on the same
     * {@link FormAdapter}.
     * 
     * @param field
     *            the field to retrieve.
     * @return all values of the field in the form.
     */
    public Iterable<String> getValues(String field);

}
