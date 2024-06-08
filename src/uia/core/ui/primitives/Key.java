package uia.core.ui.primitives;

/**
 * Keyboard key representation
 */

public class Key {

    public enum Action {TYPED, PRESSED, RELEASED}

    private final Action action;
    private final int modifiers;
    private final char keyChar;
    private final int keyCode;
    private boolean consumed = false;

    public Key(Action action, int modifiers, char keyChar, int keyCode) {
        this.action = action;
        this.modifiers = modifiers;
        this.keyCode = keyCode;
        this.keyChar = keyChar;
    }

    @Override
    public String toString() {
        return "Key{" +
                "keyChar=" + keyChar +
                ", keyCode=" + keyCode +
                ", modifiers=" + modifiers +
                ", action=" + action +
                ", consumed=" + consumed +
                '}';
    }

    /**
     * Consumes this Key. A consumed key can't be used again.
     */

    public void consume() {
        consumed = true;
    }

    /**
     * @return the Key action
     */

    public Action getAction() {
        return action;
    }

    /**
     * @return the Key modifiers
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
     * Checks if two keys have been pressed simultaneously (ie CTRL-C).
     *
     * @param mask    the mask used to compose the special code
     * @param keyCode the keyCode used to compose the special code
     * @return true if the special code has been composed
     */

    public boolean isKeystroke(int mask, int keyCode) {
        return this.keyCode == keyCode && (modifiers & mask) != 0;
    }
}
