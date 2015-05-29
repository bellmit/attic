package com.loginbox.app.acceptance.tests;

import com.loginbox.app.acceptance.framework.DslTestCase;
import org.junit.Test;

public class AdminTest extends DslTestCase {
    @Test
    public void hasAdminPage() {
        webUi.adminPage.open();
        webUi.adminPage.ensureAdminShown();
    }
}
