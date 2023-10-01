package org.pac4j.core.client.finder;

import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.pac4j.core.client.Clients;
import org.pac4j.core.client.MockIndirectClient;
import org.pac4j.core.context.MockWebContext;
import org.pac4j.core.context.WebContext;
import org.pac4j.core.exception.http.FoundAction;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.Pac4jConstants;
import org.pac4j.core.util.TestsConstants;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests {@link DefaultSecurityClientFinder}.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
public final class DefaultSecurityClientFinderTests implements TestsConstants, Pac4jConstants {

    private DefaultSecurityClientFinder finder;
    public String clientNameParameter;

    public static Object[] data() {
        return new Object[] {null, "custom"};
    }

    @BeforeEach
    void setUp() {
        finder = new DefaultSecurityClientFinder();
        if (clientNameParameter != null) {
            finder.setClientNameParameter(clientNameParameter);
        }
    }

    @MethodSource("data")
    @ParameterizedTest
    void testBlankClientName(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        val currentClients = finder.find(new Clients(), MockWebContext.create(), "  ");
        assertEquals(0, currentClients.size());
    }

    @MethodSource("data")
    @ParameterizedTest
    void testClientOnRequestAllowed(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        internalTestClientOnRequestAllowedList(NAME, NAME);
    }

    @MethodSource("data")
    @ParameterizedTest
    void testBadClientOnRequest(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        val client =
            new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val clients = new Clients(client);
        final WebContext context = MockWebContext.create().addRequestParameter(getClientNameParameter(), FAKE_VALUE);
        assertTrue(finder.find(clients, context, NAME).isEmpty());
    }

    protected String getClientNameParameter() {
        return Objects.requireNonNullElse(clientNameParameter, DEFAULT_FORCE_CLIENT_PARAMETER);
    }

    @MethodSource("data")
    @ParameterizedTest
    void testClientOnRequestAllowedList(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        internalTestClientOnRequestAllowedList(NAME, FAKE_VALUE + "," + NAME);
    }

    @MethodSource("data")
    @ParameterizedTest
    void testClientOnRequestAllowedListCaseTrim(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        internalTestClientOnRequestAllowedList("NaMe  ", FAKE_VALUE.toUpperCase() + "  ,       nAmE");
    }

    private void internalTestClientOnRequestAllowedList(final String parameterName, final String names) {
        val client =
            new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val clients = new Clients(client);
        final WebContext context = MockWebContext.create().addRequestParameter(getClientNameParameter(), parameterName);
        val currentClients = finder.find(clients, context, names);
        assertEquals(1, currentClients.size());
        assertEquals(client, currentClients.get(0));
    }

    @MethodSource("data")
    @ParameterizedTest
    void testClientOnRequestNotAllowed(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        val client1 =
            new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val client2 =
            new MockIndirectClient(MY_CLIENT_NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val clients = new Clients(client1, client2);
        final WebContext context = MockWebContext.create().addRequestParameter(getClientNameParameter(), NAME);
        assertTrue(finder.find(clients, context, MY_CLIENT_NAME).isEmpty());
    }

    @MethodSource("data")
    @ParameterizedTest
    void testClientOnRequestNotAllowedList(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        val client1 =
            new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val client2 =
            new MockIndirectClient(MY_CLIENT_NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val clients = new Clients(client1, client2);
        final WebContext context = MockWebContext.create().addRequestParameter(getClientNameParameter(), NAME);
        assertTrue(finder.find(clients, context, MY_CLIENT_NAME + "," + FAKE_VALUE).isEmpty());
    }

    @MethodSource("data")
    @ParameterizedTest
    void testNoClientOnRequest(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        val client1 =
            new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val client2 =
            new MockIndirectClient(MY_CLIENT_NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val clients = new Clients(client1, client2);
        final WebContext context = MockWebContext.create();
        val currentClients = finder.find(clients, context, MY_CLIENT_NAME);
        assertEquals(1, currentClients.size());
        assertEquals(client2, currentClients.get(0));
    }

    @MethodSource("data")
    @ParameterizedTest
    void testNoClientOnRequestBadDefaultClient(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        val client1 =
            new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val client2 =
            new MockIndirectClient(MY_CLIENT_NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val clients = new Clients(client1, client2);
        final WebContext context = MockWebContext.create();
        assertTrue(finder.find(clients, context, FAKE_VALUE).isEmpty());
    }

    @MethodSource("data")
    @ParameterizedTest
    void testNoClientOnRequestList(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        internalTestNoClientOnRequestList(MY_CLIENT_NAME + "," + NAME);
    }

    @MethodSource("data")
    @ParameterizedTest
    void testNoClientOnRequestListBlankSpaces(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        internalTestNoClientOnRequestList(MY_CLIENT_NAME + " ," + NAME);
    }

    @MethodSource("data")
    @ParameterizedTest
    void testNoClientOnRequestListDifferentCase(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        internalTestNoClientOnRequestList(MY_CLIENT_NAME.toUpperCase() + "," + NAME);
    }

    @MethodSource("data")
    @ParameterizedTest
    void testNoClientOnRequestListUppercase(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        internalTestNoClientOnRequestList(MY_CLIENT_NAME.toUpperCase() + "," + NAME);
    }

    private void internalTestNoClientOnRequestList(final String names) {
        val client1 =
            new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val client2 =
            new MockIndirectClient(MY_CLIENT_NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val clients = new Clients(client1, client2);
        final WebContext context = MockWebContext.create();
        val currentClients = finder.find(clients, context, names);
        assertEquals(2, currentClients.size());
        assertEquals(client2, currentClients.get(0));
        assertEquals(client1, currentClients.get(1));
    }

    @MethodSource("data")
    @ParameterizedTest
    void testDefaultSecurityClients(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        val client1 =
            new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val client2 =
            new MockIndirectClient(MY_CLIENT_NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val clients = new Clients(client1, client2);
        clients.setDefaultSecurityClients(MY_CLIENT_NAME);
        val result = finder.find(clients, MockWebContext.create(), null);
        assertEquals(1, result.size());
        assertEquals(client2, result.get(0));
    }

    @MethodSource("data")
    @ParameterizedTest
    void testOneClientAsDefault(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        val client1 =
            new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val clients = new Clients(client1);
        val result = finder.find(clients, MockWebContext.create(), null);
        assertEquals(1, result.size());
        assertEquals(client1, result.get(0));
    }

    @MethodSource("data")
    @ParameterizedTest
    void testBlankClientRequested(String clientNameParameter) {
        initDefaultSecurityClientFinderTests(clientNameParameter);
        val client1 =
            new MockIndirectClient(NAME, new FoundAction(LOGIN_URL), Optional.empty(), new CommonProfile());
        val clients = new Clients(client1);
        val result = finder.find(clients, MockWebContext.create(), Pac4jConstants.EMPTY_STRING);
        assertEquals(0, result.size());
    }

    public void initDefaultSecurityClientFinderTests(String clientNameParameter) {
        this.clientNameParameter = clientNameParameter;
    }
}
