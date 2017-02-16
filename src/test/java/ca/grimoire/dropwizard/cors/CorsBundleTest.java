package ca.grimoire.dropwizard.cors;

import ca.grimoire.dropwizard.cors.config.CrossOriginFilterFactory;
import ca.grimoire.dropwizard.cors.config.CrossOriginFilterFactoryHolder;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CorsBundleTest {
    private class Configuration
            extends io.dropwizard.Configuration
            implements CrossOriginFilterFactoryHolder {

        @Override
        public CrossOriginFilterFactory getCors() {
            return cors;
        }
    }

    private final CorsBundle bundle = new CorsBundle();

    private final CrossOriginFilterFactory cors = mock(CrossOriginFilterFactory.class);
    private final Configuration configuration = new Configuration();

    @SuppressWarnings("unchecked")
    protected final Bootstrap<Configuration> bootstrap = mock(Bootstrap.class);
    protected final Environment environment = mock(Environment.class);
    protected final ServletEnvironment servlets = mock(ServletEnvironment.class);

    @Before
    public void wireMocks() {
        when(environment.servlets()).thenReturn(servlets);
    }

    @Test
    public void registersFilters() throws Exception {
        bundle.run(configuration, environment);

        verify(cors).registerFilter(servlets);
    }
}
