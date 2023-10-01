package org.pac4j.core.http.callback;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.http.url.DefaultUrlResolver;
import org.pac4j.core.util.TestsConstants;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests {@link PathParameterCallbackUrlResolver}.
 *
 * @author Jerome Leleu
 * @since 3.0.0
 */
final class PathParameterCallbackUrlResolverTests implements TestsConstants {

    private static final CallbackUrlResolver resolver = new PathParameterCallbackUrlResolver();

    @Test
    void testCompute() {
        val url = resolver.compute(new DefaultUrlResolver(), CALLBACK_URL, MY_CLIENT_NAME, MockWebContext.create());
        assertEquals(CALLBACK_URL + "/" + MY_CLIENT_NAME, url);
    }

    @Test
    void testMatchesNoClientName() {
        assertFalse(resolver.matches(MY_CLIENT_NAME, MockWebContext.create()));
    }

    @Test
    void testMatchesSimplePath() {
        val context = MockWebContext.create();
        context.setPath(MY_CLIENT_NAME);
        assertTrue(resolver.matches(MY_CLIENT_NAME, context));
    }

    @Test
    void testMatchesComplexPath() {
        val context = MockWebContext.create();
        context.setPath(VALUE + "/" + MY_CLIENT_NAME);
        assertTrue(resolver.matches(MY_CLIENT_NAME, context));
    }
}
