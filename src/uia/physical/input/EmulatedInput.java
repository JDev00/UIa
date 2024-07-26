package uia.physical.input;

import uia.physical.message.MessageFactory;
import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.message.Message;
import uia.core.context.InputEmulator;
import uia.core.ui.primitives.Key;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * UIa standard {@link InputEmulator} implementation.
 */

public final class EmulatedInput implements InputEmulator {
    private final Consumer<Message> generatedInputReader;

    public EmulatedInput(Consumer<Message> generatedInputReader) {
        this.generatedInputReader = generatedInputReader;
    }

    /**
     * Helper function. Generate a new {@link ScreenTouch} object.
     */

    private void createScreenTouch(ScreenTouch.Action action, int x, int y) {
        int wheelRotation = 0;
        // creates the screenTouch object
        ScreenTouch screenTouch = new ScreenTouch(action, null, x, y, wheelRotation);
        // creates the corresponding message
        Message screenTouchMessage = MessageFactory.create(screenTouch, null);
        // dispatch it
        generatedInputReader.accept(screenTouchMessage);
    }

    /**
     * Helper function. Creates a new sequence of {@link ScreenTouch}s.
     */

    private void createScreenTouchSequence(ScreenTouch.Action action,
                                           int xStart, int yStart, int xEnd, int yEnd,
                                           int interactions, float duration) {
        int period = (int) (1_000 * duration) / interactions;
        int[] interactionNumber = {0};

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            int counter = interactionNumber[0];
            int touchX = xStart + counter * (xEnd - xStart) / interactions;
            int touchY = yStart + counter * (yEnd - yStart) / interactions;
            createScreenTouch(action, touchX, touchY);

            // increases the interactions counter
            interactionNumber[0]++;

            // terminates this executor if all interactions have been generated
            if (interactionNumber[0] == interactions) {
                executor.shutdownNow();
            }
        }, 0, period, TimeUnit.MILLISECONDS);
    }

    /**
     * Helper function. Creates a new {@link Key}.
     */

    private void generateKey(Key.Action action, char keyChar, int keyCode) {
        int keyModifiers = 0;
        // creates the key object
        Key key = new Key(action, keyModifiers, keyChar, keyCode);
        // creates the corresponding message
        Message keyMessage = MessageFactory.create(key, null);
        // dispatch the key message
        generatedInputReader.accept(keyMessage);
    }

    @Override
    public InputEmulator clickOn(int x, int y) {
        createScreenTouch(ScreenTouch.Action.CLICKED, x, y);
        return this;
    }

    @Override
    public InputEmulator moveMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd,
                                           int movements, float duration) {
        if (movements <= 0) {
            throw new IllegalArgumentException("the number of mouse 'movements' must be greater than zero");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("the duration of the movement must be greater than zero");
        }

        createScreenTouchSequence(ScreenTouch.Action.MOVED, xStart, yStart, xEnd, yEnd, movements, duration);
        return this;
    }

    @Override
    public InputEmulator dragMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd,
                                           int movements, float duration) {
        if (movements <= 0) {
            throw new IllegalArgumentException("the number of mouse 'movements' must be greater than zero");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("the duration of the movement must be greater than zero");
        }

        createScreenTouchSequence(ScreenTouch.Action.DRAGGED, xStart, yStart, xEnd, yEnd, movements, duration);
        return this;
    }

    @Override
    public InputEmulator pressKey(char key, int keyCode) {
        generateKey(Key.Action.PRESSED, key, keyCode);
        return this;
    }

    @Override
    public InputEmulator releaseKey(char key, int keyCode) {
        generateKey(Key.Action.RELEASED, key, keyCode);
        return this;
    }

    @Override
    public InputEmulator typeKey(char key, int keyCode) {
        generateKey(Key.Action.TYPED, key, keyCode);
        return this;
    }
}
