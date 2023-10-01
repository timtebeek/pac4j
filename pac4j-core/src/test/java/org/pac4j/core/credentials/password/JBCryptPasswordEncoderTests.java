package org.pac4j.core.credentials.password;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.util.TestsConstants;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link JBCryptPasswordEncoder}.
 *
 * @author Jerome Leleu
 * @since 3.1.0
 */
final class JBCryptPasswordEncoderTests implements TestsConstants {

    private final PasswordEncoder encoder = new JBCryptPasswordEncoder();

    @Test
    void test() {
        val hashedPwd = encoder.encode(PASSWORD);
        assertTrue(encoder.matches(PASSWORD, hashedPwd));
        assertFalse(encoder.matches(VALUE, hashedPwd));
    }
}
