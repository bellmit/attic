package com.unreasonent.ds.acceptance.tests;

import com.unreasonent.ds.acceptance.framework.DslTestCase;
import io.github.unacceptable.rules.Incomplete;
import org.junit.Test;

public class SquadsTest extends DslTestCase {
    @Test
    public void requiresAuth() {
        api.squad.ensureNotAuthorized();
    }

    @Test
    public void returnsNotFoundForNewAccount() {
        api.authenticateAs("squads-test");
        api.squad.ensureNotFound();
    }

    @Test
    public void storesSquad() {
        api.authenticateAs("squads-test");
        api.squad.store();
        api.squad.ensureSquad();
    }
}
