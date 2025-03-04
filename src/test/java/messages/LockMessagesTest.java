package messages;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uia.application.message.systemessages.ScreenTouchMessage;
import uia.application.message.messagingsystem.MessageLocker;
import uia.application.ui.component.ComponentHiddenRoot;
import uia.application.message.GenericMessage;
import uia.application.message.MessageFactory;
import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.message.Message;
import uia.core.ui.callbacks.OnClick;
import uia.core.ui.ViewGroup;
import uia.core.ui.View;

import java.util.ArrayList;
import java.util.List;

import static utility.TestUtility.*;
import static org.junit.jupiter.api.Assertions.*;

class LockMessagesTest {
    MessageLocker lockerService = MessageLocker.getInstance();

    ViewGroup rootView;
    View firstChild;
    View secondChild;

    static Message createBroadcastClickMessage() {
        return MessageFactory.create(
                new ScreenTouch(ScreenTouch.Action.CLICKED, ScreenTouch.Button.RIGHT, 200, 200, 0),
                null
        );
    }

    /**
     * Helper method. Sends the provided message.
     */

    void sendMessage(Message message) {
        // sends the message
        Message messageToSend = message;
        if (lockerService.isMessageToBeLocked(message)) {
            messageToSend = lockerService.createLock(message);
        }
        rootView.readMessage(messageToSend);
    }

    /**
     * Helper method. Emulates the update of the root view.
     */

    void updateGraphicalRoot() {
        View hiddenRoot = new ComponentHiddenRoot();
        hiddenRoot.getStyle()
                .setPosition(0, 0)
                .setDimension(1000, 1000);
        rootView.update(hiddenRoot);
    }

    @BeforeEach
    void beforeEach() {
        // setup graphical tree
        rootView = createSimpleTree();
        firstChild = rootView.get("view1");
        firstChild.setInputConsumer(View.InputConsumer.SCREEN_TOUCH, false);
        secondChild = rootView.get("view2");
        secondChild.setInputConsumer(View.InputConsumer.SCREEN_TOUCH, false);

        updateGraphicalRoot();
    }

    @AfterEach
    void afterEach() {
        lockerService.releaseLockOn(ScreenTouchMessage.class);
    }

    // happy paths

    @Test
    void onlyTheSourceThatLockedTheMessageTypeShouldReceiveTheseMessages() {
        String sourceLockRequester = secondChild.getID();

        // setup
        // registers callback to listen for incoming events
        List<View> clickReceivedBy = new ArrayList<>();
        rootView.registerCallback((OnClick) touches -> clickReceivedBy.add(rootView));
        firstChild.registerCallback((OnClick) touches -> clickReceivedBy.add(firstChild));
        secondChild.registerCallback((OnClick) touches -> clickReceivedBy.add(secondChild));

        // act
        // requests to lock the ScreenTouchMessage type
        boolean lockAcquired = lockerService.requestLockOn(sourceLockRequester, ScreenTouchMessage.class);
        Message message = createBroadcastClickMessage();
        sendMessage(message);

        // verify
        // the screen touch message should only be received by the second child
        assertTrue(lockAcquired);
        assertEquals(1, clickReceivedBy.size());
        assertEquals(secondChild, clickReceivedBy.get(0));
    }

    @Test
    void messagesOfTheSameTypeShouldBeLockedOnlyOnce() {
        String sourceLockRequester = "mySelf";

        // act
        boolean firstLockRequest = lockerService.requestLockOn(sourceLockRequester, ScreenTouchMessage.class);
        boolean secondLockRequest = lockerService.requestLockOn(sourceLockRequester, ScreenTouchMessage.class);

        // verify
        assertTrue(firstLockRequest);
        assertFalse(secondLockRequest);
    }

    @Test
    void aLockOnMessageShouldBeReleased() {
        String sourceLockRequester = "mySelf";

        // setup
        // locks the message type
        lockerService.requestLockOn(sourceLockRequester, GenericMessage.class);

        // act
        // unlocks the acquired lock
        boolean lockReleased = lockerService.releaseLockOn(GenericMessage.class);

        // verify
        assertTrue(lockReleased);
    }

    // sad paths

    @Test
    void anErrorShouldBeThrownWhenTryingToCreateALockedMessageOnANullMessage() {
        // act and verify
        assertThrows(IllegalArgumentException.class, () -> lockerService.createLock(null));
    }
}
