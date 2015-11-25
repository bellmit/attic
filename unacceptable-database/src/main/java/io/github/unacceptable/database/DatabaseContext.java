package io.github.unacceptable.database;

import io.github.unacceptable.alias.UsernameGenerator;
import io.github.unacceptable.lazy.Lazily;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static io.github.unacceptable.lazy.Lazily.systemProperty;

/**
 * Manages a temporary testing database during a test. This makes a few assumptions about the structure of your
 * database:
 * <ul>
 * <li>To create a database, a client must connect using a privileged "admin" connection, then issue {@code CREATE
 * DATABASE "foo"} on that connection.</li>
 * <li>To drop a database, the client must make a second privileged connection, then issue {@code DROP DATABASE
 * "foo"}.</li>
 * <li>The database will respect SQL quoting standards (MySQL is known not to support them by default).</li>
 * </ul>
 * <p>The accessor methods defined in this class default to picking up configuration using {@link
 * System#getProperty(String) system properties}, but may be overridden. They may be called during construction, so
 * must be safe to call before the constructor completes.
 */
public class DatabaseContext {
    private String template = null;
    private String databaseName = null;
    private String adminUrl = null;
    private String username = null;
    private String password = null;
    private String databaseUrl = null;
    private TestRule testRules = null;

    /**
     * @return the generated URL for the test database.
     */
    public String databaseUrl() {
        return databaseUrl = Lazily.create(databaseUrl, this::buildDatabaseUrl);
    }

    /**
     * @return the JDBC URL to use when making administrative connections to the database.
     */
    public String adminUrl() {
        return adminUrl = systemProperty(adminUrl, "database.adminUrl", this::defaultAdminUrl);
    }

    protected String defaultAdminUrl() {
        return "jdbc:postgresql://localhost/postgres";
    }

    /**
     * @return the username to connect to the database server with. This user must be able to create and destroy
     * databases.
     */
    public String username() {
        return username = systemProperty(username, "database.username", this::defaultUsername);
    }

    protected String defaultUsername() {
        return "postgres";
    }

    /**
     * @return the password to connect to the database server with, if any.
     */
    public String password() {
        return password = systemProperty(password, "database.password", this::defaultPassword);
    }

    protected String defaultPassword() {
        return "";
    }

    /**
     * @return the computed name of this context's test database.
     */
    public String databaseName() {
        String template = templateDatabaseName();
        return databaseName = Lazily.create(databaseName, () -> UsernameGenerator.defaultGenerate(template));
    }

    /**
     * @return the template to use when {@link #databaseName() computing the database name}.
     */
    public String templateDatabaseName() {
        return template = systemProperty(template, "database.nameTemplate", this::defaultTemplateDatabaseName);
    }

    protected String defaultTemplateDatabaseName() {
        return "acceptance-test";
    }

    /**
     * Returns rules that ensure a scratch database will be created before a test, and destroyed afterwards.
     *
     * @return a rule that manages the life of a testing database.
     */
    public TestRule rules() {
        return testRules = Lazily.create(testRules, this::constructRules);
    }

    protected TestRule constructRules() {
        return new TemporaryDatabaseRule();
    }

    /**
     * Can be used to inject rules (for example, rules for initializing the schema) which will run after the temporary
     * database is created, and before the temporary database is destroyed.
     *
     * @param task
     *         the task whose rules will be injected into this context's rules chain. The task's rules will be run
     *         <em>after</em> any existing rules, including the rule controlling database creation and any
     *         previously-injected rules.
     */
    public void injectDatabaseTask(DatabaseTask task) {
        final TestRule existingRule = rules();
        testRules = RuleChain.outerRule(existingRule)
                .around(task.rules());
    }

    /**
     * Runs an SQL statement (passed as a single String) on a connection to the {@link #adminUrl() administrative
     * database}.
     *
     * @param statement
     *         the SQL statement to execute.
     * @throws SQLException
     *         if execution of the statement fails.
     */
    protected void runStatement(String statement) throws SQLException {
        try (Connection connection = openAdminConnection()) {
            runStatement(connection, statement);
        }
    }

