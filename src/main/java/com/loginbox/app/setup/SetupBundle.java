package com.loginbox.app.setup;

import com.loginbox.app.admin.resources.AdminResource;
import com.loginbox.app.directory.Directories;
import com.loginbox.app.directory.bootstrap.Bootstrap;
import com.loginbox.app.directory.bootstrap.Gatekeeper;
import com.loginbox.app.password.PasswordValidator;
import com.loginbox.app.setup.filters.RedirectToSetupFilter;
import com.loginbox.app.setup.resources.SetupResource;
import com.loginbox.app.transactor.mybatis.MybatisTransactor;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Environment;
import org.apache.ibatis.session.SqlSessionFactory;

import javax.validation.Validator;

/**
 * Drives the setup mode for Login-Box. This includes the actual setup interface, as well as tools for turning the setup
 * interface on and off, filters to redirect to setup, and other niceties.
 */
public abstract class SetupBundle implements Bundle {
    /**
     * No-op.
     */
    @Override
    public void initialize(io.dropwizard.setup.Bootstrap<?> bootstrap) {

    }

    /**
     * Register the setup UI.
     *
     * @param environment
     *         the environment to configure.
     */
    @Override
    public void run(Environment environment) {
        MybatisTransactor transactor = new MybatisTransactor(this::getSqlSessionFactory);
        Directories directories = new Directories();
        PasswordValidator passwordValidator = new PasswordValidator();
        Gatekeeper setupGatekeeper = new Gatekeeper(transactor);
        Bootstrap bootstrap = new Bootstrap(
                directories, setupGatekeeper, passwordValidator);
        Validator validator = environment.getValidator();

        SetupResource setupResource = new SetupResource(
                validator, transactor, bootstrap, AdminResource.URIS);
        RedirectToSetupFilter redirectToSetupFilter = new RedirectToSetupFilter(
                setupGatekeeper, SetupResource.URIS, setupResource);

        environment.jersey().register(setupResource);
        environment.jersey().register(redirectToSetupFilter);
    }

    public abstract SqlSessionFactory getSqlSessionFactory();
}
