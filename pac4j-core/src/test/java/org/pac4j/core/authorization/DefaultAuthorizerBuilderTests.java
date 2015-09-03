/*
  Copyright 2012 - 2015 pac4j organization

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */
package org.pac4j.core.authorization;

import org.junit.Test;
import org.pac4j.core.exception.TechnicalException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Tests the {@link DefaultAuthorizerBuilder}.
 *
 * @author Jerome Leleu
 * @since 1.8.0
 */
public final class DefaultAuthorizerBuilderTests {

    private final static String NAME = "fakeAuth";

    @Test
    public void testProvidedAuthorizer() {
        final FakeAuthorizer authorizer = new FakeAuthorizer();
        final Authorizer result = DefaultAuthorizerBuilder.build(authorizer, null, null);
        assertEquals(authorizer, result);
    }

    @Test(expected = TechnicalException.class)
    public void testNameNoAuthorizers() {
        final Authorizer result = DefaultAuthorizerBuilder.build(null, NAME, null);
    }

    @Test(expected = TechnicalException.class)
    public void testNameEmptyAuthorizers() {
        final Authorizer result = DefaultAuthorizerBuilder.build(null, NAME, new HashMap<String, Authorizer>());
    }

    @Test
    public void testName() {
        final FakeAuthorizer authorizer = new FakeAuthorizer();
        final Map<String, Authorizer> authorizers = new HashMap<>();
        authorizers.put(NAME, authorizer);
        final Authorizer result = DefaultAuthorizerBuilder.build(null, NAME, authorizers);
        assertEquals(authorizer, result);
    }

    @Test
    public void testDefault() {
        final Authorizer result = DefaultAuthorizerBuilder.build(null, null, null);
        assertTrue(result instanceof IsAuthenticatedAuthorizer);
    }
}
