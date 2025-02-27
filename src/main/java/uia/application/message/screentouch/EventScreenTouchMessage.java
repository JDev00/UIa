package uia.application.message.screentouch;

import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.message.Message;

import java.util.Arrays;

/**
 * The EventScreenTouchMessage is a native message responsible for carrying
 * a list of {@link ScreenTouch} as payload.
 */

public final class EventScreenTouchMessage implements Message {
    private final ScreenTouch[] screenTouches;
    private final String source;
    private final String recipient;

    public EventScreenTouchMessage(String source, String recipient, ScreenTouch... screenTouches) {
        this.screenTouches = screenTouches;
        this.recipient = recipient;
        this.source = source;
    }

    @Override
    public String toString() {
        return "EventTouchScreenMessage{" +
                "screenTouches=" + Arrays.toString(screenTouches) +
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

    @Override
    public ScreenTouch[] getPayload() {
        return screenTouches;
    }

    /**
     * Message used to request the lock on all the screen touch messages
     */

    public static class RequestLock implements Message {
        private final String sender;

        public RequestLock(String sender) {
            this.sender = sender;
        }

        @Override
        public String toString() {
            return "RequestLock{" +
                    "sender='" + sender + '\'' +
                    '}';
        }

        @Override
        public String getSender() {
            return sender;
        }

        @Override
        public String getRecipient() {
            return "ROOT";
        }

        @Override
        public <T> T getPayload() {
            return null;
        }
    }

    /**
     * Message used to release the lock on all the screen touch messages
     */

    public static class Unlock implements Message {
        private final String sender;

        public Unlock(String sender) {
            this.sender = sender;
        }

        @Override
        public String toString() {
            return "Unlock{" +
                    "sender='" + sender + '\'' +
                    '}';
        }

        @Override
        public String getSender() {
            return sender;
        }

        @Override
        public String getRecipient() {
            return "ROOT";
        }

        @Override
        public <T> T getPayload() {
            return null;
        }
    }

    /**
     * Lock {@link EventScreenTouchMessage} implementation.
     * The lock class is used to delivery a message to a specified recipient without constraints.
     */

    public static class Lock implements Message {
        private final ScreenTouch[] screenTouches;
        private final String recipient;

        public Lock(String recipient, ScreenTouch... screenTouches) {
            this.screenTouches = screenTouches;
            this.recipient = recipient;
        }

        @Override
        public String toString() {
            return "Lock{" +
                    "screenTouches=" + Arrays.toString(screenTouches) +
                    ", recipient='" + recipient + '\'' +
                    '}';
        }

        @Override
        public String getSender() {
            return null;
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

    /**
     * Create a new Message to send to the root to request the lock on all the screen touch messages.
     * This operation, if accepted, locks the messages to the specified sender.
     *
     * @param sender the message sender (the one who would receive all the screen touch messages)
     * @return a new {@link Message} to request the lock on screen touch generated messages
     */

    public static Message requestLock(String sender) {
        return new RequestLock(sender);
    }

    /**
     * Create a new Message to send to the root to release the lock on screen touch messages
     *
     * @param sender the message sender (the one who will release the lock)
     * @return a new {@link Message} to release the lock on screen touch generated messages
     */

    public static Message releaseLock(String sender) {
        return new Unlock(sender);
    }
}
