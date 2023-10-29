package uia.physical.input;

import uia.core.Key;
import uia.core.ScreenTouch;
import uia.core.ui.context.InputEmulator;

import java.util.function.Consumer;

/**
 * Built-in {@link InputEmulator} implementation.
 */

public final class ArtificialInput implements InputEmulator {
    private final Consumer<ScreenTouch> readScreenTouches;
    private final Consumer<Key> readKey;

    public ArtificialInput(Consumer<ScreenTouch> readGeneratedScreenTouches,
                           Consumer<Key> readGeneratedKey) {
        readScreenTouches = readGeneratedScreenTouches;
        readKey = readGeneratedKey;
    }

    /**
     * Helper function. Put a new {@link ScreenTouch} on the dedicated queue.
     */

    private void putScreenPointer(ScreenTouch.Action action, ScreenTouch.Button button,
                                  int x, int y, int wheelRotation) {
        ScreenTouch result = new ScreenTouch(action, button, x, y, wheelRotation);
        readScreenTouches.accept(result);
    }

    /**
     * Helper function. Put a new sequence of {@link ScreenTouch}s on the dedicated queue.
     */

    private void putScreenTouchesSequence(ScreenTouch.Action action, ScreenTouch.Button button,
                                          int xStart, int yStart, int xEnd, int yEnd,
                                          int interactions, float duration) {
        int sleepMillis = (int) (1000 * duration) / interactions;

        new Thread(() -> {
            for (int i = 0; i <= interactions; i++) {
                int x = xStart + i * (xEnd - xStart) / interactions;
                int y = yStart + i * (yEnd - yStart) / interactions;
                putScreenPointer(action, button, x, y, 0);

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
     * Helper function. Put a new {@link Key} on the dedicated queue.
     */

    private void putKey(Key.Action action, int modifiers, char keyChar, int keyCode) {
        Key result = new Key(action, modifiers, keyChar, keyCode);
        readKey.accept(result);
    }

    @Override
    public InputEmulator clickOn(int x, int y) {
        putScreenPointer(ScreenTouch.Action.CLICKED, null, x, y, 0);
        return this;
    }

    @Override
    public InputEmulator moveMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd, int movements, float duration) {
        putScreenTouchesSequence(ScreenTouch.Action.MOVED, null,
                xStart, yStart, xEnd, yEnd, movements, duration);
        return this;
    }

    @Override
    public InputEmulator dragMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd, int movements, float duration) {
        putScreenTouchesSequence(ScreenTouch.Action.DRAGGED, null,
                xStart, yStart, xEnd, yEnd, movements, duration);
        return this;
    }

    @Override
    public InputEmulator pressKey(char key, int keyCode) {
        putKey(Key.Action.PRESSED, 0, key, keyCode);
        return this;
    }

    @Override
    public InputEmulator releaseKey(char key, int keyCode) {
        putKey(Key.Action.RELEASED, 0, key, keyCode);
        return this;
    }

    @Override
    public InputEmulator typeKey(char key, int keyCode) {
        putKey(Key.Action.TYPED, 0, key, keyCode);
        return this;
    }
}
