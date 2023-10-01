package org.pac4j.saml.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensaml.core.xml.XMLObjectBuilderFactory;
import org.opensaml.saml.common.SAMLObjectBuilder;
import org.opensaml.saml.saml2.core.NameID;
import org.pac4j.core.util.TestsConstants;
import org.pac4j.saml.util.Configuration;

/**
 * This is {@link HazelcastSAMLMessageStoreTests} for {@link HazelcastSAMLMessageStore}.
 *
 * @author Francesco Chicchiriccò
 * @since 5.0.1
 */
@ExtendWith(MockitoExtension.class)
class HazelcastSAMLMessageStoreTests implements TestsConstants {

    private final XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();

    private final Map<String, String> backendMap = new HashMap<>();

    @Mock
    private IMap<String, String> storeMapInstance;

    private HazelcastSAMLMessageStore store;

    @BeforeEach
    void setUp() {
        when(storeMapInstance.put(anyString(), anyString())).thenAnswer(ic -> {
            backendMap.put(ic.getArgument(0), ic.getArgument(1));
            return ic.getArgument(0);
        });
        when(storeMapInstance.get(anyString())).thenAnswer(ic -> backendMap.get(ic.getArgument(0)));
        when(storeMapInstance.remove(anyString())).thenAnswer(ic -> backendMap.remove(ic.getArgument(0)));

        HazelcastInstance hazelcastInstance = mock(HazelcastInstance.class);
        when(hazelcastInstance.getMap(HazelcastSAMLMessageStore.class.getSimpleName())).
                thenAnswer(ic -> storeMapInstance);

        store = new HazelcastSAMLMessageStore(hazelcastInstance);
    }

    @Test
    void setGet() {
        @SuppressWarnings("unchecked")
        NameID message = ((SAMLObjectBuilder<NameID>) builderFactory.getBuilder(NameID.DEFAULT_ELEMENT_NAME)).
                buildObject();
        message.setValue("value");

        store.set("id", message);
        verify(storeMapInstance, times(1)).put(eq("id"), eq(Base64.getEncoder().encodeToString(
                Configuration.serializeSamlObject(message).toString().getBytes(StandardCharsets.UTF_8))));

        assertEquals(message.getValue(), ((NameID) store.get("id").get()).getValue());
        verify(storeMapInstance, times(1)).get(eq("id"));
        verify(storeMapInstance, times(1)).remove(eq("id"));

        assertTrue(store.get("id").isEmpty());
    }

    @Test
    void getEmpty() {
        assertTrue(store.get("notfound").isEmpty());
        verify(storeMapInstance, times(1)).get(eq("notfound"));
        verify(storeMapInstance, times(0)).remove(anyString());
    }

    @Test
    void removeEmpty() {
        store.remove("notfound");
        verify(storeMapInstance, times(1)).remove(eq("notfound"));
    }
}
