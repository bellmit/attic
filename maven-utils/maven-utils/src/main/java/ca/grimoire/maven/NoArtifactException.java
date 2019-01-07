package ca.grimoire.maven;

/**
 * Indicates that a given artifact was unable to be loaded.
 */
@SuppressWarnings("serial")
public class NoArtifactException extends Exception {
    private static String generateMessage(String groupId, String artifactId) {
        return String.format("Artifact %s:%s not found.", groupId, artifactId);
    }

    /**
     * Creates a new exception with no cause.
     * 
     * @param groupId
     *            the group ID of the missing artifact.
     * @param artifactId
     *            the artifact ID of the missing artifact.
     */
    public NoArtifactException(String groupId, String artifactId) {
        super(generateMessage(groupId, artifactId));
    }

    /**
     * Creates a new exception triggered by another throwable condition.
     * 
     * @param groupId
     *            the group ID of the missing artifact.
     * @param artifactId
     *            the artifact ID of the missing artifact.
     * @param cause
     *            the throwable that triggered this exception.
     */
    public NoArtifactException(String groupId, String artifactId,
            Throwable cause) {
        super(generateMessage(groupId, artifactId), cause);
    }

}
