package uia.core.message;

/**
 * Message ADT.
 * <br>
 * Message is responsible for maintaining the information needed to deliver data to a recipient.
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
