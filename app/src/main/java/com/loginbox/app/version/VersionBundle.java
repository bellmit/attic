package com.loginbox.app.version;

import com.loginbox.app.version.filters.VersionFilter;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.Objects;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * Reports version information via HTTP headers. This bundle will automatically detect the version of the app from the
 * JAR manifests visible.
 */
public class VersionBundle implements Bundle {
    private final Logger log = LoggerFactory.getLogger(VersionBundle.class);
    private final String version;

    /**
     * Infers the version from MANIFEST.MF. All manifests visible to the app will be inspected; the first (in an
     * unspecified order controlled by the underlying classloader) containing a matching Implementation-Vendor and
     * Implementation-Title attribute will be used to set the version. The version will be extracted from the
     * Implementation-Version attribute.
     * <p>
     * If no manifest matches the requested implementation vendor and title, then the detected vendor will default to
     * "-SRC-" to denote that the app was likely run from sources and has no version info.
     *
     * @param implementationVendor
     *         the application's vendor.
     * @param implementationTitle
     *         the application's title.
     */
    public VersionBundle(String implementationVendor, String implementationTitle) {
        version = probeVersion(implementationVendor, implementationTitle);
    }

    private String probeVersion(String implementationVendor, String implementationTitle) {
        try {
            return probeVersion(implementationVendor, implementationTitle, Thread.currentThread().getContextClassLoader());
        } catch (IOException ioe) {
            log.warn("IO exception while trying to determine app version", ioe);
            return "-UNKNOWN-";
        }
    }

    private String probeVersion(String implementationVendor, String implementationTitle, ClassLoader classLoader) throws IOException {
        Enumeration<URL> manifestUrls = classLoader.getResources("/META-INF/MANIFEST.MF");
        while (manifestUrls.hasMoreElements()) {
            URL manifestUrl = manifestUrls.nextElement();
            Manifest manifest = loadManifest(manifestUrl);
            String version = probeVersion(implementationVendor, implementationTitle, manifest);
            if (version != null)
                return version;
        }
        return "-SRC-";
    }

    @Nullable
    private String probeVersion(String implementationVendor, String implementationTitle, Manifest manifest) {
        Attributes mainAttributes = manifest.getMainAttributes();
        if (!Objects.equals(mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VENDOR), implementationVendor))
            return null;
        if (!Objects.equals(mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_TITLE), implementationTitle))
            return null;
        return mainAttributes.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
    }

    private Manifest loadManifest(URL manifestUrl) throws IOException {
        try (InputStream in = manifestUrl.openStream()) {
            return new Manifest(in);
        }
    }

    /**
     * Does nothing.
     */
    @Override
    public void initialize(Bootstrap<?> bootstrap) {
    }

    /**
     * Wires up filters to inject version headers into all HTTP responses.
     *
     * @param environment
     *         the app's environment.
     */
    @Override
    public void run(Environment environment) {
        environment.jersey().register(new VersionFilter(version));
    }
}
