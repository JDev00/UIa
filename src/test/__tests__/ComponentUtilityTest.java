package test.__tests__;

import test.core.Test;
import test.core.TestAssertion;
import test.core.TestExecutor;
import uia.core.basement.Message;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.physical.Component;
import uia.physical.message.Messages;
import uia.physical.utility.ComponentUtility;

/**
 * Unit tests
 */
public class ComponentUtilityTest {

    @Test
    public void messageShouldBeNotifiedToAView(TestAssertion assertion) {
        assertion.assertions(1);

        // setup
        String id = "1";
        Message message = Messages.newMessage("ciao", id);
        View view = new Component(id, 0, 0, 0, 0);
        view.registerCallback((OnMessageReceived) mex -> {
            assertion.expect(mex).toBe(message);
        });

        ComponentUtility.notifyMessageCallback(view, message);
    }

    @Test
    public void messageWithNullRecipientShouldBeNotifiedToAView(TestAssertion assertion) {
        assertion.assertions(1);

        // setup
        String id = "1";
        Message message = Messages.newMessage("ciao", null);
        View view = new Component(id, 0, 0, 0, 0);
        view.registerCallback((OnMessageReceived) mex -> {
            assertion.expect(mex).toBe(message);
        });

        ComponentUtility.notifyMessageCallback(view, message);
    }

    @Test
    public void nullMessageShouldThrowNullPointerException(TestAssertion assertion) {
        assertion.assertions(1);
        assertion.expect(() -> {
            View view = new Component("", 0, 0, 0, 0);
            ComponentUtility.notifyMessageCallback(view, null);
        }).toThrowException(NullPointerException.class);
    }

    @Test
    public void nullViewShouldThrowNullPointerException(TestAssertion assertion) {
        assertion.assertions(1);
        assertion.expect(() -> {
            Message message = Messages.newMessage("", "");
            ComponentUtility.notifyMessageCallback(null, message);
        }).toThrowException(NullPointerException.class);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new ComponentUtilityTest());
    }
}
