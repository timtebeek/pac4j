package org.pac4j.core.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.pac4j.core.exception.TechnicalException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * This class tests the {@link InitializableObject} class.
 *
 * @author Jerome Leleu
 * @since 1.4.0
 */
@Slf4j
final class InitializableObjectTests {

    @Test
    void testInitCalledOnlyOnce() {
        var io = new CustomInitializableObject(false);
        assertEquals(0, io.getCounter());
        assertEquals(0, io.getNbAttempts());
        assertNull(io.getLastAttempt());

        io.init();
        assertEquals(1, io.getCounter());
        assertEquals(1, io.getNbAttempts());
        assertNotNull(io.getLastAttempt());

        io.init();
        assertEquals(1, io.getCounter());
        assertEquals(1, io.getNbAttempts());
        assertNotNull(io.getLastAttempt());
    }

    @Test
    void testInitCalledOnlyOnceDespiteFailuresButNotEnoughTimeBetweenRetries() {
        var io = new CustomInitializableObject(true);
        assertEquals(0, io.getCounter());
        assertEquals(0, io.getNbAttempts());
        assertNull(io.getLastAttempt());

        catchInit(io);
        assertEquals(1, io.getCounter());
        assertEquals(1, io.getNbAttempts());
        assertNotNull(io.getLastAttempt());

        catchInit(io);
        assertEquals(1, io.getCounter());
        assertEquals(1, io.getNbAttempts());
        assertNotNull(io.getLastAttempt());
    }

    private void catchInit(final InitializableObject initializableObject) {
        try {
            initializableObject.init();
        } catch (final TechnicalException e) {
            LOGGER.debug("Expected TechnicalException");
        }
    }

    @Test
    void testInitCalledTwiceBecauseOfFailuresAndEnoughTimeBetweenRetries() {
        var io = new CustomInitializableObject(true);
        io.setMinTimeIntervalBetweenAttemptsInMilliseconds(200);
        assertEquals(0, io.getCounter());
        assertEquals(0, io.getNbAttempts());
        assertNull(io.getLastAttempt());

        catchInit(io);
        assertEquals(1, io.getCounter());
        assertEquals(1, io.getNbAttempts());
        assertNotNull(io.getLastAttempt());
        TestsHelper.wait(400);

        catchInit(io);
        assertEquals(2, io.getCounter());
        assertEquals(2, io.getNbAttempts());
        assertNotNull(io.getLastAttempt());
    }

    @Test
    void testInitNotCalledBecauseOfMaxAttempts() {
        var io = new CustomInitializableObject(false);
        io.setMaxAttempts(0);
        assertEquals(0, io.getCounter());
        assertEquals(0, io.getNbAttempts());
        assertNull(io.getLastAttempt());

        catchInit(io);
        assertEquals(0, io.getCounter());
        assertEquals(0, io.getNbAttempts());
        assertNull(io.getLastAttempt());
    }

    private static final class CustomInitializableObject extends InitializableObject {

        private int counter;

        private boolean fails;

        public CustomInitializableObject(final boolean fails) {
            this.fails = fails;
        }

        @Override
        protected void internalInit(final boolean forceReinit) {
            this.counter++;
            if (fails) {
                throw new TechnicalException("Initialization fails");
            }
        }

        public int getCounter() {
            return this.counter;
        }
    }
}