    /**
     * Runs an SQL statement (passed as a single String) on a Connection, cleaning up statement objects automatically.
     *
     * @param connection
     *         the connection to operate on.
     * @param statement
     *         the statement to execute.
     * @throws SQLException
     *         if execution of the statement fails.
     */
    protected void runStatement(Connection connection, String statement) throws SQLException {
        try (PreparedStatement s = connection.prepareStatement(statement)) {
            s.executeUpdate();
        }
    }

    /**
     * Creates a connection to the administrative database.
     *
     * @return a new Connection to the administrative database.
     * @throws SQLException
     *         if unable to connect to the administrative database.
     */
    protected Connection openAdminConnection() throws SQLException {
        return DriverManager.getConnection(adminUrl(), username(), password());
    }

    /**
     * Creates a connection to the test database.
     *
     * @return a new Connection to the test database.
     * @throws SQLException
     *         if unable to connect to the testdatabase.
     */
    public Connection openConnection() throws SQLException {
        return DriverManager.getConnection(databaseUrl(), username(), password());
    }

    /**
     * Create the test database. By default, this {@link #runStatement(String) runs} {@code CREATE DATABASE "database
     * name"}.
     * <p>
     * This will happen automatically for tests using the {@link #rules()}.
     *
     * @throws SQLException
     *         if creating the test database fails.
     */
    public void create() throws SQLException {
        String createQuery = String.format("CREATE DATABASE \"%s\"", databaseName());
        runStatement(createQuery);
    }

    /**
     * Destroy the test database. By default, this {@link #runStatement(String) runs} {@code DROP DATABASE "database
     * name"}.
     * <p>
     * This will happen automatically for tests using the {@link #rules()}.
     *
     * @throws SQLException
     *         if destroying the test database fails.
     */
    public void destroy() throws SQLException {
        String dropQuery = String.format("DROP DATABASE \"%s\"", databaseName());
        runStatement(dropQuery);
    }

    /**
     * Construct a URL for the test database. By default, this assumes that the admin URL's scheme-specific part is
     * itself a URL, and that the path of that URL can be changed to any {@code /DATABASE-NAME}. This is appropriate for
     * PostgreSQL, MySQL, and several other databases.
     * <p>
     * When overriding this method, values such as {@link #databaseName()} the database name} must be obtained from the
     * public methods of this class.
     *
     * @return the URL of the test database.
     */
    protected String buildDatabaseUrl() {
        try {
            URI adminJdbcUri = new URI(adminUrl());                       // jdbc:postgres://foo@bar:port/admin?params=foo
            URI adminUri = new URI(adminJdbcUri.getSchemeSpecificPart()); // postgres://foo@bar:port/admin?params=foo
            URI databaseUri = new URI(                                    // postgres://foo@bar:port/test-db?params=foo
                    adminUri.getScheme(),
                    adminUri.getUserInfo(),
                    adminUri.getHost(),
                    adminUri.getPort(),
                    String.format("/%s", databaseName()),
                    adminUri.getQuery(),
                    adminUri.getFragment()
            );
            URI databaseJdbcUri = new URI(                                // jdbc:postgres://foo@bar:port/test-db?params=foo
                    adminJdbcUri.getScheme(),
                    databaseUri.toString(),
                    null);
            return databaseJdbcUri.toString();
        } catch (URISyntaxException urise) {
            throw new IllegalArgumentException(urise);
        }
    }

    private class TemporaryDatabaseRule extends ExternalResource {
        @Override
        public void before() throws SQLException {
            create();
        }

        @Override
        public void after() {
            try {
                destroy();
            } catch (SQLException sqle) {
                // Fuck it.
                throw new RuntimeException(sqle);
            }
        }
    }
}
