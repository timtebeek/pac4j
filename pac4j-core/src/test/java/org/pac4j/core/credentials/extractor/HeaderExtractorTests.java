package org.pac4j.core.credentials.extractor;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.CallContext;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.credentials.TokenCredentials;
import org.pac4j.core.exception.CredentialsException;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.core.util.TestsHelper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * This class tests the {@link HeaderExtractor}.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
final class HeaderExtractorTests implements TestsConstants {

    private final static String GOOD_HEADER = "goodHeader";
    private final static String BAD_HEADER = "badHeader";

    private final static String GOOD_PREFIX = "goodPrefix ";
    private final static String BAD_PREFIX = "badPrefix ";

    private final static HeaderExtractor extractor = new HeaderExtractor(GOOD_HEADER, GOOD_PREFIX);

    @Test
    void testRetrieveHeaderOk() {
        val context = MockWebContext.create().addRequestHeader(GOOD_HEADER, GOOD_PREFIX + VALUE);
        val credentials = (TokenCredentials) extractor.extract(new CallContext(context, null)).get();
        assertEquals(VALUE, credentials.getToken());
    }

    @Test
    void testBadHeader() {
        val context = MockWebContext.create().addRequestHeader(BAD_HEADER, GOOD_PREFIX + VALUE);
        val credentials = extractor.extract(new CallContext(context, null));
        assertFalse(credentials.isPresent());
    }

    @Test
    void testBadPrefix() {
        val context = MockWebContext.create().addRequestHeader(GOOD_HEADER, BAD_PREFIX + VALUE);
        TestsHelper.expectException(() -> extractor.extract(new CallContext(context, null)), CredentialsException.class,
            "Wrong prefix for header: " + GOOD_HEADER);
    }
}
