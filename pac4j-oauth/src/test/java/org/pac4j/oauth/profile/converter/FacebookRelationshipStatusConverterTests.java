package org.pac4j.oauth.profile.converter;

import org.junit.jupiter.api.Test;
import org.pac4j.core.profile.converter.AttributeConverter;
import org.pac4j.oauth.profile.facebook.FacebookRelationshipStatus;
import org.pac4j.oauth.profile.facebook.converter.FacebookRelationshipStatusConverter;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class test the {@link FacebookRelationshipStatusConverter} class.
 *
 * @author Jerome Leleu
 * @since 1.0.0
 */
final class FacebookRelationshipStatusConverterTests {

    private final AttributeConverter converter = new FacebookRelationshipStatusConverter();

    @Test
    void testNull() {
        assertNull(this.converter.convert(null));
    }

    @Test
    void testNotAString() {
        assertNull(this.converter.convert(Boolean.TRUE));
    }

    @Test
    void testSingle() {
        assertEquals(FacebookRelationshipStatus.SINGLE, this.converter.convert("Single"));
    }

    @Test
    void testInARelationship() {
        assertEquals(FacebookRelationshipStatus.IN_A_RELATIONSHIP, this.converter.convert("In a relationship"));
    }

    @Test
    void testEngaged() {
        assertEquals(FacebookRelationshipStatus.ENGAGED, this.converter.convert("Engaged"));
    }

    @Test
    void testMarried() {
        assertEquals(FacebookRelationshipStatus.MARRIED, this.converter.convert("Married"));
    }

    @Test
    void testItsComplicated() {
        assertEquals(FacebookRelationshipStatus.ITS_COMPLICATED, this.converter.convert("It's complicated"));
    }

    @Test
    void testInAnOpenRelationship() {
        assertEquals(FacebookRelationshipStatus.IN_AN_OPEN_RELATIONSHIP,
                     this.converter.convert("In an open relationship"));
    }

    @Test
    void testWidowed() {
        assertEquals(FacebookRelationshipStatus.WIDOWED, this.converter.convert("Widowed"));
    }

    @Test
    void testSeparated() {
        assertEquals(FacebookRelationshipStatus.SEPARATED, this.converter.convert("Separated"));
    }

    @Test
    void testDivorced() {
        assertEquals(FacebookRelationshipStatus.DIVORCED, this.converter.convert("Divorced"));
    }

    @Test
    void testInACivilUnion() {
        assertEquals(FacebookRelationshipStatus.IN_A_CIVIL_UNION, this.converter.convert("In a civil union"));
    }

    @Test
    void testInADomesticPartnership() {
        assertEquals(FacebookRelationshipStatus.IN_A_DOMESTIC_PARTNERSHIP,
                     this.converter.convert("In a domestic partnership"));
    }

    @Test
    void testSingleEnum() {
        assertEquals(FacebookRelationshipStatus.SINGLE,
                     this.converter.convert(FacebookRelationshipStatus.SINGLE.toString()));
    }

    @Test
    void testInARelationshipEnum() {
        assertEquals(FacebookRelationshipStatus.IN_A_RELATIONSHIP,
                     this.converter.convert(FacebookRelationshipStatus.IN_A_RELATIONSHIP.toString()));
    }

    @Test
    void testEngagedEnum() {
        assertEquals(FacebookRelationshipStatus.ENGAGED,
                     this.converter.convert(FacebookRelationshipStatus.ENGAGED.toString()));
    }

    @Test
    void testMarriedEnum() {
        assertEquals(FacebookRelationshipStatus.MARRIED,
                     this.converter.convert(FacebookRelationshipStatus.MARRIED.toString()));
    }

    @Test
    void testItsComplicatedEnum() {
        assertEquals(FacebookRelationshipStatus.ITS_COMPLICATED,
                     this.converter.convert(FacebookRelationshipStatus.ITS_COMPLICATED.toString()));
    }

    @Test
    void testInAnOpenRelationshipEnum() {
        assertEquals(FacebookRelationshipStatus.IN_AN_OPEN_RELATIONSHIP,
                     this.converter.convert(FacebookRelationshipStatus.IN_AN_OPEN_RELATIONSHIP.toString()));
    }

    @Test
    void testWidowedEnum() {
        assertEquals(FacebookRelationshipStatus.WIDOWED,
                     this.converter.convert(FacebookRelationshipStatus.WIDOWED.toString()));
    }

    @Test
    void testSeparatedEnum() {
        assertEquals(FacebookRelationshipStatus.SEPARATED,
                     this.converter.convert(FacebookRelationshipStatus.SEPARATED.toString()));
    }

    @Test
    void testDivorcedEnum() {
        assertEquals(FacebookRelationshipStatus.DIVORCED,
                     this.converter.convert(FacebookRelationshipStatus.DIVORCED.toString()));
    }

    @Test
    void testInACivilUnionEnum() {
        assertEquals(FacebookRelationshipStatus.IN_A_CIVIL_UNION,
                     this.converter.convert(FacebookRelationshipStatus.IN_A_CIVIL_UNION.toString()));
    }

    @Test
    void testInADomesticPartnershipEnum() {
        assertEquals(FacebookRelationshipStatus.IN_A_DOMESTIC_PARTNERSHIP,
                     this.converter.convert(FacebookRelationshipStatus.IN_A_DOMESTIC_PARTNERSHIP.toString()));
    }
}
