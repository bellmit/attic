package com.unreasonent.ds.acceptance.framework.driver;

import com.unreasonent.ds.DistantShore;
import com.unreasonent.ds.DistantShoreConfiguration;
import io.github.unacceptable.dropwizard.context.ApplicationContext;
import io.github.unacceptable.lazy.Lazily;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

public class SystemDriver {
    public final ApplicationContext<?> app = new ApplicationContext<DistantShoreConfiguration>(
            DistantShore.class,
            null /* config path */
    );

    private ApiDriver apiDriver = null;

    public TestRule rules() {
        return RuleChain
                .outerRule(app.rules());
    }

    public ApiDriver apiDriver() {
        return apiDriver = Lazily.create(apiDriver, () -> new ApiDriver(app));
    }
}
