package uia.core.basement;

/**
 * Message ADT.
 * <br>
 * Message has the responsibility to keep the required information to
 * delivery a message to a recipient.
 */

public interface Message {

    /**
     * @return the message sender
     */

    String getSender();

    /**
     * @return the message recipient; it could be null
     */

    String getRecipient();

    /**
     * @return the message content
     */

    <T> T getPayload();
}
