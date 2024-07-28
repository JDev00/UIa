import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import uia.core.ui.callbacks.OnMessageReceived;
import uia.application.message.MessageFactory;
import uia.core.basement.message.Message;
import uia.core.context.Context;
import uia.core.ui.ViewGroup;
import uia.core.ui.View;

import static utility.TestUtility.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MessagingSystem unit tests.
 */

class TestMessagingSystem {
    Context context;
    ViewGroup rootView;

    @BeforeEach
    void beforeEach() {
        rootView = createRoot();

        context = createMockContext();
        context.setView(rootView);
    }

    @AfterEach
    void afterEach() {
        // pauses this context and hides its window
        context.setLifecycleStage(Context.LifecycleStage.PAUSED);
        context.getWindow().setVisible(false);
    }

    @Test
    void allMessagesShouldBeReceivedByTheTargetView() {
        String MESSAGE_PAYLOAD = "hello";
        String TARGET_VIEW_ID = "B";
        int numberOfMessages = 50_000;
        // counts the number of passed assertions
        int[] assertionsPassed = {0};

        // setup
        View targetView = createView(TARGET_VIEW_ID, 0f, 0f, 0.1f, 0.1f);
        targetView.registerCallback((OnMessageReceived) receivedMessage -> {
            String messagePayload = receivedMessage.getPayload();
            // tests the message equivalence
            if (MESSAGE_PAYLOAD.equals(messagePayload)) {
                assertionsPassed[0]++;
            }
        });
        ViewGroup.insert(rootView, targetView);

        // act
        for (int i = 0; i < numberOfMessages; i++) {
            Message message = MessageFactory.create(MESSAGE_PAYLOAD, TARGET_VIEW_ID);
            rootView.sendMessage(message);
        }

        // verify
        waitFor(2_000);
        assertEquals(numberOfMessages, assertionsPassed[0]);
    }
}
