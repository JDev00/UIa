package uia.core.ui.callbacks;

import uia.core.basement.Callback;

/**
 * Event called when target receives the message.
 * <br>
 * This event provides an array made of:
 * <ul>
 *     <li>the message (Object);</li>
 *     <li>the sender ID (String).</li>
 * </ul>
 */

public interface OnMessageReceived extends Callback<Object[]> {
}
