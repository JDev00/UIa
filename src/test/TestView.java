package test;

import test.artefacts.TestCase;
import test.artefacts.TestSuite;
import test.artefacts.TestUtils;
import uia.core.ui.context.Context;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.callbacks.*;
import uia.physical.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static test.Sanity.*;
import static uia.utility.TrigTable.*;

/**
 * Unit tests
 */

public class TestView implements TestSuite {

    public static TestCase boundsWidthAndHeightShouldBeDifferentAfterRotation() {
        return (testAssertion) -> {
            float ROTATION = 2.145f;

            View root = createRoot();
            root.setRotation(ROTATION);

            Context context = createMockContext();
            context.setView(root);

            TestUtils.sleep(100);

            float[] bounds = root.bounds();
            float width = root.getWidth();
            float height = root.getHeight();
            float rotation = bounds[4];

            float rotatedBoundsWidth = boundX(width, height, cos(rotation), sin(rotation));
            float rotatedBoundsHeight = boundY(width, height, cos(rotation), sin(rotation));

            testAssertion.expect(rotation).toBeEqual(ROTATION);
            testAssertion.expect(bounds[2]).toBeEqual(rotatedBoundsWidth);
            testAssertion.expect(bounds[3]).toBeEqual(rotatedBoundsHeight);
        };
    }

    public static TestCase widthAndHeightShouldNotChangeAfterRotation() {
        return (testAssertion) -> {
            float ROTATION = -4.501f;
            float widthPreRotation = 720;
            float heightPreRotation = 540;

            View root = createRoot();
            root.setRotation(ROTATION);

            Context context = createMockContext();
            context.setView(root);

            TestUtils.sleep(100);

            float width = root.getWidth();
            float height = root.getHeight();
            float rotation = root.bounds()[4];

            testAssertion.expect(rotation).toBeEqual(ROTATION);
            testAssertion.expect(width).toBeEqual(widthPreRotation);
            testAssertion.expect(height).toBeEqual(heightPreRotation);
        };
    }

    public static TestCase viewShouldBeAbleToSendAMessageToAnotherView() {
        return (testAssertion) -> {
            String MESSAGE = "hello";
            String DESTINATION = "B";

            View receiver = new Component(DESTINATION, 0f, 0f, 0.1f, 0.1f);
            receiver.addCallback((OnMessageReceived) message -> testAssertion.expect(message[0]));

            ViewGroup root = createRoot();
            root.add(receiver);
            root.sendMessage(MESSAGE, DESTINATION);

            Context context = createMockContext();
            context.setView(root);

            testAssertion.toBeEqual(MESSAGE, 100);
        };
    }

    public static TestCase clickingOnViewShouldGenerateAnEvent() {
        return (testAssertion) -> {
            View root = createRoot();
            root.addCallback((OnClick) pointers -> testAssertion.expect(true));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().click(100, 100);

            testAssertion.toBeEqual(true, 50);
        };
    }

    public static TestCase mouseOnViewShouldGenerateAnEvent() {
        return (testAssertion) -> {
            View root = createRoot();
            root.addCallback((OnMouseHover) pointers -> testAssertion.expect(true));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().moveOnScreen(100, 100, 110, 110, 20, 0.1f);

            testAssertion.toBeEqual(true, 50);
        };
    }

    public static TestCase mouseEnteringViewShouldGenerateAnEvent() {
        return (testAssertion) -> {
            View root = createRoot();
            root.addCallback((OnMouseEnter) pointers -> testAssertion.expect(true));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().moveOnScreen(100, 100, 110, 110, 20, 0.1f);

            testAssertion.toBeEqual(true, 50);
        };
    }

    public static TestCase mouseExitingViewShouldGenerateAnEvent() {
        return (testAssertion) -> {
            View root = createRoot();
            root.setDimension(0.1f, 0.1f);
            root.addCallback((OnMouseExit) pointers -> testAssertion.expect(true));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().moveOnScreen(340, 270, 500, 243, 20, 0.25f);

            testAssertion.toBeEqual(true, 300);
        };
    }

    public static TestCase typingKeyShouldGenerateAnEvent() {
        return (testAssertion) -> {
            char KEY = 'a';
            int KEYCODE = 'a';

            View root = createRoot();
            root.requestFocus(true);
            root.addCallback((OnKeyTyped) key -> testAssertion.expect(key.getKeyChar()));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().typeKey(KEY, KEYCODE);

            testAssertion.toBeEqual(KEY, 100);
        };
    }

    public static TestCase releasingKeyShouldGenerateAnEvent() {
        return (testAssertion) -> {
            char KEY = 'a';
            int KEYCODE = 'a';

            View root = createRoot();
            root.requestFocus(true);
            root.addCallback((OnKeyReleased) key -> testAssertion.expect(key.getKeyChar()));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().releaseKey(KEY, KEYCODE);

            testAssertion.toBeEqual(KEY, 100);
        };
    }

    public static TestCase pressingKeyShouldGenerateAnEvent() {
        return (testAssertion) -> {
            char KEY = 'a';
            int KEYCODE = 'a';

            View root = createRoot();
            root.requestFocus(true);
            root.addCallback((OnKeyPressed) key -> testAssertion.expect(key.getKeyChar()));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().pressKey(KEY, KEYCODE);

            testAssertion.toBeEqual(KEY, 100);
        };
    }

    @Override
    public Iterator<TestCase> iterator() {
        return new ArrayList<>(Arrays.asList(
                boundsWidthAndHeightShouldBeDifferentAfterRotation(),
                widthAndHeightShouldNotChangeAfterRotation(),
                viewShouldBeAbleToSendAMessageToAnotherView(),
                clickingOnViewShouldGenerateAnEvent(),
                mouseOnViewShouldGenerateAnEvent(),
                mouseEnteringViewShouldGenerateAnEvent(),
                mouseExitingViewShouldGenerateAnEvent(),
                typingKeyShouldGenerateAnEvent(),
                releasingKeyShouldGenerateAnEvent(),
                pressingKeyShouldGenerateAnEvent()
        )).iterator();
    }

    public static void main(String[] args) {
        TestUtils.runTestSuite(new TestView());
    }
}
