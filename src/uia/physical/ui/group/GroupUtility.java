package uia.physical.ui.group;

import uia.core.ui.primitives.ScreenTouch;
import uia.physical.message.Messages;
import uia.core.basement.message.Message;
import uia.core.ui.ViewGroup;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

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
        Objects.requireNonNull(group);
        Objects.requireNonNull(message);

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
        Objects.requireNonNull(group);
        Objects.requireNonNull(keyMessage);

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
        Objects.requireNonNull(group);
        Objects.requireNonNull(screenTouchMessage);

        List<ScreenTouch> screenTouchesToDispatch = new ArrayList<>();

        if (group.isVisible()) {
            List<ScreenTouch> screenTouches = screenTouchMessage.getPayload();
            screenTouches.forEach(screenTouch -> {
                ScreenTouch currentTouch = screenTouch;

                if (group.hasClip() && !group.contains(screenTouch.getX(), screenTouch.getY())) {
                    currentTouch = ScreenTouch.copy(screenTouch, 0, 0);
                    currentTouch.consume();
                }
                screenTouchesToDispatch.add(currentTouch);
            });
        }

        Message result = Messages.newScreenEventMessage(
                screenTouchesToDispatch,
                screenTouchMessage.getRecipient()
        );
        for (int i = group.size() - 1; i >= 0; i--) {
            group.get(i).readMessage(result);
        }
    }
}
