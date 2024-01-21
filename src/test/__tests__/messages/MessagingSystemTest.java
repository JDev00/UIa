package test.__tests__.messages;

import test.core.*;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.core.ui.context.Context;
import uia.physical.message.Messages;

import static test.__tests__.Sanity.*;

/**
 * Unit tests
 */

public class MessagingSystemTest {
    Context context;
    ViewGroup rootView;

    @BeforeEachTest
    public void beforeEach() {
        rootView = createRoot();

        context = createMockContext();
        context.setView(rootView);
    }

    @Test
    public void allMessagesShouldBeReceivedByTheTargetView(TestAssertion testAssertion) {
        int messages = 50_000;
        testAssertion.assertions(messages);

        String MESSAGE = "hello";
        String TARGET = "B";

        // setup
        ViewGroup.insert(rootView, createView(TARGET, 0f, 0f, 0.1f, 0.1f));

        // test clause
        View targetView = rootView.get(TARGET);
        targetView.registerCallback((OnMessageReceived) message -> {
            testAssertion.expect(message.getPayload()).toBe(MESSAGE);
        });

        for (int i = 0; i < messages; i++) {
            rootView.sendMessage(Messages.newMessage(MESSAGE, TARGET));
        }

        TestUtils.wait(2_100);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new MessagingSystemTest());
    }
}
