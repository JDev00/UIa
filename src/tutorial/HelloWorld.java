package tutorial;

import uia.application.UIButton;
import uia.application.platform.awt.ContextAWT;
import uia.core.Font;
import uia.core.Paint;
import uia.core.ui.Context;
import uia.core.ui.ViewText;
import uia.core.ui.callbacks.OnClick;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.physical.theme.Theme;
import uia.physical.wrapper.WrapperViewGroup;
import uia.utility.Figure;

public class HelloWorld extends WrapperViewGroup {

    public HelloWorld() {
        // default components are based on the decorator pattern, so you need to pass the smallest UI unit
        // (in this example: Component).
        // Here we will create a ComponentGroup that will allow us to easily manage a set of views.
        super(new ComponentGroup(new Component("HELLOWORLD", 0.5f, 0.5f, 1f, 1f)
                .setExpanseLimit(1f, 1f)));

        // get the Paint object used by Component to colour our background
        getPaint().setColor(Theme.BACKGROUND);


        // let us create a new specialised View: a Button
        UIButton button = new UIButton(new ComponentText(
                new Component("BUTTON", 0.25f, 0.5f, 0.1f, 0.1f).setExpanseLimit(1.2f, 1.2f)
        ));
        // get the Paint used when the button is activated and set: a new color, stroke color and stroke width
        button.getPaint(UIButton.STATE.ENABLED)
                .setColor(new Paint.Color(100, 200, 255, 100))
                .setStrokeColor(Theme.WHITE)
                .setStrokeWidth(8);
        // get the Paint used when the button isn't activated and set: a new color, stroke color and stroke width
        button.getPaint(UIButton.STATE.DISABLED)
                .setColor(new Paint.Color(200, 100, 0, 50))
                .setStrokeColor(Theme.RED)
                .setStrokeWidth(8);
        // get the ViewText passed to the button constructor
        ViewText viewText = ((ViewText) button.getView());
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
        // now comes for the interesting part of the job: showing and hiding a component without creating bad dependencies.
        // remember: events are typically called after View's state is updated.
        button.addCallback((OnClick) pointers -> {
            button.sendMessage(button.isEnabled() ? "Ehy Bro, wake up!" : "Sleep now :)", "POPUP");
        });
        // create another event to listen for messages sent to this button
        button.addCallback((OnMessageReceived) message -> {
            if (message[1] == "POPUP")
                viewText.setText(((String) message[0]).contains("Hey") ? "Hide\npopup!" : "Show\npopup!");
        });


        // now, let us create a viewText. We will use it to emulate a simple popup.
        ViewText popup = new ComponentText(
                new Component("POPUP", 0.66f, 0.5f, 0.33f, 0.5f).setExpanseLimit(1.1f, 1.1f)
        );
        // set the popup shape: create a new rounded corner rectangle with 25 vertices.
        // Here, we will use Figure (a collection of shapes) to easily create a rounded rectangle.
        popup.buildGeometry(g -> Figure.rect(g, Figure.STD_VERT, Figure.STD_ROUND,
                popup.bounds()[0] / popup.bounds()[1]), true);
        // hide this popup at the beginning
        popup.setVisible(false);
        // set some text to this popup
        popup.setText("Hi, I'm a little popup.\n Unfortunately, no one has taken care of\n placing some widgets on me ;(" +
                "\nSo please, populate my landscape\n with some UI objects.\n\n Thanks!");
        // set text properties
        popup.setAlign(ViewText.AlignY.CENTER);
        popup.getFont()
                .setSize(30)
                .setStyle(Font.STYLE.ITALIC)
                .setLeadingFactor(1.2f);
        // set text color
        popup.getTextPaint().setColor(new Paint.Color("0xe813dd"));
        // creates an event to listen for messages sent to this popup from other views
        popup.addCallback((OnMessageReceived) message -> {
            // if sender ID is 'BUTTON'
            if (message[1] == "BUTTON") {
                boolean visibility = ((String) message[0]).contains("wake up");
                // shows or hides this popup
                popup.setVisible(visibility);
                // sends a message to BUTTON to inform it that popup woke up or went to sleep
                popup.sendMessage(visibility ? "Hey man!" : "Bye bye", "BUTTON");
            }
        });


        // adds button and popup to the HelloWorld group
        add(button, popup);
    }

    //

    public static void main(String[] args) {
        int[] screenSize = ContextAWT.getScreenSize();

        // creates a new AWT Context.
        Context context = new ContextAWT(4 * screenSize[0] / 5, 4 * screenSize[1] / 5);
        // starts and shows the created context
        context.start();
        // let our Context manage HelloWorld
        context.setView(new HelloWorld());
    }
}