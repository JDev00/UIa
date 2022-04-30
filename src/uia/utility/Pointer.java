package uia.core.utility;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

/**
 * A pointer represents a mouse pointer
 */

// @Test
public class Pointer {
    private int action;
    private int wheel;
    private final boolean[] buttonPressed;

    private MouseEvent obj;
    private boolean consumed;

    private int xOff;
    private int yOff;

    public Pointer() {
        obj = null;
        consumed = false;
        buttonPressed = new boolean[]{false, false, false};
    }

    public void set(Object o, int action,
                    int xOff, int yOff) {
        if (o instanceof MouseEvent) {
            obj = (MouseEvent) o;

            this.action = action;
            this.xOff = xOff;
            this.yOff = yOff;

            wheel = 0;

            if (action == MouseEvent.MOUSE_PRESSED) {
                buttonPressed[obj.getButton() - 1] = true;
            } else if (action == MouseEvent.MOUSE_RELEASED) {
                buttonPressed[obj.getButton() - 1] = false;
            } else if (action == MouseEvent.MOUSE_EXITED) {
                Arrays.fill(buttonPressed, false);
                consume();
            } else if (action == MouseEvent.MOUSE_WHEEL) {
                wheel = ((MouseWheelEvent) obj).getWheelRotation();
            }

            consumed = false;
        }
    }

    public void consume() {
        consumed = true;
    }

    /*
     * State
     */

    public Object getNative() {
        return obj;
    }

    public boolean isConsumed() {
        return consumed;
    }

    /*
     * Basic
     */

    public int getAction() {
        return action;
    }

    public int getButton() {
        return obj.getButton();
    }

    public int getX() {
        return obj.getX() + xOff;
    }

    public int getY() {
        return obj.getY() + yOff;
    }

    /*
     * Buttons
     */

    public int getPressedButtonSize() {
        int count = 0;

        for (boolean i : buttonPressed) {
            if (i) count++;
        }

        return count;
    }

    public boolean isPressed() {
        return getPressedButtonSize() != 0;
    }

    public boolean isButtonPressed(int buttonId) {
        return buttonId >= 0 && buttonId < buttonPressed.length && buttonPressed[buttonId];
    }

    /*
     * Wheel
     */

    public boolean isWheeling() {
        return wheel != 0;
    }

    public int getWheelRotation() {
        return wheel;
    }
}
