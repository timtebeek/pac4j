package org.pac4j.oidc.metadata;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.oauth2.sdk.auth.ClientAuthenticationMethod;
import com.nimbusds.oauth2.sdk.id.Issuer;
import com.nimbusds.openid.connect.sdk.SubjectType;
import com.nimbusds.openid.connect.sdk.op.OIDCProviderMetadata;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.pac4j.core.exception.TechnicalException;
import org.pac4j.oidc.config.OidcConfiguration;

/**
 * @author Mathias Loesch
 * @since 6.0.0
 */
public class OidcOpMetadataResolverTest {

    public static final JWSAlgorithm JWS_ALGORITHM = JWSAlgorithm.HS256;

    @Test
    void shouldUseFirstServerSupportedAuthMethod() throws URISyntaxException {
        OidcConfiguration configuration = getOidcConfiguration(null);

        OidcOpMetadataResolver metadataResolver = getMetadataResolver(configuration,
            List.of(ClientAuthenticationMethod.CLIENT_SECRET_POST, ClientAuthenticationMethod.CLIENT_SECRET_BASIC));

        assertEquals(ClientAuthenticationMethod.CLIENT_SECRET_POST, metadataResolver.getClientAuthentication().getMethod());
    }

    @Test
    void shouldRespectClientSupportedAuthMethod() throws URISyntaxException {
        OidcConfiguration configuration = getOidcConfiguration(Set.of(ClientAuthenticationMethod.CLIENT_SECRET_BASIC));

        OidcOpMetadataResolver metadataResolver = getMetadataResolver(configuration,
            List.of(ClientAuthenticationMethod.PRIVATE_KEY_JWT, ClientAuthenticationMethod.CLIENT_SECRET_BASIC));

        assertEquals(ClientAuthenticationMethod.CLIENT_SECRET_BASIC, metadataResolver.getClientAuthentication().getMethod());
    }

    @Test
    void shouldFailInCaseOfNoCommonAuthMethod() throws URISyntaxException {
        OidcConfiguration oidcConfiguration = getOidcConfiguration(Set.of(ClientAuthenticationMethod.CLIENT_SECRET_BASIC));

        try {
            getMetadataResolver(oidcConfiguration, List.of(ClientAuthenticationMethod.CLIENT_SECRET_POST));
            fail("TechnicalException expected");
        } catch (TechnicalException e) {
            assertEquals("None of the Token endpoint provider metadata authentication methods are supported: [client_secret_post]",
                e.getMessage());
        }
    }

    private static OidcConfiguration getOidcConfiguration(Set<ClientAuthenticationMethod> supportedClientAuthenticationMethods) {
        OidcConfiguration configuration = new OidcConfiguration();
        configuration.setClientId("clientId");
        configuration.setSecret("secret");
        configuration.setDiscoveryURI("test");
        configuration.setPreferredJwsAlgorithm(JWS_ALGORITHM);
        configuration.setSupportedClientAuthenticationMethods(supportedClientAuthenticationMethods);
        return configuration;
    }

    private static OidcOpMetadataResolver getMetadataResolver(OidcConfiguration configuration,
        List<ClientAuthenticationMethod> supportedAuthMethods) throws URISyntaxException {
        OIDCProviderMetadata providerMetadata = getOidcProviderMetadata(supportedAuthMethods);
        OidcOpMetadataResolver oidcOpMetadataResolver = new OidcOpMetadataResolver(configuration) {
            @Override
            protected OIDCProviderMetadata retrieveMetadata() {
                return providerMetadata;
            }
        };

        oidcOpMetadataResolver.init();
        return oidcOpMetadataResolver;
    }

    private static OIDCProviderMetadata getOidcProviderMetadata(List<ClientAuthenticationMethod> supportedClientAuthenticationMethods)
        throws URISyntaxException {
        OIDCProviderMetadata providerMetadata = new OIDCProviderMetadata(new Issuer("issuer"), List.of(SubjectType.PUBLIC), new URI(""));
        providerMetadata.setIDTokenJWSAlgs(List.of(JWS_ALGORITHM));
        providerMetadata.setTokenEndpointAuthMethods(supportedClientAuthenticationMethods);
        return providerMetadata;
    }


}
