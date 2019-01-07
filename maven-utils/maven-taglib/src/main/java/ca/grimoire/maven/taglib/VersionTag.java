package ca.grimoire.maven.taglib;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import ca.grimoire.maven.ArtifactDescription;
import ca.grimoire.maven.NoArtifactException;
import ca.grimoire.maven.WebappDescription;

/**
 * Writes version information about the specified maven artifact to the page.
 * The version information is drawn from either the thread context class loader
 * (if available there) or the handler's own class loader (if available there).
 * <p>
 * The groupId and artifactId for the maven artifact must both be provided via
 * the {@link #setGroupId(String)} and {@link #setArtifactId(String)} methods
 * prior to the first call to {@link #doStartTag()}.
 */
@SuppressWarnings("serial")
public class VersionTag extends TagSupport {

    private static final String FALLBACK_LABEL = "UNKNOWN";
    private ArtifactDescription artifact;
    private String artifactId;
    private String groupId;

    /**
     * Prints the located version information to the page context, then returns
     * {@link javax.servlet.jsp.tagext.Tag#EVAL_PAGE}.
     * 
     * @return EVAL_PAGE.
     * @throws JspException
     *             if there is a problem writing to the page output stream.
     */
    @Override
    public int doEndTag() throws JspException {
        try {
            if (artifact != null)
                pageContext.getOut().print(artifact.getVersion());
            else
                pageContext.getOut().print(FALLBACK_LABEL);
            return EVAL_PAGE;

        } catch (IOException ioe) {
            throw new JspException(ioe);
        }
    }

    /**
     * Locates the version for the group and artifact specified. If either is
     * <code>null</code>, this throws an exception. Always returns
     * {@link javax.servlet.jsp.tagext.Tag#SKIP_BODY} otherwise.
     * 
     * @throws IllegalStateException
     *             if the groupId or artifactId have not been set.
     * @return SKIP_BODY.
     */
    @Override
    public int doStartTag() {
        if (groupId == null)
            throw new IllegalStateException("groupId has not been set");
        if (artifactId == null)
            throw new IllegalStateException("artifactId has not been set");

        getArtifactVersion();

        return SKIP_BODY;
    }

    /**
     * Locates an {@link ArtifactDescription} for a given group and artifact
     * identifier.
     * 
     * @param groupId
     *            the group identifier of the artifact to find.
     * @param artifactId
     *            the artifact identifier for the artifact.
     * @return the located artifact information.
     * @throws NoArtifactException
     *             if no artifact information is available.
     */
    public ArtifactDescription getVersionInfo(String groupId, String artifactId)
            throws NoArtifactException {
        try {
            return WebappDescription.locate(groupId, artifactId, pageContext
                    .getServletContext());
        } catch (NoArtifactException nae) {
            return ArtifactDescription.locate(groupId, artifactId);
        }
    }

    /**
     * Releases and resets the tag handler to its post-construction state.
     * Before any further tag lifecycle (except {@link #release()}) can be
     * called the groupId and artifactId properties must be set again.
     */
    @Override
    public void release() {
        super.release();

        groupId = artifactId = null;
    }

    /**
     * Configures the artifactId to print information for. The artifactId passed
     * must not be <code>null</code>.
     * 
     * @param artifactId
     *            the artifact identifier to locate.
     * @throws IllegalArgumentException
     *             if the artifactId is <code>null</code>.
     */
    public void setArtifactId(String artifactId) {
        if (artifactId == null)
            throw new IllegalArgumentException("artifactId");
        this.artifactId = artifactId;
    }

    /**
     * Configures the groupId to print information for. The groupId passed must
     * not be <code>null</code>.
     * 
     * @param groupId
     *            the artifact groupId to locate.
     * @throws IllegalArgumentException
     *             if the groupId is <code>null</code>.
     */
    public void setGroupId(String groupId) {
        if (groupId == null)
            throw new IllegalArgumentException("groupId");
        this.groupId = groupId;
    }

    private void getArtifactVersion() {
        try {
            artifact = getVersionInfo(groupId, artifactId);
        } catch (NoArtifactException nae) {
            artifact = null;
        }
    }
}
