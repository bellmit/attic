package com.loginbox.heroku.http;

import com.google.common.collect.ImmutableList;
import io.dropwizard.jetty.RequestLogFactory;

/**
 * Default request logging behaviour on Heroku: log nothing. This defaults to an empty list of appenders, rather than
 * defaulting to appending to standard output.
 *
 * @see com.loginbox.heroku.http.HerokuServerFactory
 * @see io.dropwizard.jetty.RequestLogFactory
 */
public class HerokuRequestLogFactory extends RequestLogFactory {
    {
        setAppenders(ImmutableList.of());
    }
}
