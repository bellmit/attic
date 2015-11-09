package io.github.unacceptable.selenium.context;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.invocation.InvocationOnMock;
import org.openqa.selenium.WebDriver;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Mockito.RETURNS_MOCKS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

public class SeleniumContextTest {
    private Set<WebDriver> activeDrivers = new HashSet<>();

    @Test
    public void driverLifecycle() {
        SeleniumContext context = makeContext();

        WebDriver driver = context.webDriver();
        assertThat(activeDrivers, contains(driver));

        WebDriver again = context.webDriver();
        assertThat(again, sameInstance(driver));

        context.quit();
        assertThat(activeDrivers, not(contains(driver)));

        WebDriver restarted = context.webDriver();
        assertThat(activeDrivers, contains(restarted));
        assertThat(restarted, not(sameInstance(driver)));
    }

    @Test
    public void multipleQuits() {
        SeleniumContext context = makeContext();

        context.quit();
        context.quit();
    }

    @Test
    public void rule() throws Throwable {
        SeleniumContext context = makeContext();

        WebDriver driver = context.webDriver();

        Statement statement = mock(Statement.class);
        Description description = mock(Description.class);

        context.rules()
                .apply(statement, description)
                .evaluate();

        assertThat(activeDrivers, not(contains(driver)));
    }

    private SeleniumContext makeContext() {
        SeleniumContext context = spy(SeleniumContext.class);
        doAnswer(this::makeNewDriver).when(context).createDriver();
        return context;
    }

    private WebDriver makeNewDriver(InvocationOnMock invocation) {
        WebDriver driver = mock(WebDriver.class, RETURNS_MOCKS);
        doAnswer(i -> driverClosed(driver))
                .when(driver)
                .quit();

        activeDrivers.add(driver);
        return driver;
    }

    private Object driverClosed(WebDriver driver) {
        activeDrivers.remove(driver);
        return null;
    }
}