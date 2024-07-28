package messageStore;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uia.application.message.store.ConcreteMessageStore;
import uia.application.message.MessageFactory;
import uia.core.basement.message.MessageStore;
import uia.core.basement.message.Message;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * MessageStore unit tests.
 */

class MessageStoreTest {

    public static Message createMockMessage() {
        return MessageFactory.create("", "");
    }

    public MessageStore messageStore;

    @BeforeEach
    public void beforeEach() {
        messageStore = new ConcreteMessageStore();
    }

    @Test
    void messagesShouldBeStored() {
        // setup
        int numberOfMessages = 2;
        for (int i = 0; i < numberOfMessages; i++) {
            messageStore.add(createMockMessage());
        }

        // verify
        assertEquals(numberOfMessages, messageStore.size());
    }

    @Test
    void messagesShouldBeRemovedAndReturned() {
        // setup
        Message[] messagesToAdd = {
                createMockMessage(),
                createMockMessage(),
                createMockMessage(),
                createMockMessage(),
                createMockMessage()
        };
        Arrays.stream(messagesToAdd).forEach(messageStore::add);

        // act
        int numberOfMessageToRemove = 3;
        List<Message> result = messageStore.pop(numberOfMessageToRemove);

        // verify
        assertEquals(numberOfMessageToRemove, result.size());
        for (int i = 0; i < result.size(); i++) {
            assertEquals(messagesToAdd[i], result.get(i));
        }
    }
}
