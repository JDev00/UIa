package uia.physical.message;

import uia.core.basement.Message;
import uia.core.ui.View;

import java.util.List;
import java.util.Objects;

/**
 * MessagingSystem has the responsibility to manage and dispatch the available messages.
 */

public class MessagingSystem {
    public static final int MAX_MESSAGES_TO_PROCESS = 20000;
    private final MessageStore messageStore = MessageStore.getInstance();
    private String lockedScreenTouchRecipient = null;

    private int maxMessagesToProcess = MAX_MESSAGES_TO_PROCESS;

    /**
     * Sets the maximum number of messages that can be processed
     *
     * @param maxMessagesToProcess the maximum messages (> 0) that can be processed
     * @throws IllegalArgumentException if {@code maxMessagesPerSecond <= 0}
     */

    public void setMaxMessagesToProcess(int maxMessagesToProcess) {
        if (maxMessagesToProcess <= 0) {
            throw new IllegalArgumentException("maxMessagesPerSecond must be greater than 0");
        }
        this.maxMessagesToProcess = maxMessagesToProcess;
    }

    /**
     * Sends the dequeued messages to the specified View
     *
     * @param view a not null {@link View} to send the dequeued messages
     * @throws NullPointerException if {@code view == null}
     */

    public void sendMessagesTo(View view) {
        Objects.requireNonNull(view);

        List<Message> messages = messageStore.pop(maxMessagesToProcess);
        for (Message message : messages) {
            // try to lock messages
            if (message instanceof EventTouchScreenMessage.RequestLock && lockedScreenTouchRecipient == null) {
                lockedScreenTouchRecipient = message.getSender();
            } else if (message instanceof EventTouchScreenMessage.Unlock) {
                lockedScreenTouchRecipient = null;
            } else if (lockedScreenTouchRecipient != null && message instanceof EventTouchScreenMessage) {
                message = new EventTouchScreenMessage.Lock(message.getPayload(), lockedScreenTouchRecipient);
            }
            view.dispatchMessage(message);
        }
    }
}
