package ca.grimoire.formtree.adapter.servlet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletRequest;

import ca.grimoire.formtree.FormAdapter;

/**
 * A form adapter wrapping {@link ServletRequest}'s parameter list.
 */
public class ServletForm implements FormAdapter {

    private final ServletRequest request;

    /**
     * Adapts a given {@link ServletRequest} to the {@link FormAdapter}
     * interface.
     * 
     * @param request
     *            the request containing form data.
     */
    public ServletForm(ServletRequest request) {
        this.request = request;
    }

    /**
     * Retrieves the list of parameters in the underlying servlet request.
     * 
     * @see ca.grimoire.formtree.FormAdapter#getFields()
     */
    @Override
    public Iterable<String> getFields() {
        // Terrible pre-generics pre-collections API is terrible.
        List<String> parameters = new ArrayList<String>();
        for (Enumeration<?> parameterNames = request.getParameterNames(); parameterNames
                .hasMoreElements();) {
            parameters.add(String.valueOf(parameterNames.nextElement()));
        }
        return parameters;
    }

    /**
     * Retrieves all values of a single parameter from the underlying servlet
     * request.
     * 
     * @see ca.grimoire.formtree.FormAdapter#getValues(java.lang.String)
     */
    @Override
    public Iterable<String> getValues(String field) {
        String[] parameterValues = request.getParameterValues(field);
        return Arrays.asList(parameterValues);
    }

}
