package test.__tests__.messageStore;

import test.core.BeforeEachTest;
import test.core.Test;
import test.core.TestAssertion;
import test.core.TestExecutor;
import uia.core.message.Message;
import uia.physical.message.Messages;
import uia.physical.message.store.ConcreteMessageStore;
import uia.core.message.MessageStore;

import java.util.Arrays;
import java.util.List;

/**
 * Unit tests.
 */

public class testMessageStore {

    public static Message createMockMessage() {
        return Messages.newMessage("", "");
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

        int REQUESTED_MESSAGES = 3;
        List<Message> result = messageStore.pop(REQUESTED_MESSAGES);
        assertion.expect(result.size()).toBe(REQUESTED_MESSAGES);
        for (int i = 0; i < result.size(); i++) {
            assertion.expect(result.get(i)).toBe(messagesToAdd[i]);
        }
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new testMessageStore());
    }
}
