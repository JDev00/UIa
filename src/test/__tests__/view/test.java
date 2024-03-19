package test.__tests__.view;

import test.core.*;
import uia.core.basement.Collidable;
import uia.core.ui.context.Context;
import uia.core.ui.View;
import uia.core.ui.callbacks.*;

import static test.__tests__.utility.TestUtility.*;

/**
 * Unit tests
 */

public class test {
    Context context;
    View rootView;

    @BeforeEachTest
    public void beforeEach() {
        rootView = createRoot();

        context = createMockContext();
        context.setView(rootView);
    }

    @Test
    public void viewBoundsWidthAndHeightShouldBeDifferentAfterARotation(TestAssertion testAssertion) {
        testAssertion.assertions(3);

        // setup
        float ROTATION = 2.145f;
        rootView.setRotation(ROTATION);

        TestUtils.wait(100);

        // controls
        float[] bounds = rootView.getBounds();
        float width = rootView.getWidth();
        float height = rootView.getHeight();
        float rotation = bounds[4];

        float expectedBoundsWidth = Collidable.colliderWidth(width, height, rotation);
        float expectedBoundsHeight = Collidable.colliderHeight(width, height, rotation);

        testAssertion.expect(rotation).toBe(ROTATION);
        testAssertion.expect(bounds[2]).toBe(expectedBoundsWidth);
        testAssertion.expect(bounds[3]).toBe(expectedBoundsHeight);
    }

    @Test
    public void viewWidthAndHeightShouldNotChangeAfterARotation(TestAssertion testAssertion) {
        testAssertion.assertions(3);

        float ROTATION = -4.501f;
        float widthPreRotation = 720;
        float heightPreRotation = 540;

        // setup
        rootView.setRotation(ROTATION);

        TestUtils.wait(100);

        // controls
        float width = rootView.getWidth();
        float height = rootView.getHeight();
        float rotation = rootView.getBounds()[4];

        testAssertion.expect(rotation).toBe(ROTATION);
        testAssertion.expect(width).toBe(widthPreRotation);
        testAssertion.expect(height).toBe(heightPreRotation);
    }

    @Test
    @Skip
    public void clickingOnViewShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        rootView.registerCallback((OnClick) touches -> {
            testAssertion.expect(true).toBe(true);
        });

        context.getInputEmulator().clickOn(100, 100);
    }

    @Test
    @Skip
    public void mouseOnViewShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        rootView.registerCallback((OnMouseHover) touches -> {
            testAssertion.expect(true).toBe(true);
        });

        context.getInputEmulator().moveMouseOnScreen(
                100, 100,
                110, 110,
                20, 0.1f
        );
    }

    @Test
    @Skip
    public void mouseEnteringViewShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        rootView.registerCallback((OnMouseEnter) touches -> {
            testAssertion.expect(true).toBe(true);
        });

        context.getInputEmulator().moveMouseOnScreen(
                100, 100,
                110, 110,
                20, 0.1f
        );
    }

    @Test
    @Skip
    public void mouseExitingViewShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        rootView.setDimension(0.1f, 0.1f);
        rootView.registerCallback((OnMouseExit) touches -> {
            testAssertion.expect(true).toBe(true);
        });

        context.getInputEmulator().moveMouseOnScreen(
                340, 270,
                500, 243,
                20, 0.25f
        );
    }

    @Test
    @Skip
    public void typingKeyShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        char KEY = 'a';
        int KEYCODE = 'a';

        rootView.requestFocus(true);
        rootView.registerCallback((OnKeyTyped) key -> {
            testAssertion.expect(key.getKeyChar()).toBe(KEY);
        });

        context.getInputEmulator().typeKey(KEY, KEYCODE);
    }

    @Test
    @Skip
    public void releasingKeyShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        char KEY = 'a';
        int KEYCODE = 'a';

        rootView.requestFocus(true);
        rootView.registerCallback((OnKeyReleased) key -> {
            testAssertion.expect(key.getKeyChar()).toBe(KEY);
        });

        context.getInputEmulator().releaseKey(KEY, KEYCODE);
    }

    @Test
    @Skip
    public void pressingKeyShouldGenerateAnEvent(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        char KEY = 'a';
        int KEYCODE = 'a';

        rootView.requestFocus(true);
        rootView.registerCallback((OnKeyPressed) key -> {
            testAssertion.expect(key.getKeyChar()).toBe(KEY);
        });

        context.getInputEmulator().pressKey(KEY, KEYCODE);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new test());
    }
}
