package ca.grimoire.maven;

import junit.framework.TestCase;

/**
 * Test cases for loading an artifact description from a context-sensitive
 * location using {@link ArtifactDescription#locate(String, String)}.
 * <p>
 * All of these test cases rely on resources available in the classpath,
 * including <code>META-INF/maven/example/one/pom.properties</code>.
 */
public class TestArtifactDescriptionFromContext extends TestCase {

    /**
     * Verifies that asking for an artifact with a <code>null</code> groupId
     * fails with an exception.
     * 
     * @throws NoArtifactException
     *             if the artifact description loader throws an unexpected
     *             exception.
     */
    public void testNullGroupId() throws NoArtifactException {
        try {
            ArtifactDescription.locate(null, "one");
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
        try {
            ArtifactDescription.locate(null, "one");
            fail();
        } catch (IllegalArgumentException expected) {
            // Success case, ignore exception.
        }
    }

    /**
     * Verifies that asking for an artifact that doesn't exist on any
     * classloader fails with an exception.
     */
    public void testNonexistant() {
        try {
            ArtifactDescription.locate("example", "many");
            fail();
        } catch (NoArtifactException expected) {
            // Success case, ignore exception.
        }
    }

    /**
     * Verifies that the loader can find artifacts that exist on the thread
     * context classloader.
     * 
     * @throws NoArtifactException
     *             if the artifact description loader throws an unexpected
     *             exception.
     */
    public void testExistingOnThreadContext() throws NoArtifactException {
        ClassLoader classLoader = getClass().getClassLoader();
        Thread.currentThread().setContextClassLoader(classLoader);

        ArtifactDescription artifact = ArtifactDescription.locate("example",
                "one");

        assertEquals("example", artifact.getGroupId());
        assertEquals("one", artifact.getArtifactId());
        assertEquals("2.2", artifact.getVersion());
    }

    /**
     * Verifies that the loader can find artifacts that exist on the loader's
     * own classloader.
     * 
     * @throws NoArtifactException
     *             if the artifact description loader throws an unexpected
     *             exception.
     */
    public void testExistingOnGetClass() throws NoArtifactException {
        Thread.currentThread().setContextClassLoader(null);

        ArtifactDescription artifact = ArtifactDescription.locate("example",
                "one");

        assertEquals("example", artifact.getGroupId());
        assertEquals("one", artifact.getArtifactId());
        assertEquals("2.2", artifact.getVersion());
    }
}
