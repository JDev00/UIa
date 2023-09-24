package uia.core;

/**
 * Touch/Mouse pointer representation
 */

public class ScreenPointer {

    /**
     * Pointer's action
     */
    public enum ACTION {PRESSED, RELEASED, DRAGGED, MOVED, CLICKED, WHEEL, EXITED}

    /**
     * Mouse button representation
     */
    public enum BUTTON {LEFT, CENTER, RIGHT}

    private final ACTION action;
    private final BUTTON button;
    private final int[] desc;
    private boolean consumed = false;

    public ScreenPointer(ACTION action, BUTTON button, int x, int y, int wheel) {
        this.action = action;
        this.button = button;

        desc = new int[]{x, y, wheel};

        if (action == ACTION.EXITED) consumed = true;
    }

    /**
     * Translate this pointer
     *
     * @param x the translation along x-axis
     * @param y the translation along y-axis
     */

    public void translate(int x, int y) {
        desc[0] += x;
        desc[1] += y;
    }

    /**
     * Consume this pointer.
     * <br>
     * A consumed pointer can't be used anymore.
     */

    public void consume() {
        consumed = true;
    }

    /**
     * @return the pointer's action
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
        return desc[0];
    }

    /**
     * @return the pointer position along y-axis
     */

    public int getY() {
        return desc[1];
    }

    /**
     * @return the wheel value
     */

    public int getWheelRotation() {
        return desc[2];
    }

    /**
     * @return true if this pointer has been consumed
     */

    public boolean isConsumed() {
        return consumed;
    }
}
