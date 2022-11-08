package uia.core.platform.implementation;

import uia.core.platform.policy.Pointer;

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.Arrays;

/**
 * Mouse pointer representation
 */

public class PointerAWT implements Pointer {
    private MouseEvent mouseEvent;

    private int action;
    private int wheel;
    private final boolean[] buttonPressed;

    private int xOff;
    private int yOff;

    public PointerAWT() {
        buttonPressed = new boolean[]{false, false, false};
    }

    @Override
    public void setNative(Object o, int action, int xOff, int yOff) {
        if (o instanceof MouseEvent) {
            mouseEvent = (MouseEvent) o;

            this.action = action;
            this.xOff = xOff;
            this.yOff = yOff;

            wheel = 0;

            switch (action) {
                case PRESSED:
                    buttonPressed[mouseEvent.getButton() - 1] = true;
                    break;
                case RELEASED:
                    buttonPressed[mouseEvent.getButton() - 1] = false;
                    break;
                case EXITED:
                    Arrays.fill(buttonPressed, false);
                    consume();
                    break;
                case WHEEL:
                    wheel = ((MouseWheelEvent) mouseEvent).getWheelRotation();
                    break;
            }
        }
    }

    @Override
    public void consume() {
        mouseEvent.consume();
    }

    @Override
    public int getAction() {
        return action;
    }

    @Override
    public int getButton() {
        return mouseEvent.getButton();
    }

    @Override
    public int getX() {
        return mouseEvent.getX() + xOff;
    }

    @Override
    public int getY() {
        return mouseEvent.getY() + yOff;
    }

    @Override
    public int getPressedButtonSize() {
        int count = 0;
        for (boolean i : buttonPressed) {
            if (i) count++;
        }

        return count;
    }

    @Override
    public boolean isConsumed() {
        return mouseEvent.isConsumed();
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
        return mouseEvent;
    }
}
