package uia.core.ui.callbacks;

import uia.core.basement.Callback;

/**
 * Callback called when recipient receives the message.
 * <br>
 * It provides an array made of:
 * <ul>
 *     <li>the message as an Object;</li>
 *     <li>the sender ID as a String.</li>
 * </ul>
 */

public interface OnMessageReceived extends Callback<Object[]> {
}
