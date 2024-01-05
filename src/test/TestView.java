package test;

import test.core.*;
import uia.core.ui.context.Context;
import uia.core.ui.View;
import uia.core.ui.callbacks.*;

import static test.Sanity.*;
import static uia.utility.TrigTable.*;

/**
 * Unit tests
 */

public class TestView {

    @Test
    public static void boundsWidthAndHeightShouldBeDifferentAfterRotation(TestAssertion testAssertion) {
        testAssertion.assertions(3);

        View root = createRoot();

        Context context = createMockContext();
        context.setView(root);

        float ROTATION = 2.145f;
        root.setRotation(ROTATION);

        TestUtils.wait(100);

        float[] bounds = root.getBounds();
        float width = root.getWidth();
        float height = root.getHeight();
        float rotation = bounds[4];

        float expectedBoundsWidth = boundX(width, height, cos(rotation), sin(rotation));
        float expectedBoundsHeight = boundY(width, height, cos(rotation), sin(rotation));

        testAssertion.expect(rotation).toBe(ROTATION);
        testAssertion.expect(bounds[2]).toBe(expectedBoundsWidth);
        testAssertion.expect(bounds[3]).toBe(expectedBoundsHeight);
    }

    @Test
    public static void widthAndHeightShouldNotChangeAfterRotation(TestAssertion testAssertion) {
        testAssertion.assertions(3);

        float ROTATION = -4.501f;
        float widthPreRotation = 720;
        float heightPreRotation = 540;

        View root = createRoot();
        root.setRotation(ROTATION);

        Context context = createMockContext();
        context.setView(root);

        TestUtils.wait(100);

        float width = root.getWidth();
        float height = root.getHeight();
        float rotation = root.getBounds()[4];

        testAssertion.expect(rotation).toBe(ROTATION);
        testAssertion.expect(width).toBe(widthPreRotation);
        testAssertion.expect(height).toBe(heightPreRotation);
    }

    @Test
    public static void clickingOnViewShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        View root = createRoot();
        root.registerCallback((OnClick) touches -> testAssertion.expect(true).toBe(true));

        Context context = createMockContext();
        context.setView(root);
        TestUtils.wait(100);

        context.getInputEmulator().clickOn(100, 100);
        TestUtils.wait(100);
    }

    @Test
    public static void mouseOnViewShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        View root = createRoot();
        root.registerCallback((OnMouseHover) touches -> testAssertion.expect(true).toBe(true));

        Context context = createMockContext();
        context.setView(root);
        TestUtils.wait(100);

        context.getInputEmulator().moveMouseOnScreen(
                100, 100,
                110, 110,
                20, 0.1f);
        TestUtils.wait(100);
    }

    @Test
    @Skip
    public static void mouseEnteringViewShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        View root = createRoot();
        root.registerCallback((OnMouseEnter) touches -> testAssertion.expect(true).toBe(true));

        Context context = createMockContext();
        context.setView(root);
        context.getInputEmulator().moveMouseOnScreen(
                100, 100,
                110, 110,
                20, 0.1f);
    }

    @Test
    @Skip
    public static void mouseExitingViewShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        View root = createRoot();
        root.setDimension(0.1f, 0.1f);
        root.registerCallback((OnMouseExit) touches -> testAssertion.expect(true).toBe(true));

        Context context = createMockContext();
        context.setView(root);
        context.getInputEmulator().moveMouseOnScreen(
                340, 270,
                500, 243,
                20, 0.25f);
    }

    @Test
    @Skip
    public static void typingKeyShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        char KEY = 'a';
        int KEYCODE = 'a';

        View root = createRoot();
        root.requestFocus(true);
        root.registerCallback((OnKeyTyped) key -> testAssertion.expect(key.getKeyChar()).toBe(KEY));

        Context context = createMockContext();
        context.setView(root);
        context.getInputEmulator().typeKey(KEY, KEYCODE);
    }

    @Test
    @Skip
    public static void releasingKeyShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        char KEY = 'a';
        int KEYCODE = 'a';

        View root = createRoot();
        root.requestFocus(true);
        root.registerCallback((OnKeyReleased) key -> testAssertion.expect(key.getKeyChar()).toBe(KEY));

        Context context = createMockContext();
        context.setView(root);
        context.getInputEmulator().releaseKey(KEY, KEYCODE);
    }

    @Test
    @Skip
    public static void pressingKeyShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        char KEY = 'a';
        int KEYCODE = 'a';

        View root = createRoot();
        root.requestFocus(true);
        root.registerCallback((OnKeyPressed) key -> testAssertion.expect(key.getKeyChar()).toBe(KEY));

        Context context = createMockContext();
        context.setView(root);
        context.getInputEmulator().pressKey(KEY, KEYCODE);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new TestView());
    }
}
