package org.pac4j.core.matching.matcher;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.CallContext;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.session.MockSessionStore;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link HeaderMatcher}.
 *
 * @author Jerome Leleu
 * @since 1.9.3
 */
final class HeaderMatcherTests implements TestsConstants {

    @Test
    void testNullHeaderName() {
        val matcher = new HeaderMatcher();
        TestsHelper.expectException(() -> matcher.matches(new CallContext(MockWebContext.create(), new MockSessionStore())),
            TechnicalException.class, "headerName cannot be blank");
    }

    @Test
    void testNullExpectedValueHeader() {
        Matcher matcher = new HeaderMatcher(NAME, null);
        val context = MockWebContext.create().addRequestHeader(NAME, VALUE);
        assertFalse(matcher.matches(new CallContext(context, new MockSessionStore())));
    }

    @Test
    void testNullExpectedValueNull() {
        Matcher matcher = new HeaderMatcher(NAME, null);
        val context = MockWebContext.create();
        assertTrue(matcher.matches(new CallContext(context, new MockSessionStore())));
    }

    @Test
    void testRegexExpectedRightValueHeader() {
        Matcher matcher = new HeaderMatcher(NAME, ".*A.*");
        val context = MockWebContext.create().addRequestHeader(NAME, "BAC");
        assertTrue(matcher.matches(new CallContext(context, new MockSessionStore())));
    }

    @Test
    void testRegexExpectedBadValueHeader() {
        Matcher matcher = new HeaderMatcher(NAME, ".*A.*");
        val context = MockWebContext.create().addRequestHeader(NAME, "BOC");
        assertFalse(matcher.matches(new CallContext(context, new MockSessionStore())));
    }

    @Test
    void testRegexExpectedNullHeader() {
        Matcher matcher = new HeaderMatcher(NAME, ".*A.*");
        val context = MockWebContext.create();
        assertFalse(matcher.matches(new CallContext(context, new MockSessionStore())));
    }
}
