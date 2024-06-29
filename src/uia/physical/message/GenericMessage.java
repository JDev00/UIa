package uia.physical.message;

import uia.core.basement.message.Message;

/**
 * UIa generic {@link Message} implementation.
 */

public final class GenericMessage implements Message {
    private final String recipient;
    private final Object message;
    private final String source;

    public GenericMessage(Object message, String source, String recipient) {
        this.recipient = recipient;
        this.message = message;
        this.source = source;
    }

    @Override
    public String toString() {
        return "GenericMessage{" +
                "message=" + message +
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
    public <T> T getPayload() {
        return (T) message;
    }
}
