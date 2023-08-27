package uia.application.platform.awt.test;

import uia.application.platform.awt.ContextAWT;
import uia.application.platform.awt.test.artefacts.ComponentTracker;
import uia.physical.theme.ThemeDarcula;
import uia.core.basement.Context;
import uia.core.ui.ViewText;
import uia.core.ui.View;
import uia.core.ui.event.OnMessageReceived;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.physical.wrapper.WrapperViewGroup;

public class SanityMessageTest extends WrapperViewGroup {

    public SanityMessageTest(Context context) {
        super(new ComponentGroup(new Component("TEST", 0.5f, 0.5f, 0.5f, 0.5f)
                .setExpanseLimit(1f, 1f)));

        ViewText view1 = new ComponentText(new Component("V1", 0.25f, 0.25f, 0.25f, 0.25f));
        view1.getPaint().setColor(ThemeDarcula.W_FOREGROUND);
        view1.sendMessage("TEST", "V2");


        View view2 = new Component("V2", 0.75f, 0.5f, 0.25f, 0.25f);
        view2.getPaint().setStrokeWidth(2);
        view2.addEvent((OnMessageReceived) message -> assertThat(message[0], "TEST"));


        View tracker = new ComponentTracker(new Component("FPS", 0.1f, 0.05f, 0.2f, 0.1f), context);


        add(view1, view2, tracker);
    }

    private static void assertThat(Object message, Object answer) {
        System.out.println("TEST result:");

        if (message == answer) {
            System.out.println("PASSED");
            System.exit(0);
        } else {
            System.out.println("FAILED");
            System.exit(1);
        }
    }

    //

    public static void main(String[] args) {
        int[] dim = ContextAWT.getScreenSize();
        Context context = new ContextAWT(4 * dim[0] / 5, 4 * dim[1] / 5);
        context.start();
        context.setView(new SanityMessageTest(context));
    }
}
