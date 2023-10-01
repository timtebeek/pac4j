package org.pac4j.core.authorization.checker;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.authorization.authorizer.DefaultAuthorizers;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.MockDirectClient;
import org.pac4j.core.client.MockIndirectClient;
import org.pac4j.core.client.direct.AnonymousClient;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.context.session.MockSessionStore;
import org.pac4j.core.context.session.SessionStore;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.matching.matcher.csrf.CsrfTokenGenerator;
import org.pac4j.core.matching.matcher.csrf.DefaultCsrfTokenGenerator;
import org.pac4j.core.profile.AnonymousProfile;
import org.pac4j.core.profile.BasicUserProfile;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.util.Pac4jConstants;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link DefaultAuthorizationChecker}.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
final class DefaultAuthorizationCheckerTests implements TestsConstants {

    private final DefaultAuthorizationChecker checker = new DefaultAuthorizationChecker();

    private List<UserProfile> profiles;

    private BasicUserProfile profile;

    @BeforeEach
    void setUp() {
        profile = new BasicUserProfile();
        profiles = new ArrayList<>();
        profiles.add(profile);
    }

    private static class IdAuthorizer implements Authorizer {
        @Override
        public boolean isAuthorized(final WebContext context, final SessionStore sessionStore, final List<UserProfile> profiles) {
            return VALUE.equals(profiles.get(0).getId());
        }
    }

    @Test
    void testBlankAuthorizerNameAProfile() {
        assertTrue(checker.isAuthorized(MockWebContext.create(), new MockSessionStore(), profiles,
            Pac4jConstants.EMPTY_STRING, new HashMap<>(), new ArrayList<>()));
    }

    @Test
    void testNullAuthorizerNameAProfileGetRequest() {
        assertTrue(checker.isAuthorized(MockWebContext.create(), new MockSessionStore(), profiles, null, new HashMap<>(),
            new ArrayList<>()));
    }

    @Test
    void testNullAuthorizerNameAProfilePostRequestNoIndirectClient() {
        val context = MockWebContext.create().setRequestMethod("POST");
        assertTrue(checker.isAuthorized(context, new MockSessionStore(), profiles, null, new HashMap<>(), new ArrayList<>()));
    }

    @Test
    void testNullAuthorizerNameAProfilePostRequestIndirectClient() {
        val context = MockWebContext.create().setRequestMethod("POST");
        final List<Client> clients = new ArrayList<>();
        clients.add(new MockIndirectClient("test"));
        assertFalse(checker.isAuthorized(context, new MockSessionStore(), profiles, null, new HashMap<>(), clients));
    }

    @Test
    void testBlankAuthorizerNameAProfilePostRequestNoIndirectClient() {
        val context = MockWebContext.create().setRequestMethod("POST");
        assertTrue(checker.isAuthorized(context, new MockSessionStore(), profiles, " ", new HashMap<>(), new ArrayList<>()));
    }

    @Test
    void testBlankAuthorizerNameAProfilePostRequestIndirectClient() {
        val context = MockWebContext.create().setRequestMethod("POST");
        final List<Client> clients = new ArrayList<>();
        clients.add(new MockIndirectClient("test"));
        assertFalse(checker.isAuthorized(context, new MockSessionStore(), profiles, " ", new HashMap<>(), clients));
    }

    @Test
    void testNoneAuthorizerNameAProfilePostRequest() {
        val context = MockWebContext.create().setRequestMethod("POST");
        assertTrue(checker.isAuthorized(context, new MockSessionStore(), profiles, "noNe", new HashMap<>(), new ArrayList<>()));
    }

    @Test
    void testOneExistingAuthorizerProfileMatch() {
        profile.setId(VALUE);
        final Map<String, Authorizer> authorizers = new HashMap<>();
        authorizers.put(NAME, new IdAuthorizer());
        assertTrue(checker.isAuthorized(null, new MockSessionStore(), profiles, NAME, authorizers, new ArrayList<>()));
    }

