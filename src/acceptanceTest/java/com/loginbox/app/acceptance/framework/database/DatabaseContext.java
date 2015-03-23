package com.loginbox.app.acceptance.framework.database;

import com.loginbox.app.acceptance.framework.context.UsernameGenerator;
import io.dropwizard.testing.ConfigOverride;
import org.junit.rules.RuleChain;

import java.net.URI;
import java.net.URISyntaxException;

public class DatabaseContext {
    private final String adminUrl;
    private final String username;
    private final String password;
    private final String databaseName;
    private final String databaseUrl;

    public DatabaseContext(String adminUrl, String username, String password) {
        this.adminUrl = adminUrl;
        this.username = username;
        this.password = password;
        this.databaseName = UsernameGenerator.generate("login-box-acceptance-test");

        this.databaseUrl = buildDatabaseUrl();
    }

    public ConfigOverride databaseUrl() {
        return ConfigOverride.config("database.url", databaseUrl);
    }

    public ConfigOverride user() {
        return ConfigOverride.config("database.user", username);
    }

    public ConfigOverride password() {
        if (password == null)
            return ConfigOverride.config("database.password", "");
        return ConfigOverride.config("database.password", password);
    }

    public RuleChain rules() {
        return RuleChain.outerRule(makeTestDatabaseRule())
                .around(makeMigrationRule());
    }

    private TemporaryDatabaseRule makeTestDatabaseRule() {
        return new TemporaryDatabaseRule(adminUrl, username, password, databaseName);
    }

    private MigrateDatabaseRule makeMigrationRule() {
        return new MigrateDatabaseRule(databaseUrl, username, password);
    }

    private String buildDatabaseUrl() {
        try {
            URI adminJdbcUri = new URI(adminUrl);
            URI adminUri = new URI(adminJdbcUri.getSchemeSpecificPart());
            URI databaseUri = new URI(
                    adminUri.getScheme(),
                    adminUri.getUserInfo(),
                    adminUri.getHost(),
                    adminUri.getPort(),
                    String.format("/%s", databaseName),
                    adminUri.getQuery(),
                    adminUri.getFragment()
            );
            URI databaseJdbcUri = new URI(
                    adminJdbcUri.getScheme(),
                    databaseUri.toString(),
                    null);
            return databaseJdbcUri.toString();
        } catch (URISyntaxException urise) {
            throw new IllegalArgumentException(urise);
        }
    }
}
