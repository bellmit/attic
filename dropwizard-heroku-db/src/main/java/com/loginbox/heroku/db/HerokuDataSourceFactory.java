package com.loginbox.heroku.db;

import io.dropwizard.db.DataSourceFactory;
import org.postgresql.Driver;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.regex.Pattern;

/**
 * A {@link io.dropwizard.db.DataSourceFactory} that automatically detects Heroku's PostgreSQL add-on. This will
 * automatically pick up the {@code DATABASE_URL} environment variable (if set), and configure JDBC appropriately. It
 * can also be used with other database variables, if your application includes more than one database.
 * <p>
 * If {@code DATABASE_URL} isn't set, this will do nothing; you can still configure your local database using a YAML
 * config file, as usual.
 */
public class HerokuDataSourceFactory extends DataSourceFactory {
    /**
     * Configure using the {@code DATABASE_URL} environment variable.
     */
    public HerokuDataSourceFactory() {
        this("DATABASE_URL");
    }

    /**
     * Configure using the named environment variable. This may be useful if you have a follower in your application:
     * pass the name of the follower (for example, {@code "HEROKU_POSTGRES_ONYX"}) to this constructor.
     *
     * @param databaseEnvironmentVariable
     *         the database environment variable to parse.
     */
    public HerokuDataSourceFactory(String databaseEnvironmentVariable) {
        setDriverClass(Driver.class.getCanonicalName());

        String databaseUrl = System.getenv(databaseEnvironmentVariable);
        if (databaseUrl != null)
            applyHerokuConfig(databaseUrl);
    }

    private void applyHerokuConfig(String databaseUrl) {
        try {
            URI databaseUri = new URI(databaseUrl);

            applyCredentials(databaseUri);
            applyUrl(databaseUri);
        } catch (URISyntaxException urise) {
            throw new IllegalArgumentException(urise);
        }
    }

    private void applyUrl(URI databaseUri) throws URISyntaxException {
        String host = databaseUri.getHost();
        int port = databaseUri.getPort();
        String path = databaseUri.getPath();
        URI connectionUri = new URI(
                "jdbc:postgresql",
                null,
                host,
                port,
                path,
                null,
                null);

        setUrl(connectionUri.toString());
    }

    private void applyCredentials(URI databaseUri) {
        String userInfo = databaseUri.getUserInfo();
        if (userInfo == null)
            return;
        String[] userInfoParts = userInfo.split(Pattern.quote(":"), 2);

        if (userInfoParts.length >= 1)
            setUser(userInfoParts[0]);
        if (userInfoParts.length >= 2)
            setPassword(userInfoParts[1]);
    }
}
