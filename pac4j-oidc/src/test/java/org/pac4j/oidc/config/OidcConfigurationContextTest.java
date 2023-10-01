package org.pac4j.oidc.config;

import org.junit.jupiter.api.Test;
import org.pac4j.core.context.MockWebContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OidcConfigurationContextTest {
    @Test
    void shouldResolveScopeWhenOverriddenFromRequest() {
        var webContext = MockWebContext.create();
        webContext.setRequestAttribute(OidcConfiguration.SCOPE, "openid profile email phone");

        var oidcConfiguration = new OidcConfiguration();

        var oidcConfigurationContext = new OidcConfigurationContext(webContext, oidcConfiguration);

        var result = oidcConfigurationContext.getScope();

        assertEquals("openid profile email phone", result);
    }

    @Test
    void shouldResolveScopeWhenConfiguredProgrammatically() {
        var webContext = MockWebContext.create();

        var oidcConfiguration = new OidcConfiguration();
        oidcConfiguration.setScope("openid profile email products");

        var oidcConfigurationContext = new OidcConfigurationContext(webContext, oidcConfiguration);

        var result = oidcConfigurationContext.getScope();

        assertEquals("openid profile email products", result);
    }

    @Test
    void shouldResolveScopeFromDefaultValues() {
        var webContext = MockWebContext.create();

        var oidcConfiguration = new OidcConfiguration();

        var oidcConfigurationContext = new OidcConfigurationContext(webContext, oidcConfiguration);

        var result = oidcConfigurationContext.getScope();

        assertEquals("openid profile email", result);
    }
}
