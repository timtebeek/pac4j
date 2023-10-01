package org.pac4j.core.util;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * This class test {@link HttpUtils}
 *
 * @author Ravi Bhushan
 * @since 3.3.1
 */
class HttpUtilsTest {


    @Test
    void testBuildHttpErrorMessage() throws IOException {
        // creating mock htp connection
        HttpURLConnection connectionMock = null ;

        // expected test data for mock connection
        var testResponseBody = "{\"error_description\":\"MSIS9612: The authorization code received in [code] parameter is invalid. \"}";
        var testConnectionResponseCode =400;
        var testConnResponseMessage = "Bad Request.";

        // mocking expected test data
        try(InputStream in = new ByteArrayInputStream(testResponseBody.getBytes(StandardCharsets.UTF_8))) {
            connectionMock = Mockito.mock(HttpURLConnection.class);
            Mockito.when(connectionMock.getResponseCode()).thenReturn(testConnectionResponseCode);
            Mockito.when(connectionMock.getResponseMessage()).thenReturn(testConnResponseMessage);
            Mockito.when(connectionMock.getErrorStream()).thenReturn(in);

            //evaluating test
            var actual = HttpUtils.buildHttpErrorMessage(connectionMock);
            var expected = String.format("(%d) %s[%s]", testConnectionResponseCode, testConnResponseMessage, testResponseBody);
            assertEquals(expected, actual);
        }

    }

}
