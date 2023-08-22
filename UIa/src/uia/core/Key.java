package uia.core;

/**
 * Key representation
 */

public class Key {

    /**
     * ACTION represents the key's action
     */
    public enum ACTION {PRESSED, TYPED, RELEASED}

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
