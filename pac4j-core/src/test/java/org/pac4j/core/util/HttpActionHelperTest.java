package org.pac4j.core.util;

import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link HttpActionHelper}.
 *
 * @author Jerome LELEU
 * @since 4.0.0
 */
final class HttpActionHelperTest implements TestsConstants {

    @AfterEach
    void after() {
        HttpActionHelper.setAlwaysUse401ForUnauthenticated(true);
        HttpActionHelper.setUseModernHttpCodes(true);
    }

    @Test
    void testRedirectUrlAfterGet() {
        HttpActionHelper.setUseModernHttpCodes(true);
        val action = HttpActionHelper.buildRedirectUrlAction(MockWebContext.create(), PAC4J_URL);
        assertTrue(action instanceof FoundAction);
        assertEquals(PAC4J_URL, ((FoundAction) action).getLocation());
    }

    @Test
    void testRedirectUrlAfterPost() {
        HttpActionHelper.setUseModernHttpCodes(true);
        val action = HttpActionHelper
            .buildRedirectUrlAction(MockWebContext.create().setRequestMethod("POST"), PAC4J_URL);
        assertTrue(action instanceof SeeOtherAction);
        assertEquals(PAC4J_URL, ((SeeOtherAction) action).getLocation());
    }

    @Test
    void testRedirectUrlAfterPostWithoutModernCode() {
        HttpActionHelper.setUseModernHttpCodes(false);
        val action = HttpActionHelper
            .buildRedirectUrlAction(MockWebContext.create().setRequestMethod("POST"), PAC4J_URL);
        assertTrue(action instanceof FoundAction);
        assertEquals(PAC4J_URL, ((FoundAction) action).getLocation());
    }

    @Test
    void testFormPostContentAfterGet() {
        HttpActionHelper.setUseModernHttpCodes(true);
        val action = HttpActionHelper.buildFormPostContentAction(MockWebContext.create(), VALUE);
        assertTrue(action instanceof OkAction);
        assertEquals(VALUE, ((OkAction) action).getContent());
    }

    @Test
    void testFormPostContentAfterPost() {
        HttpActionHelper.setUseModernHttpCodes(true);
        val action = HttpActionHelper
            .buildFormPostContentAction(MockWebContext.create().setRequestMethod("POST"), VALUE);
        assertTrue(action instanceof OkAction);
        assertEquals(VALUE, ((OkAction) action).getContent());
    }

    @Test
    void testFormPostContentAfterPostWithoutModernCode() {
        HttpActionHelper.setUseModernHttpCodes(false);
        val action = HttpActionHelper
            .buildFormPostContentAction(MockWebContext.create().setRequestMethod("POST"), VALUE);
        assertTrue(action instanceof OkAction);
        assertEquals(VALUE, ((OkAction) action).getContent());
    }

    @Test
    void testBuildFormPostContent() {
        val content = HttpActionHelper.buildFormPostContent(MockWebContext.create().setFullRequestURL(CALLBACK_URL));
        assertEquals("<html>\n<body>\n<form action=\"" + CALLBACK_URL + "\" name=\"f\" method=\"post\">\n"
            + "<input value='POST' type='submit' />\n</form>\n" +
            "<script type='text/javascript'>document.forms['f'].submit();</script>\n" +
            "</body>\n</html>\n", content);
    }

    @Test
    void testBuildFormPostContentWithData() {
        val content = HttpActionHelper
            .buildFormPostContent(MockWebContext.create().setFullRequestURL(CALLBACK_URL).addRequestParameter(NAME, VALUE));
        assertEquals("<html>\n<body>\n<form action=\"" + CALLBACK_URL + "\" name=\"f\" method=\"post\">\n"
            + "<input type='hidden' name=\"" + NAME + "\" value=\"" + VALUE + "\" />\n" +
            "<input value='POST' type='submit' />\n</form>\n" +
            "<script type='text/javascript'>document.forms['f'].submit();</script>\n" +
            "</body>\n</html>\n", content);
    }

    @Test
    void testBuildUnauthenticated401WithHeader() {
        final WebContext context = MockWebContext.create();
        context.setResponseHeader(HttpConstants.AUTHENTICATE_HEADER, VALUE);
        val action = HttpActionHelper.buildUnauthenticatedAction(context);
        assertTrue(action instanceof UnauthorizedAction);
        assertEquals(VALUE, context.getResponseHeader(HttpConstants.AUTHENTICATE_HEADER).get());
    }

    @Test
    void testBuildUnauthenticated401WithoutHeader() {
        final WebContext context = MockWebContext.create();
        val action = HttpActionHelper.buildUnauthenticatedAction(context);
        assertTrue(action instanceof UnauthorizedAction);
        assertEquals("Bearer realm=\"pac4j\"", context.getResponseHeader(HttpConstants.AUTHENTICATE_HEADER).get());
    }

    @Test
    void testBuildUnauthenticated403WithHeader() {
        HttpActionHelper.setAlwaysUse401ForUnauthenticated(false);
        final WebContext context = MockWebContext.create();
        context.setResponseHeader(HttpConstants.AUTHENTICATE_HEADER, VALUE);
        val action = HttpActionHelper.buildUnauthenticatedAction(context);
        assertTrue(action instanceof UnauthorizedAction);
        assertEquals(VALUE, context.getResponseHeader(HttpConstants.AUTHENTICATE_HEADER).get());
    }

    @Test
    void testBuildUnauthenticated403WithoutHeader() {
        HttpActionHelper.setAlwaysUse401ForUnauthenticated(false);
        final WebContext context = MockWebContext.create();
        val action = HttpActionHelper.buildUnauthenticatedAction(context);
        assertTrue(action instanceof ForbiddenAction);
        assertTrue(context.getResponseHeader(HttpConstants.AUTHENTICATE_HEADER).isEmpty());
    }
}
