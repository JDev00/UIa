package uia.core.context;

/**
 * InputEmulator is designed to emulate an input from mouse and from keyboard.
 * <br>
 * It is useful to simulate the interaction between user and a graphical component.
 */

public interface InputEmulator {

    /**
     * Emulate a click on screen at the specified position
     *
     * @param x the click position on the x-axis
     * @param y the click position on the y-axis
     * @return this InputEmulator
     */

    InputEmulator clickOn(int x, int y);

    /**
     * Emulate a mouse moving (without pressing buttons) on screen.
     *
     * @param xStart    the movement starting point on the x-axis
     * @param yStart    the movement starting point on the y-axis
     * @param xEnd      the movement ending point on the x-axis
     * @param yEnd      the movement ending point on the y-axis
     * @param movements the number > 0 of mouse movements to reach to end
     * @param duration  the time > 0 required to complete the movement in seconds
     * @return this InputEmulator
     */

    InputEmulator moveMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd, int movements, float duration);

    /**
     * Emulate a mouse dragging on screen.
     *
     * @param xStart    the dragging starting point on the x-axis
     * @param yStart    the dragging starting point on the y-axis
     * @param xEnd      the dragging ending point on the x-axis
     * @param yEnd      the dragging ending point on the y-axis
     * @param movements the number > 0 of mouse movements to reach to end
     * @param duration  the time > 0 required to complete the movement in seconds
     * @return this InputEmulator
     */

    InputEmulator dragMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd, int movements, float duration);

    /**
     * Emulate a key pressed on keyboard
     *
     * @param key     the pressed key
     * @param keyCode the corresponding keycode
     * @return this InputEmulator
     */

    InputEmulator pressKey(char key, int keyCode);

    /**
     * Emulate a key released on keyboard
     *
     * @param key     the released key
     * @param keyCode the corresponding keycode
     * @return this InputEmulator
     */

    InputEmulator releaseKey(char key, int keyCode);

    /**
     * Emulate a key typed on keyboard
     *
     * @param key     the typed key
     * @param keyCode the corresponding keycode
     * @return this InputEmulator
     */

    InputEmulator typeKey(char key, int keyCode);
}
