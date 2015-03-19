# Dropwizard Heroku Support

[![Build Status](https://circleci.com/gh/login-box/dropwizard-heroku.svg)](https://circleci.com/gh/login-box/dropwizard-heroku)

Using [Dropwizard](https://dropwizard.io/) on Heroku? This module takes care of
some common cases automatically.

## Server Configuration

The `dropwizard-heroku-config` module provides some baseline configuration for
operating on Heroku:

* Automatic detection of the `$PORT` environment variable. **The admin context will also be served on this port, under `/!/admin`.**
* A log format appropriate to PaaS systems such as Heroku.

To use it in your app, add the appropriate dependencies, and extend
`com.loginbox.heroku.HerokuConfiguration` in your configuration class:

    package com.example;
    
    import com.loginbox.heroku.config.HerokuConfiguration;
    
    public class ExampleConfiguration extends HerokuConfiguration {
        // ... your configuration fields here ...
    }

Alternately, if you already have a configuration class, elements can be included using Dropwizard's YAML parser:

    server:
        # ...type, etc...
        connectors:
            - type: heroku
    logging:
        appenders:
            - type: heroku
