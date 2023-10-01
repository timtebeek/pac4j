package org.pac4j.core.util;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.profile.CommonProfile;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the {@link CommonHelper} class.
 *
 * @author Jerome Leleu
 * @since 1.4.0
 */
final class CommonHelperTests {

    private static final String URL_WITHOUT_PARAMETER = "http://host/app";

    private static final String URL_WITH_PARAMETER = "http://host/app?param=value";

    private static final String NAME = "name";

    private static final String VALUE = "va+l+ue";

    private static final String ENCODED_VALUE = "va%2Bl%2Bue";

    @Test
    void testIsNotBlankNull() {
        assertFalse(CommonHelper.isNotBlank(null));
    }

    @Test
    void testIsNotBlankEmply() {
        assertFalse(CommonHelper.isNotBlank(Pac4jConstants.EMPTY_STRING));
    }

    @Test
    void testIsNotBlankBlank() {
        assertFalse(CommonHelper.isNotBlank("     "));
    }

    @Test
    void testIsNotBlankNotBlank() {
        assertTrue(CommonHelper.isNotBlank(NAME));
    }

    @Test
    void testAssertNotBlankBlank() {
        try {
            CommonHelper.assertNotBlank(NAME, Pac4jConstants.EMPTY_STRING);
            fail("must throw an TechnicalException");
        } catch (final TechnicalException e) {
            assertEquals(NAME + " cannot be blank", e.getMessage());
        }
    }

    @Test
    void testAssertNotBlankNotBlank() {
        CommonHelper.assertNotBlank(NAME, VALUE);
    }

    @Test
    void testAssertNotNullNull() {
        try {
            CommonHelper.assertNotNull(NAME, null);
            fail("must throw an TechnicalException");
        } catch (final TechnicalException e) {
            assertEquals(NAME + " cannot be null", e.getMessage());
        }
    }

    @Test
    void testAssertNotNullNotNull() {
        CommonHelper.assertNotNull(NAME, VALUE);
    }

    @Test
    void testAddParameterNullUrl() {
        assertNull(CommonHelper.addParameter(null, NAME, VALUE));
    }

    @Test
    void testAddParameterNullName() {
        assertEquals(URL_WITH_PARAMETER, CommonHelper.addParameter(URL_WITH_PARAMETER, null, VALUE));
    }

    @Test
    void testAddParameterNullValue() {
        assertEquals(URL_WITH_PARAMETER + "&" + NAME + "=", CommonHelper.addParameter(URL_WITH_PARAMETER, NAME, null));
    }

    @Test
    void testAddParameterWithParameter() {
        assertEquals(URL_WITH_PARAMETER + "&" + NAME + "=" + ENCODED_VALUE,
                CommonHelper.addParameter(URL_WITH_PARAMETER, NAME, VALUE));
    }

    @Test
    void testAddParameterWithoutParameter() {
        assertEquals(URL_WITHOUT_PARAMETER + "?" + NAME + "=" + ENCODED_VALUE,
                CommonHelper.addParameter(URL_WITHOUT_PARAMETER, NAME, VALUE));
    }

    @Test
    void testAreEqualsOk() {
        assertTrue(CommonHelper.areEquals(null, null));
        assertTrue(CommonHelper.areEquals(VALUE, VALUE));
    }

    @Test
    void testAreEqualsIgnoreCaseAndTrimOk() {
        assertTrue(CommonHelper.areEqualsIgnoreCaseAndTrim(null, null));
        assertTrue(CommonHelper.areEqualsIgnoreCaseAndTrim(" " + VALUE.toUpperCase(), VALUE + "                "));
    }

    @Test
    void testAreEqualsFails() {
        assertFalse(CommonHelper.areEquals(VALUE, null));
        assertFalse(CommonHelper.areEquals(null, VALUE));
        assertFalse(CommonHelper.areEquals(NAME, VALUE));
    }

    @Test
    void testAreEqualsIgnoreCaseAndTrimFails() {
        assertFalse(CommonHelper.areEqualsIgnoreCaseAndTrim(VALUE, null));
        assertFalse(CommonHelper.areEqualsIgnoreCaseAndTrim(NAME, VALUE));
    }

    @Test
    void testAssertNotBlank_null() {
        assertThrows(TechnicalException.class, () -> {
            String var = null;
            CommonHelper.assertNotBlank("var", var);
        });
    }

    @Test
    void testAssertNotBlank_empty() {
        assertThrows(TechnicalException.class, () -> {
            var var = " ";
            CommonHelper.assertNotBlank("var", var);
        });
    }

    @Test
    void testAssertNotBlank_notBlank() {
        var var = "contents";
        CommonHelper.assertNotBlank("var", var);
    }

    @Test
    void testAssertNotNull_null() {
        assertThrows(TechnicalException.class, () -> {
            String var = null;
            CommonHelper.assertNotNull("var", var);
        });
    }

    @Test
    void testAssertNotNull_notBlank() {
        var var = "contents";
        CommonHelper.assertNotNull("var", var);
    }

    @Test
    void testAssertNull_null() {
        CommonHelper.assertNull("var", null);
    }

    @Test
    void testAssertNull_notNull() {
        assertThrows(TechnicalException.class, () -> {
            CommonHelper.assertNull("var", "notnull");
        });
    }

    @Test
    void testRandomStringNChars() {
        for (var i = 0; i < 128; i++) {
            testRandomString(i);
        }
    }

    private void testRandomString(final int size) {
        val s = CommonHelper.randomString(size);
        assertEquals(size, s.length());
    }

    @Test
    void testSubstringAfter() {
        assertEquals("after", CommonHelper.substringAfter("before###after", "###"));
    }

    @Test
    void testSubstringBefore() {
        assertEquals("before", CommonHelper.substringBefore("before###after", "###"));
    }

    @Test
    void testSubstringBetween() {
        assertEquals("bet", CommonHelper.substringBetween("123startbet##456", "start", "##"));
    }

    @Test
    void testIsEmpty() {
        assertTrue(CommonHelper.isEmpty(null));
        assertTrue(CommonHelper.isEmpty(new ArrayList<>()));
        assertFalse(CommonHelper.isEmpty(Arrays.asList(new String[] {VALUE})));
    }

    @Test
    void testIsNotEmpty() {
        assertFalse(CommonHelper.isNotEmpty(null));
        assertFalse(CommonHelper.isNotEmpty(new ArrayList<>()));
        assertTrue(CommonHelper.isNotEmpty(Arrays.asList(new String[] {VALUE})));
    }

    @Test
    void testGetConstructorOK() throws Exception {
        var constructor = CommonHelper.getConstructor(CommonProfile.class.getName());
        val profile = (CommonProfile) constructor.newInstance();
        assertNotNull(profile);
    }

    @Test
    void testGetConstructorMissingClass() throws Exception {
        assertThrows(ClassNotFoundException.class, () -> {
            CommonHelper.getConstructor("this.class.does.not.Exist");
        });
    }
}
