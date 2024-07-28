package uia.core.context;

/**
 * InputEmulator is designed to emulate an input from mouse and from keyboard.
 * <br>
 * It is useful to simulate the interaction between user and a graphical component.
 */

public interface InputEmulator {

    /**
     * Emulates a click on the screen at the specified location.
     *
     * @param x the click position on the x-axis
     * @param y the click position on the y-axis
     * @return this InputEmulator
     */

    InputEmulator clickOn(int x, int y);

    /**
     * Emulates a mouse moving across the screen (without pressing any buttons).
     *
     * @param xStart    the movement starting point on the x-axis
     * @param yStart    the movement starting point on the y-axis
     * @param xEnd      the movement ending point on the x-axis
     * @param yEnd      the movement ending point on the y-axis
     * @param movements the number > 0 of mouse movements to reach the end
     * @param duration  the seconds > 0 needed to complete the movement
     * @return this InputEmulator
     * @throws IllegalArgumentException if {@code movements <= 0} or {@code duration <= 0}
     */

    InputEmulator moveMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd, int movements, float duration);

    /**
     * Emulates a mouse dragging across the screen.
     *
     * @param xStart    the dragging starting point on the x-axis
     * @param yStart    the dragging starting point on the y-axis
     * @param xEnd      the dragging ending point on the x-axis
     * @param yEnd      the dragging ending point on the y-axis
     * @param movements the number > 0 of mouse movements to reach to end
     * @param duration  the seconds > 0 required to complete the movement
     * @return this InputEmulator
     * @throws IllegalArgumentException if {@code movements <= 0} or {@code duration <= 0}
     */

    InputEmulator dragMouseOnScreen(int xStart, int yStart, int xEnd, int yEnd, int movements, float duration);

    /**
     * Emulates a key pressed on keyboard.
     *
     * @param key     the pressed key
     * @param keyCode the corresponding keycode
     * @return this InputEmulator
     */

    InputEmulator pressKey(char key, int keyCode);

    /**
     * Emulates a key released on keyboard.
     *
     * @param key     the released key
     * @param keyCode the corresponding keycode
     * @return this InputEmulator
     */

    InputEmulator releaseKey(char key, int keyCode);

    /**
     * Emulates a key typed on keyboard.
     *
     * @param key     the typed key
     * @param keyCode the corresponding keycode
     * @return this InputEmulator
     */

    InputEmulator typeKey(char key, int keyCode);
}
