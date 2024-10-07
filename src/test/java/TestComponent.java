import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uia.application.message.store.ConcreteMessageStore;
import uia.application.message.store.GlobalMessageStore;
import uia.core.basement.Collidable;
import uia.core.context.Context;
import uia.core.ui.callbacks.*;
import uia.core.ui.View;

import static utility.TestUtility.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Component unit tests.
 */

class TestComponent {
    GlobalMessageStore globalMessageStore = GlobalMessageStore.getInstance();
    Context context;
    View rootView;

    @BeforeEach
    public void beforeEach() {
        rootView = createRoot();

        context = createMockContext();
        context.setView(rootView);
    }

    @AfterEach
    public void afterEach() {
        // pauses the current context
        context.setLifecycleStage(Context.LifecycleStage.PAUSED);
        context.getWindow().setVisible(false);
        // waits for the context to pause itself
        waitFor(250);
        // mounts a new store
        globalMessageStore.mount(new ConcreteMessageStore());
    }

    @Test
    void viewBoundsWidthAndHeightShouldBeDifferentAfterARotation() {
        // setup
        float expectedRotation = 2.145f;
        rootView.getStyle().setRotation(expectedRotation);
        waitFor(100);

        // verify
        float[] bounds = rootView.getBounds();
        float width = rootView.getWidth();
        float height = rootView.getHeight();
        float rotation = bounds[4];

        float expectedBoundsWidth = Collidable.colliderWidth(width, height, rotation);
        float expectedBoundsHeight = Collidable.colliderHeight(width, height, rotation);

        assertEquals(expectedRotation, rotation);
        assertEquals(expectedBoundsWidth, bounds[2]);
        assertEquals(expectedBoundsHeight, bounds[3]);
    }

    @Test
    void viewWidthAndHeightShouldNotChangeAfterARotation() {
        // setup
        float rotation = -5.141f;
        rootView.getStyle().setRotation(rotation);
        waitFor(100);

        // verify
        float widthPreRotation = 702;
        float heightPreRotation = 493;
        float componentWidth = rootView.getWidth();
        float componentHeight = rootView.getHeight();
        float componentRotation = rootView.getBounds()[4];

        assertEquals(rotation, componentRotation);
        assertEquals(widthPreRotation, componentWidth);
        assertEquals(heightPreRotation, componentHeight);
    }

    @Test
    void clickingOnViewShouldGenerateAnEvent() {
        // setup
        int[] countAssertions = {0};
        rootView.registerCallback((OnClick) touches -> countAssertions[0]++);

        // act
        waitFor(50);
        context.getInputEmulator().clickOn(100, 100);

        // verify
        waitFor(50);
        assertEquals(1, countAssertions[0]);
    }

    @Disabled("")
    @Test
    void mouseOnViewShouldGenerateAnEvent() {
        int[] countAssertions = {0};

        // verify
        rootView.registerCallback((OnMouseHover) touches -> countAssertions[0]++);

        // act
        context.getInputEmulator().moveMouseOnScreen(
                100, 100,
                200, 100,
                2, 0.1f
        );

        // verify
        waitFor(250);
        assertEquals(1, countAssertions[0]);
    }

    @Disabled("")
    @Test
    void mouseEnteringViewShouldGenerateAnEvent() {
        int[] countAssertions = {0};

        // setup
        rootView.registerCallback((OnMouseEnter) touches -> countAssertions[0]++);

        // act
        context.getInputEmulator().moveMouseOnScreen(
                0, 200,
                10, 200,
                10, 0.1f
        );

        // verify
        waitFor(250);
        assertEquals(1, countAssertions[0]);
    }

    @Disabled("")
    @Test
    void mouseExitingViewShouldGenerateAnEvent() {
        int[] countAssertions = {0};

        // setup
        float viewWidth = 0.5f;
        float viewHeight = 0.5f;
        rootView.getStyle().setDimension(viewWidth, viewHeight);
        rootView.registerCallback((OnMouseExit) touches -> countAssertions[0]++);

        // act
        int viewportWidth = context.getWindow().getViewportWidth();
        int viewportHeight = context.getWindow().getViewportHeight();
        int[] mouseStart = {
                (int) (viewWidth * viewportWidth),
                (int) (viewHeight * viewportHeight)
        };
        int[] mouseEnd = {viewportWidth, mouseStart[1]};
        context.getInputEmulator().moveMouseOnScreen(
                mouseStart[0], mouseStart[1],
                mouseEnd[0], mouseEnd[1],
                3, 0.1f
        );

        // verify
        waitFor(250);
        assertEquals(1, countAssertions[0]);
    }

    @Disabled("")
    @Test
    void typingKeyShouldGenerateAnEvent() {
        int[] countAssertions = {0};
        int KEYCODE = 'a';
        char KEY = 'a';

        // setup
        rootView.requestFocus(true);
        rootView.registerCallback((OnKeyTyped) key -> {
            if (KEY == key.getKeyChar()) {
                countAssertions[0]++;
            }
        });

        // act
        context.getInputEmulator().typeKey(KEY, KEYCODE);

        // verify
        waitFor(250);
        assertEquals(1, countAssertions[0]);
    }

    @Disabled("")
    @Test
    void releasingKeyShouldGenerateAnEvent() {
        int[] countAssertions = {0};
        int KEYCODE = 'a';
        char KEY = 'a';

        // setup
        rootView.requestFocus(true);
        rootView.registerCallback((OnKeyReleased) key -> {
            if (KEY == key.getKeyChar()) {
                countAssertions[0]++;
            }
        });

        // act
        context.getInputEmulator().releaseKey(KEY, KEYCODE);

        // verify
        waitFor(250);
        assertEquals(1, countAssertions[0]);
    }

    @Disabled("")
    @Test
    void pressingKeyShouldGenerateAnEvent() {
        int[] countAssertions = {0};
        int KEYCODE = 'a';
        char KEY = 'a';

        // setup
        rootView.requestFocus(true);
        rootView.registerCallback((OnKeyPressed) key -> {
            if (KEY == key.getKeyChar()) {
                countAssertions[0]++;
            }
        });

        // act
        context.getInputEmulator().pressKey(KEY, KEYCODE);

        // verify
        waitFor(250);
        assertEquals(1, countAssertions[0]);
    }
}
