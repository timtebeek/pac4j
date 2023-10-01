package org.pac4j.core.profile.definition;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.profile.BasicUserProfile;
import org.pac4j.core.profile.CommonProfile;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link CommonProfileDefinition}.
 *
 * @author Jerome LELEU
 * @since 5.0.0
 */
class CommonProfileDefinitionTest {

    @Test
    void testNewProfile() {
        final ProfileDefinition definition = new CommonProfileDefinition();
        val profile = definition.newProfile();
        assertTrue(profile instanceof CommonProfile);
    }

    @Test
    void testRestoreProfile() {
        final ProfileDefinition definition = new CommonProfileDefinition();
        definition.setRestoreProfileFromTypedId(true);
        val profile = definition.newProfile(BasicUserProfile.class.getName() + "#");
        assertFalse(profile instanceof CommonProfile);
        assertTrue(profile instanceof BasicUserProfile);
    }

    @Test
    void testRestoreProfileNoSeparator() {
        final ProfileDefinition definition = new CommonProfileDefinition();
        definition.setRestoreProfileFromTypedId(true);
        val profile = definition.newProfile(BasicUserProfile.class.getName());
        assertTrue(profile instanceof CommonProfile);
    }

    @Test
    void testRestoreProfileBadType() {
        final ProfileDefinition definition = new CommonProfileDefinition();
        definition.setRestoreProfileFromTypedId(true);
        val profile = definition.newProfile(String.class.getName() + "#");
        assertTrue(profile instanceof CommonProfile);
    }
}
