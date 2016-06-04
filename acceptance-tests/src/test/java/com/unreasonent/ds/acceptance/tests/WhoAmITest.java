package com.unreasonent.ds.acceptance.tests;

import com.unreasonent.ds.acceptance.framework.DslTestCase;
import org.junit.Test;

public class WhoAmITest extends DslTestCase {
    @Test
    public void withTokenIAmIdentity() {
        api.authenticateAs("test-user");
        api.whoAmI.ensureLoggedIn("test-user");
    }

    @Test
    public void withoutTokenIAmLoggedOut() {
        api.whoAmI.ensureLoggedOut();
    }
}
