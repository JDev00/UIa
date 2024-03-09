package uia.physical.utility;

import uia.core.basement.Message;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnMessageReceived;

import java.util.Objects;

/**
 * Collection of utilities for View implementations.
 */
public class ComponentUtility {

    /**
     * Reads the message and invoke the appropriate callback.
     * <br>
     * The callback notification is invoked if:
     * <ul>
     *     <li>
     *         the message recipient is equal to the view ID;
     *     </li>
     *     <li>
     *         the message recipient is null and the sender is not the recipient.
     *     </li>
     * </ul>
     *
     * @param view    a not null {@link View}
     * @param message a not null {@link Message} to notify
     * @throws NullPointerException if {@code view == null || message == null}
     */

    public static void notifyMessageCallback(View view, Message message) {
        Objects.requireNonNull(view);
        Objects.requireNonNull(message);

        String id = view.getID();
        String sender = message.getSender();
        String recipient = message.getRecipient();
        if (Objects.equals(id, recipient) || (recipient == null && !id.equals(sender))) {
            view.notifyCallbacks(OnMessageReceived.class, message);
        }
    }
}
