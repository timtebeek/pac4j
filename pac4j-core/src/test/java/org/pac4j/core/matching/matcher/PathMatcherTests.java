package org.pac4j.core.matching.matcher;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.CallContext;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.session.MockSessionStore;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsHelper;

import java.util.Collection;
import java.util.HashSet;
import java.util.regex.PatternSyntaxException;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests {@link PathMatcher}.
 *
 * @author Rob Ward
 * @since 2.0.0
 */
class PathMatcherTests {

    @Test
    void testBlankPath() {
        Matcher pathMatcher = new PathMatcher();
        assertTrue(pathMatcher.matches(new CallContext(MockWebContext.create().setPath("/page.html"), new MockSessionStore())));
        assertTrue(pathMatcher.matches(new CallContext(MockWebContext.create(), new MockSessionStore())));
    }

    @Test
    void testFixedPath() {
        val pathMatcher = new PathMatcher().excludePath("/foo");
        assertFalse(pathMatcher.matches(new CallContext(MockWebContext.create().setPath("/foo"), new MockSessionStore())));
        assertTrue(pathMatcher.matches(new CallContext(MockWebContext.create().setPath("/foo/bar"), new MockSessionStore())));
    }

    @Test
    void testBranch() {
        val pathMatcher = new PathMatcher().excludeBranch("/foo");
        assertFalse(pathMatcher.matches(new CallContext(MockWebContext.create().setPath("/foo"), new MockSessionStore())));
        assertFalse(pathMatcher.matches(new CallContext(MockWebContext.create().setPath("/foo/"), new MockSessionStore())));
        assertFalse(pathMatcher.matches(new CallContext(MockWebContext.create().setPath("/foo/bar"), new MockSessionStore())));
    }

    @Test
    void testMissingStartCharacterInRegexp() {
        TestsHelper.expectException(() -> new PathMatcher().excludeRegex("/img/.*$"), TechnicalException.class,
                "Your regular expression: '/img/.*$' must start with a ^ and end with a $ to define a full path matching");
    }

    @Test
    void testMissingEndCharacterInRegexp() {
        TestsHelper.expectException(() -> new PathMatcher().excludeRegex("^/img/.*"), TechnicalException.class,
            "Your regular expression: '^/img/.*' must start with a ^ and end with a $ to define a full path matching");
    }

    @Test
    void testBadRegexp() {
        assertThrows(PatternSyntaxException.class, () -> {
            new PathMatcher().excludeRegex("^/img/**$");
        });
    }

    @Test
    void testNoPath() {
        val pathMatcher = new PathMatcher().excludeRegex("^/$");
        assertFalse(pathMatcher.matches(new CallContext(MockWebContext.create().setPath("/"), new MockSessionStore())));
    }

    @Test
    void testMatch() {
        val matcher = new PathMatcher().excludeRegex("^/(img/.*|css/.*|page\\.html)$");
        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/js/app.js"), new MockSessionStore())));
        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/"), new MockSessionStore())));
        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/page.htm"), new MockSessionStore())));
    }

    @Test
    void testDontMatch() {
        val matcher = new PathMatcher().excludeRegex("^/(img/.*|css/.*|page\\.html)$");
        assertFalse(matcher.matches(new CallContext(MockWebContext.create().setPath("/css/app.css"), new MockSessionStore())));
        assertFalse(matcher.matches(new CallContext(MockWebContext.create().setPath("/img/"), new MockSessionStore())));
        assertFalse(matcher.matches(new CallContext(MockWebContext.create().setPath("/page.html"), new MockSessionStore())));
    }

    @Test
    void testSetters() {
        final Collection<String> excludedPaths = new HashSet<>();
        excludedPaths.add("/foo");
        final Collection<String> excludedRegexs = new HashSet<>();
        excludedRegexs.add("^/(img/.*|css/.*|page\\.html)$");

        val matcher = new PathMatcher();
        matcher.setExcludedPaths(excludedPaths);
        matcher.setExcludedPatterns(excludedRegexs);

        assertFalse(matcher.matches(new CallContext(MockWebContext.create().setPath("/foo"), new MockSessionStore())));
        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/foo/"),
            null))); // because its a fixed path, not a regex

        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/error/500.html"), new MockSessionStore())));
        assertFalse(matcher.matches(new CallContext(MockWebContext.create().setPath("/img/"), new MockSessionStore())));
    }

    @Test
    void testIncludePath() {
        val matcher = new PathMatcher().includePath("/protect");

        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/protect"), new MockSessionStore())));
        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/protected"), new MockSessionStore())));
        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/protected/index.html"), new MockSessionStore())));
        assertFalse(matcher.matches(new CallContext(MockWebContext.create().setPath("/img/logo.gif"), new MockSessionStore())));
        assertFalse(matcher.matches(new CallContext(MockWebContext.create().setPath("/callback"), new MockSessionStore())));
    }

    @Test
    void testIncludePaths() {
        val matcher = new PathMatcher().includePaths("/protect", "/css");

        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/protect"), new MockSessionStore())));
        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/protected"), new MockSessionStore())));
        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/protected/index.html"), new MockSessionStore())));
        assertTrue(matcher.matches(new CallContext(MockWebContext.create().setPath("/css/css1.css"), new MockSessionStore())));

        assertFalse(matcher.matches(new CallContext(MockWebContext.create().setPath("/img/logo.gif"), new MockSessionStore())));
        assertFalse(matcher.matches(new CallContext(MockWebContext.create().setPath("/callback"), new MockSessionStore())));
        assertFalse(matcher.matches(new CallContext(MockWebContext.create().setPath("/notprotected"), new MockSessionStore())));
    }
}
