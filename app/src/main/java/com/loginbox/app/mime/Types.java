package com.loginbox.app.mime;

import javax.ws.rs.core.MediaType;

import static com.loginbox.collections.MapLiteral.entry;
import static com.loginbox.collections.MapLiteral.hashMap;

/**
 * Standard MIME types for Login Box. Most resources should use these constants, generally in the form of
 * <pre>
 *    {@literal @}Produces({Mime.TEXT_HTML, Mime.APPLICATION_LOGIN_BOX})
 * </pre>
 * or
 * <pre>
 *    {@literal @}Consumes({Mime.APPLICATION_FORM_URLENCODED, Mime.APPLICATION_LOGIN_BOX})
 * </pre>
 */
public class Types {
    public static final String TEXT_HTML = MediaType.TEXT_HTML;
    public static final String APPLICATION_FORM_URLENCODED = MediaType.APPLICATION_FORM_URLENCODED;
    public static final String APPLICATION_LOGIN_BOX = "application/x-login-box+json;version=1";

    public static final MediaType APPLICATION_LOGIN_BOX_TYPE = new MediaType("application", "x-login-box+json", hashMap(
            entry("version", "1")
    ));
}
