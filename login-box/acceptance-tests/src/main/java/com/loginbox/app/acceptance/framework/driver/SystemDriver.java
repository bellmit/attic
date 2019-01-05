package com.loginbox.app.acceptance.framework.driver;

import com.loginbox.app.LoginBox;
import com.loginbox.app.LoginBoxConfiguration;
import io.dropwizard.testing.ConfigOverride;
import io.github.unacceptable.database.DatabaseContext;
import io.github.unacceptable.dropwizard.context.ApplicationContext;
import io.github.unacceptable.lazy.Lazily;
import io.github.unacceptable.liquibase.LiquibaseEnhancer;
import io.github.unacceptable.selenium.context.SeleniumContext;
import org.junit.rules.ExternalResource;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class SystemDriver {
    public final DatabaseContext database = LiquibaseEnhancer.configureContext(new DatabaseContext());
    public final ApplicationContext<?> app = new ApplicationContext<LoginBoxConfiguration>(
            LoginBox.class,
            null, /* config path */
            ConfigOverride.config("database.url", database.databaseUrl()),
            ConfigOverride.config("database.user", database.username()),
            ConfigOverride.config("database.password", database.password())
    );
    public final SeleniumContext selenium = new SeleniumContext();

    private Client publicApiClient = null;
    private WebUiDriver webUiDriver = null;
    private PublicApiDriver publicApiDriver = null;
    private SetupPageDriver setupPageDriver = null;
    private AdminPageDriver adminPageDriver = null;
    private LandingPageDriver landingPageDriver = null;
    private ErrorPageDriver errorPageDriver = null;

    public Client publicApiClient() {
        return publicApiClient = Lazily.create(publicApiClient, () -> ClientBuilder.newClient());
    }

    public void shutdown() {
        quitPublicApiDriver();
    }

    public void quitPublicApiDriver() {
        if (publicApiClient != null) {
            publicApiClient.close();
            publicApiClient = null;
        }
    }

    public String baseUrl() {
        return app.url();
    }

    public WebUiDriver webUiDriver() {
        return webUiDriver = Lazily.create(webUiDriver, () -> new WebUiDriver(this.selenium, this.app));
    }

    public SetupPageDriver setupPageDriver() {
        return setupPageDriver = Lazily.create(setupPageDriver, () -> new SetupPageDriver(this.selenium, this.app));
    }

    public AdminPageDriver adminPageDriver() {
        return adminPageDriver = Lazily.create(adminPageDriver, () -> new AdminPageDriver(this.selenium, this.app));
    }

    public LandingPageDriver landingPageDriver() {
        return landingPageDriver = Lazily.create(landingPageDriver, () -> new LandingPageDriver(this.selenium, this.app));
    }

    public ErrorPageDriver errorPageDriver() {
        return errorPageDriver = Lazily.create(errorPageDriver, () -> new ErrorPageDriver(this.selenium, this.app));
    }

    public TestRule rules() {
        return RuleChain
                .outerRule(database.rules())
                .around(app.rules())
                .around(selenium.rules())
                .around(new ShutdownRule());
    }

    public PublicApiDriver publicApiDriver() {
        return publicApiDriver = Lazily.create(publicApiDriver, () -> new PublicApiDriver(this, baseUrl()));
    }

    private class ShutdownRule extends ExternalResource{
        @Override
        protected void after() {
            shutdown();
        }
    }
}
