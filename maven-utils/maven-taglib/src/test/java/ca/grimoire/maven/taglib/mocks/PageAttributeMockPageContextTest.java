package ca.grimoire.maven.taglib.mocks;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.jsp.PageContext;

import junit.framework.TestCase;

/**
 * Test cases for the page attribute mockup context.
 */
public class PageAttributeMockPageContextTest extends TestCase {
    /**
     * Verifies that an attribute added using setAttribute can be located using
     * getAttribute.
     */
    public void testSetGetAttribute() {
        PageContext testContext = new PageAttributeMockPageContext(null);

        Object value = new Object();
        testContext.setAttribute("test", value);
        assertSame(value, testContext.getAttribute("test"));
    }

    /**
     * Verifies that an attribute added using the scoped version of setAttribute
     * can be located using getAttribute.
     */
    public void testSetScopeGetAttribute() {
        PageContext testContext = new PageAttributeMockPageContext(null);

        Object value = new Object();
        testContext.setAttribute("test", value, PageContext.PAGE_SCOPE);
        assertSame(value, testContext.getAttribute("test"));
    }

    /**
     * Verifies that an attribute added using setAttribute can be located using
     * findAttribute.
     */
    public void testSetFindAttribute() {
        PageContext testContext = new PageAttributeMockPageContext(null);

        Object value = new Object();
        testContext.setAttribute("test", value);
        assertSame(value, testContext.findAttribute("test"));
    }

    /**
     * Verifies that an attribute added using setAttribute can be located using
     * the scoped version of getAttribute.
     */
    public void testSetGetScopeAttribute() {
        PageContext testContext = new PageAttributeMockPageContext(null);

        Object value = new Object();
        testContext.setAttribute("test", value);

        assertSame(value, testContext.getAttribute("test",
                PageContext.PAGE_SCOPE));
    }

    /**
     * Verifies that setting an attribute adds it to the scope's attribute
     * names.
     */
    public void testSetAttributeNames() {
        PageContext testContext = new PageAttributeMockPageContext(null);

        Object value = new Object();
        testContext.setAttribute("test", value);

        Set<Object> names = new HashSet<Object>();
        Enumeration<?> attributes = testContext
                .getAttributeNamesInScope(PageContext.PAGE_SCOPE);
        while (attributes.hasMoreElements())
            names.add(attributes.nextElement());

        assertTrue(names.contains("test"));
    }

    /**
     * Verifies that setting an attribute causes it to be reported as being in
     * page scope.
     */
    public void testAttributeScope() {
        PageContext testContext = new PageAttributeMockPageContext(null);

        Object value = new Object();
        testContext.setAttribute("test", value);

        assertEquals(PageContext.PAGE_SCOPE, testContext
                .getAttributesScope("test"));
    }

    /**
     * Verifies that removing an attribute causes it to vanish completely from
     * the scope.
     */
    public void testSetRemoveAttribute() {
        PageContext testContext = new PageAttributeMockPageContext(null);

        Object value = new Object();
        testContext.setAttribute("test", value);

        testContext.removeAttribute("test");

        assertNull(testContext.getAttribute("test"));
    }

    /**
     * Verifies that removing an attribute using the scoped removeAttribute
     * method causes it to vanish completely from the scope.
     */
    public void testSetRemoveScopedAttribute() {
        PageContext testContext = new PageAttributeMockPageContext(null);

        Object value = new Object();
        testContext.setAttribute("test", value);

        testContext.removeAttribute("test", PageContext.PAGE_SCOPE);

        assertNull(testContext.getAttribute("test"));
    }
}
