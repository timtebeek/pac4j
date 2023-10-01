package org.pac4j.core.authorization.authorizer;

import com.google.common.collect.Lists;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.session.MockSessionStore;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.util.Pac4jConstants;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link RequireAnyAttributeAuthorizer}.
 *
 * @author Misagh Moayyed
 * @since 1.9.2
 */
final class RequireAnyAttributeAuthorizerTests {

    private final MockWebContext context = MockWebContext.create();

    private List<UserProfile> profiles;

    private CommonProfile profile;

    @BeforeEach
    void setUp() {
        profile = new CommonProfile();
        profiles = new ArrayList<>();
        profiles.add(profile);
    }

    @Test
    void testAttributeNotFound() {
        val authorizer = new RequireAnyAttributeAuthorizer(Pac4jConstants.EMPTY_STRING);
        authorizer.setElements("name1");
        profile.addAttribute("name2", "anything-goes-here");
        assertFalse(authorizer.isAuthorized(context, new MockSessionStore(), profiles));
    }

    @Test
    void testNoValueProvided() {
        val authorizer = new RequireAnyAttributeAuthorizer(Pac4jConstants.EMPTY_STRING);
        authorizer.setElements("name1");
        profile.addAttribute("name1", "anything-goes-here");
        assertTrue(authorizer.isAuthorized(context, new MockSessionStore(), profiles));
    }

    @Test
    void testPatternSingleValuedAttribute() {
        val authorizer = new RequireAnyAttributeAuthorizer("^value.+");
        authorizer.setElements("name1");
        profile.addAttribute("name1", "valueAddedHere");
        assertTrue(authorizer.isAuthorized(context, new MockSessionStore(), profiles));
    }

    @Test
    void testPatternFails() {
        val authorizer = new RequireAnyAttributeAuthorizer("^v");
        authorizer.setElements("name1");
        profile.addAttribute("name1", Lists.newArrayList("v1", "v2", "nothing"));
        assertFalse(authorizer.isAuthorized(context, new MockSessionStore(), profiles));
    }

    @Test
    void testMatchesPattern() {
        val authorizer = new RequireAnyAttributeAuthorizer("^v\\d");
        authorizer.setElements("name1");
        profile.addAttribute("name1", Lists.newArrayList("v1", "v2", "nothing"));
        profile.addAttribute("name2", "v3");
        assertTrue(authorizer.isAuthorized(context, new MockSessionStore(), profiles));
    }

    @Test
    void testMatchesEverythingByDefault() {
        val authorizer = new RequireAnyAttributeAuthorizer();
        authorizer.setElements("name1");
        profile.addAttribute("name1", Lists.newArrayList("v1", "v2"));
        profile.addAttribute("name2", "v3");
        assertTrue(authorizer.isAuthorized(context, new MockSessionStore(), profiles));
    }
}
