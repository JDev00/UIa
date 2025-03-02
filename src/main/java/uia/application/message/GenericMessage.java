package uia.application.message;

import uia.core.basement.message.Message;

/**
 * General purpose {@link Message} implementation.
 */

public final class GenericMessage implements Message {
    private final String recipient;
    private final Object payload;
    private final String sender;

    public GenericMessage(String sender, String recipient, Object payload) {
        this.recipient = recipient;
        this.payload = payload;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "GenericMessage{sender='" + sender + "' , recipient='" + recipient + "', payload=" + payload + '}';
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
    public <T> T getPayload() {
        return (T) payload;
    }
}
