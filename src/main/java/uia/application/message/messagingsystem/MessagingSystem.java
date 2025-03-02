package uia.application.message.messagingsystem;

import uia.application.message.screentouch.ScreenTouchMessage;
import uia.application.message.store.GlobalMessageStore;
import uia.core.basement.message.MessageStore;
import uia.core.basement.message.Message;
import uia.core.ui.View;

import java.util.Objects;
import java.util.List;

/**
 * MessagingSystem is responsible for managing and dispatching messages.
 */

public class MessagingSystem {
    public static final int MAX_MESSAGES_TO_PROCESS = 25_000;

    private final MessageStore globalMessageStore = GlobalMessageStore.getInstance();
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
     * Sends the dequeued messages to the given View.
     *
     * @param view a {@link View} to send the dequeued messages
     * @throws NullPointerException if {@code view == null}
     */

    public void sendMessagesTo(View view) {
        Objects.requireNonNull(view);

        List<Message> messages = globalMessageStore.pop(maxMessagesToProcess);
        for (Message message : messages) {
            // 1. message lock
            if (message instanceof ScreenTouchMessage.RequestLock && lockedScreenTouchRecipient == null) {
                lockedScreenTouchRecipient = message.getSender();
            } else if (message instanceof ScreenTouchMessage.Unlock) {
                lockedScreenTouchRecipient = null;
            } else if (lockedScreenTouchRecipient != null && message instanceof ScreenTouchMessage) {
                message = new ScreenTouchMessage.Lock(lockedScreenTouchRecipient, message.getPayload());
            }

            // 2. dispatches the message to the view
            view.readMessage(message);
        }
    }
}
