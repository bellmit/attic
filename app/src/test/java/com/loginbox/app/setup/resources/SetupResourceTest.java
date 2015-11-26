package com.loginbox.app.setup.resources;

import com.google.common.collect.Sets;
import com.loginbox.app.admin.resources.AdminResource;
import com.loginbox.app.csrf.CsrfToken;
import com.loginbox.app.csrf.context.CsrfValidator;
import com.loginbox.app.directory.bootstrap.Bootstrap;
import com.loginbox.app.identity.User;
import com.loginbox.app.identity.UserInfo;
import com.loginbox.app.setup.api.SetupForm;
import com.loginbox.app.transactor.mybatis.MybatisTransactor;
import com.loginbox.transactor.transactable.Transform;
import org.apache.ibatis.session.SqlSession;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Set;

import static com.loginbox.matchers.RedirectMatcher.redirectsTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SetupResourceTest {
    private final Validator validator = mock(Validator.class);
    private final MybatisTransactor transactor = mock(MybatisTransactor.class);
    private final Bootstrap bootstrap = mock(Bootstrap.class);
    private final AdminResource.Uris adminUris = mock(AdminResource.Uris.class);

    private final SetupResource resource = new SetupResource(validator, transactor, bootstrap, adminUris);

    private final CsrfValidator csrfValidator = mock(CsrfValidator.class);

    private final SetupForm request = mock(SetupForm.class);
    private final UserInfo userInfo = mock(UserInfo.class);
    private final CsrfToken csrfToken = mock(CsrfToken.class);

    private final UriInfo uriInfo = mock(UriInfo.class);
    private final URI adminUrl = URI.create("http://example.com/admin");
    @SuppressWarnings("unchecked")
    private final Transform<SqlSession, UserInfo, User> setupAction = mock(Transform.class);

    @Before
    public void wireMocks() {
        when(csrfValidator.issue()).thenReturn(csrfToken);

        when(bootstrap.setupAction()).thenReturn(setupAction);

        when(request.userInfo()).thenReturn(userInfo);
        when(request.getCsrfToken()).thenReturn(csrfToken);

        when(adminUris.get(uriInfo)).thenReturn(adminUrl);
    }

    @Test
    public void setupForm() {
        SetupForm form = resource.setupForm(csrfValidator);
        assertThat(form.getCsrfToken(), is(csrfToken));
    }

    @Test
    public void executesSetupActionFromForm() throws Exception {
        User user = mock(User.class);
        when(transactor.apply(setupAction, userInfo)).thenReturn(user);

        Response response = resource.createInitialUserFromForm(request, csrfValidator, uriInfo);

        assertThat(response, redirectsTo(adminUrl));

        InOrder inOrder = inOrder(csrfValidator, transactor);
        inOrder.verify(csrfValidator).consume(csrfToken);
        inOrder.verify(transactor).apply(setupAction, userInfo);
    }

    @Test
    public void returnsInvalidFormAsException() throws Exception {
        User user = mock(User.class);
        when(transactor.apply(setupAction, userInfo)).thenReturn(user);

        @SuppressWarnings("unchecked")
        ConstraintViolation<SetupForm> violation = mock(ConstraintViolation.class);
        @SuppressWarnings("unchecked")
        Set<ConstraintViolation<SetupForm>> violations = Sets.newHashSet(violation);
        when(validator.validate(request)).thenReturn(violations);

        try {
            Response response = resource.createInitialUserFromForm(request, csrfValidator, uriInfo);
            fail();
        } catch (BadRequestException expected) {
            verify(csrfValidator).consume(csrfToken);

            {
                /*
                 * I've hoisted the verify, matcher, matcher, call verify method out into separate lines here so that
                 * I can shut up the compile warning about `any(Transform.class)` and type-safety. The following calls
                 * _must_ happen in a specific order, or Mockito gets very sad:
                 *
                 * 1. Create the verifier stub (verify(...))
                 * 2. Create the matchers (any(...) and friends)
                 * 3. Call the method being verified (.apply, in this case).
                 *
                 * Creating the matchers before the verifiers won't work, so the obvious variable hoisting causes
                 *
                 *     org.mockito.exceptions.misusing.InvalidUseOfMatchersException:
                 *     Misplaced argument matcher detected here:
                 *
                 *     -> at com.loginbox.app.setup.resources.SetupResourceTest.returnsInvalidFormAsException(SetupResourceTest.java:107)
                 *
                 *     You cannot use argument matchers outside of verification or stubbing.
                 */
                MybatisTransactor verifyTransactor = verify(transactor, never());
                @SuppressWarnings("unchecked")
                Transform<SqlSession, UserInfo, ?> anyTransform = any(Transform.class);
                verifyTransactor.apply(anyTransform, any(UserInfo.class));
            }
        }

    }

    @Test
    public void executesSetupActionFromJson() throws Exception {
        User user = mock(User.class);
        when(transactor.apply(setupAction, userInfo)).thenReturn(user);

        Response response = resource.createInitialUserFromJson(request, csrfValidator, uriInfo);

        assertThat(response, redirectsTo(adminUrl));

        InOrder inOrder = inOrder(csrfValidator, transactor);
        inOrder.verify(csrfValidator).consume(csrfToken);
        inOrder.verify(transactor).apply(setupAction, userInfo);
    }
}
