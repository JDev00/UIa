package uia.core.platform.implementation;

import uia.core.platform.policy.Key;

import java.awt.event.KeyEvent;

/**
 * {@link Key} implementation for Windows/Linux desktop
 */

public class KeyAWT implements Key {
    private KeyEvent keyEvent;

    private char keyChar;
    private int keyCode;
    private int modifiers;
    private int action;

    @Override
    public void setNative(Object o, int action) {
        if (o instanceof KeyEvent) {
            keyEvent = (KeyEvent) o;

            keyCode = keyEvent.getKeyCode();
            keyChar = keyEvent.getKeyChar();
            modifiers = keyEvent.getModifiers();

            this.action = action;
        }
    }

    @Override
    public void consume() {
        keyEvent.consume();
    }

    @Override
    public char getKeyChar() {
        return keyChar;
    }

    @Override
    public int getKeyCode() {
        return keyCode;
    }

    @Override
    public int getModifiers() {
        return modifiers;
    }

    @Override
    public int getAction() {
        return action;
    }

    @Override
    public boolean isConsumed() {
        return keyEvent.isConsumed();
    }

    @Override
    public boolean codeTextSelectionAll() {
        return Key.code(this, KeyEvent.CTRL_MASK, KeyEvent.VK_A);
    }

    @Override
    public boolean codeTextSelectionRight() {
        return Key.code(this, KeyEvent.SHIFT_MASK, KeyEvent.VK_RIGHT);
    }

    @Override
    public boolean codeTextSelectionLeft() {
        return Key.code(this, KeyEvent.SHIFT_MASK, KeyEvent.VK_LEFT);
    }

    @Override
    public boolean codeTextCopy() {
        return Key.code(this, KeyEvent.CTRL_MASK, KeyEvent.VK_C);
    }

    @Override
    public boolean codeTextPaste() {
        return Key.code(this, KeyEvent.CTRL_MASK, KeyEvent.VK_V);
    }

    @Override
    public Object getNative() {
        return keyEvent;
    }
}
