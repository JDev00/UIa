package uia.core;

/**
 * Screen pointer representation
 */

public class Pointer {
    /**
     * ACTION represents the pointer's action
     */
    public enum ACTION {PRESSED, RELEASED, DRAGGED, MOVED, CLICKED, WHEEL, EXITED}

    /**
     * BUTTON state
     */
    public enum BUTTON {LEFT, CENTER, RIGHT}

    private final ACTION action;
    private final BUTTON button;
    private final int wheel;
    private int x;
    private int y;
    private boolean consumed = false;

    public Pointer(ACTION action, BUTTON button, int x, int y, int wheel) {
        this.action = action;
        this.button = button;
        this.x = x;
        this.y = y;
        this.wheel = wheel;

        if (action == ACTION.EXITED) consumed = true;
    }

    /**
     * Translate this pointer
     *
     * @param x the translation along x-axis
     * @param y the translation along y-axis
     */

    public void translate(int x, int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Consume this pointer
     */

    public void consume() {
        consumed = true;
    }

    /**
     * @return the performed action
     */

    public ACTION getAction() {
        return action;
    }

    /**
     * @return the button used to perform the action; it could be null
     */

    public BUTTON getButton() {
        return button;
    }

    /**
     * @return the pointer position along x-axis
     */

    public int getX() {
        return x;
    }

    /**
     * @return the pointer position along y-axis
     */

    public int getY() {
        return y;
    }

    /**
     * @return the wheel value
     */

    public int getWheelRotation() {
        return wheel;
    }

    /**
     * @return true if this pointer has been consumed
     */

    public boolean isConsumed() {
        return consumed;
    }
}