    @Test
    void testOneExistingAuthorizerProfileDoesNotMatch() {
        internalTestOneExistingAuthorizerProfileDoesNotMatch(NAME);
    }

    @Test
    void testOneExistingAuthorizerProfileDoesNotMatchCasTrim() {
        internalTestOneExistingAuthorizerProfileDoesNotMatch("   NaME       ");
    }

    private void internalTestOneExistingAuthorizerProfileDoesNotMatch(final String name) {
        final Map<String, Authorizer> authorizers = new HashMap<>();
        authorizers.put(NAME, new IdAuthorizer());
        assertFalse(checker.isAuthorized(null, new MockSessionStore(), profiles, name, authorizers, new ArrayList<>()));
    }

    @Test
    void testOneAuthorizerDoesNotExist() {
        final Map<String, Authorizer> authorizers = new HashMap<>();
        authorizers.put(NAME, new IdAuthorizer());
        TestsHelper.expectException(() -> checker.isAuthorized(null, new MockSessionStore(), profiles, VALUE, authorizers,
            new ArrayList<>()), TechnicalException.class, "The authorizer '" + VALUE + "' must be defined in the security configuration");
    }

    @Test
    void testTwoExistingAuthorizerProfileMatch() {
        profile.setId(VALUE);
        profile.addRole(ROLE);
        final Map<String, Authorizer> authorizers = new HashMap<>();
        authorizers.put(NAME, new IdAuthorizer());
        authorizers.put(VALUE, new RequireAnyRoleAuthorizer(ROLE));
        assertTrue(checker.isAuthorized(null, new MockSessionStore(), profiles, NAME + Pac4jConstants.ELEMENT_SEPARATOR + VALUE,
            authorizers, new ArrayList<>()));
    }

    @Test
    void testTwoExistingAuthorizerProfileDoesNotMatch() {
        profile.addRole(ROLE);
        final Map<String, Authorizer> authorizers = new HashMap<>();
        authorizers.put(NAME, new IdAuthorizer());
        authorizers.put(VALUE, new RequireAnyRoleAuthorizer(ROLE));
        assertFalse(checker.isAuthorized(null, new MockSessionStore(), profiles, NAME + Pac4jConstants.ELEMENT_SEPARATOR + VALUE,
            authorizers, new ArrayList<>()));
    }

    @Test
    void testTwoAuthorizerOneDoesNotExist() {
        assertThrows(TechnicalException.class, () -> {
            final Map<String, Authorizer> authorizers = new HashMap<>();
            authorizers.put(NAME, new IdAuthorizer());
            checker.isAuthorized(null, new MockSessionStore(), profiles, NAME + Pac4jConstants.ELEMENT_SEPARATOR + VALUE,
                authorizers, new ArrayList<>());
        });
    }

    @Test
    void testNullAuthorizers() {
        assertThrows(TechnicalException.class, () -> {
            assertTrue(checker.isAuthorized(null, new MockSessionStore(), profiles, null));
            checker.isAuthorized(null, new MockSessionStore(), profiles, "auth1", null, new ArrayList<>());
        });
    }

    @Test
    void testZeroAuthorizers() {
        assertTrue(checker.isAuthorized(MockWebContext.create(), new MockSessionStore(), profiles, new ArrayList<>()));
        assertTrue(checker.isAuthorized(MockWebContext.create(), new MockSessionStore(), profiles,
            Pac4jConstants.EMPTY_STRING, new HashMap<>(), new ArrayList<>()));
    }

    @Test
    void testOneExistingAuthorizerProfileMatch2() {
        profile.setId(VALUE);
        final List<Authorizer> authorizers = new ArrayList<>();
        authorizers.add(new IdAuthorizer());
        assertTrue(checker.isAuthorized(null, new MockSessionStore(), profiles, authorizers));
    }

