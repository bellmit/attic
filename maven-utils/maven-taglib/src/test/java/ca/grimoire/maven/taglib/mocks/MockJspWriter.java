package ca.grimoire.maven.taglib.mocks;

import java.io.PrintWriter;

import javax.servlet.jsp.JspWriter;

/**
 * A JspWriter implementation used by to provide output tracking. All calls to
 * output routines are passed to a PrintWriter.
 */
public final class MockJspWriter extends JspWriter {

    private static final int MOCK_BUFFER_SZ = NO_BUFFER;

    private final PrintWriter out;

    /**
     * Creates a new mock writer.
     * 
     * @param out
     *            the print writer to pass output to.
     */
    public MockJspWriter(PrintWriter out) {
        super(MOCK_BUFFER_SZ, false);
        this.out = out;
    }

    /**
     * Returns the number of bytes remaining before output blocks.
     * 
     * @return a large number.
     */
    @Override
    public int getRemaining() {
        return bufferSize;
    }

    /**
     * Clears the contents of the buffer.
     */
    @Override
    public void clear() {
        return;
    }

    /**
     * Clears the contents of the buffer.
     */
    @Override
    public void clearBuffer() {
        clear();
    }

    /**
     * Flushes and closes the JSP writer.
     */
    @Override
    public void close() {
        out.close();
    }

    /**
     * Flushes the JSP writer.
     */
    @Override
    public void flush() {
        out.flush();
    }

    /**
     * Writes a line separator.
     */
    @Override
    public void newLine() {
        out.println();
    }

    /** {@inheritDoc} */
    @Override
    public void println() {
        out.println();
    }

    /** {@inheritDoc} */
    @Override
    public void print(char c) {
        out.print(c);
    }

    /** {@inheritDoc} */
    @Override
    public void println(char c) {
        out.println(c);
    }

    /** {@inheritDoc} */
    @Override
    public void print(double d) {
        out.print(d);
    }

    /** {@inheritDoc} */
    @Override
    public void println(double d) {
        out.println(d);
    }

    /** {@inheritDoc} */
    @Override
    public void print(float f) {
        out.print(f);
    }

    /** {@inheritDoc} */
    @Override
    public void println(float f) {
        out.println(f);
    }

    /** {@inheritDoc} */
    @Override
    public void print(int i) {
        out.print(i);
    }

    /** {@inheritDoc} */
    @Override
    public void println(int i) {
        out.println(i);
    }

    /** {@inheritDoc} */
    @Override
    public void print(long l) {
        out.print(l);
    }

    /** {@inheritDoc} */
    @Override
    public void println(long l) {
        out.println(l);
    }

    /** {@inheritDoc} */
    @Override
    public void print(boolean b) {
        out.print(b);
    }

    /** {@inheritDoc} */
    @Override
    public void print(char[] s) {
        out.print(s);
    }

    /** {@inheritDoc} */
    @Override
    public void print(Object obj) {
        out.print(obj);
    }

    /** {@inheritDoc} */
    @Override
    public void print(String s) {
        out.print(s);
    }

    /** {@inheritDoc} */
    @Override
    public void println(boolean x) {
        out.println(x);
    }

    /** {@inheritDoc} */
    @Override
    public void println(char[] x) {
        out.println(x);
    }

    /** {@inheritDoc} */
    @Override
    public void println(Object x) {
        out.println(x);
    }

    /** {@inheritDoc} */
    @Override
    public void println(String x) {
        out.println(x);
    }

    /** {@inheritDoc} */
    @Override
    public void write(char[] buf, int off, int len) {
        out.write(buf, off, len);
    }
}
