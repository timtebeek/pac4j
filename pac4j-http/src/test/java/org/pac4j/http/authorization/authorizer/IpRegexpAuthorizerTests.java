package org.pac4j.http.authorization.authorizer;

import org.junit.jupiter.api.Test;
import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.session.MockSessionStore;
import org.pac4j.core.exception.TechnicalException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the {@link IpRegexpAuthorizer}.
 *
 * @author Jerome Leleu
 * @since 1.8.1
 */
final class IpRegexpAuthorizerTests {

    private final static String GOOD_IP = "goodIp";
    private final static String BAD_IP = "badIp";

    private final static Authorizer authorizer = new IpRegexpAuthorizer(GOOD_IP);

    @Test
    void testNoPattern() {
        assertThrows(TechnicalException.class, () -> {
            Authorizer authorizer = new IpRegexpAuthorizer();
            authorizer.isAuthorized(MockWebContext.create(), new MockSessionStore(), null);
        });
    }

    @Test
    void testValidateGoodIP() {
        assertTrue(authorizer.isAuthorized(MockWebContext.create().setRemoteAddress(GOOD_IP), new MockSessionStore(), null));
    }

    @Test
    void testValidateBadIP() {
        assertFalse(authorizer.isAuthorized(MockWebContext.create().setRemoteAddress(BAD_IP), new MockSessionStore(), null));
    }
}
