package ca.grimoire.bom.maven;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Methods for manipulating paths and lists of paths.
 */
public final class Paths {
    private Paths() {
        throw new UnsupportedOperationException();
    }

    /**
     * Strips the base directory off of each element of a list of files. This is
     * equivalent to running {@link #strip(File, File)} on every element of
     * <var>files</var> with the same <var>baseDirectory</var>, so all of the
     * restrictions on that method apply to the elements of <var>files</var> as
     * well.
     * 
     * @param files
     *            the files to remove the prefix from.
     * @param baseDirectory
     *            the directory to remove from each file.
     * @return the relative paths to each file.
     * @throws IllegalArgumentException
     *             if any element of <var>files</var> is not a descendent of
     *             <var>baseDirectory</var>
     * @see #strip(File, File)
     */
    public static List<String> stripAll(List<File> files, File baseDirectory) {
        List<String> paths = new ArrayList<String>(files.size());
        for (File file : files) {
            paths.add(strip(file, baseDirectory));
        }
        return paths;
    }

    /**
     * Strips the base directory off of a file. The returned relative path will
     * satisfy the condition
     * 
     * <pre>
     * new File(baseDirectory, name).equals(file)
     * </pre>
     * 
     * provided <var>file</var> is a descendent of <var>baseDirectory</var>. In
     * particular, if <var>baseDirectory</var> is absolute, then <var>file</var>
     * must be as well.
     * 
     * @param file
     *            the file to strip.
     * @param baseDirectory
     *            the prefix to remove.
     * @return the relative path from <var>baseDirectory</var> to
     *         <var>file</var>.
     * @throws IllegalArgumentException
     *             if <var>file</var> is not a descendent of
     *             <var>baseDirectory</var>
     */
    public static String strip(File file, File baseDirectory) {
        LinkedList<String> names = new LinkedList<String>();
        for (File f = file; f == null || !f.equals(baseDirectory); f = f
                .getParentFile()) {
            if (f == null)
                throw new IllegalArgumentException(
                        String.format("File %s is not a descendent of %s",
                                file,
                                baseDirectory));

            names.addFirst(f.getName());
        }

        return join(names, File.separatorChar);
    }

    private static String join(List<String> names, char separator) {
        StringBuilder relativeName = new StringBuilder();
        for (String name : names) {
            if (relativeName.length() > 0)
                relativeName.append(separator);
            relativeName.append(name);
        }
        return relativeName.toString();
    }

}
