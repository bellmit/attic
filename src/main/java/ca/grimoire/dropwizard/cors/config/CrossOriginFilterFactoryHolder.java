package ca.grimoire.dropwizard.cors.config;

/**
 * Dropwizard configurations that provide a {@link CrossOriginFilterFactory} should implement this interface as well, to
 * allow the CORS bundle to automatically configure your application.
 * <pre>
 * public class HelloConfiguration
 *     extends Configuration
 *     implements CrossOriginFilterFactoryHolder {
 *
 *     {@literal @}Valid
 *     {@literal @}NotNull
 *     private CrossOriginFilterFactory cors = new CrossOriginFilterFactory();
 *     // Optional
 *     public void setCors(CrossOriginFilterFactory cors) { this.cors = cors; }
 *     // Not optional
 *     public CrossOriginFilterFactory getCors() { return this.cors; }
 *
 *     // ...
 * }
 * </pre>
 */
public interface CrossOriginFilterFactoryHolder {
    /**
     * @return the CORS configuration associated with this application configuration.
     */
    public CrossOriginFilterFactory getCors();
}
