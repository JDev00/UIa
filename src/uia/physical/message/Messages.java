package uia.physical.message;

import uia.core.ui.primitives.Key;
import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.Message;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Messages collects a set of utilities to easily create the desired type of message.
 */

public class Messages {

    /**
     * Creates a new screen touch event message.
     *
     * @param screenTouches the {@link ScreenTouch}s to be provided
     * @param recipient     the message recipient; it could be null
     * @throws NullPointerException if {@code screenTouches == null}
     */

    public static Message newScreenEventMessage(List<ScreenTouch> screenTouches, String recipient) {
        Objects.requireNonNull(screenTouches);
        return new EventTouchScreenMessage(screenTouches, null, recipient);
    }

    /**
     * Creates a new screen touch event message.
     *
     * @param screenTouch the {@link ScreenTouch} to be provided
     * @param recipient   the message recipient; it could be null
     * @throws NullPointerException if {@code screenTouch == null}
     */

    public static Message newScreenEventMessage(ScreenTouch screenTouch, String recipient) {
        Objects.requireNonNull(screenTouch);
        List<ScreenTouch> screenTouches = Collections.singletonList(screenTouch);
        return newScreenEventMessage(screenTouches, recipient);
    }

    /**
     * Creates a new key event message.
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
     * Creates a new generic message.
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

    /**
     * Creates a new generic message.
     *
     * @param message   a not null Object to delivery
     * @param recipient the message recipient; it could be null
     * @throws NullPointerException if {@code message == null}
     */

    public static Message newMessage(Object message, String recipient) {
        Objects.requireNonNull(message);
        return newMessage(message, null, recipient);
    }
}
