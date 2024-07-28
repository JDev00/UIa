import org.junit.jupiter.api.Test;

import uia.application.ui.component.utility.ComponentUtility;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.application.message.MessageFactory;
import uia.application.ui.component.Component;
import uia.core.basement.message.Message;
import uia.core.ui.View;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ComponentUtility unit tests.
 */

class TestComponentUtility {
    String VIEW_ID = "1";

    View createView() {
        return new Component(VIEW_ID, 0f, 0f, 0f, 0f);
    }

    @Test
    void messageShouldBeNotified() {
        // setup
        View view = createView();
        Message message = MessageFactory.create("hello", VIEW_ID);

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
