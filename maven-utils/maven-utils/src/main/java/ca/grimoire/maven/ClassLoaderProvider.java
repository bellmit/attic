package ca.grimoire.maven;

import java.io.InputStream;

/**
 * A {@link ResourceProvider} backed by any {@link ClassLoader}. Delegates
 * straight through to the ClassLoader's resource methods.
 */
public class ClassLoaderProvider implements ResourceProvider {

    /**
     * Wraps the passed ClassLoader for use as a ResourceProvider.
     * 
     * @param classLoader
     *            the ClassLoader to wrap.
     */
    public ClassLoaderProvider(ClassLoader classLoader) {
        if (classLoader == null)
            throw new IllegalArgumentException("classLoader");
        this.classLoader = classLoader;
    }

    /**
     * @see ca.grimoire.maven.ResourceProvider#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream(String resource) {
        return classLoader.getResourceAsStream(resource);
    }

    private final ClassLoader classLoader;
}
