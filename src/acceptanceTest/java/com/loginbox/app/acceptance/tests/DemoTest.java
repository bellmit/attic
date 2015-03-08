package com.loginbox.app.acceptance.tests;

import com.loginbox.app.acceptance.framework.DslTestCase;
import org.junit.Test;

public class DemoTest extends DslTestCase {
    @Test
    public void navigate() {
        webUi.open();
        webUi.ensureDemoMessageShown();
    }
}
