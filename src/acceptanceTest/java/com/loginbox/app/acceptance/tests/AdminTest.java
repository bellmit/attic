package com.loginbox.app.acceptance.tests;

import com.loginbox.app.acceptance.framework.DslTestCase;
import org.junit.Before;
import org.junit.Test;

public class AdminTest extends DslTestCase {
    @Before
    public void setupLoginBox() {
        webUi.open();
        webUi.setupApp();
        webUi.close();
    }

    @Test
    public void hasAdminPage() {
        webUi.adminPage.open();
        webUi.adminPage.ensureAdminShown();
    }
}
