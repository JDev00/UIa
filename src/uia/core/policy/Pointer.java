package uia.core.policy;

/**
 * ADT used to describe a screen pointer.
 * <br>
 * <b>Note that Pointer primarily acts as a bridge between API framework and native platform.</b>
 */

public interface Pointer {
    int PRESSED = 0;
    int RELEASED = 1;
    int DRAGGED = 2;
    int MOVED = 3;
    int CLICKED = 4;
    int EXITED = 5;
    int WHEEL = 6;

    /**
     * Set the object used to handle a pointer natively on the platform.
     * <b>Note that {@link Pointer#isConsumed()} will be set to false.</b>
     *
     * @param o      the native object
     * @param action the action performed by the object
     * @param xOff   a position offset along x-axis
     * @param yOff   a position offset along y-axis
     */

    void setNative(Object o, int action, int xOff, int yOff);

    /**
     * Consume this pointer.
     * <b>A consumed pointer can't be used by Pages or Views.</b>
     */

    void consume();

    /**
     * @return the performed action
     */

    int getAction();

    /**
     * <b>Note that in touch-screen Context this method will always return 0</b>
     *
     * @return the button used to perform the action
     */

    int getButton();

    /**
     * <b>Note that in touch-screen Context this method will return 0</b>
     *
     * @return the number of pressed buttons
     */

    int getPressedButtonSize();

    /**
     * <b>Note that in touch-screen Context this method will always return 0</b>
     *
     * @return the mouse wheel value
     */

    int getWheelRotation();

    /**
     * @return the pointer position along x-axis
     */

    int getX();

    /**
     * @return the pointer position along y-axis
     */

    int getY();

    /**
     * @return true if this pointer has been consumed
     */

    boolean isConsumed();

    /**
     * @return true if this pointer is pressed
     */

    boolean isPressed();

    /**
     * <b>Note that in touch-screen Context this method will always return {@code false}</b>
     *
     * @param buttonId a value {@code > 0} used to specify the button
     * @return true if the specified button is pressed
     */

    boolean isButtonPressed(int buttonId);

    /**
     * <b>Note that in touch-screen Context this method will always return {@code false}</b>
     *
     * @return true mouse wheel is used
     */

    boolean isWheeling();

    /**
     * @return the native object
     */

    Object getNative();
}
