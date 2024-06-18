package uia.physical.message.store;

import uia.core.basement.message.Message;
import uia.core.basement.message.MessageStore;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Concrete, not thread-safe, implementation of {@link MessageStore}.
 */

public class ConcreteMessageStore implements MessageStore {
    private final LinkedList<Message> messages;

    public ConcreteMessageStore() {
        messages = new LinkedList<>();
    }

    @Override
    public void add(Message message) {
        Objects.requireNonNull(message);
        messages.add(message);
    }

    @Override
    public List<Message> pop(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("size must be greater than 0");
        }

        int length = Math.min(messages.size(), size);
        List<Message> result = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            result.add(messages.poll());
        }
        return result;
    }

    @Override
    public int size() {
        return messages.size();
    }
}
