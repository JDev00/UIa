package uia.physical.message;

import java.util.LinkedList;
import java.util.List;

/**
 * Centralized storage for messages.
 * <br>
 * Version 1.1
 */

public final class MessageStore {
    private static final MessageStore MESSAGE_STORE = new MessageStore();

    private final List<Object[]> list;

    private MessageStore() {
        list = new LinkedList<>();
    }

    /**
     * Remove all messages from this store
     */

    public void clear() {
        list.clear();
    }

    /**
     * Add a new message data to this store.
     * <br>
     * The message data must have the following format:
     * <ul>
     *     <li>message (Object);</li>
     *     <li>source ID (String);</li>
     *     <li>destination ID (String).</li>
     * </ul>
     */

    public void add(Object[] data) {
        list.add(data);
    }

    /**
     * Remove and return the first added message data
     *
     * @return the first added message
     */

    public Object[] pop() {
        try {
            return list.remove(0);
        } catch (Exception e) {
            return null;
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
