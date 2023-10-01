package org.pac4j.cas.profile;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.util.TestsConstants;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * General test cases for {@link CasRestProfile}.
 *
 * @author Jacob Severson
 * @since 1.8.0
 */
final class CasRestProfileTests implements TestsConstants {

    @Test
    void testClearProfile() {
        val profile = new CasRestProfile(ID, USERNAME);
        profile.removeLoginData();
        assertNull(profile.getTicketGrantingTicketId());
    }
}
