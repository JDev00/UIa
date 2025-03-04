package uia.application.message.messagingsystem;

import uia.core.basement.message.Message;

/**
 * The LockedMessage represents an acquired message to be sent to
 * a specific recipient.
 */

public class LockedMessage implements Message {
    private final Message message;
    private final String recipient;

    public LockedMessage(String recipient, Message message) {
        this.recipient = recipient;
        this.message = message;
    }

    @Override
    public String getSender() {
        return this.message.getSender();
    }

    @Override
    public String getRecipient() {
        return this.recipient;
    }

    @Override
    public Message getPayload() {
        return message;
    }
}
