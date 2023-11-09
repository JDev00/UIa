package uia.physical.message;

import uia.core.basement.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Centralized storage for messages.
 * <br>
 * Version 1.2
 */

public final class MessageStore {
    private static final MessageStore MESSAGE_STORE = new MessageStore();

    private final LinkedList<Message> list;

    private MessageStore() {
        list = new LinkedList<>();
    }

    /**
     * Remove all messages from this store
     */

    public void clear() {
        synchronized (list) {
            list.clear();
        }
    }

    /**
     * Add a new message data to this store
     *
     * @param message a not null {@link Message}
     * @throws NullPointerException if {message == null}
     */

    public void add(Message message) {
        synchronized (list) {
            Objects.requireNonNull(message);
            list.add(message);
        }
    }

    /**
     * Remove and return the first added message
     *
     * @return the first added message or null
     */

    public Message pop() {
        synchronized (list) {
            try {
                return list.poll();
            } catch (Exception e) {
                return null;
            }
        }
    }

    /**
     * @return the number of stored messages
     */

    public int size() {
        return list.size();
    }

    /**
     * @return the Storage instance
     */

    public static MessageStore getInstance() {
        return MESSAGE_STORE;
    }
}
