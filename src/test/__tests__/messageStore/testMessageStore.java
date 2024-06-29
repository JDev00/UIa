package test.__tests__.messageStore;

import test.core.BeforeEachTest;
import test.core.Test;
import test.core.TestAssertion;
import test.core.TestExecutor;
import uia.core.basement.message.Message;
import uia.physical.message.MessageFactory;
import uia.physical.message.store.ConcreteMessageStore;
import uia.core.basement.message.MessageStore;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests.
 */

public class testMessageStore {

    public static Message createMockMessage() {
        return MessageFactory.create("", "");
    }

    public MessageStore messageStore;

    @BeforeEachTest
    public void beforeEach() {
        messageStore = new ConcreteMessageStore();
    }

    @Test
    public void messagesShouldBeStored(TestAssertion assertion) {
        assertion.expect(1);

        // setup
        int MESSAGES = 2;
        for (int i = 0; i < MESSAGES; i++) {
            messageStore.add(createMockMessage());
        }

        // verify
        assertion.expect(messageStore.size()).toBe(MESSAGES);
    }

    @Test
    public void messagesShouldBeRemovedAndReturned(TestAssertion assertion) {
        assertion.assertions(4);

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
        int NUMBER_OF_MESSAGE_TO_REMOVE = 3;
        List<Message> result = messageStore.pop(NUMBER_OF_MESSAGE_TO_REMOVE);

        // verify
        assertion.expect(result.size()).toBe(NUMBER_OF_MESSAGE_TO_REMOVE);
        for (int i = 0; i < result.size(); i++) {
            assertion.expect(result.get(i)).toBe(messagesToAdd[i]);
        }
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new testMessageStore());
    }
}
