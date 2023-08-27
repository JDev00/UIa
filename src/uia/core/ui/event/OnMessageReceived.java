package uia.core.ui.event;

import uia.core.basement.Event;
import uia.core.ui.View;

/**
 * Event called when target receives the message.
 * <br>
 * This event provides an array made of:
 * <ul>
 *     <li>the message (Object);</li>
 *     <li>the sender ID (String).</li>
 * </ul>
 */

public interface OnMessageReceived extends Event<Object[]> {
}
