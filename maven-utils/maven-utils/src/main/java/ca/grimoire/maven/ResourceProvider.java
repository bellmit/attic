package ca.grimoire.maven;

import java.io.InputStream;

import javax.servlet.ServletContext;

/**
 * A generalization over {@link ClassLoader#getResourceAsStream(String)},
 * {@link ServletContext#getResourceAsStream(String)}, and friends.
 */
public interface ResourceProvider {

    /**
     * Opens a stream over the named resource. If there is no resource with the
     * passed name, then the provider must return <code>null</code>.
     * <p>
     * Paths that are "absolute" relative to this resource provider are not
     * required to begin with a slash. Implementations are responsible for
     * translating paths to an appropriate local form.
     * 
     * @param resource
     *            the resource to open.
     * @return an open stream at the beginning of the named resource, or
     *         <code>null</code>.
     * @see ClassLoader#getResourceAsStream(String)
     */
    public InputStream getResourceAsStream(String resource);
}
