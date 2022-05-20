package uia.core.policy;

/**
 * Key ADT used to describe a key event.
 * <br>
 * <b>Note that Key primarily acts as a bridge between API framework and native platform.</b>
 */

public interface Key {

    /**
     * Set the object used to handle a key natively on the platform.
     * <b>Note that {@link Pointer#isConsumed()} will be set to false.</b>
     *
     * @param o      the native object
     * @param action the action performed by the object
     */

    void set(Object o, int action);

    /**
     * Consume this keyEnc.
     * <b>A consumed keyEnc can't be used by Pages or Views.</b>
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
     * @return true if this keyEnc has been consumed
     */

    boolean isConsumed();

    /**
     * Return true if user composed a code for select the entire text.
     * <b>Note that this function is platform dependent, as consequence every platform has its own implementation.
     * <br>
     * Windows uses ctrl-a.
     * </b>
     *
     * @return true if text selection code has been composed
     */

    boolean codeTextSelectionAll();

    /**
     * Return true if user composed a code for increase text selection.
     * <b>Note that this function is platform dependent, as consequence every platform has its own implementation.
     * <br>
     * Windows uses shift-right.
     * </b>
     *
     * @return true if right text selection code has been composed
     */

    boolean codeTextSelectionRight();

    /**
     * Return true if user composed a code for decrease text selection.
     * <b>Note that this function is platform dependent, as consequence every platform has its own implementation.
     * <br>
     * Windows uses shift-left.
     * </b>
     *
     * @return true if left text selection code has been composed
     */

    boolean codeTextSelectionLeft();

    /**
     * Return true if user composed a code for copy text.
     * <b>Note that this function is platform dependent, as consequence every platform has its own implementation.
     * <br>
     * Windows uses ctrl-c.
     * </b>
     *
     * @return true if copy code has been composed
     */

    boolean codeTextCopy();

    /**
     * Return true if user composed a code for paste text.
     * <b>Note that this function is platform dependent, as consequence every platform has its own implementation.
     * <br>
     * Windows uses ctrl-v.
     * </b>
     *
     * @return true if paste code has been composed
     */

    boolean codeTextPaste();

    /**
     * @return the native object
     */

    Object getNative();

    /**
     * @param key     a not null {@link Key} instance
     * @param mask    the first char of the special code
     * @param keyCode a keyCode used to compose the special code
     * @return true if the special code has been composed
     */

    static boolean code(Key key,
                        int mask, int keyCode) {
        return key != null
                && (key.getKeyCode() == keyCode)
                && ((key.getModifiers() & mask) != 0);
    }
}
