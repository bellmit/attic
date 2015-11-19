package io.github.unacceptable.liquibase;

import io.github.unacceptable.database.DatabaseContext;
import liquibase.Liquibase;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;

import java.sql.Connection;
import java.sql.SQLException;

import static io.github.unacceptable.lazy.Lazily.systemProperty;

/**
 * A utility for a database managed by Liquibase migrations. This will automatically apply migrations (by
 * default, from {@code migrations.xml}) when creating a test database.
 * <p>
 * By default, this context will apply <em>all</em> migrations. However, the "migration.contexts" property can be set,
 * or the {@link #defaultContexts() default contexts} provided, to take advantage of Liquibase's migration contexts.
 * These can be used to include test data in the migrations, if needed.
 *
 * @see io.github.unacceptable.database.DatabaseContext
 */
public class LiquibaseEnhancer {
    private String migrationsUrl;
    private String contexts;

    /**
     * Static helper for {@link #configure}.  The standard way to use this within the system driver looks like this:
     * <pre>
     * DatabaseContext database = LiquibaseEnhancer.configureContext(new DatabaseContext());
     * </pre>
     */
    public static <C extends DatabaseContext> C configureContext(C context) {
        return new LiquibaseEnhancer().configure(context);
    }

    /**
     * Configures a {@link DatabaseContext} to run the Liquibase migrations as part of the
     * {@link DatabaseContext#rules} using {@link #migrate(DatabaseContext)}.
     *
     * @return The same {@link DatabaseContext} as the param, but configured to run Liquibase migrations.
     */
    public <C extends DatabaseContext> C configure(C context) {
        context.injectDatabaseTask(() -> buildRules(context));
        return context;
    }

    /**
     * Reads the migrations resource path from the {@code migration.path} System property, defaulting to {@link
     * #defaultMigrationsResource()} if not set.
     *
     * @return the resource path of the root migrations list.
     */
    public String migrationsResource() {
        return migrationsUrl = systemProperty(migrationsUrl, "migration.path", this::defaultMigrationsResource);
    }

    /**
     * @return the default migrations resource path.
     */
    protected String defaultMigrationsResource() {
        return "migrations.xml";
    }

    /**
     * Reads the migration contexts from the "migration.contexts" system property, defaulting to {@link
     * #defaultContexts()} if not set.
     *
     * @return the list of migration contexts, as a comma-separated string, to apply to the database.
     * @see liquibase.Contexts
     */
    public String contexts() {
        return contexts = systemProperty(contexts, "migration.contexts", this::defaultContexts);
    }

    /**
     * @return the default list of migration contexts, as a comma-separated string.
     */
    protected String defaultContexts() {
        return "";
    }

    /**
     * @return a test rule that ensures a test database will be set up <em>and migrated</em> before the execution of the
     * wrapped tests, and destroyed after each test.
     */
    protected TestRule buildRules(DatabaseContext context) {
        return new MigrationRule(context);
    }

    /**
     * Immediately migrate the database. This will be applied automatically during tests if
     * {@link #configureContext(DatabaseContext)} is used.
     *
     * @throws SQLException       if the test database is unavailable.
     * @throws LiquibaseException if the migration process fails.
     */
    public void migrate(DatabaseContext context) throws SQLException, LiquibaseException {
        try (Connection connection = context.openConnection()) {
            migrate(connection);
        }
    }

    /**
     * Apply migrations to a specific connection.
     *
     * @param connection the connection to apply migrations on.
     * @throws LiquibaseException if the migration process fails.
     */
    protected void migrate(Connection connection) throws LiquibaseException {
        Liquibase liquibase = makeLiquibase(connection);
        migrate(liquibase);
    }

    private void migrate(Liquibase liquibase) throws LiquibaseException {
        String contexts = contexts();
        liquibase.update(contexts);
    }

    private Liquibase makeLiquibase(Connection connection) throws LiquibaseException {
        String changeLogFile = migrationsResource();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        ClassLoaderResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor(classLoader);
        JdbcConnection jdbcConnection = new JdbcConnection(connection);

        return new Liquibase(changeLogFile, resourceAccessor, jdbcConnection);
    }

    private class MigrationRule extends ExternalResource {
        private final DatabaseContext context;

        public MigrationRule(final DatabaseContext context) {
            this.context = context;
        }

        @Override
        public void before() throws SQLException, LiquibaseException {
            migrate(context);
        }
    }
}
