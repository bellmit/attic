package ca.grimoire.maven;

import javax.servlet.ServletContext;

/**
 * Utility methods for locating {@link ca.grimoire.maven.ArtifactDescription}
 * information in servlet environments.
 * 
 * @since 1.2
 */
public class WebappDescription {
    private WebappDescription() {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to locate artifact information on a servlet context classloader.
     * If the artifact information is unavailable, this throws a
     * {@link NoArtifactException}. This is equivalent to calling
     * {@link ArtifactDescription#locate(String, String, ResourceProvider)} with
     * a {@link ServletContextProvider}.
     * 
     * @param groupId
     *            the group ID of the artifact to locate.
     * @param artifactId
     *            the artifact ID of the artifact to locate.
     * @param servletContext
     *            the ServletContext to search.
     * @return the corresponding artifact description.
     * @throws NoArtifactException
     *             if the artifact metadata does not exist on the passed servlet
     *             context.
     */
    public static ArtifactDescription locate(String groupId,
            String artifactId,
            ServletContext servletContext) throws NoArtifactException {
        if (servletContext == null)
            throw new IllegalArgumentException(
                    "servletContext must not be null.");

        return ArtifactDescription.locate(groupId,
                artifactId,
                adapt(servletContext));
    }

    private static ResourceProvider adapt(ServletContext servletContext) {
        return new ServletContextProvider(servletContext);
    }
}
