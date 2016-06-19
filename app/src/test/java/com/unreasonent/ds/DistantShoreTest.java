package com.unreasonent.ds;

import com.loginbox.dropwizard.mybatis.MybatisBundle;
import com.unreasonent.ds.auth.AuthBundle;
import com.unreasonent.ds.axon.AxonBundle;
import com.unreasonent.ds.cors.CorsBundle;
import com.unreasonent.ds.database.DatabaseBundle;
import com.unreasonent.ds.squad.SquadBundle;
import io.dropwizard.Configuration;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import static com.unreasonent.ds.bundle.WrapsBundle.wrapsBundle;
import static javafx.scene.input.KeyCode.T;
import static org.hamcrest.Matchers.isA;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DistantShoreTest {
    @SuppressWarnings("unchecked")
    private final Bootstrap<DistantShoreConfiguration> bootstrap = mock(Bootstrap.class);

    private final DistantShore app = new DistantShore();

    @Test
    @SuppressWarnings("unchecked")
    public void registersBundles() {
        app.initialize(bootstrap);

        verify(bootstrap).addBundle(argThat(isA(CorsBundle.class)));
        verify(bootstrap).addBundle(argThat(isA(AuthBundle.class)));
        verify(bootstrap).addBundle(argThat(isA(DatabaseBundle.class)));
        verify(bootstrap).addBundle(argThat(isA(MybatisBundle.class)));
        verify(bootstrap).addBundle(argThat(wrapsBundle(isA(MigrationsBundle.class))));
        verify(bootstrap).addBundle(argThat(wrapsBundle(isA(AxonBundle.class))));
        verify(bootstrap).addBundle(argThat(wrapsBundle(isA(SquadBundle.class))));
    }

}