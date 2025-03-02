package uia.application.message.messagingsystem;

import uia.core.basement.message.Message;

import java.util.HashMap;
import java.util.Map;

/**
 * The MessageLocker is responsible for handling the message locking lifecycle.
 */

public class MessageLocker {
    private final Map<Class<? extends Message>, String> lockTable;

    private MessageLocker() {
        lockTable = new HashMap<>();
    }

    /**
     * Locks the provided message type to the specified source. All messages
     * of this type will then only be processed by the provided source.
     *
     * @param sourceID    the id of the source
     * @param messageType the type of the message to locked
     * @return true if the lock has been acquired; false if it was already acquired by someone else
     */

    public boolean requestLockOn(String sourceID, Class<? extends Message> messageType) {
        boolean isTypeFree = !lockTable.containsKey(messageType);
        if (isTypeFree) {
            lockTable.put(messageType, sourceID);
        }
        return isTypeFree;
    }

    /**
     * Unlocks the provided message type.
     *
     * @param messageType the type of message to be unlocked
     * @return true if the type is unlocked, false otherwise
     */

    public boolean unlock(Class<? extends Message> messageType) {
        return true;
    }

    /**
     * Creates a lock message.
     */

    public Message createLock(Message message) {
        return null;
    }
}
