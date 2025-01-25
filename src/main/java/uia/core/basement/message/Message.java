package uia.core.basement.message;

import java.util.Arrays;
import java.util.Objects;

/**
 * Message ADT.
 * <br>
 * Message is responsible for maintaining the information needed to
 * deliver data to a recipient.
 */

public interface Message {

    /**
     * Checks whether the message is intended for at least one of the specified
     * recipients.
     *
     * @param recipients the recipients the message is intended for
     * @return true if the message is intended for at least one of the specified recipients
     * @throws NullPointerException if {@code message == null || recipients == null}
     */

    static boolean isMessageFor(Message message, String... recipients) {
        Objects.requireNonNull(message);
        Objects.requireNonNull(recipients);

        String messageRecipient = message.getRecipient();
        return Arrays.asList(recipients).contains(messageRecipient);
    }

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
