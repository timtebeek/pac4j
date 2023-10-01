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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests {@link IsAnonymousAuthorizer}.
 *
 * @author Jerome Leleu
 * @since 1.9.0
 */
final class IsAnonymousAuthorizerTests implements TestsConstants {

    private IsAnonymousAuthorizer authorizer;

    private List<UserProfile> profiles;

    @BeforeEach
    void setUp() {
        authorizer = new IsAnonymousAuthorizer();
        profiles = new ArrayList<>();
    }

    @Test
    void testNoProfile() {
        assertTrue(authorizer.isAuthorized(null, new MockSessionStore(), profiles));
    }

    @Test
    void testAnonymousProfile() {
        profiles.add(new AnonymousProfile());
        assertTrue(authorizer.isAuthorized(null, new MockSessionStore(), profiles));
    }

    @Test
    void testAnonProfileTwoProfiles() {
        profiles.add(new AnonymousProfile());
        profiles.add(new CommonProfile());
        assertFalse(authorizer.isAuthorized(null, new MockSessionStore(), profiles));
    }

    @Test
    void testTwoAnonProfiles() {
        profiles.add(new AnonymousProfile());
        profiles.add(new AnonymousProfile());
        assertTrue(authorizer.isAuthorized(null, new MockSessionStore(), profiles));
    }

    @Test
    void testCommonProfile() {
        profiles.add(new CommonProfile());
        assertFalse(authorizer.isAuthorized(null, new MockSessionStore(), profiles));
    }

    @Test
    void testCommonProfileRedirectionUrl() {
        profiles.add(new CommonProfile());
        authorizer.setRedirectionUrl(PAC4J_URL);
        TestsHelper.expectException(() -> authorizer.isAuthorized(MockWebContext.create(), new MockSessionStore(), profiles),
            HttpAction.class, "Performing a 302 HTTP action");
    }
}
