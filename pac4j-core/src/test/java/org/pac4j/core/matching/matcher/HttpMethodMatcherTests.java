package org.pac4j.core.matching.matcher;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.CallContext;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link HttpMethodMatcher}.
 *
 * @author Jerome Leleu
 * @since 1.9.3
 */
final class HttpMethodMatcherTests implements TestsConstants {

    @Test
    void testNullMethods() {
        val matcher = new HttpMethodMatcher();
        TestsHelper.expectException(() -> matcher.matches(new CallContext(MockWebContext.create(), null)),
            TechnicalException.class, "methods cannot be null");
    }

    @Test
    void testBadMethod() {
        Matcher matcher = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.GET);
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.POST.name());
        assertFalse(matcher.matches(new CallContext(context, null)));
    }

    @Test
    void testGoodMethod() {
        Matcher matcher = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.POST);
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.POST.name());
        assertTrue(matcher.matches(new CallContext(context, null)));
    }

    @Test
    void testBadMethod2() {
        Matcher matcher = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.GET, HttpConstants.HTTP_METHOD.PUT);
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.POST.name());
        assertFalse(matcher.matches(new CallContext(context, null)));
    }

    @Test
    void testGoodMethod2() {
        Matcher matcher = new HttpMethodMatcher(HttpConstants.HTTP_METHOD.DELETE, HttpConstants.HTTP_METHOD.POST);
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.POST.name());
        assertTrue(matcher.matches(new CallContext(context, null)));
    }
}
