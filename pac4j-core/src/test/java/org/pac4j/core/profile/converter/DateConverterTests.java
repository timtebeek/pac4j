package org.pac4j.core.profile.converter;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class tests the {@link DateConverter} class.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
final class DateConverterTests {

    private static final String FORMAT = "yyyy.MM.dd";

    private final DateConverter converter = new DateConverter(FORMAT);

    private static final String GOOD_DATE = "2012.01.01";

    private static final String BAD_DATE = "2012/01/01";

    @Test
    void testNull() {
        assertNull(this.converter.convert(null));
    }

    @Test
    void testNotAString() {
        assertNull(this.converter.convert(Boolean.TRUE));
    }

    @Test
    void testGoodDate() {
        val d = (Date) this.converter.convert(GOOD_DATE);
        val simpleDateFormat = new SimpleDateFormat(FORMAT);
        assertEquals(GOOD_DATE, simpleDateFormat.format(d));
    }

    @Test
    void testBadDate() {
        assertNull(this.converter.convert(BAD_DATE));
    }
}
