package uia.core.basement.message;

import java.util.List;

/**
 * MessageStore ADT.
 * <br>
 * MessageStore is responsible for storing messages.
 */

public interface MessageStore {

    /**
     * Adds a new message to this store.
     *
     * @param message a {@link Message}
     * @throws NullPointerException if {message == null}
     */

    void add(Message message);

    /**
     * Removes and returns the specified amount of messages.
     *
     * @param size a value > 0. If the given value is greater than the number of messages in the store,
     *             it returns all the available messages
     * @return a new list of Messages
     * @throws IllegalArgumentException if {@code size <= 0}
     */

    List<Message> pop(int size);

    /**
     * @return the number of messages in this store
     */

    int size();
}
