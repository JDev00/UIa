package test;

import test.artefacts.TestCase;
import test.artefacts.TestUtils;
import test.artefacts.TestValidation;
import uia.core.ui.Context;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.callbacks.OnMessageReceived;
import uia.physical.Component;

import static test.Sanity.*;
import static uia.utility.TrigTable.*;

/**
 * Unit test
 */

public class TestView {

    public static TestCase boundsWidthAndHeightShouldBeDifferentAfterRotation() {
        return () -> {
            float ROTATION = 2.145f;

            View root = createRoot();
            root.setRotation(ROTATION);

            Context context = createAWTContext();
            context.setView(root);

            TestUtils.sleep(200);

            float[] bounds = root.bounds();
            float width = root.getWidth();
            float height = root.getHeight();
            float rotation = bounds[4];

            float rotatedBoundsWidth = boundX(width, height, cos(rotation), sin(rotation));
            float rotatedBoundsHeight = boundY(width, height, cos(rotation), sin(rotation));

            TestValidation control = new TestValidation();
            control.expect(rotation).toBeEqual(ROTATION);
            control.expect(bounds[2]).toBeEqual(rotatedBoundsWidth);
            control.expect(bounds[3]).toBeEqual(rotatedBoundsHeight);
            return control;
        };
    }

    public static TestCase viewWidthAndHeightShouldNotChangeAfterRotation() {
        return () -> {
            float ROTATION = 10.001f;

            float widthPreRotation;
            float heightPreRotation;

            View root = createRoot();

            Context context = createAWTContext();
            context.setView(root);

            TestUtils.sleep(200);
            {
                widthPreRotation = root.getWidth();
                heightPreRotation = root.getHeight();
                root.setRotation(ROTATION);
            }
            TestUtils.sleep(200);

            float[] bounds = root.bounds();
            float width = root.getWidth();
            float height = root.getHeight();
            float rotation = bounds[4];

            TestValidation control = new TestValidation();
            control.expect(rotation).toBeEqual(ROTATION);
            control.expect(width).toBeEqual(widthPreRotation);
            control.expect(height).toBeEqual(heightPreRotation);
            return control;
        };
    }

    public static TestCase viewShouldBeAbleToSendAMessageToAnotherView() {
        return () -> {
            String MESSAGE = "hello";
            String DESTINATION = "B";

            TestValidation validation = new TestValidation();

            View receiver = new Component(DESTINATION, 0f, 0f, 0.1f, 0.1f);
            receiver.addCallback((OnMessageReceived) message -> validation.expect(message[0]));

            ViewGroup root = createRoot();
            root.add(receiver);
            root.sendMessage(MESSAGE, DESTINATION);

            Context context = createAWTContext();
            context.setView(root);

            TestUtils.sleep(10);
            validation.toBeEqual(MESSAGE);

            return validation;
        };
    }

    public static void main(String[] args) {
        TestUtils.runTest(boundsWidthAndHeightShouldBeDifferentAfterRotation());
        TestUtils.runTest(viewWidthAndHeightShouldNotChangeAfterRotation());
        TestUtils.runTest(viewShouldBeAbleToSendAMessageToAnotherView());
    }
}
