package com.unreasonent.ds.acceptance.framework.driver;

import com.unreasonent.ds.DistantShore;
import com.unreasonent.ds.DistantShoreConfiguration;
import com.unreasonent.ds.acceptance.framework.context.JwtTokenGenerator;
import com.unreasonent.ds.acceptance.framework.rest.ApiContext;
import io.dropwizard.testing.ConfigOverride;
import io.github.unacceptable.database.DatabaseContext;
import io.github.unacceptable.dropwizard.context.ApplicationContext;
import io.github.unacceptable.lazy.Lazily;
import io.github.unacceptable.liquibase.LiquibaseEnhancer;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

public class SystemDriver {
    public final DatabaseContext database = LiquibaseEnhancer.configureContext(new DatabaseContext());
    public final ApplicationContext<?> app = new ApplicationContext<DistantShoreConfiguration>(
            DistantShore.class,
            null /* config path */,
            ConfigOverride.config("database.url", database.databaseUrl()),
            ConfigOverride.config("database.user", database.username()),
            ConfigOverride.config("database.password", database.password()),
            ConfigOverride.config("oauth.secret", JwtTokenGenerator.DEFAULT_SECRET_BASE64) /* "secret" */
    );

    public final ApiContext apiContext = new ApiContext(app);

    private WhoAmIDriver whoAmIDriver = null;

    public TestRule rules() {
        return RuleChain
                .outerRule(database.rules())
                .around(app.rules());
    }

    public WhoAmIDriver whoAmIDriver() {
        return whoAmIDriver = Lazily.create(whoAmIDriver, () -> new WhoAmIDriver(apiContext));
    }
}
