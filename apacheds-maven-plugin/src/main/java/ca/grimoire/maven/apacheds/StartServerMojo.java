package ca.grimoire.maven.apacheds;

import java.io.File;
import java.util.List;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Starts an LDAP server.
 * 
 * @goal start
 */
public class StartServerMojo extends AbstractMojo {

    /**
     * The TCP port number to run Apache DS on.
     * 
     * @parameter expression="${ldap.port}" default-value="10389"
     */
    private int port;

    /**
     * The directory to store the LDAP directory data in.
     * 
     * @parameter expression="${ldap.workingDirectory}"
     *            default-value="${project.build.directory}/apache-ds/"
     */
    private File workingDirectory;

    /**
     * An LDIF file or directory to preload the LDAP directory against.
     * 
     * @parameter optional="true" expression="${ldap.ldifDirectory}"
     */
    private File ldifDirectory;

    /**
     * Partitions to create in the LDAP server.
     * 
     * @parameter
     */
    private List<Partition> partitions;

    /**
     * @component
     */
    private LdapServerLifecycle ldapServerLifecycle;

    /**
     * @parameter expression="${plugin.artifacts}"
     * @required
     * @readonly
     */
    private List<Artifact> pluginArtifacts;

    @Override
    public void execute() throws MojoExecutionException {
        String originalClasspath = System.getProperty("java.class.path");
        String classpath = buildPluginClasspath();
        System.setProperty("java.class.path", classpath);
        try {
            ldapServerLifecycle.reset(workingDirectory);
            if (partitions != null)
                ldapServerLifecycle.addPartitions(partitions);
            ldapServerLifecycle.configureLdapServer(port);
            if (ldifDirectory != null)
                ldapServerLifecycle.configureLdifDirectory(ldifDirectory);
            ldapServerLifecycle.startServer();
        } catch (Exception e) {
            throw new MojoExecutionException("Failed to start LDAP server.", e);
        } finally {
            System.setProperty("java.class.path", originalClasspath);
        }
    }

    private String buildPluginClasspath() {
        StringBuilder sb = new StringBuilder();
        for (Artifact pluginDependency : pluginArtifacts) {
            if (sb.length() > 0)
                sb.append(File.pathSeparator);
            sb.append(pluginDependency.getFile().toURI());
        }
        return sb.toString();
    }
}
