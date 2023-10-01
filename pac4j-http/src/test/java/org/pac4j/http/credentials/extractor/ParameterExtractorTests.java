package org.pac4j.http.credentials.extractor;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.CallContext;
import org.pac4j.core.context.HttpConstants;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.session.MockSessionStore;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.credentials.extractor.ParameterExtractor;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This class tests the {@link ParameterExtractor}.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
final class ParameterExtractorTests implements TestsConstants {

    private final static String GOOD_PARAMETER = "goodParameter";

    private final static ParameterExtractor getExtractor = new ParameterExtractor(GOOD_PARAMETER, true, false);
    private final static ParameterExtractor postExtractor = new ParameterExtractor(GOOD_PARAMETER, false, true);

    @Test
    void testRetrieveGetParameterOk() {
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.GET.name())
            .addRequestParameter(GOOD_PARAMETER, VALUE);
        val credentials = (TokenCredentials) getExtractor.extract(new CallContext(context, new MockSessionStore())).get();
        assertEquals(VALUE, credentials.getToken());
    }

    @Test
    void testRetrievePostParameterOk() {
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.POST.name())
            .addRequestParameter(GOOD_PARAMETER, VALUE);
        val credentials = (TokenCredentials) postExtractor.extract(new CallContext(context, new MockSessionStore())).get();
        assertEquals(VALUE, credentials.getToken());
    }

    @Test
    void testRetrievePostParameterNotSupported() {
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.POST.name())
            .addRequestParameter(GOOD_PARAMETER, VALUE);
        TestsHelper.expectException(() -> getExtractor.extract(new CallContext(context, new MockSessionStore())),
                CredentialsException.class, "POST requests not supported");
    }

    @Test
    void testRetrieveGetParameterNotSupported() {
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.GET.name())
            .addRequestParameter(GOOD_PARAMETER, VALUE);
        TestsHelper.expectException(() -> postExtractor.extract(new CallContext(context, new MockSessionStore())),
                CredentialsException.class, "GET requests not supported");
    }

    @Test
    void testRetrieveNoGetParameter() {
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.GET.name());
        val credentials = getExtractor.extract(new CallContext(context, new MockSessionStore()));
        assertFalse(credentials.isPresent());
    }

    @Test
    void testRetrieveNoPostParameter() {
        val context = MockWebContext.create().setRequestMethod(HttpConstants.HTTP_METHOD.POST.name());
        val credentials = postExtractor.extract(new CallContext(context, new MockSessionStore()));
        assertFalse(credentials.isPresent());
    }
}
