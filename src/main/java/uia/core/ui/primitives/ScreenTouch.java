package uia.core.ui.primitives;

import java.util.Objects;

/**
 * UIa mouse pointer or touch (on mobile context) abstraction.
 */

public class ScreenTouch {
    public enum Action {PRESSED, RELEASED, DRAGGED, MOVED, CLICKED, WHEEL, EXITED}

    public enum Button {LEFT, CENTER, RIGHT}

    private final Action action;
    private final Button button;
    private final int x;
    private final int y;
    private final int wheel;
    private boolean consumed = false;

    public ScreenTouch(Action action, Button button, int x, int y, int wheel) {
        this.x = x;
        this.y = y;
        this.wheel = wheel;

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
                ", x=" + x +
                ", y=" + y +
                ", wheel=" + wheel +
                ", consumed=" + consumed +
                '}';
    }

    /**
     * @return the position on the x-axis
     */

    public int getX() {
        return x;
    }

    /**
     * @return the position on the y-axis
     */

    public int getY() {
        return y;
    }

    /**
     * @return mouse wheel rotation
     */

    public int getWheelRotation() {
        return wheel;
    }

    /**
     * Consumes this ScreenTouch and makes it unusable.
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
     * Copies the specified ScreenTouch.
     *
     * @param screenTouch a screenTouch to be copied
     * @param translateX  the translation on the x-axis of the copied screenTouch
     * @param translateY  the translation on the y-axis of the copied screenTouch
     * @return a new ScreenTouch populated with the same attributes of the given one
     */

    public static ScreenTouch copy(ScreenTouch screenTouch, int translateX, int translateY) {
        return new ScreenTouch(
                screenTouch.getAction(),
                screenTouch.getButton(),
                screenTouch.getX() + translateX,
                screenTouch.getY() + translateY,
                screenTouch.getWheelRotation());
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
