package com.loginbox.app.admin;

import com.loginbox.app.admin.resources.AdminResource;
import com.loginbox.app.dropwizard.BundleTestCase;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AdminBundleTest extends BundleTestCase {
    private final AdminBundle adminBundle = new AdminBundle();

    @Test
    public void registersAdminEcosystem() {
        adminBundle.run(environment);

        verify(jersey).register(isA(AdminResource.class));
    }
}