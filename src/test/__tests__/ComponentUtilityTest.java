package test.__tests__;

import test.core.Test;
import test.core.TestAssertion;
import test.core.TestExecutor;
import uia.core.basement.message.Message;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.application.message.MessageFactory;
import uia.application.ui.component.Component;
import uia.application.ui.component.utility.ComponentUtility;

/**
 * Unit tests.
 */

public class ComponentUtilityTest {
    static String VIEW_ID = "1";

    static View createView() {
        return new Component(VIEW_ID, 0f, 0f, 0f, 0f);
    }

    @Test
    public void messageShouldBeNotified(TestAssertion assertion) {
        assertion.assertions(1);

        // setup
        View view = createView();
        Message message = MessageFactory.create("ciao", VIEW_ID);

        // verify
        view.registerCallback((OnMessageReceived) mex -> {
            assertion.expect(mex).toBe(message);
        });

        // act
        ComponentUtility.notifyMessageListeners(view, message);
    }

    @Test
    public void messageWithNullRecipientShouldBeNotified(TestAssertion assertion) {
        assertion.assertions(1);

        // setup
        View view = createView();
        Message message = MessageFactory.create("ciao", null);

        // verify
        view.registerCallback((OnMessageReceived) mex -> {
            assertion.expect(mex).toBe(message);
        });

        // act
        ComponentUtility.notifyMessageListeners(view, message);
    }

    @Test
    public void tryingToNotifyClientsWithANullMessageShouldThrowAnException(TestAssertion assertion) {
        assertion.assertions(1);

        // verify
        assertion.expect(() -> {
            View view = createView();
            ComponentUtility.notifyMessageListeners(view, null);
        }).toThrowException(NullPointerException.class);
    }

    @Test
    public void nullViewShouldThrowNullPointerException(TestAssertion assertion) {
        assertion.assertions(1);

        // verify
        assertion.expect(() -> {
            Message message = MessageFactory.create("", "");
            ComponentUtility.notifyMessageListeners(null, message);
        }).toThrowException(NullPointerException.class);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new ComponentUtilityTest());
    }
}
