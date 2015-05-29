package com.loginbox.app.acceptance.tests;

import com.loginbox.app.acceptance.framework.DslTestCase;
import org.junit.Test;

public class LandingPageTest extends DslTestCase {
    @Test
    public void hasLandingPage() {
        webUi.open();
        webUi.landingPage.ensureLandingPageShown();
    }
}
