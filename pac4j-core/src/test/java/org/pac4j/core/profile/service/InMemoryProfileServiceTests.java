package org.pac4j.core.profile.service;

import lombok.val;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.pac4j.core.context.CallContext;
import org.pac4j.core.credentials.UsernamePasswordCredentials;
import org.pac4j.core.credentials.password.PasswordEncoder;
import org.pac4j.core.credentials.password.ShiroPasswordEncoder;
import org.pac4j.core.exception.AccountNotFoundException;
import org.pac4j.core.profile.CommonProfile;
import org.pac4j.core.util.TestsConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the {@link InMemoryProfileService}.
 *
 * @author Elie Roux
 * @since 2.1.0
 */
public final class InMemoryProfileServiceTests implements TestsConstants {

    private static final String TEST_ID = "testId";
    private static final String TEST_LINKED_ID = "testLinkedId";
    private static final String TEST_USER = "testUser";
    private static final String TEST_USER2 = "testUser2";
    private static final String TEST_PASS = "testPass";
    private static final String TEST_PASS2 = "testPass2";
    private static final String IDPERSON1 = "idperson1";
    private static final String IDPERSON2 = "idperson2";
    private static final String IDPERSON3 = "idperson3";

    public final static PasswordEncoder PASSWORD_ENCODER = new ShiroPasswordEncoder(new DefaultPasswordService());
    public InMemoryProfileService<CommonProfile> inMemoryProfileService;


    @BeforeEach
    void setUp() {
        inMemoryProfileService = new InMemoryProfileService<>(x -> new CommonProfile());
        inMemoryProfileService.setPasswordEncoder(PASSWORD_ENCODER);
        val password = PASSWORD_ENCODER.encode(PASSWORD);
        // insert sample data
        final Map<String, Object> properties1 = new HashMap<>();
        properties1.put(USERNAME, GOOD_USERNAME);
        properties1.put(FIRSTNAME, FIRSTNAME_VALUE);
        var profile = new CommonProfile();
        profile.build(IDPERSON1, properties1);
        inMemoryProfileService.create(profile, PASSWORD);
        // second person,
        final Map<String, Object> properties2 = new HashMap<>();
        properties2.put(USERNAME, MULTIPLE_USERNAME);
        profile = new CommonProfile();
        profile.build(IDPERSON2, properties2);
        inMemoryProfileService.create(profile, PASSWORD);
        final Map<String, Object> properties3 = new HashMap<>();
        properties3.put(USERNAME, MULTIPLE_USERNAME);
        properties3.put(PASSWORD, password);
        profile = new CommonProfile();
        profile.build(IDPERSON3, properties3);
        inMemoryProfileService.create(profile, PASSWORD);
    }

    @Test
    void authentFailed() {
        assertThrows(AccountNotFoundException.class, () -> {
            val credentials = new UsernamePasswordCredentials(BAD_USERNAME, PASSWORD);
            inMemoryProfileService.validate(new CallContext(null, null), credentials);
        });
    }

    @Test
    void authentSuccessSingleAttribute() {
        val credentials = new UsernamePasswordCredentials(GOOD_USERNAME, PASSWORD);
        inMemoryProfileService.validate(new CallContext(null, null), credentials);
        val profile = credentials.getUserProfile();
        assertNotNull(profile);
        assertEquals(GOOD_USERNAME, profile.getUsername());
        assertEquals(2, profile.getAttributes().size());
        assertEquals(FIRSTNAME_VALUE, profile.getAttribute(FIRSTNAME));
    }

    @Test
    void testCreateUpdateFindDelete() {
        val profile = new CommonProfile();
        profile.setId(TEST_ID);
        profile.setLinkedId(TEST_LINKED_ID);
        profile.addAttribute(USERNAME, TEST_USER);
        // create
        inMemoryProfileService.create(profile, TEST_PASS);
        // check credentials
        val credentials = new UsernamePasswordCredentials(TEST_USER, TEST_PASS);
        inMemoryProfileService.validate(new CallContext(null, null), credentials);
        val profile1 = credentials.getUserProfile();
        assertNotNull(profile1);
        // check data
        val results = getData(TEST_ID);
        assertEquals(1, results.size());
        val result = results.get(0);
        assertEquals(5, result.size());
        assertEquals(TEST_ID, result.get("id"));
        assertEquals(TEST_LINKED_ID, result.get(AbstractProfileService.LINKEDID));
        assertNotNull(result.get(AbstractProfileService.SERIALIZED_PROFILE));
        assertEquals(TEST_USER, result.get(USERNAME));
        // findById
        val profile2 = inMemoryProfileService.findById(TEST_ID);
        assertEquals(TEST_ID, profile2.getId());
        assertEquals(TEST_LINKED_ID, profile2.getLinkedId());
        assertEquals(TEST_USER, profile2.getUsername());
        assertEquals(1, profile2.getAttributes().size());
        // update with password
        profile.addAttribute(USERNAME, TEST_USER2);
        inMemoryProfileService.update(profile, TEST_PASS2);
        var results2 = getData(TEST_ID);
        assertEquals(1, results2.size());
        var result2 = results2.get(0);
        assertEquals(5, result2.size());
        assertEquals(TEST_ID, result2.get("id"));
        assertEquals(TEST_LINKED_ID, result2.get(AbstractProfileService.LINKEDID));
        assertNotNull(result2.get(AbstractProfileService.SERIALIZED_PROFILE));
        assertEquals(TEST_USER2, result2.get(USERNAME));
        // check credentials
        val credentials2 = new UsernamePasswordCredentials(TEST_USER2, TEST_PASS2);
        inMemoryProfileService.validate(new CallContext(null, null), credentials2);
        val profile3 = credentials.getUserProfile();
        assertNotNull(profile3);
        // update with no password update
        inMemoryProfileService.update(profile, null);
        results2 = getData(TEST_ID);
        assertEquals(1, results2.size());
        result2 = results2.get(0);
        assertEquals(5, result2.size());
        assertEquals(TEST_USER2, result2.get(USERNAME));
        // check credentials
        inMemoryProfileService.validate(new CallContext(null, null), credentials2);
        val profile4 = credentials.getUserProfile();
        assertNotNull(profile4);
        // remove
        inMemoryProfileService.remove(profile);
        val results3 = getData(TEST_ID);
        assertEquals(0, results3.size());
    }

    private List<Map<String, Object>> getData(final String id) {
        return inMemoryProfileService.read(Arrays.asList("id", "username", "linkedid", "password", "serializedprofile"), "id", id);
    }
}
