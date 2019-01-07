package ca.grimoire.maven.taglib.mocks;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.servlet.jsp.PageContext;

/**
 * A mockup page context used to test JSP components which only need page
 * attribute support. All other page context features are delegated to another
 * page context.
 */
public class PageAttributeMockPageContext extends MockPageContext {
    /**
     * Creates a new mock page context, using another page context for features
     * besides page attributes.
     * 
     * @param base
     *            the base page context.
     */
    public PageAttributeMockPageContext(PageContext base) {
        super(base);
    }

    /**
     * Searches for an attribute in page, request, session (if valid), and
     * application scopes, in order. Request, session, and app scopes are
     * provided by the underlying page context.
     * 
     * @param name
     *            the attribute to find.
     * @return the attribute value, or <code>null</code>.
     */
    @Override
    public Object findAttribute(String name) {
        Object found = getAttribute(name);
        if (found == null)
            found = super.findAttribute(name);

        return found;
    }

    /**
     * Returns an attribute from page scope.
     * 
     * @param name
     *            the attribute to find.
     * @return the attribute value in page scope, or <code>null</code>.
     */
    @Override
    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    /**
     * Returns an attribute from the specified scope.
     * 
     * @param name
     *            the attribute to find.
     * @param scope
     *            the scope to search.
     * @return the attribute value in the specified scope, or <code>null</code>.
     */
    @Override
    public Object getAttribute(String name, int scope) {
        if (scope == PAGE_SCOPE)
            return getAttribute(name);
        return super.getAttribute(name, scope);
    }

    /**
     * Returns the names of attributes in a given scope. Page scope attributes
     * are provided by this mock object.
     * 
     * @param scope
     *            the scope to list.
     * @return an enumeration of attribute names.
     */
    @Override
    public Enumeration<String> getAttributeNamesInScope(int scope) {
        if (scope == PAGE_SCOPE)
            return attributes.keys();
        return super.getAttributeNamesInScope(scope);
    }

    /**
     * Returns the scope of an attribute, or 0.
     * 
     * @param name
     *            the attribute to find.
     * @return the attribute scope, or 0.
     */
    @Override
    public int getAttributesScope(String name) {
        if (attributes.containsKey(name))
            return PAGE_SCOPE;
        return super.getAttributesScope(name);
    }

    /**
     * Removes an attribute from page scope.
     * 
     * @param name
     *            the attribute to remove.
     */
    @Override
    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /**
     * Removes an attribute from the given scope.
     * 
     * @param name
     *            the attribute to remove.
     * @param scope
     *            the scope to remove from.
     */
    @Override
    public void removeAttribute(String name, int scope) {
        if (scope == PAGE_SCOPE)
            removeAttribute(name);
        else
            super.removeAttribute(name, scope);
    }

    /**
     * Sets an attribute in page scope.
     * 
     * @param name
     *            the attribute to set.
     * @param value
     *            the attribute value.
     */
    @Override
    public void setAttribute(String name, Object value) {
        attributes.put(name, value);
    }

    /**
     * Sets an attribute in the given scope.
     * 
     * @param name
     *            the attribute to set.
     * @param value
     *            the attribute value.
     * @param scope
     *            the scope to set attributes in.
     */
    @Override
    public void setAttribute(String name, Object value, int scope) {
        if (scope == PAGE_SCOPE)
            setAttribute(name, value);
        else
            super.setAttribute(name, value, scope);
    }

    // Using Hashtable because it has Enumeration support built-in.
    private final Hashtable<String, Object> attributes = new Hashtable<String, Object>();
}
