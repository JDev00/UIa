package uia.physical.message;

import uia.core.ScreenTouch;
import uia.core.basement.Message;

import java.util.List;

/**
 * EventTouchScreenMessage has the responsibility to wrap a List of {@link ScreenTouch}s as payload
 */

public final class EventTouchScreenMessage implements Message {
    private final List<ScreenTouch> screenTouches;
    private final String source;
    private final String recipient;

    public EventTouchScreenMessage(List<ScreenTouch> screenTouches, String source, String recipient) {
        this.screenTouches = screenTouches;
        this.source = source;
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "EventTouchScreenMessage{" +
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
     * Lock {@link EventTouchScreenMessage} implementation.
     * The lock class is used to delivery a message to a specified recipient without constraints.
     */

    public static class Lock implements Message {
        private final List<ScreenTouch> screenTouches;
        private final String recipient;

        public Lock(List<ScreenTouch> screenTouches, String recipient) {
            this.screenTouches = screenTouches;
            this.recipient = recipient;
        }

        @Override
        public String toString() {
            return "Lock{" +
                    "screenTouches=" + screenTouches +
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
        @SuppressWarnings("unchecked")
        public List<ScreenTouch> getPayload() {
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