    @Test
    void testOneExistingAuthorizerProfileDoesNotMatch2() {
        final List<Authorizer> authorizers = new ArrayList<>();
        authorizers.add(new IdAuthorizer());
        assertFalse(checker.isAuthorized(null, new MockSessionStore(), profiles, authorizers));
    }

    @Test
    void testTwoExistingAuthorizerProfileMatch2() {
        profile.setId(VALUE);
        profile.addRole(ROLE);
        final List<Authorizer> authorizers = new ArrayList<>();
        authorizers.add(new IdAuthorizer());
        authorizers.add(new RequireAnyRoleAuthorizer(ROLE));
        assertTrue(checker.isAuthorized(null, new MockSessionStore(), profiles, authorizers));
    }

    @Test
    void testTwoExistingAuthorizerProfileDoesNotMatch2() {
        profile.addRole(ROLE);
        final List<Authorizer> authorizers = new ArrayList<>();
        authorizers.add(new IdAuthorizer());
        authorizers.add(new RequireAnyRoleAuthorizer(ROLE));
        assertFalse(checker.isAuthorized(null, new MockSessionStore(), profiles, authorizers));
    }

    @Test
    void testNullProfile() {
        assertThrows(TechnicalException.class, () -> {
            checker.isAuthorized(null, new MockSessionStore(), null, new ArrayList<>());
        });
    }


