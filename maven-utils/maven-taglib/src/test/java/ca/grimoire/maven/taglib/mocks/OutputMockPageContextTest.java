package ca.grimoire.maven.taglib.mocks;

import java.io.IOException;

import junit.framework.TestCase;

/**
 * Test cases for the output-handling mock page context.
 */
public class OutputMockPageContextTest extends TestCase {
    /**
     * Verifies that output passed to the context's writer is available from the
     * context's getWritten query.
     * 
     * @throws IOException
     *             if the test fails.
     */
    public void testHelloString() throws IOException {
        OutputMockPageContext context = new OutputMockPageContext(null);

        context.getOut().print("Hello, world");
        assertEquals("Hello, world", context.getWritten());
    }
}
