package uia.application.platform.awt.test;

import uia.application.platform.awt.ContextAWT;
import uia.application.platform.awt.test.artefacts.ComponentTracker;
import uia.core.Image;
import uia.core.ui.Context;
import uia.core.ui.View;
import uia.core.ui.ViewText;
import uia.physical.theme.Theme;
import uia.physical.ComponentText;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentImage;
import uia.physical.wrapper.WrapperViewGroup;
import uia.utility.Utility;

public class SanityBasementUITest extends WrapperViewGroup {

    public SanityBasementUITest(Context context) {
        super(new ComponentGroup(new Component("TEST0", 0.5f, 0.5f, 1f, 1f).setExpanseLimit(1f, 1f)));

        getPaint().setColor(Theme.BACKGROUND);


        ViewText text = new ComponentText(new Component("TEXT", 0.33f, 0.25f, 0.5f, 0.75f));
        text.setAlign(ViewText.AlignX.RIGHT);
        text.setText(Utility.readAll("..\\uia\\src\\uia\\application\\platform\\awt\\test\\SanityBasementUITest.java"));
        text.setRotation(0.3f);


        ComponentImage image = new ComponentImage(new Component("IMAGE", 0.7f, 0.5f, 0.33f, 0.5f));
        image.getImage().load("..\\uia\\sample\\img0.png").setMode(Image.MODE.CENTER);


        ComponentGroup group = new ComponentGroup(new Component("GROUP", 0.4f, 0.5f, 0.5f, 0.5f)
                .setExpanseLimit(1.005f, 1.005f));
        group.getPaint().setColor(Theme.DARK_GREY);
        group.add(text, image);


        ComponentTracker tracker = new ComponentTracker(
                new Component("FPS", 0.05f, 0.05f, 0.1f, 0.1f), context
        );


        add(group, tracker);
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        try {
            View view = get("GROUP");
            view.setRotation(view.bounds()[4] + 0.005f);
        } catch (Exception ignored) {
        }
    }

    //

    public static void main(String[] args) {
        int[] dim = ContextAWT.getScreenSize();
        Context context = new ContextAWT(4 * dim[0] / 5, 4 * dim[1] / 5);
        context.start();
        context.setView(new SanityBasementUITest(context));
    }
}
