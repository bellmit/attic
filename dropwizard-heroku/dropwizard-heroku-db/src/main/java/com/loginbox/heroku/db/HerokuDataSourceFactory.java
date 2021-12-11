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
        /*
         * <rant>
         *     There doesn't seem to be a way to safely decompose a URI into its component pieces and reassemble it. In
         *     particular, there's no way to avoid mangling the query part in some way: `getQuery()` -- used below --
         *     will un-quote quoted characters, which can change the semantics of the URL by, for example, turning
         *     `foo=%26&bar=bar` into `foo=&&bar=bar`, while `getRawQuery()` -- which returns the already-escaped form
         *     -- can't be passed to any of URI's constructors without re-escaping the query and turning
         *     `foo=%26&bar=bar` into `foo=%2526&bar=bar`.
         *
         *     I've opted for the former. There aren't many PostgreSQL JDBC client options that could plausibly contain
         *     a percent sign, a question mark, or an ampersand. Looking at the 9.4 client docs, I only see these
         *     likely suspects:
         *
         *     * sslfactoryarg, which is only set when users supply their own SSL socket factory -- rare
         *     * ApplicationName, which almost never contains punctuation in practice
         *
         *     User bug reports will invariably tell me I've missed a case, I'm sure, but the bottom line here is that
         *     java.net.URI is a terribly-designed class. That it's the SECOND attempt at creating a standard URI class
         *     is damning.
         * </rant>
         */
        String query = databaseUri.getQuery();
        URI connectionUri = new URI(
                "jdbc:postgresql",
                null,
                host,
                port,
                path,
                query,
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
