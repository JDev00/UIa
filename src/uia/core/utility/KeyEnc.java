package uia.core.utility;

import java.awt.event.KeyEvent;

/**
 * KeyEvent encapsulation
 */

// @Test
public class KeyEnc {
    private KeyEvent keyEvent;
    private boolean consume;

    private int action;

    public KeyEnc() {
        keyEvent = null;
        consume = false;
    }

    public void set(Object obj, int action) {
        if (obj instanceof KeyEvent) {
            keyEvent = (KeyEvent) obj;
            consume = false;

            this.action = action;
        }
    }

    public void consume() {
        consume = true;
    }

    public Object getNative() {
        return keyEvent;
    }

    public int getAction() {
        return action;
    }

    public boolean isConsumed() {
        return consume;
    }
}
