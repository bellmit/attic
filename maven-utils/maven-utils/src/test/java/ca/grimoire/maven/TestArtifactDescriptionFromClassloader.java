package ca.grimoire.maven;

import junit.framework.TestCase;

/**
 * Test cases for loading an artifact description from a classloader using
 * {@link ArtifactDescription#locate(String, String, ClassLoader)}.
 * <p>
 * All of these test cases rely on resources available in the classpath,
 * including <code>META-INF/maven/example/one/pom.properties</code>.
 */
public class TestArtifactDescriptionFromClassloader extends TestCase {

    /**
     * Verifies that asking for an artifact with a <code>null</code> groupId
     * fails with an exception.
     * 
     * @throws NoArtifactException
     *             if the artifact description loader throws an unexpected
     *             exception.
     */
    public void testNullGroupId() throws NoArtifactException {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            ArtifactDescription.locate(null, "one", classLoader);
            fail();
        } catch (IllegalArgumentException expected) {
            // Success case, ignore exception.
        }
    }

    /**
     * Verifies that asking for an artifact with a <code>null</code> artifactId
     * fails with an exception.
     * 
     * @throws NoArtifactException
     *             if the artifact description loader throws an unexpected
     *             exception.
     */
    public void testNullArtifactId() throws NoArtifactException {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            ArtifactDescription.locate(null, "one", classLoader);
            fail();
        } catch (IllegalArgumentException expected) {
            // Success case, ignore exception.
        }
    }

    /**
     * Verifies that asking for an artifact that doesn't exist on a classloader
     * fails with an exception.
     */
    public void testNonexistant() {
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            ArtifactDescription.locate("example", "many", classLoader);
            fail();
        } catch (NoArtifactException expected) {
            // Success case, ignore exception.
        }
    }

    /**
     * Verifies that a classloader that contains an artifact description can be
     * queried to obtain that artifact description.
     * 
     * @throws NoArtifactException
     *             if the artifact description loader throws an unexpected
     *             exception.
     */
    public void testExisting() throws NoArtifactException {
        ClassLoader classLoader = getClass().getClassLoader();
        ArtifactDescription artifact = ArtifactDescription.locate("example",
                "one", classLoader);

        assertEquals("example", artifact.getGroupId());
        assertEquals("one", artifact.getArtifactId());
        assertEquals("2.2", artifact.getVersion());
    }

}
