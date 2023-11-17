package uia.physical.message;

import uia.core.Key;
import uia.core.ScreenTouch;
import uia.core.basement.Message;

import java.util.List;
import java.util.Objects;

/**
 * Message factory
 */

public class Messages {

    /**
     * Create a new screen touch event message
     *
     * @param screenTouches a not null List of {@link ScreenTouch}s
     * @param recipient     the message recipient; it could be null
     * @throws NullPointerException if {@code screenTouches == null}
     */

    public static Message newScreenEventMessage(List<ScreenTouch> screenTouches, String recipient) {
        Objects.requireNonNull(screenTouches);
        return new EventTouchScreenMessage(screenTouches, null, recipient);
    }

    /**
     * Create a new key event message
     *
     * @param key       a not null {@link Key}
     * @param recipient the message recipient; it could be null
     * @throws NullPointerException if {@code key == null}
     */

    public static Message newKeyEventMessage(Key key, String recipient) {
        Objects.requireNonNull(key);
        return new EventKeyMessage(key, null, recipient);
    }

    /**
     * Create a new generic message
     *
     * @param message   a not null Object to delivery
     * @param recipient the message recipient; it could be null
     * @throws NullPointerException if {@code message == null}
     */

    public static Message newMessage(Object message, String recipient) {
        Objects.requireNonNull(message);
        return new GenericMessage(message, null, recipient);
    }

    /**
     * Create a new generic message
     *
     * @param message   a not null Object to delivery
     * @param source    the message source; it could be null
     * @param recipient the message recipient; it could be null
     * @throws NullPointerException if {@code message == null}
     */

    public static Message newMessage(Object message, String source, String recipient) {
        Objects.requireNonNull(message);
        return new GenericMessage(message, source, recipient);
    }
}
