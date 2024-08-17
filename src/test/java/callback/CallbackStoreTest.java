package callback;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uia.application.events.CallbackStore;
import uia.core.basement.Callable;
import uia.core.basement.Callback;

import static org.junit.jupiter.api.Assertions.*;
import static utility.TestUtility.waitFor;

/**
 * Unit tests.
 */

class CallbackStoreTest {

    @FunctionalInterface
    interface MockCustomCallback extends Callback<Boolean> {
    }

    Callback<Integer> mockCallback = value -> {
    };

    Callable callable;

    @BeforeEach
    void beforeEach() {
        callable = new CallbackStore(10);
    }

    // happy paths
    @Test
    void callbackShouldBeRegisteredAndItsIDReturned() {
        // act
        long callbackID = callable.registerCallback(mockCallback);

        // verify
        long unexpectedCallbackID = -1;
        assertNotEquals(unexpectedCallbackID, callbackID);
        int expectedCallbackNumber = 1;
        assertEquals(expectedCallbackNumber, callable.numberOfCallbacks());
    }

    @Test
    void registeredCallbackShouldBeRemoved() {
        // setup
        // registers a callback first
        long callbackID = callable.registerCallback(mockCallback);

        // act
        callable.unregisterCallback(callbackID);

        // verify
        int expectedCallbackNumber = 0;
        assertEquals(expectedCallbackNumber, callable.numberOfCallbacks());
    }

    @Test
    void callbacksOfTheGivenTypeShouldBeNotifiedWithTheGivenValue() {
        // setup
        // creates a custom callback
        MockCustomCallback customCallback = value -> assertEquals(true, value);
        // creates a second callback. This should not be executed, so the
        // assertion will not be verified
        Callback<Boolean> standardCallback = value -> assertEquals(false, value);
        // registers the callback
        callable.registerCallback(standardCallback);
        callable.registerCallback(customCallback);

        // act
        callable.notifyCallbacks(MockCustomCallback.class, true);
        waitFor(250);
    }

    // sad paths
    @Test
    void duplicatedCallbackShouldBeDiscarded() {
        // setup
        // registers a callback
        callable.registerCallback(mockCallback);

        // act
        // tries to register the same callback again
        long duplicatedCallbackID = callable.registerCallback(mockCallback);

        // verify
        long expectedCallbackID = -1;
        assertEquals(expectedCallbackID, duplicatedCallbackID);
    }

    @Test
    void tryingToRegisterNullCallbackShouldThrowAnError() {
        // act and verify
        String expectedMessage = "'null' callbacks are forbidden";
        assertThrows(NullPointerException.class, () -> callable.registerCallback(null), expectedMessage);
    }

    @Test
    void tryingToRemoveAnUnregisteredCallbackShouldHaveNoEffect() {
        // setup
        // registers a callback
        callable.registerCallback(mockCallback);

        // act
        long unregisteredCallbackID = 0;
        callable.unregisterCallback(unregisteredCallbackID);

        // verify
        int expectedCallbackNumber = 1;
        assertEquals(expectedCallbackNumber, callable.numberOfCallbacks());
    }
}
