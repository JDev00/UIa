package uia.core;

/**
 * Key representation
 */

public class Key {

    /**
     * ACTION represents the key's action
     */
    public enum ACTION {PRESSED, TYPED, RELEASED}

    public static final int KEY_CANCEL = 3;
    public static final int KEY_BACKSPACE = 8;
    public static final int KEY_TAB = 9;
    public static final int KEY_ENTER = 10;
    public static final int KEY_SHIFT = 16;
    public static final int KEY_CTRL = 17;
    public static final int KEY_ALT = 18;
    public static final int KEY_CAPS_LOCK = 20;
    public static final int KEY_ESCAPE = 27;
    public static final int KEY_SPACE = 32;
    public static final int KEY_HOME = 36;
    public static final int KEY_LEFT = 37;
    public static final int KEY_UP = 38;
    public static final int KEY_RIGHT = 39;
    public static final int KEY_DOWN = 40;
    public static final int KEY_DELETE = 127;
    public static final int KEY_NUM_LOCK = 144;
    public static final int KEY_INSERT = 155;
    public static final int KEY_WINDOWS = 524;
    public static final int KEY_ALT_GRAPH = 65406;

    public static final int MASK_SHIFT = 1;
    public static final int MASK_CTRL = 2;
    public static final int MASK_META = 4;
    public static final int MASK_ALT = 8;

    public static final int KEY_0 = 48;
    public static final int KEY_9 = 57;
    public static final int KEY_A = 65;
    public static final int KEY_Z = 90;

    private final char keyChar;
    private final int keyCode;
    private final int modifiers;
    private final ACTION action;

    private boolean consumed = false;

    public Key(ACTION action, int modifiers, char keyChar, int keyCode) {
        this.action = action;
        this.modifiers = modifiers;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
    }

    /**
     * Consume this Key
     */

    public void consume() {
        consumed = true;
    }

    /**
     * @return the Key's action
     */

    public ACTION getAction() {
        return action;
    }

    /**
     * @return the Key's modifiers
     */

    public int getModifiers() {
        return modifiers;
    }

    /**
     * @return the Key as a char
     */

    public char getKeyChar() {
        return keyChar;
    }

    /**
     * @return the Key as an integer code
     */

    public int getKeyCode() {
        return keyCode;
    }

    /**
     * @return true if this Key has been consumed
     */

    public boolean isConsumed() {
        return consumed;
    }

    /**
     * Function used to check if two keys have been pressed simultaneously (ie CTRL-C).
     *
     * @param mask    the mask used to compose the special code
     * @param keyCode the keyCode used to compose the special code
     * @return true if the special code has been composed
     */

    public boolean isCode(int mask, int keyCode) {
        return this.keyCode == keyCode && (modifiers & mask) != 0;
    }
}
