package com.unreasonent.ds.acceptance.tests;

import com.unreasonent.ds.acceptance.framework.DslTestCase;
import com.unreasonent.ds.acceptance.framework.page.WebUi;
import org.junit.Before;
import org.junit.Test;

public class LandingPageTest extends DslTestCase {
    @Test
    public void showsLandingPageOnFirstVisit() {
        webUi.open();
        webUi.ensureLandingPageShown();
    }
}
