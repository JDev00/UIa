package uia.application.message.screentouch;

import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.message.Message;

import java.util.Arrays;

/**
 * The EventScreenTouchMessage is a system message responsible for carrying
 * screen touches.
 */

public final class ScreenTouchMessage implements Message {
    private final ScreenTouch[] screenTouches;
    private final String recipient;
    private final String sender;

    public ScreenTouchMessage(String sender, String recipient, ScreenTouch... screenTouches) {
        this.screenTouches = screenTouches;
        this.recipient = recipient;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return "EventTouchScreenMessage{screenTouches=" + Arrays.toString(screenTouches) +
                ", source='" + sender + '\'' +
                ", recipient='" + recipient + "'}";
    }

    @Override
    public String getSender() {
        return sender;
    }

    @Override
    public String getRecipient() {
        return recipient;
    }

    @Override
    public ScreenTouch[] getPayload() {
        return screenTouches;
    }
}
