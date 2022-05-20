package uia.core.platform;

import uia.core.policy.Pointer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

/**
 * A pointer represents a mouse pointer
 */

public class PointerAWT implements Pointer {
    private int action;
    private int wheel;
    private final boolean[] buttonPressed;

    private MouseEvent obj;
    private boolean consumed;

    private int xOff;
    private int yOff;

    public PointerAWT() {
        obj = null;
        consumed = false;
        buttonPressed = new boolean[]{false, false, false};
    }

    @Override
    public void setNative(Object o, int action, int xOff, int yOff) {
        if (o instanceof MouseEvent) {
            obj = (MouseEvent) o;

            this.action = action;
            this.xOff = xOff;
            this.yOff = yOff;

            wheel = 0;

            if (action == PRESSED) {
                buttonPressed[obj.getButton() - 1] = true;
            } else if (action == RELEASED) {
                buttonPressed[obj.getButton() - 1] = false;
            } else if (action == EXITED) {
                Arrays.fill(buttonPressed, false);
                consume();
            } else if (action == WHEEL) {
                wheel = ((MouseWheelEvent) obj).getWheelRotation();
            }

            consumed = false;
        }
    }

    @Override
    public void consume() {
        consumed = true;
    }

    @Override
    public int getAction() {
        return action;
    }

    @Override
    public int getButton() {
        return obj.getButton();
    }

    @Override
    public int getX() {
        return obj.getX() + xOff;
    }

    @Override
    public int getY() {
        return obj.getY() + yOff;
    }

    @Override
    public int getPressedButtonSize() {
        int count = 0;

        for (boolean i : buttonPressed) {

            if (i)
                count++;
        }

        return count;
    }

    @Override
    public boolean isConsumed() {
        return consumed;
    }

    @Override
    public boolean isPressed() {
        return getPressedButtonSize() != 0;
    }

    @Override
    public boolean isButtonPressed(int buttonId) {
        return buttonId >= 0 && buttonId < buttonPressed.length && buttonPressed[buttonId];
    }

    @Override
    public boolean isWheeling() {
        return wheel != 0;
    }

    @Override
    public int getWheelRotation() {
        return wheel;
    }

    @Override
    public Object getNative() {
        return obj;
    }
}