    @Test
    void testCsrfCheckPost() {
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.POST.name());
        CsrfTokenGenerator generator = new DefaultCsrfTokenGenerator();
        final SessionStore sessionStore = new MockSessionStore();
        generator.get(context, sessionStore);
        assertFalse(checker.isAuthorized(context, sessionStore, profiles, DefaultAuthorizers.CSRF_CHECK, new HashMap<>(),
            new ArrayList<>()));
    }

    @Test
    void testCsrfCheckPostTokenParameter() {
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.POST.name());
        val generator = new DefaultCsrfTokenGenerator();
        final SessionStore sessionStore = new MockSessionStore();
        val token = generator.get(context, sessionStore);
        context.addRequestParameter(Pac4jConstants.CSRF_TOKEN, token);
        sessionStore.set(context, Pac4jConstants.CSRF_TOKEN, token);
        sessionStore.set(context, Pac4jConstants.CSRF_TOKEN_EXPIRATION_DATE, new Date().getTime() + 1000 * generator.getTtlInSeconds());
        assertTrue(checker.isAuthorized(context, sessionStore, profiles, DefaultAuthorizers.CSRF_CHECK, new HashMap<>(),
            new ArrayList<>()));
    }

    @Test
    void testIsAnonymous() {
        profiles.clear();
        profiles.add(new AnonymousProfile());
        assertTrue(checker.isAuthorized(null, new MockSessionStore(), profiles, DefaultAuthorizers.IS_ANONYMOUS, new HashMap<>(),
            new ArrayList<>()));
    }

    @Test
    void testIsAuthenticated() {
        assertTrue(checker.isAuthorized(null, new MockSessionStore(), profiles, DefaultAuthorizers.IS_AUTHENTICATED, new HashMap<>(),
            new ArrayList<>()));
    }

    @Test
    void testIsFullyAuthenticated() {
        assertTrue(checker.isAuthorized(null, new MockSessionStore(), profiles, DefaultAuthorizers.IS_FULLY_AUTHENTICATED,
            new HashMap<>(), new ArrayList<>()));
    }

    @Test
    void testIsRemembered() {
        profile.setRemembered(true);
        assertTrue(checker.isAuthorized(null, new MockSessionStore(), profiles, DefaultAuthorizers.IS_REMEMBERED, new HashMap<>(),
            new ArrayList<>()));
    }

    @Test
    void testDefaultAuthorizersNoClient() {
        assertEquals(List.of(DefaultAuthorizationChecker.IS_AUTHENTICATED_AUTHORIZER),
            checker.computeDefaultAuthorizers(MockWebContext.create(), new ArrayList<>(), new ArrayList<>(), new HashMap<>()));
    }

    @Test
    void testDefaultAuthorizersAnonymousClient() {
        final List<Client> clients = List.of(AnonymousClient.INSTANCE);
        assertEquals(0, checker.computeDefaultAuthorizers(MockWebContext.create(), new ArrayList<>(), clients, new HashMap<>()).size());
    }

    @Test
    void testDefaultAuthorizersDirectClient() {
        final List<Client> clients = List.of(new MockDirectClient("test"));
        assertEquals(List.of(DefaultAuthorizationChecker.IS_AUTHENTICATED_AUTHORIZER),
            checker.computeDefaultAuthorizers(MockWebContext.create(), new ArrayList<>(), clients, new HashMap<>()));
    }

    @Test
    void testDefaultAuthorizersIndirectClient() {
        final List<Client> clients = List.of(new MockIndirectClient("test"));
        assertEquals(Arrays.asList(DefaultAuthorizationChecker.CSRF_AUTHORIZER, DefaultAuthorizationChecker.IS_AUTHENTICATED_AUTHORIZER),
            checker.computeDefaultAuthorizers(MockWebContext.create(), new ArrayList<>(), clients, new HashMap<>()));
    }

    @Test
    void testDefaultAuthorizersIndirectAndAnonymousClients() {
        final List<Client> clients = Arrays.asList(new MockIndirectClient("test"), AnonymousClient.INSTANCE);
        assertEquals(List.of(DefaultAuthorizationChecker.CSRF_AUTHORIZER),
            checker.computeDefaultAuthorizers(MockWebContext.create(), new ArrayList<>(), clients, new HashMap<>()));
    }

    @Test
    void testComputeAuthorizerNoClientIsFullyAuthenticated() {
        assertEquals(List.of(DefaultAuthorizationChecker.IS_FULLY_AUTHENTICATED_AUTHORIZER),
            checker.computeAuthorizers(MockWebContext.create(), new ArrayList<>(),
                DefaultAuthorizers.IS_FULLY_AUTHENTICATED, new HashMap<>(), new ArrayList<>()));
    }

    @Test
    void testComputeAuthorizerNoClientPlusIsFullyAuthenticated() {
        assertEquals(Arrays.asList(DefaultAuthorizationChecker.IS_AUTHENTICATED_AUTHORIZER,
            DefaultAuthorizationChecker.IS_FULLY_AUTHENTICATED_AUTHORIZER),
            checker.computeAuthorizers(MockWebContext.create(), new ArrayList<>(),
                "+" + DefaultAuthorizers.IS_FULLY_AUTHENTICATED, new HashMap<>(), new ArrayList<>()));
    }

    @Test
    void testComputeAuthorizersOverrideDefault() {
        final Map<String, Authorizer> authorizers = new HashMap<>();
        authorizers.put(DefaultAuthorizers.IS_AUTHENTICATED, DefaultAuthorizationChecker.IS_FULLY_AUTHENTICATED_AUTHORIZER);
        assertEquals(List.of(DefaultAuthorizationChecker.IS_FULLY_AUTHENTICATED_AUTHORIZER),
            checker.computeAuthorizers(MockWebContext.create(), new ArrayList<>(), DefaultAuthorizers.IS_AUTHENTICATED,
                authorizers, new ArrayList<>()));
    }

    @Test
    void testComputeAuthorizersOverrideEmptyDefault() {
        final Map<String, Authorizer> authorizers = new HashMap<>();
        authorizers.put(DefaultAuthorizers.IS_AUTHENTICATED, DefaultAuthorizationChecker.IS_FULLY_AUTHENTICATED_AUTHORIZER);
        assertEquals(List.of(DefaultAuthorizationChecker.IS_FULLY_AUTHENTICATED_AUTHORIZER),
            checker.computeAuthorizers(MockWebContext.create(), new ArrayList<>(), null,
                authorizers, new ArrayList<>()));
    }
}
