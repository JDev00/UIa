package uia.core;

import java.util.Arrays;
import java.util.Objects;

/**
 * Mouse pointer or Touch (on mobile context) representation
 */

public class ScreenTouch {
    public enum Action {PRESSED, RELEASED, DRAGGED, MOVED, CLICKED, WHEEL, EXITED}

    public enum Button {LEFT, CENTER, RIGHT}

    private final Action action;
    private final Button button;
    private final int[] desc;
    private boolean consumed = false;

    public ScreenTouch(Action action, Button button, int x, int y, int wheel) {
        desc = new int[]{x, y, wheel};

        this.action = action;
        this.button = button;
        if (action == Action.EXITED) {
            consumed = true;
        }
    }

    @Override
    public String toString() {
        return "ScreenTouch{" +
                "action=" + action +
                ", button=" + button +
                ", data=" + Arrays.toString(desc) +
                ", consumed=" + consumed +
                '}';
    }

    /**
     * Translates this ScreenTouch
     *
     * @param x the translation on x-axis
     * @param y the translation on y-axis
     */

    public void translate(int x, int y) {
        desc[0] += x;
        desc[1] += y;
    }

    /**
     * @return the position on x-axis
     */

    public int getX() {
        return desc[0];
    }

    /**
     * @return the position on y-axis
     */

    public int getY() {
        return desc[1];
    }

    /**
     * @return the scrolling value
     */

    public int getWheelRotation() {
        return desc[2];
    }

    /**
     * Consumes this ScreenTouch. A consumed ScreenTouch can't be used again.
     */

    public void consume() {
        consumed = true;
    }

    /**
     * @return true if it has been consumed
     */

    public boolean isConsumed() {
        return consumed;
    }

    /**
     * @return the performed action
     */

    public Action getAction() {
        return action;
    }

    /**
     * @return the button used to perform the action; it could be null
     */

    public Button getButton() {
        return button;
    }

    /**
     * Copy this ScreenTouch
     *
     * @return a new ScreenTouch populated with the same attributes of the current one
     */

    public ScreenTouch copy() {
        return new ScreenTouch(action, button, desc[0], desc[1], desc[2]);
    }

    /**
     * Checks if the specified ScreenTouch made the specified Action
     *
     * @param screenTouch a not null {@link ScreenTouch}
     * @param action      a not null {@link Action}
     * @return true if the specified ScreenTouch made the specified Action
     * @throws NullPointerException if {@code screenTouch == null or action == null}
     */

    public static boolean madeAction(ScreenTouch screenTouch, Action action) {
        Objects.requireNonNull(screenTouch);
        Objects.requireNonNull(action);
        return screenTouch.getAction().equals(action);
    }
}
