package io.github.unacceptable.dropwizard.driver;

import io.github.unacceptable.dropwizard.context.ApplicationContext;
import io.github.unacceptable.selenium.context.SeleniumContext;

/**
 * A Selenium driver associated with a specific ApplicationContext. In addition to the basic SeleniumDriver features,
 * this also provides convenient access to the ApplicationContext to its subclasses and clients.
 */
public class SeleniumDriver extends io.github.unacceptable.selenium.driver.SeleniumDriver {
    protected final ApplicationContext<?> app;

    public SeleniumDriver(SeleniumContext context, ApplicationContext<?> app) {
        super(context);
        this.app = app;
    }
}
