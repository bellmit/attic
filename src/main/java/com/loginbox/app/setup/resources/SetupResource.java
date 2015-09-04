package com.loginbox.app.setup.resources;

import com.loginbox.app.admin.resources.AdminResource;
import com.loginbox.app.csrf.CsrfToken;
import com.loginbox.app.csrf.context.CsrfValidator;
import com.loginbox.app.directory.bootstrap.Bootstrap;
import com.loginbox.app.identity.User;
import com.loginbox.app.identity.UserInfo;
import com.loginbox.app.mime.Types;
import com.loginbox.app.setup.api.SetupForm;
import com.loginbox.app.transactor.mybatis.MybatisTransactor;
import com.loginbox.transactor.Transactor;
import com.loginbox.transactor.transactable.Transform;
import org.apache.ibatis.session.SqlSession;

import javax.validation.ConstraintViolation;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Set;

/**
 * Processes initial setup, creating the initial admin user, etc.
 */
@Path("/setup/")
public class SetupResource {
    public static class Uris {
        public URI setupForm(UriInfo uriInfo) {
            return uriInfo
                    .getBaseUriBuilder()
                    .path(SetupResource.class)
                    .build();
        }
    }

    public static Uris URIS = new Uris();

    public SetupResource(
            Validator validator,
            /* Be more specific than strictly necessary here to silence an ugly Jersey warning:
             *
             * WARNING: Parameter 1 of type com.loginbox.transactor.Transactor<? extends org.apache.ibatis.session.SqlSession>
             *     from public com.loginbox.app.setup.resources.SetupResource(com.loginbox.transactor.Transactor<? extends org.apache.ibatis.session.SqlSession>,com.loginbox.app.directory.bootstrap.Bootstrap,com.loginbox.app.admin.resources.AdminResource$Uris)
             *     is not resolvable to a concrete type.
             *
             * Jersey apparently can't figure out that this is a pre-created resource, and therefore will never require
             * injection.
             */
            MybatisTransactor transactor,
            Bootstrap bootstrap,
            AdminResource.Uris adminUris) {
        this.validator = validator;
        this.transactor = transactor;
        this.bootstrap = bootstrap;
        this.adminUris = adminUris;
    }

    private final Validator validator;
    private final Transactor<? extends SqlSession> transactor;
    private final Bootstrap bootstrap;
    private final AdminResource.Uris adminUris;

    /**
     * Render the setup form.
     */
    @GET
    @Produces({Types.TEXT_HTML, Types.APPLICATION_LOGIN_BOX})
    public SetupForm setupForm(@Context CsrfValidator csrfValidator) {
        CsrfToken csrfToken = csrfValidator.issue();
        return new SetupForm(csrfToken);
    }

    /**
     * Bootstrap the built-in directory and inserts an initial user into it.
     */
    @POST
    @Consumes(Types.APPLICATION_FORM_URLENCODED)
    public Response createInitialUserFromForm(
            @BeanParam SetupForm request,
            @Context CsrfValidator csrfValidator,
            @Context UriInfo uriInfo) throws Exception {
        csrfValidator.consume(request.getCsrfToken());

        /* Do this explicitly here - when passed a @BeanParam form, DropWizard skips @Valid. */
        Set<ConstraintViolation<SetupForm>> violations = validator.validate(request);
        if(!violations.isEmpty()) {
            throw new BadRequestException();
        }

        return createInitialUser(request, uriInfo);
    }

    /**
     * Bootstrap the built-in directory and inserts an initial user into it.
     */
    @POST
    @Consumes(Types.APPLICATION_LOGIN_BOX)
    public Response createInitialUserFromJson(
            @Valid SetupForm request,
            @Context CsrfValidator csrfValidator,
            @Context UriInfo uriInfo) throws Exception {
        csrfValidator.consume(request.getCsrfToken());

        return createInitialUser(request, uriInfo);
    }

    private Response createInitialUser(SetupForm request, UriInfo uriInfo) throws Exception {
        UserInfo userInfo = request.userInfo();
        Transform<SqlSession, UserInfo, User> setupAction = bootstrap.setupAction();
        User user = transactor.apply(setupAction, userInfo);

        return Response
                .seeOther(adminUris.get(uriInfo))
                .build();
    }
}
