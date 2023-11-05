package uia.physical.message;

import uia.core.basement.Message;

public class Messages {

    /**
     * Create a screen event message
     */

    public static Message newScreenEventMessage(Object message, String recipient) {
        return new GenericMessage(Message.Type.EVENT_SCREEN_TOUCH, message, null, recipient);
    }

    /**
     * Create a key event message
     */

    public static Message newKeyEventMessage(Object message, String recipient) {
        return new GenericMessage(Message.Type.EVENT_KEY, message, null, recipient);
    }

    /**
     * Create a message
     */

    public static Message createMessage(Object message, String recipient) {
        return new GenericMessage(Message.Type.OTHER, message, null, recipient);
    }
}
