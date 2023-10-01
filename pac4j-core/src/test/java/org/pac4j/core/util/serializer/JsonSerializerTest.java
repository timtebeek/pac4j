package org.pac4j.core.util.serializer;

import lombok.val;
import org.junit.jupiter.api.Test;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.profile.UserProfile;
import org.pac4j.core.util.TestsConstants;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests {@link Serializer}.
 *
 * @author Jerome LELEU
 * @since 3.9.0
 */
class JsonSerializerTest implements TestsConstants {

    @Test
    void testString() {
        UserProfile profile = new CommonProfile();
        profile.setId(ID);
        profile.addAttribute(KEY, VALUE);

        val serializer = new JsonSerializer(CommonProfile.class);
        val encoded = serializer.serializeToString(profile);
        UserProfile decoded = (CommonProfile) serializer.deserializeFromString(encoded);

        assertEquals(decoded.getId(), profile.getId());
        assertEquals(1, profile.getAttributes().size());
        assertEquals(VALUE, profile.getAttribute(KEY));
    }

    @Test
    void testBytes() {
        UserProfile profile = new CommonProfile();
        profile.setId(ID);
        profile.addAttribute(KEY, VALUE);

        val serializer = new JsonSerializer(CommonProfile.class);
        val encoded = serializer.serializeToBytes(profile);
        UserProfile decoded = (CommonProfile) serializer.deserializeFromBytes(encoded);

        assertEquals(decoded.getId(), profile.getId());
        assertEquals(1, profile.getAttributes().size());
        assertEquals(VALUE, profile.getAttribute(KEY));
    }
}
