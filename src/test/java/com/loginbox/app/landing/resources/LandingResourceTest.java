package com.loginbox.app.landing.resources;

import com.loginbox.app.landing.api.LandingPage;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;

public class LandingResourceTest {
    private final LandingResource resource = new LandingResource();

    @Test
    public void returnsLandingPage() {
        LandingPage landingPage = resource.hello();

        assertThat(landingPage, is(not(nullValue())));
    }
}