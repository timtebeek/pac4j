package org.pac4j.jwt.config.signature;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.core.util.TestsHelper;
import org.pac4j.jwt.config.AbstractKeyEncryptionConfigurationTests;
import org.pac4j.jwt.util.JWKHelper;

import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link ECSignatureConfiguration}.
 *
 * @author Jerome Leleu
 * @since 1.9.2
 */
final class ECSignatureConfigurationTests extends AbstractKeyEncryptionConfigurationTests {

    @Override
    protected String getAlgorithm() {
        return "EC";
    }

    private JWTClaimsSet buildClaims() {
        return new JWTClaimsSet.Builder().subject(VALUE).build();
    }

    @Test
    void testMissingPrivateKey() {
        val config = new ECSignatureConfiguration();
        TestsHelper.expectException(() -> config.sign(buildClaims()), TechnicalException.class, "privateKey cannot be null");
    }

    @Test
    void testMissingPublicKey() {
        val config = new ECSignatureConfiguration();
        if (TestsHelper.getJdkVersion() > 17) {
            config.setAlgorithm(JWSAlgorithm.ES384);
        }
        config.setPrivateKey((ECPrivateKey) buildKeyPair().getPrivate());
        val signedJWT = config.sign(buildClaims());
        TestsHelper.expectException(() -> config.verify(signedJWT), TechnicalException.class, "publicKey cannot be null");
    }

    @Test
    void testMissingAlgorithm() {
        val config = new ECSignatureConfiguration(buildKeyPair(), null);
        TestsHelper.expectException(config::init, TechnicalException.class, "algorithm cannot be null");
    }

    @Test
    void testBadAlgorithm() {
        val config = new ECSignatureConfiguration(buildKeyPair(), JWSAlgorithm.HS256);
        TestsHelper.expectException(config::init, TechnicalException.class,
            "Only the ES256, ES384 and ES512 algorithms are supported for elliptic curve signature");
    }

    @Test
    void buildFromJwk() {
        final Curve curve;
        if (TestsHelper.getJdkVersion() > 17) {
            curve = Curve.P_384;
        } else {
            curve = Curve.P_256;
        }
        val json = new ECKey.Builder(curve, (ECPublicKey) buildKeyPair().getPublic()).build().toJSONString();
        JWKHelper.buildECKeyPairFromJwk(json);
    }

    @Test
    void testSignVerify() throws JOSEException {
        val config = new ECSignatureConfiguration(buildKeyPair());
        if (TestsHelper.getJdkVersion() > 17) {
            config.setAlgorithm(JWSAlgorithm.ES384);
        }
        val claims = new JWTClaimsSet.Builder().subject(VALUE).build();
        val signedJwt = config.sign(claims);
        assertTrue(config.verify(signedJwt));
    }
}
