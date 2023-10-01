package org.pac4j.oauth.profile;

import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.pac4j.oauth.profile.hiorgserver.HiOrgServerProfile;
import org.pac4j.oauth.profile.hiorgserver.HiOrgServerProfileDefinition;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * This class tests the {@link HiOrgServerProfile} class.
 *
 * @author Martin Böhmer
 * @since 3.2.0
 */
class HiOrgServerProfileTest {

    @Test
    void testGetRoles() {
        var rolesAsInt = 1 + 2 + 4 + 8 + 16 + 32 + 64 + 128 + 256 + 512 + 1024;
        var body = "{ \"" + HiOrgServerProfileDefinition.USER_ID + "\": 12345, \""
                + HiOrgServerProfileDefinition.ROLES + "\": " + rolesAsInt + " }";
        val profileDefinition = new HiOrgServerProfileDefinition();
        val profile = profileDefinition.extractUserProfile(body);
        val roles = profile.getRoles();
        Assertions.assertEquals(rolesAsInt, profile.getRolesAsInteger());
        for (var i = 0; i <= 10; i++) {
            var groupId = (int) Math.pow(2, i);
            var groupIdAsString = String.valueOf(groupId);
            assertTrue(roles.contains(groupIdAsString));
            assertTrue(profile.hasRole(groupId));
        }
    }

}
