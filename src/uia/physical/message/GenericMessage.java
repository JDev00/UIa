package uia.physical.message;

import uia.core.basement.Message;

/**
 * GenericMessage has the responsibility to wrap a generic payload
 */

public class GenericMessage implements Message {
    private final Object message;
    private String source;
    private final String recipient;

    public GenericMessage(Object message, String source, String recipient) {
        this.message = message;
        this.source = source;
        this.recipient = recipient;
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
