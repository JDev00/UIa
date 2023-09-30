package test;

import test.artefacts.TestValidation;
import uia.application.platform.awt.ContextAWT;
import uia.core.ui.Context;
import uia.core.ui.ViewText;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.physical.Component;
import uia.physical.ComponentGroup;
import uia.physical.ComponentText;
import uia.physical.wrapper.WrapperViewGroup;

public class testViewSendAndReceiveMessage extends WrapperViewGroup {

    public testViewSendAndReceiveMessage() {
        super(new ComponentGroup(new Component("TEST", 0.5f, 0.5f, 0.5f, 0.5f)));


        ViewText view1 = new ComponentText(new Component("A", 0.25f, 0.25f, 0.25f, 0.25f));
        view1.sendMessage("abc", "B");


        View view2 = new Component("B", 0.75f, 0.5f, 0.25f, 0.25f);
        view2.addCallback((OnMessageReceived) message -> TestValidation.assertThat(message[0], "abc"));


        add(view1, view2);
    }

    //

    public static void main(String[] args) {
        Context context = new ContextAWT(200, 200);
        context.start();
        context.setView(new testViewSendAndReceiveMessage());
    }
}
