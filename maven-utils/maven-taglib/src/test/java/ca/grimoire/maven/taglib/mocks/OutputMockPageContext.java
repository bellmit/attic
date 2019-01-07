package ca.grimoire.maven.taglib.mocks;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;

/**
 * A mock page context for tracking what output has been written. All other
 * features are optionally provided by another page context.
 */
public class OutputMockPageContext extends MockPageContext {
    /**
     * Creates a new mock output context.
     * 
     * @param base
     *            the base page context providing other features.
     */
    public OutputMockPageContext(PageContext base) {
        super(base);
        out = new StringWriter();
        printOut = new PrintWriter(out);
    }

    /**
     * Returns a JspWriter implementation that writes to this mock context.
     * 
     * @return a JspWriter.
     */
    @Override
    public JspWriter getOut() {
        return new MockJspWriter(printOut);
    }

    /**
     * Returns the data written to the JSP output.
     * 
     * @return the data written out.
     */
    public String getWritten() {
        return out.toString();
    }

    private final StringWriter out;

    private final PrintWriter printOut;
}
