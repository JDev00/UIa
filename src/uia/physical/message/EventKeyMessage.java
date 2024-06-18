package uia.physical.message;

import uia.core.ui.primitives.Key;
import uia.core.basement.message.Message;

/**
 * EventKeyMessage is a UIa framework message that carries a {@link Key} as payload.
 */

public class EventKeyMessage implements Message {
    private final Key key;
    private final String source;
    private final String recipient;

    public EventKeyMessage(Key key, String source, String recipient) {
        this.key = key;
        this.source = source;
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "EventKeyMessage{" +
                "key=" + key +
                ", source='" + source + '\'' +
                ", recipient='" + recipient + '\'' +
                '}';
    }

    @Override
    public String getSender() {
        return source;
    }

    @Override
    public String getRecipient() {
        return recipient;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Key getPayload() {
        return key;
    }
}
