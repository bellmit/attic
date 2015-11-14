package io.github.unacceptable.dropwizard.context;

import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

public class ApplicationContextTest {
    @Before
    public void configureProperties() {
        System.clearProperty("app.url");
    }

    public static class TestConfig extends Configuration {
    }

    @Path("/hello")
    public static class TestResource {
        @GET
        public String hello() {
            return "Hello, world.";
        }
    }

    public static class TestApp extends Application<TestConfig> {
        @Override
        public void run(TestConfig configuration, Environment environment) throws Exception {
            environment.jersey().register(TestResource.class);
        }
    }

    @Test
    public void urlPaths() {
        ApplicationContext<TestConfig> applicationContext = spy(new ApplicationContext<TestConfig>() {
            @Override
            protected Class<? extends Application<TestConfig>> mainClass() {
                return TestApp.class;
            }
        });
        doReturn("https://app.example.com/").when(applicationContext).url();

        assertThat(
                applicationContext.url("/path/to/resource"),
                equalTo("https://app.example.com/path/to/resource"));
        assertThat(
                applicationContext.url("path/to/resource"),
                equalTo("https://app.example.com/path/to/resource"));
        assertThat( // surprise!
                applicationContext.url("//////////path/to/resource/"),
                equalTo("https://app.example.com/path/to/resource/"));
    }

    @Test
    public void detectsOneShotApp() throws Throwable {
        ApplicationContext<TestConfig> applicationContext = new ApplicationContext<TestConfig>() {
            @Override
            protected Class<? extends Application<TestConfig>> mainClass() {
                return TestApp.class;
            }
        };

        assertThat(
                applicationContext.rules(),
                instanceOf(DropwizardAppRule.class));
        applicationContext
                .rules()
                .apply(
                        new Statement() {
                            @Override
                            public void evaluate() throws Throwable {
                                get(applicationContext.url("/hello"))
                                        .then()
                                        .body(equalTo("Hello, world."));
                            }
                        },
                        Description.createSuiteDescription(getClass()))
                .evaluate();
        ;
    }

    @Test
    public void detectsExistingApp() {
        System.setProperty("app.url", "https://example.com/");
        ApplicationContext<TestConfig> applicationContext = new ApplicationContext<TestConfig>() {
            @Override
            protected Class<? extends Application<TestConfig>> mainClass() {
                return TestApp.class;
            }
        };

        assertThat(
                applicationContext.rules(),
                not(instanceOf(DropwizardAppRule.class)));
        assertThat(
                applicationContext.url(),
                equalTo("https://example.com/"));
    }
}
