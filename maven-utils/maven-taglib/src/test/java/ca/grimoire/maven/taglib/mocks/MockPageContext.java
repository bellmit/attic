package ca.grimoire.maven.taglib.mocks;

import java.io.IOException;
import java.util.Enumeration;

import javax.el.ELContext;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.el.ExpressionEvaluator;
import javax.servlet.jsp.el.VariableResolver;

/**
 * A simple "shell" mock page context which passes all methods through to an
 * underlying page context. The underlying context may be null, in which case
 * the method will fail.
 */
@SuppressWarnings("deprecation")
public class MockPageContext extends PageContext {

    private final PageContext baseContext;

    /**
     * Creates a new mock context.
     * 
     * @param base
     *            the base page context.
     */
    public MockPageContext(PageContext base) {
        this.baseContext = base;
    }

    /** {@inheritDoc} */
    @Override
    public Object findAttribute(String arg0) {
        return baseContext.findAttribute(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public void forward(String arg0) throws ServletException, IOException {
        baseContext.forward(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public Object getAttribute(String arg0) {
        return baseContext.getAttribute(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public Object getAttribute(String arg0, int arg1) {
        return baseContext.getAttribute(arg0, arg1);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    public Enumeration getAttributeNamesInScope(int arg0) {
        return baseContext.getAttributeNamesInScope(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public int getAttributesScope(String arg0) {
        return baseContext.getAttributesScope(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public ELContext getELContext() {
        return baseContext.getELContext();
    }

    /** {@inheritDoc} */
    @Override
    public Exception getException() {
        return baseContext.getException();
    }

    /** {@inheritDoc} */
    @Override
    @Deprecated
    public ExpressionEvaluator getExpressionEvaluator() {
        return baseContext.getExpressionEvaluator();
    }

    /** {@inheritDoc} */
    @Override
    public JspWriter getOut() {
        return baseContext.getOut();
    }

    /** {@inheritDoc} */
    @Override
    public Object getPage() {
        return baseContext.getPage();
    }

    /** {@inheritDoc} */
    @Override
    public ServletRequest getRequest() {
        return baseContext.getRequest();
    }

    /** {@inheritDoc} */
    @Override
    public ServletResponse getResponse() {
        return baseContext.getResponse();
    }

    /** {@inheritDoc} */
    @Override
    public ServletConfig getServletConfig() {
        return baseContext.getServletConfig();
    }

    /** {@inheritDoc} */
    @Override
    public ServletContext getServletContext() {
        return baseContext.getServletContext();
    }

    /** {@inheritDoc} */
    @Override
    public HttpSession getSession() {
        return baseContext.getSession();
    }

    /** {@inheritDoc} */
    @Override
    @Deprecated
    public VariableResolver getVariableResolver() {
        return baseContext.getVariableResolver();
    }

    /** {@inheritDoc} */
    @Override
    public void handlePageException(Exception arg0) throws ServletException,
            IOException {
        baseContext.handlePageException(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public void handlePageException(Throwable arg0) throws ServletException,
            IOException {
        baseContext.handlePageException(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public void include(String arg0) throws ServletException, IOException {
        baseContext.include(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public void include(String arg0, boolean arg1) throws ServletException,
            IOException {
        baseContext.include(arg0, arg1);
    }

    /** {@inheritDoc} */
    @Override
    public void initialize(Servlet arg0, ServletRequest arg1,
            ServletResponse arg2, String arg3, boolean arg4, int arg5,
            boolean arg6) throws IOException, IllegalStateException,
            IllegalArgumentException {
        baseContext.initialize(arg0, arg1, arg2, arg3, arg4, arg5, arg6);
    }

    /** {@inheritDoc} */
    @Override
    public void release() {
        baseContext.release();
    }

    /** {@inheritDoc} */
    @Override
    public void removeAttribute(String arg0) {
        baseContext.removeAttribute(arg0);
    }

    /** {@inheritDoc} */
    @Override
    public void removeAttribute(String arg0, int arg1) {
        baseContext.removeAttribute(arg0, arg1);
    }

    /** {@inheritDoc} */
    @Override
    public void setAttribute(String arg0, Object arg1) {
        baseContext.setAttribute(arg0, arg1);
    }

    /** {@inheritDoc} */
    @Override
    public void setAttribute(String arg0, Object arg1, int arg2) {
        baseContext.setAttribute(arg0, arg1, arg2);
    }
}
