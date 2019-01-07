package ca.grimoire.maven;

import junit.framework.TestCase;

/**
 * Test cases for artifact descriptions as inert noun classes.
 */
public class TestArtifactDescription extends TestCase {
    /**
     * Verifies that the bean property accessors for an artifact description
     * return the configured values.
     */
    public void testBeanAccessors() {
        ArtifactDescription artifact = new ArtifactDescription("foobie",
                "bletch", "1.Many");
        assertEquals("foobie", artifact.getGroupId());
        assertEquals("bletch", artifact.getArtifactId());
        assertEquals("1.Many", artifact.getVersion());
    }

    /**
     * Verifies that the constructor forbids <code>null</code> groupIds.
     */
    public void testNullGroupIdException() {
        try {
            new ArtifactDescription(null, "bletch", "2.X");
            fail();
        } catch (IllegalArgumentException expected) {
            // Success case.
        }
    }

    /**
     * Verifies that the constructor forbids <code>null</code> artifactIds.
     */
    public void testNullArtifactIdException() {
        try {
            new ArtifactDescription("rhubarb", null, "2.X");
            fail();
        } catch (IllegalArgumentException expected) {
            // Success case.
        }
    }

    /**
     * Verifies that the constructor forbids <code>null</code> versions.
     */
    public void testNullVersionException() {
        try {
            new ArtifactDescription("foobie", "boggle", null);
            fail();
        } catch (IllegalArgumentException expected) {
            // Success case.
        }
    }
}
