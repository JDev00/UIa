package uia.core.basement;

/**
 * Message is a wrapper for a message to delivery and the recipient
 */

public interface Message {

    enum Type {EVENT_SCREEN_TOUCH, EVENT_KEY, OTHER}

    /**
     * @return the message sender
     */

    String getSender();

    /**
     * @return the message recipient; it could be null
     */

    String getRecipient();

    /**
     * @return the message {@link Type}
     */

    Type getType();

    /**
     * @return the message cast to the specified type
     */

    <T> T getMessage();
}
