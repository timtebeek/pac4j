package org.pac4j.core.profile.converter;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class tests the {@link LocaleConverter} class.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
final class LocaleConverterTests {

    private final LocaleConverter converter = new LocaleConverter();

    @Test
    void testNull() {
        assertNull(this.converter.convert(null));
    }

    @Test
    void testNotAString() {
        assertNull(this.converter.convert(Boolean.TRUE));
    }

    @Test
    void testLanguage() {
        val locale = (Locale) this.converter.convert("fr");
        assertEquals("fr", locale.getLanguage());
    }

    @Test
    void testLanguageCountry() {
        val locale = (Locale) this.converter.convert(Locale.FRANCE.toString());
        assertEquals(Locale.FRANCE.getLanguage(), locale.getLanguage());
        assertEquals(Locale.FRANCE.getCountry(), locale.getCountry());
    }

    @Test
    void testBadLocale() {
        assertNull(this.converter.convert("1_2_3"));
    }
}
