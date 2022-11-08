package uia.core.platform.implementation;

import uia.core.platform.policy.Key;

import java.awt.event.KeyEvent;

/**
 * AWT {@link Key} implementation
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
    public Object getNative() {
        return keyEvent;
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
    public boolean isCode(int mask, int keyCode) {
        return this.keyCode == keyCode && (modifiers & mask) != 0;
    }
}
