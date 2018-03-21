package ca.grimoire.dropwizard.cors.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jetty.setup.ServletEnvironment;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.hibernate.validator.constraints.NotEmpty;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.validation.constraints.NotNull;
import java.util.EnumSet;

/**
 * A YAML-compatible configuration class which constructs a Jetty {@link CrossOriginFilter}. The constructed filter
 * matches every request to the associated application, authorizes all requests from a list of known domains, and allows
 * browser clients to send the following headers:
 * <ul>
 *     <li>{@code Authorization}, which allows browser clients to submit OAuth tokens</li>
 *     <li>{@code X-Requested-With}, which allows the browser to label requests originating from page scripts</li>
 *     <li>{@code Content-Type}, which allows the browser to correctly identify the content of requests other than HTML
 *         forms</li>
 *     <li>{@code Accept}, which allows the browser to request specific or non-standard response types</li>
 *     <li>{@code Origin}, which allows the browser to label requests with the originating domain</li>
 * </ul>
 * This class accepts configuration in any of three ways:
 * <ol>
 *     <li>It can be configured using bean properties, which means it participates in YAML or JSON data binding and
 *         can be used as part of a Dropwizard application's configuration class. If bean properties do not provide
 *         the required values, then</li>
 *     <li>It can be configured using environment variables. If neither bean properties nor environment variables
 *         provide the required values, then</li>
 *     <li>It can use default values, which provide minimal privilege and are suitable for development only.</li>
 * </ol>
 * See the documentation of the bean properties, below.
 */
public class CrossOriginFilterFactory {
    /* The default Jetty set, plus Authorization to allow clients to pass JWT tokens instead of browser creds. */
    private static final String DEFAULT_ALLOWED_HEADERS = "Authorization,X-Requested-With,Content-Type,Accept,Origin";

    @NotNull
    @NotEmpty
    private String origins = defaultOrigins();

    @NotNull
    @NotEmpty
    private String allowedHeaders = defaultAllowedHeaders();

    private String allowedMethods = defaultAllowedMethods();

    private static String defaultOrigins() {
        String origins = System.getenv("CORS_ORIGINS");
        if (origins != null)
            return origins;
        return "http://localhost:*";
    }

    private static String defaultAllowedHeaders() {
        String allowedHeaders = System.getenv("CORS_ALLOWED_HEADERS");
        if (allowedHeaders != null)
            return allowedHeaders;
        return DEFAULT_ALLOWED_HEADERS;
    }

    /*
     * Note that this method can return null (as opposed to those above)
     * because we fall back to Jetty's default if no config is specified.
     */
    private static String defaultAllowedMethods() {
        return System.getenv("CORS_ALLOWED_METHODS");
    }

    @JsonProperty
    public String getOrigins() {
        return origins;
    }

    /**
     * Configure a list of origins to allow cross-origin requests from. Each origin takes the form
     * {@code proto://origin}. The provided values will be matched against the {@code Origin} header of incoming CORS
     * requests, and may contain the wildcard {@code *} (which matches any sequence of characters in the header, and
     * may match multiple domain name segments).
     * <p>
     * The list of origins must be provided as a single, comma-separated string.
     * <p>
     * If not set, this property will default to the value of the {@code CORS_ORIGINS} environment variable, or to
     * {@code http://localhost:*} if the {@code CORS_ORIGINS} environment variable is also unset.
     *
     * @param origins the origin list to use.
     */
    @JsonProperty
    public void setOrigins(String origins) {
        this.origins = origins;
    }

    @JsonProperty
    public String getAllowedHeaders() {
        return allowedHeaders;
    }

    /**
     * Configure a list of allowed headers for cross-origin requests.
     * <p>
     * The list of allowed headers must be provided as a single, comma-separated string.
     * <p>
     * If not set, this property will default to the value of the {@code CORS_ALLOWED_HEADERS} environment variable, or to
     * {@code DEFAULT_ALLOWED_HEADERS} if the {@code CORS_ALLOWED_HEADERS} environment variable is also unset.
     *
     * @param allowedHeaders the list of allowed headers to use.
     */
    @JsonProperty
    public void setAllowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    @JsonProperty
    public String getAllowedMethods() {
        return allowedMethods;
    }

    /**
     * Configure a list of allowed methods for cross-origin requests.
     * <p>
     * The list of allowed methods must be provided as a single, comma-separated string.
     * <p>
     * If not set, this property will default to the value of the {@code CORS_ALLOWED_METHODS} environment variable,
     * or to the Jetty built-in default (which is currently GET,POST,HEAD) if the {@code CORS_ALLOWED_METHODS}
     * environment variable is also unset.
     *
     * @param allowedMethods the list of allowed methods to use.
     */
    @JsonProperty
    public void setAllowedMethods(String allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    /**
     * Constructs a new {@link CrossOriginFilter} for the configured origins, and associates it with the provided
     * {@link ServletEnvironment}.
     *
     * @param servlets the Dropwizard servlet environment to configure.
     */
    public void registerFilter(ServletEnvironment servlets) {
        FilterRegistration.Dynamic filter = servlets.addFilter("CrossOriginFilter", CrossOriginFilter.class);
        filter.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, getOrigins());
        filter.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, getAllowedHeaders());
        String methods = getAllowedMethods();
        if (methods != null && !methods.isEmpty()) {
            filter.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, methods);
        }

        filter.addMappingForUrlPatterns(
                EnumSet.of(DispatcherType.REQUEST),
                /* isMatchAfter */ false,
                "*")
        ;
    }
}
