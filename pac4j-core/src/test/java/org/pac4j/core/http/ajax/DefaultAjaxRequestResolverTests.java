package org.pac4j.core.http.ajax;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.CallContext;
import org.pac4j.core.context.MockWebContext;

import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Tests the {@link DefaultAjaxRequestResolver}.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
final class DefaultAjaxRequestResolverTests {

    private final AjaxRequestResolver resolver = new DefaultAjaxRequestResolver();

    @Test
    void testRealAjaxRequest() {
        val context = MockWebContext.create().addRequestHeader("X-Requested-With", "XMLHttpRequest");
        assertTrue(resolver.isAjax(new CallContext(context, null)));
    }

    @Test
    void testForcedAjaxParameter() {
        val context = MockWebContext.create().addRequestParameter("is_ajax_request", "true");
        assertTrue(resolver.isAjax(new CallContext(context, null)));
    }

    @Test
    void testForcedAjaxHeader() {
        val context = MockWebContext.create().addRequestHeader("is_ajax_request", "true");
        assertTrue(resolver.isAjax(new CallContext(context, null)));
    }

    @Test
    void testNotAnAjaxRequest() {
        assertFalse(resolver.isAjax(new CallContext(MockWebContext.create(), null)));
    }
}
