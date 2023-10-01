package org.pac4j.jee.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.util.Pac4jConstants;
import org.pac4j.core.util.TestsConstants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests {@link JEEContext}.
 *
 * @author Jerome Leleu
 * @since 3.0.0
 */
final class JEEContextTest implements TestsConstants {

    private static final String CTX = "/ctx";
    private static final String PATH = "/path";
    private static final String CTX_PATH = "/ctx/path";

    private HttpServletRequest request;

    private HttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @Test
    void testGetHeaderNameMatches() {
        internalTestGetHeader("kEy");
    }

    @Test
    void testGetHeaderNameStriclyMatches() {
        internalTestGetHeader(KEY);
    }

    private void internalTestGetHeader(final String key) {
        Set<String> headerNames = new HashSet<>();
        headerNames.add(KEY);
        when(request.getHeaderNames()).thenReturn(Collections.enumeration(headerNames));
        when(request.getHeader(KEY)).thenReturn(VALUE);
        WebContext context = new JEEContext(request, response);
        assertEquals(VALUE, context.getRequestHeader(key).get());
    }

    @Test
    void testGetPathNullFullPath() {
        when(request.getRequestURI()).thenReturn(null);
        WebContext context = new JEEContext(request, response);
        assertEquals(Pac4jConstants.EMPTY_STRING, context.getPath());
    }

    @Test
    void testGetPathFullpath() {
        when(request.getRequestURI()).thenReturn(CTX_PATH);
        WebContext context = new JEEContext(request, response);
        assertEquals(CTX_PATH, context.getPath());
    }

    @Test
    void testGetRequestUrl() throws Exception {
        when(request.getRequestURL()).thenReturn(new StringBuffer("https://pac4j.org?name=value&name2=value2"));
        WebContext context = new JEEContext(request, response);
        assertEquals("https://pac4j.org", context.getRequestURL());
    }

    @Test
    void testGetPathFullpathContext() {
        when(request.getRequestURI()).thenReturn(CTX_PATH);
        when(request.getContextPath()).thenReturn(CTX);
        WebContext context = new JEEContext(request, response);
        assertEquals(PATH, context.getPath());
    }

    @Test
    void testGetPathDoubleSlashFullpathContext() {
        when(request.getRequestURI()).thenReturn("/" + CTX_PATH);
        when(request.getContextPath()).thenReturn(CTX);
        WebContext context = new JEEContext(request, response);
        assertEquals(PATH, context.getPath());
    }
}
