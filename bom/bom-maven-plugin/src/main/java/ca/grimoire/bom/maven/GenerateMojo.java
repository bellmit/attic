package ca.grimoire.bom.maven;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.maven.model.Resource;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectHelper;
import org.apache.tools.ant.types.selectors.SelectorUtils;

/**
 * Generates a bill of materials by scanning the resources in the project.
 * 
 * @goal generate
 * @phase generate-resources
 * @requiresProject
 */
public class GenerateMojo extends AbstractMojo {

    /**
     * The directory to create BOM files under. This directory will be added as
     * a resource tree to the project artifact.
     * 
     * @parameter default-value="${project.build.directory}/bom"
     * @required
     */
    private File outputDirectory;

    /**
     * The directory within the JAR to store BOM files under.
     * 
     * @parameter default-value="META-INF/bom/"
     * @required
     */
    private String directory;

    /**
     * The name of the BOM file to generate.
     * 
     * @parameter expression="${bom.name}"
     * @required
     */
    private String name;

    /**
     * The encoding to use when writing BOM files.
     * 
     * @parameter expression="${bom.encoding}" default-value="UTF-8"
     * @required
     */
    private String encoding;

    /**
     * A list of Ant-style patterns specifying files to include in the BOM. If
     * not specified, every file is included.
     * 
     * @parameter
     */
    private List<String> includes = Arrays.asList("**/*");

    /**
     * A list of Ant-style patterns specifying files to exclude from the BOM. If
     * not specified, no files are excluded.
     * 
     * @parameter
     */
    private List<String> excludes = Collections.emptyList();

    /**
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    private MavenProject project;

    /**
     * @component
     */
    private MavenProjectHelper projectHelper;

    @Override
    public void execute() throws MojoExecutionException {
        List<String> bomEntries = findAllBomEntries(project.getResources());
        try {
            writeBom(bomEntries);
        } catch (IOException ioe) {
            throw new MojoExecutionException("Unable to write BOM.", ioe);
        }
        addBomResource();
    }

    private void addBomResource() {
        if (!hasBomResource(project))
            projectHelper.addResource(project,
                    outputDirectory.toString(),
                    Collections.EMPTY_LIST,
                    Collections.EMPTY_LIST);
    }

    private boolean hasBomResource(MavenProject project) {
        for (Object o : project.getResources()) {
            Resource resource = (Resource) o;
            if (isBomOutputResource(resource))
                return true;
        }
        return false;
    }

    private List<String> findAllBomEntries(List<?> resources) {
        List<String> bomEntries = new ArrayList<String>();

        for (Object o : resources) {
            Resource resource = (Resource) o;
            if (!isBomOutputResource(resource))
                bomEntries.addAll(findBomEntries(resource));
        }
        return bomEntries;
    }

    private boolean isBomOutputResource(Resource resource) {
        return new File(resource.getDirectory()).getAbsoluteFile()
                .equals(outputDirectory.getAbsoluteFile());
    }

    private List<String> findBomEntries(Resource resource) {
        // nb. the docs for getDirectory claim the returned path is relative
        // to the directory the POM is in. This is a damnable lie as far as
        // I can tell: the returned path has been absolute every time I
        // looked at it.
        String resourcePath = resource.getDirectory();
        File resourceDirectory = new File(resourcePath);
        List<File> bomFiles = findBomFiles(resourceDirectory);
        List<String> paths = Paths.stripAll(bomFiles, resourceDirectory);
        return paths;
    }

    private PrintWriter openForWriting(File directory,
            String name,
            String encoding) throws UnsupportedEncodingException,
            FileNotFoundException {
        return new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(
                new FileOutputStream(new File(directory, name))), encoding));
    }

    private List<File> findBomFiles(File resourceDirectory) {
        List<File> bomEntries = new ArrayList<File>();
        for (File element : resourceDirectory.listFiles()) {
            if (element.isDirectory())
                bomEntries.addAll(findBomFiles(element));
            else if (accept(element))
                bomEntries.add(element);
        }
        return bomEntries;
    }

    private boolean accept(File element) {
        for (String excludePattern : excludes)
            if (SelectorUtils.match(excludePattern, element.getPath()))
                return false;
        for (String includePattern : includes)
            if (SelectorUtils.match(includePattern, element.getPath()))
                return true;
        return false;
    }

    private void writeBom(List<String> bomEntries)
            throws UnsupportedEncodingException, FileNotFoundException {
        File bomDirectory = ensureBomDirectoryExists(outputDirectory, directory);

        PrintWriter out = openForWriting(bomDirectory, name, encoding);
        try {
            for (String entry : bomEntries)
                out.println(entry);
        } finally {
            out.close();
        }
    }

    private File ensureBomDirectoryExists(File outputDirectory, String bomPath) {
        File bomDirectory = new File(outputDirectory, bomPath);
        bomDirectory.mkdirs();
        return bomDirectory;
    }
}
