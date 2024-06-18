package uia.core.ui.callbacks;

import uia.core.basement.Callback;
import uia.core.basement.message.Message;

/**
 * Callback called when recipient receives the message.
 * <br>
 * It provides a not null {@link Message}.
 */

public interface OnMessageReceived extends Callback<Message> {
}
