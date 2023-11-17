package example;

import uia.application.UIButton;
import uia.application.desktop.ContextSwing;
import uia.core.Font;
import uia.core.Paint;
import uia.core.ui.context.Context;
import uia.core.ui.View;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnClick;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.physical.message.Messages;
import uia.physical.theme.Theme;
import uia.physical.wrapper.WrapperViewGroup;

/**
 * Demonstrative example. Draw a simple button that allows to show and hide a popup.
 */

public class HelloWorld extends WrapperViewGroup {

    public HelloWorld() {
        // default components are based on the decorator pattern, so you need to pass the smallest UI unit
        // (in this example: Component).
        // Here we will create a ComponentGroup that will allow us to easily manage a set of views.
        super(new ComponentGroup(new Component("HELLO_WORLD", 0.5f, 0.5f, 1f, 1f)));
        getPaint().setColor(Theme.DARK_GREY);

        // let us create a new specialised View: a Button
        UIButton button = createCustomButton();
        // now comes for the interesting part of the job: showing and hiding a View without creating dependencies.
        button.registerCallback((OnClick) touches -> {
            String message = button.isEnabled() ? "Wake up!" : "Sleep now :)";
            button.sendMessage(Messages.newMessage(message, "POPUP"));
        });
        // add another callback to listen for messages sent to this button
        button.registerCallback((OnMessageReceived) message -> {
            String text = message.<String>getPayload().contains("Hey") ? "Hide\npopup!" : "Show\npopup!";
            ViewText viewText = (ViewText) button.getView();
            viewText.setText(text);
        });

        // now, creates a new simple popup View.
        View popup = createSimplePopup();
        // creates an event to listen for messages sent to this popup from other views
        popup.registerCallback((OnMessageReceived) message -> {
            // if the sender ID is 'BUTTON'
            boolean visibility = message.<String>getPayload().contains("Wake up");
            // shows or hides this popup accordingly
            popup.setVisible(visibility);
            // sends a message to BUTTON to inform it that popup woke up or went to sleep
            String messageToSend = visibility ? "Hey!" : "Bye bye";
            popup.sendMessage(Messages.newMessage(messageToSend, "BUTTON"));
        });

        // adds button and popup to the HelloWorld group
        add(button, popup);
    }

    private static UIButton createCustomButton() {
        UIButton result = new UIButton(new ComponentText(
                new Component("BUTTON", 0.25f, 0.5f, 0.1f, 0.1f, 1.2f, 1.2f)
        ));
        // get the Paint used when the button is activated and set: a new color, stroke color and stroke width
        result.getPaint(UIButton.STATE.ENABLED)
                .setColor(new Paint.Color(100, 200, 255, 100))
                .setStrokeColor(Theme.WHITE)
                .setStrokeWidth(8);
        // get the Paint used when the button isn't activated and set: a new color, stroke color and stroke width
        result.getPaint(UIButton.STATE.DISABLED)
                .setColor(new Paint.Color(200, 100, 0, 50))
                .setStrokeColor(Theme.RED)
                .setStrokeWidth(4);
        // get the ViewText passed to the button constructor
        ViewText viewText = ((ViewText) result.getView());
        // set the text alignment along y-axis
        viewText.setAlign(ViewText.AlignY.CENTER);
        // set some text
        viewText.setText("Show\npopup!");
        // get the ViewText's Font and set a new size and a new style
        viewText.getFont()
                .setSize(18)
                .setStyle(Font.STYLE.BOLD);
        // get the Paint used by viewText and set a new color
        viewText.getTextPaint().setColor(Theme.GREEN);
        return result;
    }

    private static View createSimplePopup() {
        // now, let us create a viewText. We will use it to emulate a simple popup.
        ViewText result = new ComponentText(
                new Component("POPUP", 0.66f, 0.5f, 0.33f, 0.5f, 1.1f, 1.1f)
        );
        // hide this popup at the beginning
        result.setVisible(false);
        // set some text to this popup
        result.setText("Hi, I'm a popup!");
        // set text properties
        result.setAlign(ViewText.AlignY.CENTER);
        result.getFont()
                .setSize(25)
                .setStyle(Font.STYLE.ITALIC)
                .setLeadingFactor(1.2f);
        // set text color
        result.getTextPaint().setColor(new Paint.Color("0xe813dd"));
        return result;
    }

    private static Context createContext() {
        int[] screenSize = ContextSwing.getScreenSize();
        // creates a new AWT Context.
        Context result = new ContextSwing(4 * screenSize[0] / 5, 4 * screenSize[1] / 5);
        // shows the Window frame handled by this Context
        result.getWindow().show();
        // starts the created context
        result.setLifecycleStage(Context.LifecycleStage.RUN);
        return result;
    }

    public static void main(String[] args) {
        Context context = createContext();
        context.setView(new HelloWorld());
    }
}
