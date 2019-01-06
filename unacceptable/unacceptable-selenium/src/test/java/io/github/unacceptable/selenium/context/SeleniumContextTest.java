package io.github.unacceptable.selenium.context;

import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.mockito.invocation.InvocationOnMock;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.sameInstance;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SeleniumContextTest {
    private Set<WebDriver> activeDrivers = new HashSet<>();

    @Test
    public void driverLifecycle() {
        SeleniumContext context = makeContext();

        WebDriver driver = context.webDriver();
        assertThat(activeDrivers, contains(driver));
        verify(driver.manage().timeouts())
                .implicitlyWait(anyInt(), eq(TimeUnit.SECONDS));
        verify(driver.manage().window())
                .setSize(any(Dimension.class));

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
        WebDriver driver = mock(WebDriver.class, RETURNS_DEEP_STUBS);
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