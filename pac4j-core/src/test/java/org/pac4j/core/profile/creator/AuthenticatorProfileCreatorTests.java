package org.pac4j.core.profile.creator;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.credentials.Credentials;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.util.TestsConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This class tests the {@link AuthenticatorProfileCreator}.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
final class AuthenticatorProfileCreatorTests implements TestsConstants {

    @Test
    void testReturnNoProfile() {
        assertFalse(AuthenticatorProfileCreator.INSTANCE.create(null, new TokenCredentials(TOKEN)).isPresent());
    }

    @Test
    void testReturnProfile() {
        UserProfile profile = new CommonProfile();
        final Credentials credentials = new TokenCredentials(TOKEN);
        credentials.setUserProfile(profile);
        val profile2 = (CommonProfile) AuthenticatorProfileCreator.INSTANCE.create(null, credentials).get();
        assertEquals(profile, profile2);
    }
}
