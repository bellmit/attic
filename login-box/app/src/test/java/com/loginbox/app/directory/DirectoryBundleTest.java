package com.loginbox.app.directory;

import com.loginbox.app.dropwizard.BundleTestCase;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;

public class DirectoryBundleTest extends BundleTestCase {
    protected final DirectoryBundle bundle = new DirectoryBundle();

    @Test
    public void createsDirectories() {
        bundle.initialize(bootstrap);
        bundle.run(environment);

        Directories directories = bundle.getDirectories();
        assertThat(directories, not(nullValue()));
    }
}
