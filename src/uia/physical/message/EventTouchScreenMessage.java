package uia.physical.message;

import uia.core.ScreenTouch;
import uia.core.basement.Message;

import java.util.List;

/**
 * EventTouchScreenMessage has the responsibility to wrap a List of {@link ScreenTouch}s as payload
 */

public class EventTouchScreenMessage implements Message {
    private final List<ScreenTouch> screenTouches;
    private String source;
    private final String recipient;

    public EventTouchScreenMessage(List<ScreenTouch> screenTouches, String source, String recipient) {
        this.screenTouches = screenTouches;
        this.source = source;
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "GenericMessage{" +
                "screenTouches=" + screenTouches +
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
    public List<ScreenTouch> getPayload() {
        return screenTouches;
    }
}
