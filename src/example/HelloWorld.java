package example;

import uia.physical.ui.component.text.ComponentText;
import uia.core.ui.style.TextVerticalAlignment;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.core.rendering.geometry.Geometries;
import uia.physical.ui.component.WrapperView;
import uia.physical.ui.group.ComponentGroup;
import uia.physical.message.MessageFactory;
import uia.physical.ui.component.Component;
import uia.core.basement.message.Message;
import uia.core.ui.callbacks.OnClick;
import uia.core.rendering.font.Font;
import uia.core.context.Context;
import uia.physical.ui.Theme;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.View;

import api.swing.ContextSwing;

/**
 * Demonstrative example. Display a simple button that allows to show and hide a popup.
 */

public class HelloWorld extends WrapperView {
    private static final String BUTTON_ID = "MY_BUTTON";
    private static final String POPUP_ID = "POPUP";

    public HelloWorld() {
        // default components are based on the decorator pattern, so you need to pass the smallest UI unit
        // (in this example: Component).
        // Here we will create a ComponentGroup that will allow us to easily manage a list of views.
        super(new ComponentGroup(
                new Component("HELLO_WORLD", 0.5f, 0.5f, 1f, 1f))
        );
        getStyle().setBackgroundColor(Theme.DARK_GRAY);

        // uses a ViewText to create a simple button
        boolean[] isButtonEnabled = {false};
        ViewText button = createCustomButton(BUTTON_ID);
        // now comes for the interesting part of the job: showing and hiding a View without creating dependencies.
        // when clicked, it sends a message to the popup.
        button.registerCallback((OnClick) touches -> {
            // changes the view state: enabled/disabled
            isButtonEnabled[0] = !isButtonEnabled[0];
            // creates the message
            String messagePayload = isButtonEnabled[0] ? "Hi!" : "Bye";
            Message message = MessageFactory.create(messagePayload, POPUP_ID);
            // sends the message
            button.sendMessage(message);
        });
        // add another callback to listen for messages sent to this button
        button.registerCallback((OnMessageReceived) message -> {
            String payload = message.getPayload();
            String textToDisplay = payload.contains("Hey") ? "Hide\npopup!" : "Show\npopup!";
            button.setText(textToDisplay);
        });

        // creates a simple popup.
        View popup = createPopup(POPUP_ID);
        // adds a callback that will be invoked when a message, for this popup, is received
        popup.registerCallback((OnMessageReceived) receivedMessage -> {
            String payload = receivedMessage.getPayload();
            boolean visibility = payload.contains("Hi");
            // shows or hides this popup accordingly
            popup.setVisible(visibility);
            // sends a message to the button to tell it that the popup is visible or not
            String messagePayload = visibility ? "Hey!" : "Bye";
            Message messageToSend = MessageFactory.create(messagePayload, BUTTON_ID);
            popup.sendMessage(messageToSend);
        });

        // add button and popup to the group
        ViewGroup.insert(getView(), button, popup);
    }

    /**
     * Helper function. Creates a custom button.
     */

    private static ViewText createCustomButton(String id) {
        ViewText result = new ComponentText(
                new Component(id, 0.25f, 0.5f, 0.1f, 0.1f).setExpanseLimit(1.2f, 1.2f)
        );
        // sets some text
        result.setText("Show\npopup!");
        // sets the style of the text component
        result.getStyle()
                .setGeometry(geometry -> Geometries.rect(
                                geometry,
                                Geometries.STD_VERT,
                                0.25f,
                                result.getWidth() / result.getHeight()
                        ), true
                )
                .setTextAlignment(TextVerticalAlignment.CENTER)
                .setBackgroundColor(Theme.ROYAL_BLUE)
                .setFontStyle(Font.FontStyle.BOLD)
                .setTextColor(Theme.WHITE)
                .setFontSize(18f);

        return result;
    }

    /**
     * Helper function. Creates a simple popup.
     */

    private static View createPopup(String id) {
        // create a viewText. It will be used to emulate a simple popup.
        ViewText result = new ComponentText(
                new Component(id, 0.66f, 0.5f, 0.33f, 0.5f)
        );
        // hide this popup at the beginning
        result.setVisible(false);
        // set some text to this popup
        result.setText("Hello!\nI'm a simple popup.");
        // set text style
        result.getStyle()
                .setTextAlignment(TextVerticalAlignment.CENTER)
                .setFontStyle(Font.FontStyle.ITALIC)
                .setTextColor(Theme.DARK_GRAY)
                .setFontSize(25);

        return result;
    }

    public static void main(String[] args) {
        // Now create the Context and attach the HelloWorld View to it.
        // This will display the HelloWorld View.
        Context context = ContextSwing.createAndStart(1000, 500);
        context.setView(new HelloWorld());
    }
}
