package uia.application.message.systemessages;

import uia.core.basement.message.Message;
import uia.core.ui.primitives.Key;

/**
 * EventKeyMessage is a system message responsible for carrying a single key.
 */

public final class EventKeyMessage implements Message {
    private final String recipient;
    private final String source;
    private final Key key;

    public EventKeyMessage(Key key, String source, String recipient) {
        this.recipient = recipient;
        this.source = source;
        this.key = key;
    }

    @Override
    public String toString() {
        return "EventKeyMessage{key=" + key +
                ", source='" + source + '\'' +
                ", recipient='" + recipient + "'}";
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
