package uia.application.message.systemessages;

import uia.core.basement.message.Message;
import uia.core.ui.primitives.Key;

/**
 * KeyMessage is a system message responsible for carrying a single key.
 */

public final class KeyMessage implements Message {
    private final String recipient;
    private final String sender;
    private final Key key;

    public KeyMessage(String sender, String recipient, Key key) {
        this.recipient = recipient;
        this.sender = sender;
        this.key = key;
    }

    @Override
    public String toString() {
        return "EventKeyMessage{key=" + key +
                ", sender='" + sender + '\'' +
                ", recipient='" + recipient + "'}";
    }

    @Override
    public String getSender() {
        return sender;
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
