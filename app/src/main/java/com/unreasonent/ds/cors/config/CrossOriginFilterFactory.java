package com.unreasonent.ds.cors.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jetty.setup.ServletEnvironment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.validation.constraints.NotNull;
import java.util.EnumSet;

public class CrossOriginFilterFactory {
    /* The default Jetty set, plus Authorization to allow clients to pass JWT tokens instead of browser creds. */
    private static final String ALLOWED_HEADERS = "Authorization,X-Requested-With,Content-Type,Accept,Origin";

    @NotNull
    @NotEmpty
    private String origins = defaultOrigins();

    private static String defaultOrigins() {
        String origins = System.getenv("CORS_ORIGINS");
        if (origins != null)
            return origins;
        return "http://localhost:*";
    }

    @JsonProperty
    public String getOrigins() {
        return origins;
    }

    @JsonProperty
    public void setOrigins(String origins) {
        this.origins = origins;
    }

    public void registerFilter(ServletEnvironment servlets) {
        FilterRegistration.Dynamic filter = servlets.addFilter("CrossOriginFilter", CrossOriginFilter.class);
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, origins);
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, ALLOWED_HEADERS);

        filter.addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST),
                /* isMatchAfter */ false,
                "*")
        ;
    }
}
