# UIa - Graphical User Interface framework for Java.
<br>
UIa is a cross-platform and cross-library framework for Java. It is designed to make it easy to create the graphical aspects of an application.
<br>
From the architectural point of view, UIa is built on top of a native graphics library or a third-party graphical framework. To achieve the cross-platform/library goal, UIa's core has been designed to be
highly adaptive.
<br>
At the moment, core package is structured as follows:

![Screenshot](UIa-UML.pdf)

As you can see in the above picture, two contracts are the foundamentals of UIa:
<ul>
  <li>Context: is the framework basement and has the responbility to show the window frame and handle a single View;</li>
  <li>View: is the basement for every widget managed and/or created with UIa.</li>
</ul>

According to its architecture, migrating a project between two platforms is easy: you need to implement the right Context for the target platform and use it as application basement.
A Desktop Context implementation, based on Java AWT, has been already provided. If you have other needs, try experimenting with creating your own custom Context implementation.
<br>
<br>
The following part shows a simple Hello World application made with UIa. It displays a button on the left and, when the user clicks on it, a simple popup appears on the right. To hide the popup, simply click on the button.

```java
import uia.application.UIButton;
import uia.application.platform.awt.ContextAWT;
import uia.core.Font;
import uia.core.Paint;
import uia.core.basement.Context;
import uia.core.ui.ViewText;
import uia.core.ui.event.OnClick;
import uia.core.ui.event.OnMessageReceived;
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
        button.getPaintEnabled()
                .setColor(new Paint.Color(100, 200, 255, 100))
                .setStrokeColor(Theme.WHITE)
                .setStrokeWidth(8);
        // get the Paint used when the button isn't activated and set: a new color, stroke color and stroke width
        button.getPaintNotEnabled()
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
        button.addEvent((OnClick) pointers -> {
            button.sendMessage(button.isEnabled() ? "Ehy Bro, wake up!" : "Sleep now :)", "POPUP");
        });
        // create another event to listen for messages sent to this button
        button.addEvent((OnMessageReceived) message -> {
            if (message[1] == "POPUP")
                viewText.setText(((String) message[0]).contains("Hey") ? "Hide\npopup!" : "Show\npopup!");
        });


        // now, let us create a viewText. We will use it to emulate a simple popup.
        ViewText popup = new ComponentText(
                new Component("POPUP", 0.66f, 0.5f, 0.33f, 0.5f).setExpanseLimit(1.1f, 1.1f)
        );
        // set the popup shape: create a new rounded corner rectangle with 25 vertices.
        // Here, we will use Figure (a collection of shapes) to easily create a rounded rectangle.
        popup.buildGeom((v, g) -> Figure.rect(g, Figure.STD_VERT, Figure.STD_ROUND, v.desc()[0] / v.desc()[1]), true);
        // hide this popup at the beginning
        popup.setVisible(false);
        // set some text to this popup
        popup.setText("Hi, i'm a small popup.\n Unfortunately nobody take care of\n place some widget on me ;(" +
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
        popup.addEvent((OnMessageReceived) message -> {
            // if sender ID is 'BUTTON'
            if (message[1] == "BUTTON") {
                boolean visibility = ((String) message[0]).contains("wake up");
                // shows or hides this popup
                popup.setVisible(visibility);
                // sends a message to BUTTON to inform it that our popup woke up or went to sleep
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
```
