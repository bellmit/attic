package ca.grimoire.bom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Methods for scanning classloaders for BOM files and loading the associated
 * resources.
 */
public final class BomLoader {
    /** The default encoding used to parse BOM files ({@value} ). */
    public static final String DEFAULT_ENCODING = "UTF-8";

    private static final PathFilter DEFAULT_FILTER = new PathFilter() {
        @Override
        public boolean accept(String path) {
            return true;
        }
    };

    /**
     * Convenience method for calling
     * {@link #findResources(ClassLoader, String, String, PathFilter)} with the
     * {@link #DEFAULT_ENCODING default encoding} and a universally-accepting
     * filter.
     * 
     * @param classLoader
     *            the {@link ClassLoader} to scan.
     * @param bom
     *            the path to the BOM resource within the classloader.
     * @return a list of URLs pointing to resources in <var>classLoader</var>.
     * @throws IOException
     *             if this method is unable to load the BOM.
     */
    public static List<URL> findResources(ClassLoader classLoader, String bom)
            throws IOException {
        return findResources(classLoader, bom, DEFAULT_ENCODING, DEFAULT_FILTER);
    }

    /**
     * Convenience method for calling
     * {@link #findResources(ClassLoader, String, String, PathFilter)} with the
     * {@link #DEFAULT_ENCODING default encoding}.
     * 
     * @param classLoader
     *            the {@link ClassLoader} to scan.
     * @param bom
     *            the path to the BOM resource within the classloader.
     * @param filter
     *            a {@link PathFilter} that restricts the returned resources.
     * @return a list of URLs pointing to resources in <var>classLoader</var>.
     * @throws IOException
     *             if this method is unable to load the BOM.
     */
    public static List<URL> findResources(ClassLoader classLoader,
            String bom,
            PathFilter filter) throws IOException {
        return findResources(classLoader, bom, DEFAULT_ENCODING, filter);
    }

    /**
     * Convenience method for calling
     * {@link #findResources(ClassLoader, String, String, PathFilter)} with a
     * universally-accepting filter.
     * 
     * @param classLoader
     *            the {@link ClassLoader} to scan.
     * @param bom
     *            the path to the BOM resource within the classloader.
     * @param encoding
     *            the encoding to parse BOMs with.
     * @return a list of URLs pointing to resources in <var>classLoader</var>.
     * @throws IOException
     *             if this method is unable to load the BOM.
     */
    public static List<URL> findResources(ClassLoader classLoader,
            String bom,
            String encoding) throws IOException {
        return findResources(classLoader, bom, encoding, DEFAULT_FILTER);
    }

    /**
     * Returns the URLs of every resource listed in a given BOM visible on
     * <var>classLoader</var>. Every occurrence of <var>bom</var> is parsed,
     * allowing multiple JARs to provide the same BOM; the returned list
     * contains every element on any visible BOM.
     * <p>
     * The passed <var>filter</var> parameter can be used to restrict the
     * returned resource URLs by matching paths out of the BOM.
     * 
     * @param classLoader
     *            the {@link ClassLoader} to scan.
     * @param bom
     *            the path to the BOM resource within the classloader.
     * @param encoding
     *            the encoding to parse BOMs with.
     * @param filter
     *            a {@link PathFilter} that restricts the returned resources.
     * @return a list of URLs pointing to resources in <var>classLoader</var>.
     * @throws IOException
     *             if this method is unable to load the BOM.
     * @see #findResources(ClassLoader, String)
     * @see #findResources(ClassLoader, String, PathFilter)
     * @see #findResources(ClassLoader, String, String)
     * @see #loadBom(ClassLoader, URL, String, PathFilter)
     */
    public static List<URL> findResources(ClassLoader classLoader,
            String bom,
            String encoding,
            PathFilter filter) throws IOException {
        List<URL> resources = new ArrayList<URL>();

        Enumeration<URL> bomResources = classLoader.getResources(bom);
        while (bomResources.hasMoreElements()) {
            URL bomResource = bomResources.nextElement();
            resources
                    .addAll(loadBom(classLoader, bomResource, encoding, filter));
        }

        return resources;
    }

    /**
     * Scans a single resource as a BOM.
     * <p>
     * The passed <var>filter</var> parameter can be used to restrict the
     * returned resource URLs by matching paths out of the BOM.
     * 
     * @param classLoader
     *            the {@link ClassLoader} to scan.
     * @param bomResource
     *            the URL of the BOM resource to parse.
     * @param encoding
     *            the encoding to parse BOMs with.
     * @param filter
     *            a {@link PathFilter} that restricts the returned resources.
     * @return a list of URLs pointing to resources in <var>classLoader</var>.
     * @throws IOException
     *             if this method is unable to load the BOM.
     * @see #findResources(ClassLoader, String, String, PathFilter)
     */
    public static List<URL> loadBom(ClassLoader classLoader,
            URL bomResource,
            String encoding,
            PathFilter filter) throws IOException {
        List<URL> elements = new ArrayList<URL>();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                bomResource.openStream(), encoding));
        try {
            for (String line = in.readLine(); line != null; line = in
                    .readLine()) {
                if (filter.accept(line)) {
                    Enumeration<URL> resources = classLoader.getResources(line);
                    while (resources.hasMoreElements())
                        elements.add(resources.nextElement());
                }
            }
        } finally {
            in.close();
        }
        return elements;
    }

    private BomLoader() {
        throw new UnsupportedOperationException();
    }
}
