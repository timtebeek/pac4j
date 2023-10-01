package org.pac4j.saml.util;

import lombok.val;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for class {@link SAML2Utils}.
 *
 * @author jkacer
 * @since 1.8.0
 */
final class SAML2UtilsTests {

    @Test
    void twoNullUrisMustEqual() {
        assertTrue(SAML2Utils.urisEqualAfterPortNormalization(null, null));
    }

    @Test
    void nullUriAndNonNullUriMustNotEqual() throws URISyntaxException {
        val uri = new URI("http://somewhere/something");
        assertFalse(SAML2Utils.urisEqualAfterPortNormalization(uri, null));
        assertFalse(SAML2Utils.urisEqualAfterPortNormalization(null, uri));
    }

    @Test
    void uriMustEqualItself() throws URISyntaxException {
        val uri = new URI("http://somewhere/something");
        assertTrue(SAML2Utils.urisEqualAfterPortNormalization(uri, uri));
    }

    @Test
    void twoSameUrisMustEqual() throws URISyntaxException {
        val uri1 = new URI("http://somewhere/something");
        val uri2 = new URI("http://somewhere/something");
        assertTrue(SAML2Utils.urisEqualAfterPortNormalization(uri1, uri2));
        assertTrue(SAML2Utils.urisEqualAfterPortNormalization(uri2, uri1));
    }

    @Test
    void twoDifferntUrisMustNotEqual() throws URISyntaxException {
        val uri1 = new URI("http://somewhere/something1");
        val uri2 = new URI("http://somewhere/something2");
        assertFalse(SAML2Utils.urisEqualAfterPortNormalization(uri1, uri2));
        assertFalse(SAML2Utils.urisEqualAfterPortNormalization(uri2, uri1));
    }

    @Test
    void sameUrisWithImplicitAndExplicitHttpPortMustEqual() throws URISyntaxException {
        val uri1 = new URI("http://somewhere:80/something");
        val uri2 = new URI("http://somewhere/something");
        assertTrue(SAML2Utils.urisEqualAfterPortNormalization(uri1, uri2));
        assertTrue(SAML2Utils.urisEqualAfterPortNormalization(uri2, uri1));
    }

    @Test
    void sameUrisWithImplicitAndExplicitHttpsPortMustEqual() throws URISyntaxException {
        val uri1 = new URI("https://somewhere:443/something");
        val uri2 = new URI("https://somewhere/something");
        assertTrue(SAML2Utils.urisEqualAfterPortNormalization(uri1, uri2));
        assertTrue(SAML2Utils.urisEqualAfterPortNormalization(uri2, uri1));
    }

    @Test
    void differentUrisWithImplicitAndExplicitHttpPortMustNotEqual() throws URISyntaxException {
        val uri1 = new URI("http://somewhere:80/something1");
        val uri2 = new URI("http://somewhere/something2");
        assertFalse(SAML2Utils.urisEqualAfterPortNormalization(uri1, uri2));
        assertFalse(SAML2Utils.urisEqualAfterPortNormalization(uri2, uri1));
    }

    @Test
    void differentUrisWithImplicitAndExplicitHttpsPortMustNotEqual() throws URISyntaxException {
        val uri1 = new URI("https://somewhere:443/something1");
        val uri2 = new URI("https://somewhere/something2");
        assertFalse(SAML2Utils.urisEqualAfterPortNormalization(uri1, uri2));
        assertFalse(SAML2Utils.urisEqualAfterPortNormalization(uri2, uri1));
    }
}
