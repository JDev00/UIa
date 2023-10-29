package uia.physical.input;

import uia.core.Key;
import uia.core.ScreenPointer;
import uia.core.ui.context.InputEmulator;

import java.util.function.Consumer;

/**
 * Built-in {@link InputEmulator} implementation.
 */

public final class ArtificialInput implements InputEmulator {
    /*private final SynchronousQueue<ScreenPointer> screenPointersQueue;
    private final SynchronousQueue<Key> keyQueue;*/

    private final Consumer<ScreenPointer> readScreenPointer;
    private final Consumer<Key> readKey;

    public ArtificialInput(Consumer<ScreenPointer> readGeneratedScreenPointer,
                           Consumer<Key> readGeneratedKey) {
        readScreenPointer = readGeneratedScreenPointer;
        readKey = readGeneratedKey;

        /*screenPointersQueue = new SynchronousQueue<>();

        ScheduledExecutorService screenPointersInputExecutor = Executors.newSingleThreadScheduledExecutor();
        screenPointersInputExecutor.scheduleAtFixedRate(() -> {
            try {
                ScreenPointer result = screenPointersQueue.poll(50, TimeUnit.MILLISECONDS);
                readGeneratedScreenPointer.accept(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.MILLISECONDS);

        keyQueue = new SynchronousQueue<>();

        ScheduledExecutorService keyInputExecutor = Executors.newSingleThreadScheduledExecutor();
        keyInputExecutor.scheduleAtFixedRate(() -> {
            try {
                Key result = keyQueue.poll(50, TimeUnit.MILLISECONDS);
                readGeneratedKey.accept(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, 0, 10, TimeUnit.MILLISECONDS);*/
    }

    /**
     * Helper function. Put a new {@link ScreenPointer} on the dedicated queue.
     */

    private void putScreenPointer(ScreenPointer.ACTION action, ScreenPointer.BUTTON button,
                                  int x, int y, int wheelRotation) {
        ScreenPointer result = new ScreenPointer(action, button, x, y, wheelRotation);
        readScreenPointer.accept(result);
        /*try {
            screenPointersQueue.put(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Helper function. Put a new sequence of {@link ScreenPointer}s on the dedicated queue.
     */

    private void putScreenPointerSequence(ScreenPointer.ACTION action, ScreenPointer.BUTTON button,
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

    private void putKey(Key.ACTION action, int modifiers, char keyChar, int keyCode) {
        Key result = new Key(action, modifiers, keyChar, keyCode);
        readKey.accept(result);
        /*try {
            keyQueue.put(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    public InputEmulator clickOn(int x, int y) {
        putScreenPointer(ScreenPointer.ACTION.CLICKED, null, x, y, 0);
        return this;
    }

    @Override
    public InputEmulator moveMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd, int movements, float duration) {
        putScreenPointerSequence(ScreenPointer.ACTION.MOVED, null,
                xStart, yStart, xEnd, yEnd, movements, duration);
        return this;
    }

    @Override
    public InputEmulator dragMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd, int movements, float duration) {
        putScreenPointerSequence(ScreenPointer.ACTION.DRAGGED, null,
                xStart, yStart, xEnd, yEnd, movements, duration);
        return this;
    }

    @Override
    public InputEmulator pressKey(char key, int keyCode) {
        putKey(Key.ACTION.PRESSED, 0, key, keyCode);
        return this;
    }

    @Override
    public InputEmulator releaseKey(char key, int keyCode) {
        putKey(Key.ACTION.RELEASED, 0, key, keyCode);
        return this;
    }

    @Override
    public InputEmulator typeKey(char key, int keyCode) {
        putKey(Key.ACTION.TYPED, 0, key, keyCode);
        return this;
    }
}
