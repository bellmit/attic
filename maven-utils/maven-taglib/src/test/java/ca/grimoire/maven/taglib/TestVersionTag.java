package ca.grimoire.maven.taglib;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import junit.framework.TestCase;
import ca.grimoire.maven.ArtifactDescription;
import ca.grimoire.maven.NoArtifactException;
import ca.grimoire.maven.taglib.mocks.OutputMockPageContext;

/**
 * Test cases for the <mvn:version> JSP tag handler.
 */
public class TestVersionTag extends TestCase {

    private class CallCheckingVersionTag extends VersionTag {
        private int calls = 0;
        private String groupId;
        private String artifactId;
        private String version;

        /**
         * Creates a checked tag. When {@link #getVersionInfo(String, String)}
         * is called this tag checks the passed groupId and artifactId against
         * the ones passed to this constructor, then returns an artifact
         * description with the passed version.
         * <p>
         * This tag's call count is initally zero.
         * 
         * @param groupId
         *            the groupId to expect.
         * @param artifactId
         *            the artifactId to expect.
         * @param version
         *            the version to return.
         */
        public CallCheckingVersionTag(String groupId, String artifactId,
                String version) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
        }

        /**
         * Verifies the parameters and then returns a fixed artifact
         * description.
         * 
         * @param groupId
         *            the group ID to check.
         * @param artifactId
         *            the artifact ID to check.
         * @return a constant artifact description.
         */
        @Override
        public ArtifactDescription getVersionInfo(String groupId,
                String artifactId) {
            assertEquals(this.groupId, groupId);
            assertEquals(this.artifactId, artifactId);

            ++calls;

            return new ArtifactDescription(groupId, artifactId, version);
        }

