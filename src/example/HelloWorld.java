package example;

import uia.application.UIButton;
import uia.application.desktop.ContextSwing;
import uia.core.Font;
import uia.core.paint.Color;
import uia.core.ui.ViewGroup;
import uia.core.ui.context.Context;
import uia.core.ui.View;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnClick;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.physical.component.Component;
import uia.physical.ComponentGroup;
import uia.physical.component.ComponentText;
import uia.physical.message.Messages;
import uia.physical.theme.Theme;
import uia.physical.component.WrapperView;

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
        getPaint().setColor(Theme.DARK_GRAY);

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
        UIButton result = new UIButton(new ComponentText(
                new Component("BUTTON", 0.25f, 0.5f, 0.1f, 0.1f).setExpanseLimit(1.2f, 1.2f)
        ));
        // get the Paint used when the button is activated and set: a color, stroke color and stroke width
        result.getPaint(UIButton.STATE.ON)
                .setColor(Color.createColor(100, 200, 255, 100))
                .setStrokeColor(Theme.WHITE)
                .setStrokeWidth(8);
        // get the Paint used when the button isn't activated and set: a new color, stroke color and stroke width
        result.getPaint(UIButton.STATE.OFF)
                .setColor(Color.createColor(200, 100, 0, 50))
                .setStrokeColor(Theme.RED)
                .setStrokeWidth(4);
        // get the ViewText passed to the button constructor
        ViewText viewText = result.getView();
        // set the text alignment along y-axis
        viewText.setAlign(ViewText.AlignY.CENTER);
        // set some text
        viewText.setText("Show\npopup!");
        // get the ViewText's Font and set a new size and a new style
        viewText.getFont()
                .setStyle(Font.STYLE.BOLD)
                .setSize(18);
        // get the Paint used by viewText and set a new color
        viewText.getTextPaint().setColor(Theme.LIME);
        return result;
    }

    /**
     * Helper function. Creates a simple popup
     */

    private static View createSimplePopup() {
        // now, let us create a viewText. We will use it to emulate a simple popup.
        ViewText result = new ComponentText(
                new Component("POPUP", 0.66f, 0.5f, 0.33f, 0.5f).setExpanseLimit(1.1f, 1.1f)
        );
        // hide this popup at the beginning
        result.setVisible(false);
        // set some text to this popup
        result.setText("Hi, I'm a popup!");
        // set text properties
        result.setAlign(ViewText.AlignY.CENTER);
        result.getFont()
                .setStyle(Font.STYLE.ITALIC)
                .setLeadingFactor(1.2f)
                .setSize(25);
        // set text color
        result.getTextPaint().setColor(Color.createColor("0xe813dd"));
        return result;
    }

    public static void main(String[] args) {
        // Now create the Context and attach the HelloWorld View to it.
        // This will display the HelloWorld View.
        Context context = ContextSwing.createAndStart(1000, 500);
        context.setView(new HelloWorld());
    }
}
