package ca.grimoire.maven;

import java.io.InputStream;

import javax.servlet.ServletContext;

/**
 * A {@link ResourceProvider} backed by a {@link ServletContext}. Delegates
 * straight through to the ServletContext resource methods, after ensuring that
 * resource names begin with a '/' character.
 */
public class ServletContextProvider implements ResourceProvider {

    /**
     * Wraps the passed ServletContext for use as a ResourceProvider.
     * 
     * @param servletContext
     *            the ServletContext to wrap.
     */
    public ServletContextProvider(ServletContext servletContext) {
        if (servletContext == null)
            throw new IllegalArgumentException("classLoader");
        this.servletContext = servletContext;
    }

    /**
     * @see ca.grimoire.maven.ResourceProvider#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream(String resource) {
        if (!resource.startsWith("/"))
            resource = "/" + resource;
        return servletContext.getResourceAsStream(resource);
    }

    private final ServletContext servletContext;
}
