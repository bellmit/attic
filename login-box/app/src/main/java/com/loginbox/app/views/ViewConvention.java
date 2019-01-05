package com.loginbox.app.views;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.views.View;
import org.glassfish.jersey.linking.InjectLink;

import javax.annotation.Nullable;
import javax.ws.rs.Path;
import java.net.URI;

/**
 * Provides site-wide conventions for views, including link sections.
 * <p>
 * Generally, subclasses (views) should embed their payloads, rather than storing them as a JSON attribute:
 * <pre>
 *     public class Example extends ViewConventions {
 *         // ... constructors ...
 *         @JsonUnwrapped
 *         public Payload getExamplePayload() { return payload; }
 *     }
 * </pre>
 * This creates JSON representations that look like (assume that Payload has a "foo" property)
 * <pre>
 *     {
 *         "links": { ... },
 *         "foo": 5
 *     }
 * </pre>
 * rather than payloads that look like
 * <pre>
 *     {
 *         "links": { ... },
 *         "examplePayload": {
 *             "foo": 5
 *         }
 *     }
 * </pre>
 * (Those JSON-API folks can go hang.)
 * <p>
 * Incoming link metadata from requests will be silently ignored.
 */
public class ViewConvention extends View {
    /*
     * Cheat here to avoid depending on app-specific resource classes. The Jersey link provider doesn't care that a
     * resource is actually exported, just that it has a URL path.
     */
    @Path("/")
    private static class RootUrl {
    }

    /**
     * Subclass to add additional links to your view, then attach your subclass to the view by passing it when chaining
     * to {@link #ViewConvention(String, com.loginbox.app.views.ViewConvention.Links) this class' constructor. <p> For
     * example:
     * <pre>
     *     public class Sample extends ViewConvention {
     *         public static class Links extends ViewConvention.Links {
     *            {@literal @}InjectLink(resource = SomeResource.class)
     *             private URI seeAlso;
     *
     *            {@literal @}JsonProperty
     *            {@literal @}JsonInclude(JsonInclude.Include.NON_NULL)
     *             public URI getSeeAlso() { return seeAlso; }
     *         }
     *
     *         public Sample() {
     *             super("sample.ftl", new Links());
     *         }
     *     }
     * </pre>
     * The additional links will appear in the view's JSON representation:
     * <pre>
     *     {
     *         "links": {
     *             "base": "/",
     *             "seeAlso": "/some-resource/"
     *         }
     *     }
     * </pre>
     * They will also be available as bean properties during view rendering.
     * <p>
     * Views that do not need extra links can use the default links by chaining to {@link #ViewConvention(String) the
     * one-argument constructor}, instead.
     */
    public static class Links {
        @InjectLink(resource = RootUrl.class)
        private URI base;

        /**
         * The application base URI, from which all links in this representation can be resolved.
         */
        @JsonProperty
        // Null during unit tests, since no link injection get applied outside of Jersey. Suppress.
        // However, serialize "" for non-ambiguity.
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public URI getBase() {
            return base;
        }
    }

    @JsonIgnore
    private Links links;

    /**
     * Use the default links.
     *
     * @see View#View(String)
     */
    protected ViewConvention(String templateName) {
        this(templateName, new Links());
    }

    /**
     * Use the specified links.
     *
     * @see View#View(String)
     */
    protected ViewConvention(String templateName, Links links) {
        super(templateName);
        this.links = links;
    }

    /**
     * Link metadata for the view. This may be absent.
     */
    @Nullable
    @JsonProperty
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    public Links getLinks() {
        return links;
    }

    @JsonIgnore
    public void setLinks(@Nullable Links links) {
        this.links = links;
    }
}
