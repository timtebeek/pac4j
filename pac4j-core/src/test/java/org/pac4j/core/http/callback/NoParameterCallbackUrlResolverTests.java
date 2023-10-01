package org.pac4j.core.http.callback;

import org.junit.jupiter.api.Test;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.http.url.DefaultUrlResolver;
import org.pac4j.core.util.TestsConstants;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests {@link NoParameterCallbackUrlResolver}.
 *
 * @author Jerome Leleu
 * @since 3.0.0
 */
final class NoParameterCallbackUrlResolverTests implements TestsConstants {

    private static final CallbackUrlResolver resolver = new NoParameterCallbackUrlResolver();

    @Test
    void testCompute() {
        assertEquals(CALLBACK_URL, resolver.compute(new DefaultUrlResolver(), CALLBACK_URL, MY_CLIENT_NAME, MockWebContext.create()));
    }

    @Test
    void testMatches() {
        assertFalse(resolver.matches(MY_CLIENT_NAME, MockWebContext.create()));
    }
}
