package test.__tests__.messages;

import test.core.*;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.core.context.Context;
import uia.application.message.MessageFactory;

import static test.__tests__.utility.TestUtility.*;

/**
 * Unit tests.
 */

public class messagingSystemTest {
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
        String TARGET_VIEW = "B";

        // setup
        ViewGroup.insert(rootView, createView(TARGET_VIEW, 0f, 0f, 0.1f, 0.1f));

        // verify
        View targetView = rootView.get(TARGET_VIEW);
        targetView.registerCallback((OnMessageReceived) receivedMessage -> {
            String messagePayload = receivedMessage.getPayload();
            testAssertion.expect(messagePayload).toBe(MESSAGE);
        });

        // act
        for (int i = 0; i < messages; i++) {
            rootView.sendMessage(MessageFactory.create(MESSAGE, TARGET_VIEW));
        }
        TestUtils.wait(2_100);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new messagingSystemTest());
    }
}
