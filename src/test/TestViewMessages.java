package test;

import test.core.TestCase;
import test.core.TestSuite;
import test.core.TestUtils;
import uia.core.ui.ViewGroup;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.core.ui.context.Context;
import uia.physical.message.Messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static test.Sanity.*;

public class TestViewMessages implements TestSuite {

    public static TestCase allTheSendMessagesShouldBeReceived() {
        return (testAssertion) -> {
            int messages = 50_000;
            testAssertion.assertions(messages);

            String MESSAGE = "hello", TARGET = "B";

            // test setup
            ViewGroup root = createRoot();
            root.add(createView(TARGET, 0f, 0f, 0.1f, 0.1f));

            Context context = createMockContext();
            context.setView(root);

            // test clause
            root.get(TARGET).registerCallback((OnMessageReceived) message -> {
                testAssertion.expect(message.getMessage()).toBeEqual(MESSAGE);
            });

            for (int i = 0; i < messages; i++) {
                root.sendMessage(Messages.newMessage(MESSAGE, TARGET));
            }

            TestUtils.waitMillis(2_100);
        };
    }

    @Override
    public Iterator<TestCase> iterator() {
        return new ArrayList<>(Arrays.asList(
                allTheSendMessagesShouldBeReceived()
        )).iterator();
    }

    public static void main(String[] args) {
        TestUtils.runTestSuite(new TestViewMessages());
    }
}
