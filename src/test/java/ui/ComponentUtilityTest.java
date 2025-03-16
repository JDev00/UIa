package ui;

import org.junit.jupiter.api.Test;

import uia.application.ui.component.utility.ComponentUtility;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.application.message.MessageFactory;
import uia.application.ui.component.Component;
import uia.core.basement.message.Message;
import uia.core.ui.View;

import static org.junit.jupiter.api.Assertions.*;

class ComponentUtilityTest {
    private String viewID = "1";

    /**
     * Helper method. Creates a new View.
     */

    private View createView() {
        return new Component(viewID, 0f, 0f, 0f, 0f);
    }

    @Test
    void messageShouldBeNotified() {
        // setup
        View view = createView();
        Message message = MessageFactory.create("hello", viewID);

        // verify
        view.registerCallback((OnMessageReceived) receivedMessage -> assertEquals(message, receivedMessage));

        // act
        ComponentUtility.notifyMessageListeners(view, message);
    }

    @Test
    void messageWithNullRecipientShouldBeNotified() {
        // setup
        View view = createView();
        Message message = MessageFactory.create("hello", null);

        // verify
        view.registerCallback((OnMessageReceived) receivedMessage -> assertEquals(message, receivedMessage));

        // act
        ComponentUtility.notifyMessageListeners(view, message);
    }

    @Test
    void tryingToNotifyClientsWithANullMessageShouldThrowAnException() {
        View view = createView();

        // verify
        assertThrows(NullPointerException.class, () -> ComponentUtility.notifyMessageListeners(view, null));
    }

    @Test
    void nullViewShouldThrowNullPointerException() {
        Message message = MessageFactory.create("", "");

        // verify
        assertThrows(NullPointerException.class, () -> ComponentUtility.notifyMessageListeners(null, message));
    }
}
