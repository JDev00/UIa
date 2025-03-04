package uia.application.message.messagingsystem;

import uia.core.basement.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * The MessageLocker is responsible for handling the message locking lifecycle.
 */

public final class MessageLocker {
    private static final MessageLocker MESSAGE_LOCKER = new MessageLocker();

    private final Map<Class<? extends Message>, String> lockTable;

    private MessageLocker() {
        lockTable = new HashMap<>();
    }

    /**
     * Locks the provided message type to the specified source. All messages
     * of this type will then only be processed by the provided source.
     *
     * @param sourceID    the ID of the source that requested the lock on the message type
     * @param messageType the type of the message to be locked
     * @return true if the lock has been acquired; false if it has been already acquired by someone else
     */

    public boolean requestLockOn(String sourceID, Class<? extends Message> messageType) {
        boolean isTypeFree = !lockTable.containsKey(messageType);
        if (isTypeFree) {
            lockTable.put(messageType, sourceID);
        }
        return isTypeFree;
    }

    /**
     * Releases the lock on the provided message type.
     *
     * @param messageType the type of message to be unlocked
     * @return true if the type is unlocked, false otherwise
     */

    public boolean releaseLockOn(Class<? extends Message> messageType) {
        return lockTable.remove(messageType) != null;
    }

    /**
     * @return true if the provided message is to be locked; false otherwise
     */

    public boolean isMessageToBeLocked(Message message) {
        return lockTable.containsKey(message.getClass());
    }

    /**
     * Creates a locked message.
     *
     * @param message the message to be locked
     * @return a new locked message
     */

    public LockedMessage createLock(Message message) {
        if (message == null) {
            throw new IllegalArgumentException("a null message can't be locked");
        }

        Class<? extends Message> messageType = message.getClass();
        String recipient = lockTable.get(messageType);
        return new LockedMessage(recipient, message);
    }

    /**
     * @return the unique instance of the {@link MessageLocker}
     */

    public static MessageLocker getInstance() {
        return MESSAGE_LOCKER;
    }
}
