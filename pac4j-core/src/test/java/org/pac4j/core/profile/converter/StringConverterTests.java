package org.pac4j.core.profile.converter;

import org.junit.jupiter.api.Test;
import org.pac4j.core.util.TestsConstants;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class tests the {@link StringConverter} class.
 *
 * @author Jerome Leleu
 * @since 1.1.0
 */
final class StringConverterTests implements TestsConstants {

    private final AttributeConverter converter = new StringConverter();

    @Test
    void testNull() {
        assertNull(this.converter.convert(null));
    }

    @Test
    void testListNull() {
        assertNull(this.converter.convert(new ArrayList<>()));
    }

    @Test
    void testNotAString() {
        assertNull(this.converter.convert(Boolean.TRUE));
    }

    @Test
    void testString() {
        assertEquals(VALUE, this.converter.convert(VALUE));
    }

    @Test
    void testListString() {
        assertEquals(VALUE, this.converter.convert(List.of(VALUE)));
    }
}
