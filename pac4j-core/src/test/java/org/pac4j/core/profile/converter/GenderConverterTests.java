package org.pac4j.core.profile.converter;

import org.junit.jupiter.api.Test;
import org.pac4j.core.profile.Gender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * This class tests the {@link GenderConverter} class.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
final class GenderConverterTests {

    private final AttributeConverter converter = new GenderConverter();
    private final AttributeConverter converterNumber = new GenderConverter("2", "1");
    private final AttributeConverter converterChinese = new GenderConverter("男", "女");

    @Test
    void testNull() {
        assertNull(this.converter.convert(null));
    }

    @Test
    void testNotAString() {
        assertEquals(Gender.UNSPECIFIED, this.converter.convert(Boolean.TRUE));
    }

    @Test
    void testMale() {
        assertEquals(Gender.MALE, this.converter.convert("m"));
    }

    @Test
    void testFemale() {
        assertEquals(Gender.FEMALE, this.converter.convert("f"));
    }

    @Test
    void testMaleNumber() {
        assertEquals(Gender.MALE, this.converterNumber.convert(2));
    }

    @Test
    void testFemaleNumber() {
        assertEquals(Gender.FEMALE, this.converterNumber.convert(1));
    }

    @Test
    void testUnspecified() {
        assertEquals(Gender.UNSPECIFIED, this.converter.convert("unspecified"));
    }

    @Test
    void testMaleEnum() {
        assertEquals(Gender.MALE, this.converter.convert(Gender.MALE.toString()));
    }

    @Test
    void testFemaleEnum() {
        assertEquals(Gender.FEMALE, this.converter.convert(Gender.FEMALE.toString()));
    }

    @Test
    void testUnspecifiedEnum() {
        assertEquals(Gender.UNSPECIFIED, this.converter.convert(Gender.UNSPECIFIED.toString()));
    }

    @Test
    void testMaleChinese() {
        assertEquals(Gender.MALE, this.converterChinese.convert("男"));
    }

    @Test
    void testFemaleChinese() {
        assertEquals(Gender.FEMALE, this.converterChinese.convert("女"));
    }

    @Test
    void testUnspecifiedChinese() {
        assertEquals(Gender.UNSPECIFIED, this.converterChinese.convert("其他"));
    }
}
