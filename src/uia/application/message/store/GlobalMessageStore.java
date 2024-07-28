package uia.application.message.store;

import uia.core.basement.message.MessageStore;
import uia.core.basement.message.Message;

import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

/**
 * GlobalMessageStore provides a unique, thread-safe, ubiquitous place where to store the application's messages.
 * <br><br>
 * <b>Mount and Unmount operations</b>
 * <br>
 * It is possible to mount and unmount specific {@link MessageStore} implementations at runtime.
 * <br>
 * Formally, GlobalMessageStore is a wrapper whose responsibility is implemented by a concrete instance of
 * {@link MessageStore}.
 */

public final class GlobalMessageStore implements MessageStore {
    private static final GlobalMessageStore GLOBAL_MESSAGE_STORE = new GlobalMessageStore();

    private MessageStore messageStore;

    private GlobalMessageStore() {
        messageStore = new ConcreteMessageStore();
    }

    /**
     * Mounts the given MessageStore.
     *
     * @param messageStore a MessageStore to be mounted
     * @throws NullPointerException if {@code messageStore == null}
     */

    public synchronized void mount(MessageStore messageStore) {
        Objects.requireNonNull(messageStore);
        this.messageStore = messageStore;
    }

    /**
     * Unmounts the current MessageStore.
     * <br>
     * Once the current store has been unmounted, all the MessageStore operations
     * will fail until a new store is mounted.
     *
     * @return the unmounted messageStore
     */

    public synchronized MessageStore unmount() {
        MessageStore result = messageStore;
        messageStore = null;
        return result;
    }

    @Override
    public synchronized void add(Message message) {
        if (messageStore != null) {
            messageStore.add(message);
        }
    }

    @Override
    public synchronized List<Message> pop(int size) {
        List<Message> result = new ArrayList<>(0);
        if (messageStore != null) {
            result = messageStore.pop(size);
        }
        return result;
    }

    @Override
    public synchronized int size() {
        int result = 0;
        if (messageStore != null) {
            result = messageStore.size();
        }
        return result;
    }

    /**
     * @return the unique GlobalMessageStore instance
     */

    public static GlobalMessageStore getInstance() {
        return GLOBAL_MESSAGE_STORE;
    }
}
