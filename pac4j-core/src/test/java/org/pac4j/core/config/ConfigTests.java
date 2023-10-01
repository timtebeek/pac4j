package org.pac4j.core.config;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.authorization.authorizer.Authorizer;
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer;
import org.pac4j.core.client.Client;
import org.pac4j.core.client.MockIndirectClient;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.exception.http.FoundAction;
import org.pac4j.core.matching.matcher.CacheControlMatcher;
import org.pac4j.core.matching.matcher.Matcher;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.TestsConstants;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests the {@link Config}.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
final class ConfigTests implements TestsConstants {

    @Test
    void testNullAuthorizersSetter() {
        assertThrows(TechnicalException.class, () -> {
            val config = new Config();
            config.setAuthorizers(null);
        });
    }

    @Test
    void testNullAuthorizersConstructor() {
        assertThrows(TechnicalException.class, () -> {
            new Config((Map<String, Authorizer>) null);
        });
    }

    @Test
    void testAddAuthorizer() {
        val config = new Config();
        Authorizer authorizer = new RequireAnyRoleAuthorizer();
        config.addAuthorizer(NAME, authorizer);
        assertEquals(authorizer, config.getAuthorizers().get(NAME));
    }

    @Test
    void testConstructor() {
        val client =
            new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val config = new Config(CALLBACK_URL, client);
        assertEquals(CALLBACK_URL, config.getClients().getCallbackUrl());
        assertEquals(client, config.getClients().findAllClients().get(0));
    }

    @Test
    void testFluent() {
        Client client = new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        Authorizer authorizer = new RequireAnyRoleAuthorizer();
        Matcher matcher = new CacheControlMatcher();
        val config = new Config(CALLBACK_URL).addClient(client).addAuthorizer(NAME, authorizer).addMatcher(NAME, matcher);
        assertEquals(CALLBACK_URL, config.getClients().getCallbackUrl());
        assertEquals(1, config.getClients().findAllClients().size());
        assertEquals(client, config.getClients().findAllClients().get(0));
        assertEquals(1, config.getAuthorizers().size());
        assertEquals(authorizer, config.getAuthorizers().get(NAME));
        assertEquals(1, config.getMatchers().size());
        assertEquals(matcher, config.getMatchers().get(NAME));
    }
}
