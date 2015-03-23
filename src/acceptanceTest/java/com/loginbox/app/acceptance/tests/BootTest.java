package com.loginbox.app.acceptance.tests;

import com.loginbox.app.acceptance.framework.DslTestCase;
import org.junit.Test;

public class BootTest extends DslTestCase {
    @Test
    public void boot() {
        webUi.open();
    }
}
