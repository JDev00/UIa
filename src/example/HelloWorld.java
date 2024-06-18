package example;

import uia.core.ui.style.TextVerticalAlignment;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.physical.ui.component.ComponentText;
import uia.physical.ui.component.WrapperView;
import uia.core.rendering.color.Color;
import uia.physical.ui.group.ComponentGroup;
import uia.core.rendering.font.Font;
import uia.physical.ui.component.Component;
import uia.platform.swing.ContextSwing;
import uia.physical.message.Messages;
import uia.core.ui.callbacks.OnClick;
import uia.core.context.Context;
import uia.physical.ui.theme.Theme;
import uia.application.UIButton;
import uia.core.ui.ViewGroup;
import uia.core.ui.ViewText;
import uia.core.ui.View;

/**
 * Demonstrative example. Display a simple button that allows to show and hide a popup.
 */

public class HelloWorld extends WrapperView {

    public HelloWorld() {
        // default components are based on the decorator pattern, so you need to pass the smallest UI unit
        // (in this example: Component).
        // Here we will create a ComponentGroup that will allow us to easily manage a set of views.
        super(new ComponentGroup(
                new Component("HELLO_WORLD", 0.5f, 0.5f, 1f, 1f))
        );
        getStyle().setBackgroundColor(Theme.DARK_GRAY);

        // let us create a new specialised View: a Button
        UIButton button = createCustomButton();
        // now comes for the interesting part of the job: showing and hiding a View without creating dependencies.
        button.registerCallback((OnClick) touches -> {
            String messageToSend = button.isEnabled() ? "Wake up!" : "Sleep now :)";
            button.sendMessage(Messages.newMessage(messageToSend, "POPUP"));
        });
        // add another callback to listen for messages sent to this button
        button.registerCallback((OnMessageReceived) message -> {
            String payload = message.getPayload();
            String textToDisplay = payload.contains("Hey") ? "Hide\npopup!" : "Show\npopup!";
            ViewText viewText = button.getView();
            viewText.setText(textToDisplay);
        });

        // now create a new simple popup.
        View popup = createSimplePopup();
        // creates an event to listen for messages sent to this popup from other views
        popup.registerCallback((OnMessageReceived) message -> {
            String payload = message.getPayload();
            boolean visibility = payload.contains("Wake up");
            // shows or hides this popup accordingly
            popup.setVisible(visibility);
            // sends a message to BUTTON to inform it that popup woke up or went to sleep
            String messageToSend = visibility ? "Hey!" : "Bye bye";
            popup.sendMessage(Messages.newMessage(messageToSend, "BUTTON"));
        });

        // adds button and popup to the HelloWorld group
        ViewGroup.insert(getView(), button, popup);
    }

    /**
     * Helper function. Creates a custom demonstrative button
     */

    private static UIButton createCustomButton() {
        ViewText text = new ComponentText(
                new Component("BUTTON", 0.25f, 0.5f, 0.1f, 0.1f).setExpanseLimit(1.2f, 1.2f)
        );
        // set some text
        text.setText("Show\npopup!");
        // set the style for text
        text.getStyle()
                .setTextAlignment(TextVerticalAlignment.CENTER)
                .setFontStyle(Font.FontStyle.BOLD)
                .setFontSize(18f);

        UIButton result = new UIButton(text);
        result.setStateStyleFunction(UIButton.State.OFF, style -> style
                .setBackgroundColor(Color.createColor(100, 200, 100, 50))
                .setBorderColor(Theme.LIME)
                .setTextColor(Theme.LIME)
                .setBorderWidth(6)
        );
        result.setStateStyleFunction(UIButton.State.ON, style -> style
                .setBackgroundColor(Color.createColor(200, 100, 0, 50))
                .setBorderColor(Theme.RED)
                .setTextColor(Theme.RED)
                .setBorderWidth(2)
        );

        return result;
    }

    /**
     * Helper function. Creates a simple popup
     */

    private static View createSimplePopup() {
        // create a viewText. It will be used to emulate a simple popup.
        ViewText result = new ComponentText(
                new Component("POPUP", 0.66f, 0.5f, 0.33f, 0.5f)
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
