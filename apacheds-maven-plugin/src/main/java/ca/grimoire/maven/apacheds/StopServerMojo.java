package ca.grimoire.maven.apacheds;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;

/**
 * Stops an LDAP server previously started by <code>apacheds:start</code>.
 * 
 * @goal stop
 */
public class StopServerMojo extends AbstractMojo {

    /**
     * @component
     */
    private LdapServerLifecycle ldapServerLifecycle;

    @Override
    public void execute() throws MojoExecutionException {
        try {
            ldapServerLifecycle.stopServer();
        } catch (Exception e) {
            throw new MojoExecutionException(
                    "Unable to shut down LDAP server.", e);
        }
    }

}
