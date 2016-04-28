package com.unreasonent.ds.frontend.api;

import io.dropwizard.views.View;

/**
 * Abuse Dropwizard Views to generate and return the frontend HTML. This is simpler than trying to serve a static asset,
 * weirdly.
 */
public class FrontendView extends View {
    public FrontendView() {
        super("/index.html.ftl");
    }
}
