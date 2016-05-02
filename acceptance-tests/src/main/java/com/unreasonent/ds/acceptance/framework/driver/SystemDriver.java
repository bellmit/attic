package com.unreasonent.ds.acceptance.framework.driver;

import com.unreasonent.ds.DistantShore;
import com.unreasonent.ds.DistantShoreConfiguration;
import io.github.unacceptable.dropwizard.context.ApplicationContext;
import io.github.unacceptable.lazy.Lazily;
import io.github.unacceptable.selenium.context.SeleniumContext;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;

public class SystemDriver {
    public final ApplicationContext<?> app = new ApplicationContext<DistantShoreConfiguration>(
            DistantShore.class,
            null /* config path */
    );
    public final SeleniumContext selenium = new SeleniumContext();

    private WebUiDriver webUiDriver = null;
    
    public TestRule rules() {
        return RuleChain
                .outerRule(app.rules())
                .around(selenium.rules());
    }

    public WebUiDriver webUiDriver() {
        return webUiDriver = Lazily.create(webUiDriver, () -> new WebUiDriver(selenium, app));
    }
}
