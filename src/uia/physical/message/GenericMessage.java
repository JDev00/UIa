package uia.physical.message;

import uia.core.basement.Message;

public class GenericMessage implements Message {
    private final Type type;
    private final Object message;
    private String source;
    private final String recipient;

    public GenericMessage(Type type, Object message, String source, String recipient) {
        this.type = type;
        this.message = message;
        this.source = source;
        this.recipient = recipient;
    }

    @Override
    public String getSender() {
        return source;
    }

    @Override
    public String getRecipient() {
        return recipient;
    }

    @Override
    public Type getType() {
        return type;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getMessage() {
        return (T) message;
    }
}
