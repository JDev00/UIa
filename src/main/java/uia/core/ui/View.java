package uia.core.ui;

import uia.core.basement.message.Message;
import uia.core.ui.primitives.Drawable;
import uia.core.basement.*;

/**
 * View ADT.
 * <br>
 * A View is a special area on the screen with the user can interact with. Shortly, it is the basement for
 * every graphical widget built with UIa. From the architectural point of view, it is made up of two components:
 * <ul>
 * <li>{@link Drawable} used to define the graphical aspects: geometry and style;</li>
 * <li>{@link Callback}s used to respond to an event when it is detected.</li>
 * </ul>
 * <b>A View is identified by an ID assigned at declaration time.</b>
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
     *         KEY: allows the View to consume the received keyboard keys.
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
