package org.pac4j.core.authorization.authorizer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.session.MockSessionStore;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.UserProfile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.pac4j.core.authorization.authorizer.OrAuthorizer.or;
import static org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer.requireAnyRole;

/**
 * Tests {@link OrAuthorizer}
 *
 * @author Sergey Morgunov
 * @since 3.4.0
 */
@SuppressWarnings("PMD.TooManyStaticImports")
class OrAuthorizerTests {

    private List<UserProfile> profiles = new ArrayList<>();

    @BeforeEach
    void setUp() {
        UserProfile profile = new CommonProfile();
        profile.addRole("profile_role");
        profiles.add(profile);
    }

    @Test
    void testDisjunctionAuthorizer1() {
        final Authorizer authorizer = or(
            requireAnyRole("profile_role2")
        );
        assertFalse(authorizer.isAuthorized(MockWebContext.create(), new MockSessionStore(), profiles));
    }

    @Test
    void testDisjunctionAuthorizer2() {
        final Authorizer authorizer = or(
            requireAnyRole("profile_role")
        );
        assertTrue(authorizer.isAuthorized(MockWebContext.create(), new MockSessionStore(), profiles));
    }
}