        /**
         * Returns the number of times since construction that
         * {@link #getVersionInfo(String, String)} has been called.
         * 
         * @return the count of getVersionInfo calls.
         */
        public int getCalls() {
            return calls;
        }
    }

    /**
     * Verifies that the tag handler's lifecycle methods actually query itss
     * {@link VersionTag#getVersionInfo(String,String)} method, with the correct
     * parameters.
     * 
     * @throws JspException
     *             if the tag handler throws an unexpected exception.
     */
    public void testGetArtifactInfoParams() throws JspException {
        CallCheckingVersionTag tag = new CallCheckingVersionTag("example",
                "one", "2.X");

        // Set up expectations
        OutputMockPageContext outputContext = new OutputMockPageContext(null);

        // Set context classloader to something sane.
        Thread.currentThread().setContextClassLoader(
                getClass().getClassLoader());

        // Simulated tag lifecycle
        initializeTagLifecycle(tag, outputContext);

        tag.setGroupId("example");
        tag.setArtifactId("one");

        // Tag evaluation
        tag.doStartTag();
        tag.doEndTag();

        assertTrue(tag.getCalls() > 0);
    }

    /**
     * Verifies that the VersionTag tag handler writes the version string
     * exactly as returned from its getVersionInfo method to the page.
     * 
     * @throws JspException
     *             if the tag handler throws an unexpected exception.
     */
    public void testWritesReturnedVersionInfo() throws JspException {
        CallCheckingVersionTag tag = new CallCheckingVersionTag("foobie",
                "bletch", "1.Z");

        // Set up expectations
        OutputMockPageContext outputContext = new OutputMockPageContext(null);

        // Set context classloader to something sane.
        Thread.currentThread().setContextClassLoader(
                getClass().getClassLoader());

        // Simulated tag lifecycle
        initializeTagLifecycle(tag, outputContext);

        tag.setGroupId("foobie");
        tag.setArtifactId("bletch");

        // Tag evaluation
        tag.doStartTag();
        tag.doEndTag();

        assertEquals("1.Z", outputContext.getWritten());
    }

    public void testMessageOnNoArtifact() throws JspException {
        VersionTag tag = new VersionTag() {
            @Override
            public ArtifactDescription getVersionInfo(String groupId,
                    String artifactId) throws NoArtifactException {
                throw new NoArtifactException("foobie", "bletch");
            }
        };

        // Set up expectations
        OutputMockPageContext outputContext = new OutputMockPageContext(null);

        // Set context classloader to something sane.
        Thread.currentThread().setContextClassLoader(
                getClass().getClassLoader());

        // Simulated tag lifecycle
        initializeTagLifecycle(tag, outputContext);

        tag.setGroupId("foobie");
        tag.setArtifactId("bletch");

        // Tag evaluation
        tag.doStartTag();
        tag.doEndTag();

        assertEquals("UNKNOWN", outputContext.getWritten());
    }

    /**
     * Verifies that, if the named artifact is available, the tag handler
     * returns {@link Tag#EVAL_PAGE} when told to finish evaluation.
     * 
     * @throws JspException
     *             if the tag handler throws an unexpected exception.
     */
    public void testLocatableEvalPage() throws JspException {
        VersionTag tag = new CallCheckingVersionTag("example", "one", "1.Z");

        OutputMockPageContext outputContext = new OutputMockPageContext(null);

        // Simulated tag lifecycle
        initializeTagLifecycle(tag, outputContext);

        tag.setGroupId("example");
        tag.setArtifactId("one");

        // Tag evaluation
        tag.doStartTag();
        assertEquals(Tag.EVAL_PAGE, tag.doEndTag());
    }

    /**
     * Verifies that, if the named artifact is available, the tag handler
     * returns {@link Tag#SKIP_BODY} when told to begin evaluation.
     * 
     * @throws JspException
     *             if the tag handler throws an unexpected exception.
     */
    public void testLocatableSkipBody() throws JspException {
        VersionTag tag = new CallCheckingVersionTag("example", "one", "1.Z");

        OutputMockPageContext outputContext = new OutputMockPageContext(null);

        // Simulated tag lifecycle
        initializeTagLifecycle(tag, outputContext);

        tag.setGroupId("example");
        tag.setArtifactId("one");

        // Tag evaluation
        assertEquals(Tag.SKIP_BODY, tag.doStartTag());
    }

    /**
     * Verifies that, if the artifactId attribute is not set, an exception is
     * thrown.
     * 
     * @throws JspException
     *             if the tag handler throws an unexpected exception.
     */
    public void testNoArtifactIdException() throws JspException {
        VersionTag tag = new VersionTag();

        // Simulated tag lifecycle
        initializeTagLifecycle(tag, new OutputMockPageContext(null));

        tag.setGroupId("example");

        // Tag evaluation
        try {
            tag.doStartTag();
            tag.doEndTag();
            fail();
        } catch (IllegalStateException expected) {
            // Success case. Ignore exception.
        }
    }

    /**
     * Runs a complete, successful tag lifecycle and then resets it. Once the
     * reset has been performed the tag handler should be back to its
     * "incompletely configured" state with no groupId or artifactId.
     * 
     * @throws JspException
     *             if the tag handler throws an unexpected exception.
     */
    public void testReleaseClearsState() throws JspException {
        VersionTag tag = new CallCheckingVersionTag("example", "one", "1.Z");

        // Set up expectations
        OutputMockPageContext outputContext = new OutputMockPageContext(null);

        // Set context classloader to something sane.
        Thread.currentThread().setContextClassLoader(
                getClass().getClassLoader());

        // Simulated tag lifecycle
        initializeTagLifecycle(tag, outputContext);

        tag.setGroupId("example");
        tag.setArtifactId("one");

        // Tag evaluation
        tag.doStartTag();
        tag.doEndTag();

        // Release!
        tag.release();

        try {
            // Tag evaluation
            tag.doStartTag();
            tag.doEndTag();
            fail();

        } catch (IllegalStateException expected) {
            // Success case.
        }
    }

    /**
     * Verifies that, if the groupId attribute is not set, an exception is
     * thrown.
     * 
     * @throws JspException
     *             if the tag handler throws an unexpected exception.
     */
    public void testNoGroupIdException() throws JspException {
        VersionTag tag = new VersionTag();

        // Simulated tag lifecycle
        initializeTagLifecycle(tag, new OutputMockPageContext(null));

        tag.setArtifactId("one");

        // Tag evaluation
        try {
            tag.doStartTag();
            tag.doEndTag();
            fail();
        } catch (IllegalStateException expected) {
            // Success case. Ignore exception.
        }
    }

    /**
     * Verifies that, if the artifactId attribute is explicitly set to
     * <code>null</code>, an exception is thrown.
     */
    public void testNullArtifactIdException() {
        VersionTag tag = new VersionTag();

        // Simulated tag lifecycle
        initializeTagLifecycle(tag, new OutputMockPageContext(null));

        try {
            tag.setArtifactId(null);
            fail();
        } catch (IllegalArgumentException expected) {
            // Success case. Ignore exception.
        }
    }

    /**
     * Verifies that, if the groupId attribute is explicitly set to
     * <code>null</code>, an exception is thrown.
     */
    public void testNullGroupIdException() {
        VersionTag tag = new VersionTag();

        // Simulated tag lifecycle
        initializeTagLifecycle(tag, new OutputMockPageContext(null));

        try {
            tag.setGroupId(null);
            fail();
        } catch (IllegalArgumentException expected) {
            // Success case. Ignore exception.
        }
    }

    private void initializeTagLifecycle(Tag tag, PageContext mockPageContext) {
        tag.setPageContext(mockPageContext);
        tag.setParent(null);
    }
}
