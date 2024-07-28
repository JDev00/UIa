package uia.application.ui.group;

import uia.application.message.MessageFactory;
import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.message.Message;
import uia.core.ui.ViewGroup;

import java.util.Objects;

/**
 * Collection of functions for ViewGroup components implementation.
 */

public final class GroupUtility {

    private GroupUtility() {
    }

    /**
     * Dispatches the specified message to the group children.
     *
     * @param group   the group whose children will receive the message
     * @param message the message to be dispatched
     * @throws NullPointerException if {@code group == null || message == null}
     */

    public static void dispatchMessageToChildren(ViewGroup group, Message message) {
        Objects.requireNonNull(message);
        Objects.requireNonNull(group);

        for (int i = group.size() - 1; i >= 0; i--) {
            group.get(i).readMessage(message);
        }
    }

    /**
     * Dispatches the specified key message to the group children.
     *
     * @param group      the group whose children will receive the message
     * @param keyMessage the message to be dispatched
     * @throws NullPointerException if {@code group == null || message == null}
     */

    public static void dispatchKeyMessageToChildren(ViewGroup group, Message keyMessage) {
        Objects.requireNonNull(keyMessage);
        Objects.requireNonNull(group);

        if (group.isVisible()) {
            for (int i = group.size() - 1; i >= 0; i--) {
                group.get(i).readMessage(keyMessage);
            }
        }
    }

    /**
     * Dispatches the screen touch message to the group children.
     *
     * @param group              the group whose children will receive the screenTouches
     * @param screenTouchMessage the screen touch message to be dispatched
     * @throws NullPointerException if {@code group == null || message == null}
     */

    public static void dispatchScreenTouchMessageToChildren(ViewGroup group, Message screenTouchMessage) {
        Objects.requireNonNull(screenTouchMessage);
        Objects.requireNonNull(group);

        ScreenTouch[] screenTouchesToDispatch = {};

        if (group.isVisible()) {
            ScreenTouch[] screenTouches = screenTouchMessage.getPayload();
            screenTouchesToDispatch = new ScreenTouch[screenTouches.length];

            for (int i = 0; i < screenTouches.length; i++) {
                ScreenTouch screenTouch = screenTouches[i];
                if (group.hasClip()
                        && !group.contains(screenTouch.getX(), screenTouch.getY())) {
                    // copies the screenTouch
                    screenTouch = ScreenTouch.copy(screenTouch, 0, 0);
                    // consumes the copied screenTouch
                    screenTouch.consume();
                }
                screenTouchesToDispatch[i] = screenTouch;
            }
        }

        // creates the message to be dispatched
        String recipient = screenTouchMessage.getRecipient();
        Message messageToSend = MessageFactory.create(screenTouchesToDispatch, recipient);
        // dispatches the message
        for (int i = group.size() - 1; i >= 0; i--) {
            group.get(i).readMessage(messageToSend);
        }
    }
}
