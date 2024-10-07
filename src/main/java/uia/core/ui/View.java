package uia.core.ui;

import uia.core.basement.message.Message;
import uia.core.ui.primitives.Drawable;
import uia.core.basement.*;

/**
 * A View is a complete UI object the user can interact with.
 *
 * <p><b>Identity</b>
 * <br>
 * A View is uniquely identified by a unique ID assigned at the time of creation.
 * Such an ID can be assigned automatically or by the user, depending on the
 * specific implementation.
 *
 * <p><b>Relevant functionalities</b>
 * <ul>
 *     <li>
 *     Messages: each View can send and read one {@link Message} at a time
 *     respectively via the {@link View#sendMessage(Message)} and {@link View#readMessage(Message)}
 *     methods. In addition, when a View receives a message, the {@link uia.core.ui.callbacks.OnMessageReceived}
 *     event is fired.
 *     <br>
 *     Messages can be sent to and received from views regardless of their position in
 *     the graphical tree. This feature can be used to decouple views, among other things.
 *     </li>
 *     <li>
 *     InputConsumer: decides whether or not to consume the specific input that passes through the View.
 *     <br>
 *     <i>Example:</i>
 *     <br>
 *     Imagine you have two views, A and B. A lies on top of B. By consuming the screen touches of A,
 *     B won't receive the screen touches that A received.
 *     <br>
 *     This feature is useful when hidden views need to receive the same input of
 *     covering views received.
 *     </li>
 *     <li>
 *     Events: View recognises a plethora of events. As a result of the event, an associated callback
 *     is invoked. View supports callback registration for known events. Eventually, callbacks
 *     can be defined and registered for new events. To manually trigger callbacks of a given class, the
 *     {@link View#notifyCallbacks(Class, Object)} method must be used.
 *     </li>
 * </ul>
 */

public interface View extends Callable, Drawable, Collidable {

    /**
     * InputConsumer defines what type of input can be consumed by the View.
     * <ul>
     *     <li>
     *         SCREEN_TOUCH: allows the View to consume the received touches (clicks on the
     *         desktop context, or screen touches on the mobile context)
     *     </li>
     *     <li>
     *         KEY: allows the View to consume the received keyboard keys
     *     </li>
     * </ul>
     */

    enum InputConsumer {SCREEN_TOUCH, KEY}

    /**
     * Requests or removes the View focus.
     * <br>
     * A View is allowed to interact directly with user only when it is on focus.
     *
     * @param request true to request to this View to put on focus; false to remove focus
     * @implSpec focus can't be requested if a View is not visible
     */

    void requestFocus(boolean request);

    /**
     * @return true if this View is on focus
     */

    boolean isOnFocus();

    /**
     * Makes this View visible or not visible.
     * <br>
     * A View set to invisible also loses its focus.
     *
     * @param visible true to set this View visible
     * @implSpec when set to invisible (visible = false), a View must lose its focus.
     */

    void setVisible(boolean visible);

    /**
     * @return true if this View is visible
     */

    boolean isVisible();

    /**
     * The input consumer functionality, when enabled, consumes input given to this View.
     * <br>
     * For a list of consumable inputs, see {@link InputConsumer}.
     *
     * @param inputConsumer       the input consumer type: see {@link InputConsumer}
     * @param enableInputConsumer true to consume the specified input type; false otherwise
     */

    void setInputConsumer(InputConsumer inputConsumer, boolean enableInputConsumer);

    /**
     * Sends a message to another View.
     *
     * @param message the message to be sent
     * @throws NullPointerException if {@code message == null}
     */

    void sendMessage(Message message);

    /**
     * Reads the specified message.
     *
     * @param message a message to read
     * @throws NullPointerException if {@code message == null}
     */

    void readMessage(Message message);

    /**
     * Updates this View state
     *
     * @param parent the not null parent of this View
     * @throws NullPointerException if {@code == null}
     */

    void update(View parent);

    /**
     * @return this View ID
     */

    String getID();
}
