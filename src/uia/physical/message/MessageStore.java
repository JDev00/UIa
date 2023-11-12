package uia.physical.message;

import uia.core.basement.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Centralized storage for messages.
 * <br>
 * Version 1.3
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
     * @return the first added message or null if this store is empty
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
     * Remove and return the specified amount of messages
     *
     * @param size a value > 0. If the specified value is greater than this store, it returns all the available messages
     * @return a new List of Messages
     * @throws IllegalArgumentException if {@code size <= 0}
     */

    public List<Message> pop(int size) {
        synchronized (list) {
            if (size <= 0) {
                throw new IllegalArgumentException("size must be greater than 0");
            }
            List<Message> out = new LinkedList<>();
            int i = 0;
            int length = Math.min(list.size(), size);
            while (i < length) {
                out.add(list.poll());
                i++;
            }
            return out;
        }
    }

    /**
     * @return the number of stored messages
     */

    public int size() {
        synchronized (list) {
            return list.size();
        }
    }

    /**
     * @return the Storage instance
     */

    public static MessageStore getInstance() {
        return MESSAGE_STORE;
    }
}
