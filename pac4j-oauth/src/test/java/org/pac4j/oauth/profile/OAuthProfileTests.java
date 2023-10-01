package org.pac4j.oauth.profile;

import org.junit.jupiter.api.Test;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.oauth.profile.dropbox.DropBoxProfile;
import org.pac4j.oauth.profile.github.GitHubProfile;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * General test cases for GitHubProfile.
 *
 * @author Jacob Severson
 * @since  1.8.0
 */
final class OAuthProfileTests implements TestsConstants {

    @Test
    void testClearDropBoxProfile() {
        var profile = new DropBoxProfile();
        profile.setAccessToken(VALUE);
        profile.setAccessSecret(VALUE);
        profile.removeLoginData();
        assertNull(profile.getAccessToken());
        assertNull(profile.getAccessSecret());
    }

    @Test
    void testClearGitHubProfile() {
        var profile = new GitHubProfile();
        profile.setAccessToken("testToken");
        profile.removeLoginData();
        assertNull(profile.getAccessToken());
    }
}
