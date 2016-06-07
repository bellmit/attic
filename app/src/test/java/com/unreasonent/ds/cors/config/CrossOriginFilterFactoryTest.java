package com.unreasonent.ds.cors.config;

import io.dropwizard.jetty.setup.ServletEnvironment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.junit.Before;
import org.junit.Test;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CrossOriginFilterFactoryTest {
    private final CrossOriginFilterFactory factory = new CrossOriginFilterFactory();

    private final ServletEnvironment servlets = mock(ServletEnvironment.class);
    private final FilterRegistration.Dynamic filter = mock(FilterRegistration.Dynamic.class);

    @Before
    public void wireMocks() {
        doReturn(filter).when(servlets).addFilter("CrossOriginFilter", CrossOriginFilter.class);
    }

    @Test
    public void registersFilter() {
        factory.setOrigins("http://unit.example.com");

        factory.registerFilter(servlets);

        verify(servlets).addFilter("CrossOriginFilter", CrossOriginFilter.class);
        verify(filter).setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "http://unit.example.com");
        verify(filter).setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "Authorization,X-Requested-With,Content-Type,Accept,Origin");
        verify(filter).addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST),
                false,
                "*"
        );
    }
}