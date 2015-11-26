package com.loginbox.app.acceptance.tests;

import com.loginbox.app.acceptance.framework.DslTestCase;
import org.junit.Test;

public class VersionTest extends DslTestCase {
    @Test
    public void returnsVersion() {
        publicApi.ensureVersionPresent();
    }
}
