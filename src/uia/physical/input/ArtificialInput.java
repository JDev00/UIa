package uia.physical.input;

import uia.physical.message.MessageFactory;
import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.message.Message;
import uia.core.context.InputEmulator;
import uia.core.ui.primitives.Key;

import java.util.function.Consumer;

/**
 * Concrete {@link InputEmulator} implementation.
 */

public final class ArtificialInput implements InputEmulator {
    private final Consumer<Message> eventMessageReader;

    public ArtificialInput(Consumer<Message> eventMessageReader) {
        this.eventMessageReader = eventMessageReader;
    }

    /**
     * Helper function. Generate a new {@link ScreenTouch} object.
     */

    private void generateScreenPointer(ScreenTouch.Action action, ScreenTouch.Button button,
                                       int x, int y, int wheelRotation) {
        // creates the screenTouch object
        ScreenTouch screenTouch = new ScreenTouch(action, button, x, y, wheelRotation);
        // creates the corresponding message
        Message screenTouchMessage = MessageFactory.create(screenTouch, null);
        // dispatch it
        eventMessageReader.accept(screenTouchMessage);
    }

    /**
     * Helper function. Generate a new sequence of {@link ScreenTouch}s.
     */

    private void generateScreenTouchesSequence(ScreenTouch.Action action, ScreenTouch.Button button,
                                               int xStart, int yStart, int xEnd, int yEnd,
                                               int interactions, float duration) {
        int sleepMillis = (int) (1000 * duration) / interactions;

        new Thread(() -> {
            for (int i = 0; i <= interactions; i++) {
                int x = xStart + i * (xEnd - xStart) / interactions;
                int y = yStart + i * (yEnd - yStart) / interactions;
                generateScreenPointer(action, button, x, y, 0);

                if (sleepMillis > 0) {
                    try {
                        Thread.sleep(sleepMillis);
                    } catch (Exception ignored) {
                    }
                }
            }
        }).start();
    }

    /**
     * Helper function. Generate a new {@link Key}.
     */

    private void generateKey(Key.Action action, int modifiers, char keyChar, int keyCode) {
        // creates the key object
        Key key = new Key(action, modifiers, keyChar, keyCode);
        // creates the corresponding message
        Message keyMessage = MessageFactory.create(key, null);
        // dispatch it
        eventMessageReader.accept(keyMessage);
    }

    @Override
    public InputEmulator clickOn(int x, int y) {
        generateScreenPointer(ScreenTouch.Action.CLICKED, null, x, y, 0);
        return this;
    }

    @Override
    public InputEmulator moveMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd, int movements, float duration) {
        generateScreenTouchesSequence(ScreenTouch.Action.MOVED, null,
                xStart, yStart, xEnd, yEnd, movements, duration);
        return this;
    }

    @Override
    public InputEmulator dragMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd, int movements, float duration) {
        generateScreenTouchesSequence(ScreenTouch.Action.DRAGGED, null,
                xStart, yStart, xEnd, yEnd, movements, duration);
        return this;
    }

    @Override
    public InputEmulator pressKey(char key, int keyCode) {
        generateKey(Key.Action.PRESSED, 0, key, keyCode);
        return this;
    }

    @Override
    public InputEmulator releaseKey(char key, int keyCode) {
        generateKey(Key.Action.RELEASED, 0, key, keyCode);
        return this;
    }

    @Override
    public InputEmulator typeKey(char key, int keyCode) {
        generateKey(Key.Action.TYPED, 0, key, keyCode);
        return this;
    }
}
