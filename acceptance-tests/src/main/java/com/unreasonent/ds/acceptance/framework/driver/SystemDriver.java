package com.unreasonent.ds.acceptance.framework.driver;

import com.unreasonent.ds.DistantShore;
import com.unreasonent.ds.DistantShoreConfiguration;
import com.unreasonent.ds.acceptance.framework.context.JwtTokenGenerator;
import com.unreasonent.ds.acceptance.framework.rest.ApiContext;
import io.dropwizard.testing.ConfigOverride;
import io.github.unacceptable.dropwizard.context.ApplicationContext;
import io.github.unacceptable.lazy.Lazily;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

public class SystemDriver {
    public final ApplicationContext<?> app = new ApplicationContext<DistantShoreConfiguration>(
            DistantShore.class,
            null /* config path */,
            ConfigOverride.config("oauth.secret", JwtTokenGenerator.DEFAULT_SECRET_BASE64) /* "secret" */
    );

    public final ApiContext apiContext = new ApiContext(app);

    private WhoAmIDriver whoAmIDriver = null;

    public TestRule rules() {
        return RuleChain
                .outerRule(app.rules());
    }

    public WhoAmIDriver whoAmIDriver() {
        return whoAmIDriver = Lazily.create(whoAmIDriver, () -> new WhoAmIDriver(apiContext));
    }
}
