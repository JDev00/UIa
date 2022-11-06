package uia.core.platform.policy;

/**
 * ADT used to describe a screen pointer.
 * <br>
 * Note that Pointer primarily acts as a bridge between framework and native platform.
 */

public interface Pointer {
    /**
     * PRESSED state indicates that a pointer is currently pressed
     */
    int PRESSED = 0;

    /**
     * RELEASED state indicates that a pointer has been released
     */
    int RELEASED = 1;

    /**
     * DRAGGED state indicates that a pointer is currently pressed and is moving around
     */
    int DRAGGED = 2;

    /**
     * MOVED state indicates that a pointer isn't currently pressed and is moving around
     */
    int MOVED = 3;

    /**
     * CLICKED state indicates that a pointer has been clicked
     */
    int CLICKED = 4;

    /**
     * EXITED state indicates that a pointer has left the UIa's main window
     */
    int EXITED = 5;

    /**
     * WHEEL state indicates that a pointer is currently wheeling
     */
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
     * @param buttonId a value {@code > 0} used to specify the button
     * @return true if the specified button is pressed
     */

    boolean isButtonPressed(int buttonId);

    /**
     * Note that in touch-screen Context this method will always return {@code false}
     *
     * @return true if a pointer is wheeling
     */

    boolean isWheeling();

    /**
     * @return the native pointer object
     */

    Object getNative();
}
