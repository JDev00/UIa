package test;

import test.core.TestCase;
import test.core.TestSuite;
import test.core.TestUtils;
import uia.core.ui.context.Context;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.callbacks.*;
import uia.physical.message.Messages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static test.Sanity.*;
import static test.core.TestUtils.waitMillis;
import static uia.utility.TrigTable.*;

/**
 * Unit tests
 */

public class TestView implements TestSuite {

    public static TestCase boundsWidthAndHeightShouldBeDifferentAfterRotation() {
        return (testAssertion) -> {
            View root = createRoot();

            Context context = createMockContext();
            context.setView(root);

            float ROTATION = 2.145f;
            root.setRotation(ROTATION);

            waitMillis(100);

            float[] bounds = root.bounds();
            float width = root.getWidth();
            float height = root.getHeight();
            float rotation = bounds[4];

            float expectedBoundsWidth = boundX(width, height, cos(rotation), sin(rotation));
            float expectedBoundsHeight = boundY(width, height, cos(rotation), sin(rotation));

            testAssertion.expect(rotation).toBeEqual(ROTATION);
            testAssertion.expect(bounds[2]).toBeEqual(expectedBoundsWidth);
            testAssertion.expect(bounds[3]).toBeEqual(expectedBoundsHeight);
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

            waitMillis(100);

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
            testAssertion.assertions(1);

            String MESSAGE = "hello", TARGET = "B";

            // test setup
            ViewGroup root = createRoot();
            root.add(createView(TARGET, 0f, 0f, 0.1f, 0.1f));

            Context context = createMockContext();
            context.setView(root);

            // test clause
            root.get(TARGET).registerCallback((OnMessageReceived) message -> {
                testAssertion.expect(message.getMessage()).toBeEqual(MESSAGE);
                System.out.println("ok!");
            });
            root.sendMessage(Messages.newMessage(MESSAGE, TARGET));
        };
    }

    public static TestCase clickingOnViewShouldGenerateAnEvent() {
        return (testAssertion) -> {
            testAssertion.assertions(1);

            View root = createRoot();
            root.registerCallback((OnClick) touches -> testAssertion.expect(true).toBeEqual(true));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().clickOn(100, 100);
        };
    }

    public static TestCase mouseOnViewShouldGenerateAnEvent() {
        return (testAssertion) -> {
            testAssertion.assertions(1);

            View root = createRoot();
            root.registerCallback((OnMouseHover) touches -> testAssertion.expect(true).toBeEqual(true));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().moveMouseOnScreen(
                    100, 100,
                    110, 110,
                    20, 0.1f);
        };
    }

    public static TestCase mouseEnteringViewShouldGenerateAnEvent() {
        return (testAssertion) -> {
            testAssertion.assertions(1);

            View root = createRoot();
            root.registerCallback((OnMouseEnter) touches -> testAssertion.expect(true).toBeEqual(true));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().moveMouseOnScreen(
                    100, 100,
                    110, 110,
                    20, 0.1f);
        };
    }

    public static TestCase mouseExitingViewShouldGenerateAnEvent() {
        return (testAssertion) -> {
            testAssertion.assertions(1);

            View root = createRoot();
            root.setDimension(0.1f, 0.1f);
            root.registerCallback((OnMouseExit) touches -> testAssertion.expect(true).toBeEqual(true));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().moveMouseOnScreen(
                    340, 270,
                    500, 243,
                    20, 0.25f);
        };
    }

    public static TestCase typingKeyShouldGenerateAnEvent() {
        return (testAssertion) -> {
            testAssertion.assertions(1);

            char KEY = 'a';
            int KEYCODE = 'a';

            View root = createRoot();
            root.requestFocus(true);
            root.registerCallback((OnKeyTyped) key -> testAssertion.expect(key.getKeyChar()).toBeEqual(KEY));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().typeKey(KEY, KEYCODE);
        };
    }

    public static TestCase releasingKeyShouldGenerateAnEvent() {
        return (testAssertion) -> {
            testAssertion.assertions(1);

            char KEY = 'a';
            int KEYCODE = 'a';

            View root = createRoot();
            root.requestFocus(true);
            root.registerCallback((OnKeyReleased) key -> testAssertion.expect(key.getKeyChar()).toBeEqual(KEY));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().releaseKey(KEY, KEYCODE);
        };
    }

    public static TestCase pressingKeyShouldGenerateAnEvent() {
        return (testAssertion) -> {
            testAssertion.assertions(1);

            char KEY = 'a';
            int KEYCODE = 'a';

            View root = createRoot();
            root.requestFocus(true);
            root.registerCallback((OnKeyPressed) key -> testAssertion.expect(key.getKeyChar()).toBeEqual(KEY));

            Context context = createMockContext();
            context.setView(root);
            context.getInputEmulator().pressKey(KEY, KEYCODE);
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
