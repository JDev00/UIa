package messages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uia.application.message.store.GlobalMessageStore;
import uia.core.basement.message.MessageStore;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.application.message.MessageFactory;
import uia.core.basement.message.Message;
import uia.core.ui.ViewGroup;
import uia.core.ui.View;

import java.util.List;

import static utility.TestUtility.*;

import static org.junit.jupiter.api.Assertions.*;

class MessagingSystemTest {
    ViewGroup rootView;

    /**
     * Emulates the messaging system.
     */

    void dispatchMessages() {
        MessageStore store = GlobalMessageStore.getInstance();
        List<Message> messages = store.pop(1_000);
        messages.forEach(rootView::readMessage);
    }

    @BeforeEach
    void beforeEach() {
        rootView = createSimpleTree();
    }

    @Disabled("")
    @Test
    void allMessagesShouldBeReceivedByTheTargetView() {
        String messagePayload = "hello";
        String targetViewID = "view2";
        int numberOfMessages = 1_000;
        // counts the number of passed assertions
        int[] assertionsPassed = {0};

        // setup
        View firstChild = rootView.get(targetViewID);
        firstChild.registerCallback((OnMessageReceived) receivedMessage -> {
            String receivedPayload = receivedMessage.getPayload();
            if (messagePayload.equals(receivedPayload)) {
                assertionsPassed[0]++;
            }
        });

        // act
        for (int i = 0; i < numberOfMessages; i++) {
            Message message = MessageFactory.create(messagePayload, targetViewID);
            rootView.sendMessage(message);
        }
        dispatchMessages();

        // verify
        waitUntil(() -> numberOfMessages == assertionsPassed[0], 50, 1_000);
        assertEquals(numberOfMessages, assertionsPassed[0]);
    }
}
