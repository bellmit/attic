package com.unreasonent.ds.acceptance.framework;

import com.unreasonent.ds.acceptance.framework.context.TestContext;
import com.unreasonent.ds.acceptance.framework.driver.SystemDriver;
import com.unreasonent.ds.acceptance.framework.dsl.Api;
import io.github.unacceptable.rules.IncompleteTestSupport;
import org.junit.Rule;
import org.junit.rules.TestRule;

/**
 * Base class for DSL-driven tests. Provides DSL APIs to test suites, and ensures that the various drivers are set up
 * and torn down per test.
 * <p>
 * See the specific DSL entry points for further documentation.
 */
public class DslTestCase {
    private final SystemDriver systemDriver = new SystemDriver();
    private final TestContext testContext = new TestContext();

    /**
     * Web user interface DSL. See {@link Api the DSL class} for details.
     */
    public final Api api = new Api(systemDriver, testContext);

    /**
     * Not part of the testing API. Public only because of JUnit limitations.
     */
    @Rule
    public final TestRule systemDriverRules = systemDriver.rules();

    /**
     * Not part of the testing API. Public only because of JUnit limitations.
     */
    @Rule
    public final TestRule incompleteTestRule = new IncompleteTestSupport();
}
