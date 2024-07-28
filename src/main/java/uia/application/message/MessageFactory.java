package uia.application.message;

import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.message.Message;
import uia.core.ui.primitives.Key;

import java.util.Objects;

/**
 * MessageFactory is responsible for creating messages of the desired type.
 */

public final class MessageFactory {

    private MessageFactory() {
    }

    /**
     * Creates a new Message.
     *
     * @param payload   the payload of the message
     * @param source    the sender of the message; it could be null
     * @param recipient the recipient of the message; it could be null
     * @return the created Message
     * @throws NullPointerException if {@code payload == null}
     */

    public static Message create(Object payload, String source, String recipient) {
        Objects.requireNonNull(payload);

        Message result;
        if (payload instanceof ScreenTouch[]) {
            result = new EventTouchScreenMessage(source, recipient, (ScreenTouch[]) payload);
        } else if (payload instanceof ScreenTouch) {
            result = new EventTouchScreenMessage(source, recipient, (ScreenTouch) payload);
        } else if (payload instanceof Key) {
            result = new EventKeyMessage((Key) payload, source, recipient);
        } else {
            result = new GenericMessage(payload, source, recipient);
        }
        return result;
    }

    /**
     * Creates a new Message with a null sender.
     *
     * @param payload   the payload of the message
     * @param recipient the recipient of the message; it could be null
     * @return the created Message
     * @throws NullPointerException if {@code payload == null}
     */

    public static Message create(Object payload, String recipient) {
        return create(payload, null, recipient);
    }
}
