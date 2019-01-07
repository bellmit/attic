package ca.grimoire.maven.taglib.mocks;

import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.TestCase;

/**
 * Test cases for the stream-wrapping mock JSP writer.
 */
public class MockJspWriterTest extends TestCase {
    /**
     * Verifies that the mock writer passes output to the underlying stream.
     */
    public void testStreamOutput() {
        StringWriter writer = new StringWriter();
        MockJspWriter jspWriter = new MockJspWriter(new PrintWriter(writer));

        jspWriter.print("Hello, world");
        jspWriter.close();

        assertEquals("Hello, world", writer.getBuffer().toString());
    }
}
