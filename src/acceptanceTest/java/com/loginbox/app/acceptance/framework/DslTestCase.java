package com.loginbox.app.acceptance.framework;

import com.loginbox.app.acceptance.framework.context.TestContext;
import com.loginbox.app.acceptance.framework.driver.SystemDriver;
import com.loginbox.app.acceptance.framework.page.WebUi;
import org.junit.After;

/**
 * Base class for DSL-driven tests. Provides DSL APIs to test suites, and ensures that the various drivers are set up
 * and torn down per test.
 * <p>
 * For example:
 * <pre>
 * public class Example {
 *     @Test
 *     public void openUi() {
 *         webUi.open();
 *     }
 * }
 * </pre>
 * <p>
 * See the specific DSL entry points for further documentation.
 */
public class DslTestCase {
    private final SystemDriver systemDriver = new SystemDriver();
    private final TestContext testContext = new TestContext();

    /**
     * Web user interface DSL. See {@link WebUi the DSL class} for details.
     */
    protected final WebUi webUi = new WebUi(systemDriver, testContext);

    @After
    public void shutdown() {
        systemDriver.shutdown();
    }
}
