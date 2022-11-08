package uia.core.platform.policy;

/**
 * Key ADT used to describe a key event.
 * <br>
 * Key acts as a bridge between framework and native key object.
 * If you want to access native key use {@link #getNative()}.
 */

public interface Key {
    /**
     * PRESSED action indicates that this key is actually pressed
     */
    int PRESSED = 0;
    /**
     * RELEASED action indicates that this key has been released
     */
    int RELEASED = 1;
    /**
     * TYPED action indicates that this key has been typed
     */
    int TYPED = 2;

    int KEY_CANCEL = 3;
    int KEY_BACKSPACE = 8;
    int KEY_TAB = 9;
    int KEY_ENTER = 10;
    int KEY_SHIFT = 16;
    int KEY_CTRL = 17;
    int KEY_ALT = 18;
    int KEY_CAPS_LOCK = 20;
    int KEY_ESCAPE = 27;
    int KEY_SPACE = 32;
    int KEY_HOME = 36;
    int KEY_LEFT = 37;
    int KEY_UP = 38;
    int KEY_RIGHT = 39;
    int KEY_DOWN = 40;
    int KEY_DELETE = 127;
    int KEY_NUM_LOCK = 144;
    int KEY_INSERT = 155;
    int KEY_WINDOWS = 524;
    int KEY_ALT_GRAPH = 65406;

    int MASK_SHIFT = 1;
    int MASK_CTRL = 2;
    int MASK_META = 4;
    int MASK_ALT = 8;

    int KEY_0 = 48;
    int KEY_9 = 57;
    int KEY_A = 65;
    int KEY_Z = 90;

    /**
     * Set the object used to handle a key natively on the platform
     *
     * @param o      the native object
     * @param action the action performed by the object
     */

    void setNative(Object o, int action);

    /**
     * Consume this Key. A consumed Key can't be used by Views.
     */

    void consume();

    /**
     * @return the native key object
     */

    Object getNative();

    /**
     * @return the Key as a char
     */

    char getKeyChar();

    /**
     * @return the Key as an integer code
     */

    int getKeyCode();

    /**
     * @return the Key's modifiers
     */

    int getModifiers();

    /**
     * @return the Key's action
     */

    int getAction();

    /**
     * @return true if this Key has been consumed
     */

    boolean isConsumed();

    /**
     * Function used to check if two keys have been pressed simultaneously (ie CTRL-C).
     *
     * @param mask    the mask used to compose the special code
     * @param keyCode the keyCode used to compose the special code
     * @return true if the special code has been composed
     */

    boolean isCode(int mask, int keyCode);
}
