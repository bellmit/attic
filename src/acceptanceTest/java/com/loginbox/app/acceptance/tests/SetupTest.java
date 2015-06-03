package com.loginbox.app.acceptance.tests;

import com.loginbox.app.acceptance.framework.DslTestCase;
import org.junit.Test;

public class SetupTest extends DslTestCase {
    @Test
    public void landingPageRedirectsToSetup() {
        webUi.open();
        webUi.setupPage.ensureWelcomeShown();
    }

    @Test
    public void canSetupApp() {
        webUi.open();
        webUi.setupPage.enterUsername();
        webUi.setupPage.enterContactEmail();
        webUi.setupPage.enterPassword();
        webUi.setupPage.confirmPassword();
        webUi.setupPage.clickCompleteSetup();
        webUi.adminPage.ensureAdminShown();
    }

    @Test
    public void setupRequiresUsername() {
        webUi.open();
        /* skip: webUi.setupPage.enterUsername(); */
        webUi.setupPage.enterContactEmail();
        webUi.setupPage.enterPassword();
        webUi.setupPage.confirmPassword();
        webUi.setupPage.clickCompleteSetup();
        webUi.errorPage.ensure("error: HTTP ERROR 400", "message: Bad Request");
    }

    @Test
    public void setupRequiresContactEmail() {
        webUi.open();
        webUi.setupPage.enterUsername();
        /* skip: webUi.setupPage.enterContactEmail(); */
        webUi.setupPage.enterPassword();
        webUi.setupPage.confirmPassword();
        webUi.setupPage.clickCompleteSetup();
        webUi.errorPage.ensure("error: HTTP ERROR 400", "message: Bad Request");
    }

    @Test
    public void setupRequiresPassword() {
        webUi.open();
        webUi.setupPage.enterUsername();
        webUi.setupPage.enterContactEmail();
        /* skip: webUi.setupPage.enterPassword(); */
        /* skip: webUi.setupPage.confirmPassword(); */
        webUi.setupPage.clickCompleteSetup();
        webUi.errorPage.ensure("error: HTTP ERROR 400", "message: Bad Request");
    }

    @Test
    public void setupRequiresMatchingPasswords() {
        webUi.open();
        webUi.setupPage.enterUsername();
        webUi.setupPage.enterContactEmail();
        webUi.setupPage.enterPassword("password-a");
        webUi.setupPage.confirmPassword("password-b");
        webUi.setupPage.clickCompleteSetup();
        webUi.errorPage.ensure("error: HTTP ERROR 400", "message: Bad Request");
    }

    @Test
    public void normalUiAfterSetupCompleted() {
        webUi.open();
        webUi.setupApp();
        webUi.close();

        webUi.open();
        webUi.landingPage.ensureLandingPageShown();
    }
}
