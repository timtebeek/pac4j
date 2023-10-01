package org.pac4j.core.authorization.authorizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.session.MockSessionStore;
import org.pac4j.core.exception.http.HttpAction;
import org.pac4j.core.profile.AnonymousProfile;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link IsAuthenticatedAuthorizer}.
 *
 * @author Jerome Leleu
 * @since 1.9.0
 */
class IsAuthenticatedAuthorizerTests implements TestsConstants {

    protected Authorizer authorizer;

    protected List<UserProfile> profiles;

    protected CommonProfile profile;

    @BeforeEach
    void setUp() {
        authorizer = newAuthorizer();
        profiles = new ArrayList<>();
        profile = new CommonProfile();
        profile.setRemembered(isRemembered());
    }

    protected Authorizer newAuthorizer() {
        return new IsAuthenticatedAuthorizer();
    }

    protected boolean isRemembered() {
        return false;
    }

    @Test
    void testNoProfile() {
        assertFalse(authorizer.isAuthorized(null, new MockSessionStore(), profiles));
    }

    @Test
    void testAnonymousProfile() {
        profiles.add(new AnonymousProfile());
        assertFalse(authorizer.isAuthorized(null, new MockSessionStore(), profiles));
    }

    @Test
    void testCommonProfileTwoProfiles() {
        profiles.add(new AnonymousProfile());
        profiles.add(profile);
        assertTrue(authorizer.isAuthorized(null, new MockSessionStore(), profiles));
    }

    @Test
    void testCommonProfile() {
        profiles.add(profile);
        assertTrue(authorizer.isAuthorized(null, new MockSessionStore(), profiles));
    }

    @Test
    void testAnonymousProfileRedirectionUrl() {
        profiles.add(new AnonymousProfile());
        ((IsAuthenticatedAuthorizer) authorizer).setRedirectionUrl(PAC4J_URL);
        TestsHelper.expectException(() -> authorizer.isAuthorized(MockWebContext.create(), new MockSessionStore(), profiles),
            HttpAction.class, "Performing a 302 HTTP action");
    }
}
