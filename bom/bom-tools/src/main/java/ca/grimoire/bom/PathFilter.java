package ca.grimoire.bom;

/**
 * Accepts or rejects paths.
 */
public interface PathFilter {
    /**
     * @param path
     *            the candidate path.
     * @return <code>true</code> if this filter accepts <var>path</var>.
     */
    public boolean accept(String path);
}
