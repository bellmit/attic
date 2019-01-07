package ca.grimoire.formtree.adapter.jaxrs;

import javax.ws.rs.core.MultivaluedMap;

import ca.grimoire.formtree.FormAdapter;

/**
 * An adapter for JAX-RS {@link MultivaluedMap}-based form handlers.
 */
public class MultivaluedMapForm implements FormAdapter {

    private final MultivaluedMap<String, String> formData;

    /**
     * Adapts a given {@link MultivaluedMap} for use as a {@link FormAdapter}.
     * 
     * @param formData
     *            the form data to provide.
     */
    public MultivaluedMapForm(MultivaluedMap<String, String> formData) {
        this.formData = formData;
    }

    /**
     * Retrieves the list of keys from the underlying map.
     * 
     * @see ca.grimoire.formtree.FormAdapter#getFields()
     */
    @Override
    public Iterable<String> getFields() {
        return formData.keySet();
    }

    /**
     * Retrieves the values for a given key in the underlying map.
     * 
     * @see ca.grimoire.formtree.FormAdapter#getValues(java.lang.String)
     */
    @Override
    public Iterable<String> getValues(String field) {
        return formData.get(field);
    }

}
