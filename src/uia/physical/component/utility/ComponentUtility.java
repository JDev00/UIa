package uia.physical.component.utility;

import uia.core.Key;
import uia.core.ScreenTouch;
import uia.core.basement.Message;
import uia.core.shape.Shape;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnKeyPressed;
import uia.core.ui.callbacks.OnKeyReleased;
import uia.core.ui.callbacks.OnKeyTyped;
import uia.core.ui.callbacks.OnMessageReceived;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Collection of utilities for View implementations.
 */
public class ComponentUtility {

    /**
     * Returns the screen touches (not consumed) that are within the specified view area.
     *
     * @param view           a not null {@link View}
     * @param screenTouches  the screen touches to control
     * @param consumeTouches true to consume touches that are within the given view
     * @return a new list filled with touches (copied) that are within the view area, adjusted to the boundaries of the view
     * @throws NullPointerException if {@code view == null || screenTouches == null}
     */

    public static List<ScreenTouch> getAndConsumeTouchesOnViewArea(View view, List<ScreenTouch> screenTouches, boolean consumeTouches) {
        Objects.requireNonNull(view);
        Objects.requireNonNull(screenTouches);

        List<ScreenTouch> result = new ArrayList<>(2);
        if (view.isVisible()) {
            float[] viewBounds = view.getBounds();
            int[] touchOffset = {
                    -((int) viewBounds[0]),
                    -((int) viewBounds[1])
            };
            // returns touches that lie on the given view
            screenTouches.forEach(touch -> {
                if (!touch.isConsumed()
                        && touch.getAction() != ScreenTouch.Action.EXITED
                        && view.contains(touch.getX(), touch.getY())) {
                    ScreenTouch copiedScreenTouch = ScreenTouch.copy(touch, touchOffset[0], touchOffset[1]);
                    copiedScreenTouch.consume();
                    result.add(copiedScreenTouch);

                    // consumes the original screenTouch if necessary
                    if (consumeTouches) {
                        touch.consume();
                    }
                }
            });
        }
        return result;
    }

    /**
     * Copies and consumes the given screen touches.
     *
     * @param view          a not null {@link View}
     * @param screenTouches the screen touches to copy and consume
     * @return a new list filled with the copied (and consumed) touches, adjusted to the boundaries of the view
     * @throws NullPointerException if {@code view == null || screenTouches == null}
     */

    public static List<ScreenTouch> copyAndConsumeTouches(View view, List<ScreenTouch> screenTouches) {
        Objects.requireNonNull(view);
        Objects.requireNonNull(screenTouches);

        List<ScreenTouch> result = new ArrayList<>(2);
        if (view.isVisible()) {
            float[] viewBounds = view.getBounds();
            int[] touchOffset = {
                    -((int) viewBounds[0]),
                    -((int) viewBounds[1])
            };
            screenTouches.forEach(touch -> {
                touch.consume();
                ScreenTouch copiedScreenTouch = ScreenTouch.copy(touch, touchOffset[0], touchOffset[1]);
                copiedScreenTouch.consume();
                result.add(copiedScreenTouch);
            });
        }
        return result;
    }

    /**
     * Notifies the message listeners with the specified message.
     * <br>
     * Listeners are notified if:
     * <ul>
     *     <li>
     *         the message recipient is equal to the view ID;
     *     </li>
     *     <li>
     *         the message recipient is null and the sender is not the recipient.
     *     </li>
     * </ul>
     *
     * @param view    a not null {@link View}
     * @param message a {@link Message} to be notified
     * @throws NullPointerException if {@code view == null || message == null}
     */

    public static void notifyMessageListeners(View view, Message message) {
        Objects.requireNonNull(view);
        Objects.requireNonNull(message);

        String id = view.getID();
        String sender = message.getSender();
        String recipient = message.getRecipient();
        if (Objects.equals(id, recipient) || (recipient == null && !id.equals(sender))) {
            view.notifyCallbacks(OnMessageReceived.class, message);
        }
    }

    /**
     * Notifies key listeners with the specified key.
     *
     * @param view       a not null {@link View}
     * @param key        the {@link Key} to be notified
     * @param consumeKey true to consume to given key
     * @throws NullPointerException if {@code view == null || key == null}
     */

    public static void notifyKeyListeners(View view, Key key, boolean consumeKey) {
        Objects.requireNonNull(view);
        Objects.requireNonNull(key);

        if (!key.isConsumed() && view.isVisible() && view.isOnFocus()) {
            if (consumeKey) {
                key.consume();
            }
            switch (key.getAction()) {
                case PRESSED:
                    view.notifyCallbacks(OnKeyPressed.class, key);
                    break;
                case TYPED:
                    view.notifyCallbacks(OnKeyTyped.class, key);
                    break;
                case RELEASED:
                    view.notifyCallbacks(OnKeyReleased.class, key);
                    break;
            }
        }
    }

    /**
     * Makes the specified Shape suitable for the clip region operation.
     *
     * @param view  a not null View used to prepare the Shape
     * @param shape a not null Shape to be prepared to the clip region operation
     * @throws NullPointerException if {@code view == null or {@code shape == null}
     */

    public static void makeShapeForClipRegion(View view, Shape shape) {
        makeShapeForClipRegion(view, shape, 1f, 1f);
    }

    /**
     * Makes the specified Shape suitable for the clip region operation.
     *
     * @param view   a not null View used to prepare the Shape
     * @param shape  a not null Shape to be prepared to the clip region operation
     * @param scaleX a value (> 0) used to scale the shape on the x-axis
     * @param scaleY a value (> 0) used to scale the shape on the y-axis
     * @throws NullPointerException if {@code view == null or {@code shape == null}
     */

    public static void makeShapeForClipRegion(View view, Shape shape,
                                              float scaleX, float scaleY) {
        Objects.requireNonNull(view);
        Objects.requireNonNull(shape);
        if (scaleX <= 0) {
            throw new IllegalArgumentException("scaleX must be greater than 0");
        }
        if (scaleY <= 0) {
            throw new IllegalArgumentException("scaleY must be greater than 0");
        }
        float[] bounds = view.getBounds();
        float width = view.getWidth();
        float height = view.getHeight();
        shape.setGeometry(view.getGeometry());
        shape.setPosition(bounds[0] + bounds[2] / 2f, bounds[1] + bounds[3] / 2f);
        shape.setDimension(scaleX * width, scaleY * height);
        shape.setRotation(bounds[4]);
    }
}
