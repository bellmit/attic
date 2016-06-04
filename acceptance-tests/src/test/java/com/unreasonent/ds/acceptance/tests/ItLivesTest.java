package com.unreasonent.ds.acceptance.tests;

import com.unreasonent.ds.acceptance.framework.DslTestCase;
import org.junit.Test;

public class ItLivesTest extends DslTestCase {
    @Test
    public void rootUrlNotFound() {
        api.ensureNotFound();
    }
}
