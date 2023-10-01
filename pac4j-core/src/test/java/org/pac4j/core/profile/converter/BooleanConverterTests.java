package org.pac4j.core.profile.converter;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the {@link BooleanConverter} class.
 *
 * @author Jerome Leleu
 * @since 1.1.0
 */
final class BooleanConverterTests {

    private final BooleanConverter converter = new BooleanConverter();

    @Test
    void testNull() {
        assertFalse((Boolean) this.converter.convert(null));
    }

    @Test
    void testNotAStringNotABoolean() {
        assertFalse((Boolean) this.converter.convert(new Date()));
    }

    @Test
    void testBooleanFalse() {
        assertEquals(Boolean.FALSE, this.converter.convert(Boolean.FALSE));
    }

    @Test
    void testBooleanTrue() {
        assertEquals(Boolean.TRUE, this.converter.convert(Boolean.TRUE));
    }

    @Test
    void testFalse() {
        assertEquals(Boolean.FALSE, this.converter.convert("false"));
    }

    @Test
    void testTrue() {
        assertEquals(Boolean.TRUE, this.converter.convert("true"));
    }

    @Test
    void testOneString() {
        assertEquals(Boolean.TRUE, this.converter.convert("1"));
    }

    @Test
    void testOneNumber() {
        assertEquals(Boolean.TRUE, this.converter.convert(1));
    }
}
