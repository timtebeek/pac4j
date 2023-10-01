package org.pac4j.core.profile.converter;

import org.junit.jupiter.api.Test;
import org.pac4j.core.util.Pac4jConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class tests the {@link IntegerConverter} class.
 *
 * @author Jerome Leleu
 * @since 1.1.0
 */
final class IntegerConverterTests {

    private final IntegerConverter converter = new IntegerConverter();

    private static final int VALUE = 12;

    @Test
    void testNull() {
        assertNull(this.converter.convert(null));
    }

    @Test
    void testNotAStringNotAnInteger() {
        assertNull(this.converter.convert(Boolean.TRUE));
    }

    @Test
    void testInteger() {
        assertEquals(VALUE, (int) this.converter.convert(VALUE));
    }

    @Test
    void testIntegerString() {
        assertEquals(VALUE, (int) this.converter.convert(Pac4jConstants.EMPTY_STRING + VALUE));
    }
}
