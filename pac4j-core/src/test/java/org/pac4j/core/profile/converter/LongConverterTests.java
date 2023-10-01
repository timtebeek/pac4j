package org.pac4j.core.profile.converter;

import org.junit.jupiter.api.Test;
import org.pac4j.core.util.Pac4jConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class tests the {@link LongConverter} class.
 *
 * @author Jerome Leleu
 * @since 1.2.0
 */
final class LongConverterTests {

    private final LongConverter converter = new LongConverter();

    private static final int INT_VALUE = 5;
    private static final long LONG_VALUE = 1234567890123L;

    @Test
    void testNull() {
        assertNull(this.converter.convert(null));
    }

    @Test
    void testNotAStringNotAnInteger() {
        assertNull(this.converter.convert(Boolean.TRUE));
    }

    @Test
    void testLong() {
        assertEquals(LONG_VALUE, (long) this.converter.convert(LONG_VALUE));
    }

    @Test
    void testLongString() {
        assertEquals(LONG_VALUE, (long) this.converter.convert(Pac4jConstants.EMPTY_STRING + LONG_VALUE));
    }

    @Test
    void testInteger() {
        assertEquals(INT_VALUE, (long) this.converter.convert(Integer.valueOf(INT_VALUE)));
    }
}
