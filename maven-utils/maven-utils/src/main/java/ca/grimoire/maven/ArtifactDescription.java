package ca.grimoire.maven;

import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * Represents information about a single maven artifact. This class provides
 * some convenience methods for loading artifact information from classloaders.
 */
public class ArtifactDescription {

    private static final String ARTIFACT_ID_PROPERTY = "artifactId";

    private static final String GROUP_ID_PROPERTY = "groupId";

    private static final String ARTIFACT_PROPERTIES_PATH = "META-INF/maven/{0}/{1}/pom.properties";

    private static final String VERSION_PROPERTY = "version";

    /**
     * Attempts to locate artifact information. Searches for artifact
     * information first on the current thread's context classloader, then on
     * this class's loading classloader. If the artifact information is
     * unavailable in either of those places, this throws a
     * {@link NoArtifactException}.
     * 
     * @param groupId
     *            the group ID of the artifact to locate.
     * @param artifactId
     *            the artifact ID of the artifact to locate.
     * @return the corresponding artifact description.
     * @throws NoArtifactException
     *             if artifact metadata isn't available on the thread's context
     *             classloader or this class's classloader.
     */
    public static ArtifactDescription locate(String groupId, String artifactId)
            throws NoArtifactException {

        ClassLoader contextClassLoader = Thread.currentThread()
                .getContextClassLoader();

        if (contextClassLoader != null)
            try {
                return locate(groupId, artifactId, adapt(contextClassLoader));
            } catch (NoArtifactException nae) {
                /*
                 * The recovery case for this is the same as the case when
                 * contextClassLoader is null, so we quietly suppress this
                 * exception and carry on to the next attempt. The second
                 * attempt will throw an exception of its own if it fails, so
                 * it's not worth duplicating the code here.
                 */
            }

        ClassLoader ownClassLoader = ArtifactDescription.class.getClassLoader();
        assert (ownClassLoader != null);
        return locate(groupId, artifactId, adapt(ownClassLoader));

    }

    /**
     * Attempts to locate artifact information on a {@link ResourceProvider}. If
     * the artifact information is unavailable, this throws a
     * {@link NoArtifactException}.
     * 
     * @param groupId
     *            the group ID of the artifact to locate.
     * @param artifactId
     *            the artifact ID of the artifact to locate.
     * @param resourceProvider
     *            the resourceProvider to search.
     * @return the corresponding artifact description.
     * @throws NoArtifactException
     *             if the artifact metadata does not exist on the passed
     *             resourceProvider.
     */
    public static ArtifactDescription locate(String groupId, String artifactId,
            ResourceProvider resourceProvider) throws NoArtifactException {
        if (resourceProvider == null)
            throw new IllegalArgumentException(
                    "resourceProvider must not be null.");

        String resourcePath = buildArtifactPropertiesPath(groupId, artifactId);

        try {
            ArtifactDescription artifact = createFromResource(resourcePath,
                    resourceProvider);

            if (artifact == null)
                throw new NoArtifactException(groupId, artifactId);

            return artifact;
        } catch (IOException ioe) {
            throw new NoArtifactException(groupId, artifactId, ioe);
        }
    }

    /**
     * Attempts to locate artifact information on a classloader. If the artifact
     * information is unavailable, this throws a {@link NoArtifactException}.
     * This is equivalent to calling
     * {@link #locate(String, String, ResourceProvider)} with a
     * {@link ClassLoaderProvider}.
     * 
     * @param groupId
     *            the group ID of the artifact to locate.
     * @param artifactId
     *            the artifact ID of the artifact to locate.
     * @param classLoader
     *            the classloader to search.
     * @return the corresponding artifact description.
     * @throws NoArtifactException
     *             if the artifact metadata does not exist on the passed
     *             classloader.
     */
    public static ArtifactDescription locate(String groupId, String artifactId,
            ClassLoader classLoader) throws NoArtifactException {
        if (classLoader == null)
            throw new IllegalArgumentException("classLoader must not be null.");

        return locate(groupId, artifactId, adapt(classLoader));
    }

    private static ResourceProvider adapt(ClassLoader classLoader) {
        return new ClassLoaderProvider(classLoader);
    }

    private static String buildArtifactPropertiesPath(String groupId,
            String artifactId) {
        if (groupId == null)
            throw new IllegalArgumentException("groupId must not be null");
        if (artifactId == null)
            throw new IllegalArgumentException("artifactId must not be null");

        return MessageFormat.format(ARTIFACT_PROPERTIES_PATH, groupId,
                artifactId);
    }

    private static ArtifactDescription createFromResource(String resourcePath,
            ResourceProvider resourceProvider) throws IOException {
        assert (resourcePath != null);
        assert (resourceProvider != null);

        Properties artifactProperties = createProperties(resourcePath,
                resourceProvider);
        if (artifactProperties == null)
            return null;

        String groupId = artifactProperties.getProperty(GROUP_ID_PROPERTY);
        String artifactId = artifactProperties
                .getProperty(ARTIFACT_ID_PROPERTY);
        String version = artifactProperties.getProperty(VERSION_PROPERTY);

        return new ArtifactDescription(groupId, artifactId, version);

    }

    private static Properties createProperties(String resourcePath,
            ResourceProvider resourceProvider) throws IOException {
        assert (resourcePath != null);
        assert (resourceProvider != null);

        InputStream propertiesStream = resourceProvider
                .getResourceAsStream(resourcePath);
        if (propertiesStream == null)
            return null;
        try {
            Properties artifactProperties = new Properties();
            artifactProperties.load(propertiesStream);
            return artifactProperties;
        } finally {
            propertiesStream.close();
        }
    }

    /**
     * Creates a new artifact record.
     * 
     * @param groupId
     *            the artifact's groupId.
     * @param artifactId
     *            the artifact's artifactId.
     * @param version
     *            the artifact's version.
     */
    public ArtifactDescription(String groupId, String artifactId, String version) {
        if (groupId == null)
            throw new IllegalArgumentException("groupId must not be null");
        if (artifactId == null)
            throw new IllegalArgumentException("artifactId must not be null");
        if (version == null)
            throw new IllegalArgumentException("version must not be null");

        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    /**
     * Returns the artifact's artifactId.
     * 
     * @return the artifact's artifactId.
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * Returns the artifact's groupId.
     * 
     * @return the artifact's groupId.
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * Returns the artifact's version.
     * 
     * @return the artifact's version.
     */
    public String getVersion() {
        return version;
    }

    private final String artifactId;
    private final String groupId;
    private final String version;

}
