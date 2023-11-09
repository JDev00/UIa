package uia.physical.message;

import uia.core.basement.Message;

/**
 * Message factory
 */

public class Messages {

    /**
     * Create a new screen touch event message
     */

    public static Message newScreenEventMessage(Object message, String recipient) {
        return new GenericMessage(Message.Type.EVENT_SCREEN_TOUCH, message, null, recipient);
    }

    /**
     * Create a new key event message
     */

    public static Message newKeyEventMessage(Object message, String recipient) {
        return new GenericMessage(Message.Type.EVENT_KEY, message, null, recipient);
    }

    /**
     * Create a new message
     */

    public static Message newMessage(Object message, String recipient) {
        return new GenericMessage(Message.Type.OTHER, message, null, recipient);
    }

    /**
     * Create a new message
     */

    public static Message newMessage(Object message, String source, String recipient) {
        return new GenericMessage(Message.Type.OTHER, message, source, recipient);
    }
}
