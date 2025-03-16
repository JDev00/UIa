package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import uia.application.message.MessageFactory;
import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.message.Message;
import uia.core.basement.Collidable;
import uia.core.ui.primitives.Key;
import uia.core.ui.callbacks.*;
import uia.core.ui.View;

import static utility.TestUtility.*;
import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {
    View rootView;

    @BeforeEach
    void beforeEach() {
        rootView = createRoot();
        rootView.requestFocus(true);
        updateView(1000, 1000, rootView);
    }

    @Disabled
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

    @Disabled
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
        Message message = MessageFactory.create(
                new ScreenTouch(ScreenTouch.Action.CLICKED, ScreenTouch.Button.RIGHT, 100, 100, 0),
                null);
        rootView.readMessage(message);

        // verify
        assertEquals(1, countAssertions[0]);
    }

    @Test
    void mouseOnViewShouldGenerateAnEvent() {
        int[] countAssertions = {0};

        // verify
        rootView.registerCallback((OnMouseHover) touches -> countAssertions[0]++);

        // act
        Message[] messages = {
                MessageFactory.create(new ScreenTouch(ScreenTouch.Action.MOVED, null, 100, 100, 0), null),
                MessageFactory.create(new ScreenTouch(ScreenTouch.Action.MOVED, null, 100, 100, 0), null)
        };
        rootView.readMessage(messages[0]);
        rootView.readMessage(messages[1]);

        // verify
        assertEquals(1, countAssertions[0]);
    }

    @Test
    void mouseEnteringViewShouldGenerateAnEvent() {
        int[] countAssertions = {0};

        // setup
        rootView.registerCallback((OnMouseEnter) touches -> countAssertions[0]++);

        // act
        Message message = MessageFactory.create(
                new ScreenTouch(ScreenTouch.Action.MOVED, ScreenTouch.Button.RIGHT, 100, 100, 0),
                null);
        rootView.readMessage(message);

        // verify
        assertEquals(1, countAssertions[0]);
    }

    @Test
    void mouseExitingViewShouldGenerateAnEvent() {
        int[] countAssertions = {0};

        // setup
        rootView.registerCallback((OnMouseExit) touches -> countAssertions[0]++);

        // act
        Message[] messages = {
                MessageFactory.create(new ScreenTouch(ScreenTouch.Action.MOVED, ScreenTouch.Button.RIGHT, 100, 100, 0), null),
                MessageFactory.create(new ScreenTouch(ScreenTouch.Action.MOVED, ScreenTouch.Button.RIGHT, 10_000, 100, 0), null)
        };
        rootView.readMessage(messages[0]);
        rootView.readMessage(messages[1]);

        // verify
        assertEquals(1, countAssertions[0]);
    }

    @Test
    void typingKeyShouldGenerateAnEvent() {
        int[] countAssertions = {0};
        int keycode = 'a';
        char key = 'a';

        // setup
        rootView.registerCallback((OnKeyTyped) receivedKey -> {
            if (key == receivedKey.getKeyChar()) {
                countAssertions[0]++;
            }
        });

        // act
        Message message = MessageFactory.create(
                new Key(Key.Action.TYPED, 0, key, keycode),
                null);
        rootView.readMessage(message);

        // verify
        assertEquals(1, countAssertions[0]);
    }

    @Test
    void releasingKeyShouldGenerateAnEvent() {
        int[] countAssertions = {0};
        int keycode = 'a';
        char key = 'a';

        // setup
        rootView.registerCallback((OnKeyReleased) receivedKey -> {
            if (key == receivedKey.getKeyChar()) {
                countAssertions[0]++;
            }
        });

        // act
        Message message = MessageFactory.create(
                new Key(Key.Action.RELEASED, 0, key, keycode),
                null);
        rootView.readMessage(message);

        // verify
        assertEquals(1, countAssertions[0]);
    }

    @Test
    void pressingKeyShouldGenerateAnEvent() {
        int[] countAssertions = {0};
        int keycode = 'a';
        char key = 'a';

        // setup
        rootView.registerCallback((OnKeyPressed) receivedKey -> {
            if (key == receivedKey.getKeyChar()) {
                countAssertions[0]++;
            }
        });

        // act
        Message message = MessageFactory.create(
                new Key(Key.Action.PRESSED, 0, key, keycode),
                null);
        rootView.readMessage(message);

        // verify
        assertEquals(1, countAssertions[0]);
    }
}
