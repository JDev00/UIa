package uia.core.platform.policy;

/**
 * Key ADT used to describe a key event.
 * <br>
 * Note that Key acts as a bridge between framework and native platform.
 */

public interface Key {

    /**
     * Set the object used to handle a key natively on the platform.
     * <b>Note that {@link Key#isConsumed()} will be set to false.</b>
     *
     * @param o      the native object
     * @param action the action performed by the object
     */

    void setNative(Object o, int action);

    /**
     * Consume this Key.
     * <b>A consumed Key can't be used by Pages or Views.</b>
     */

    void consume();

    /**
     * @return the handled key char
     */

    char getKeyChar();

    /**
     * @return the handled key code
     */

    int getKeyCode();

    /**
     * @return the handled modifiers
     */

    int getModifiers();

    /**
     * @return the performed action
     */

    int getAction();

    /**
     * @return true if this Key has been consumed
     */

    boolean isConsumed();

    /**
     * Return true if user composed a code for select the entire text.
     * Note that this function is platform dependent, as consequence every platform has its own implementation.
     *
     * @return true if text selection code has been composed
     */

    boolean codeTextSelectionAll();

    /**
     * Return true if user composed a code for increase text selection.
     * Note that this function is platform dependent, as consequence every platform has its own implementation.
     *
     * @return true if right text selection code has been composed
     */

    boolean codeTextSelectionRight();

    /**
     * Return true if user composed a code for decrease text selection.
     * Note that this function is platform dependent, as consequence every platform has its own implementation.
     *
     * @return true if left text selection code has been composed
     */

    boolean codeTextSelectionLeft();

    /**
     * Return true if user composed a code for copy text.
     * Note that this function is platform dependent, as consequence every platform has its own implementation.
     *
     * @return true if copy code has been composed
     */

    boolean codeTextCopy();

    /**
     * Return true if user composed a code for paste text.
     * Note that this function is platform dependent, as consequence every platform has its own implementation.
     *
     * @return true if paste code has been composed
     */

    boolean codeTextPaste();

    /**
     * @return the native key handler
     */

    Object getNative();

    /**
     * @param key     a not null {@link Key} instance
     * @param mask    the first char of the special code
     * @param keyCode a keyCode used to compose the special code
     * @return true if the special code has been composed
     */

    static boolean code(Key key, int mask, int keyCode) {
        return key != null
                && (key.getKeyCode() == keyCode)
                && ((key.getModifiers() & mask) != 0);
    }
}
