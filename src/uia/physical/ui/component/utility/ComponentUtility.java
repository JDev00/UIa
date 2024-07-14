package uia.physical.ui.component.utility;

import uia.core.ui.callbacks.OnMessageReceived;
import uia.core.rendering.geometry.Geometries;
import uia.core.rendering.geometry.Geometry;
import uia.core.ui.callbacks.OnKeyReleased;
import uia.core.ui.primitives.ScreenTouch;
import uia.core.ui.callbacks.OnKeyPressed;
import uia.core.basement.message.Message;
import uia.core.ui.callbacks.OnKeyTyped;
import uia.core.rendering.Transform;
import uia.core.ui.primitives.Key;
import uia.core.ui.View;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Objects;
import java.util.List;

import static uia.utility.MathUtility.rotateX;
import static uia.utility.MathUtility.rotateY;

/**
 * Collection of utilities for View implementations.
 */

public final class ComponentUtility {

    private ComponentUtility() {
    }

    /**
     * Returns the screen touches (not consumed) that are within the specified view area.
     *
     * @param view           a not null {@link View}
     * @param screenTouches  the screen touches to control
     * @param consumeTouches true to consume touches that are within the given view
     * @return a new list filled with touches (copied) that are within the view area, adjusted to the boundaries of the view
     * @throws NullPointerException if {@code view == null || screenTouches == null}
     */

    public static ScreenTouch[] getAndConsumeTouchesOnViewArea(View view, boolean consumeTouches, ScreenTouch... screenTouches) {
        Objects.requireNonNull(screenTouches);
        Objects.requireNonNull(view);

        List<ScreenTouch> result = Collections.emptyList();
        if (view.isVisible()) {
            // reallocates the array with the number of screenTouches
            result = new ArrayList<>(screenTouches.length);

            // calculates the screenTouch offset
            float[] viewBounds = view.getBounds();
            int[] touchOffset = {
                    -((int) viewBounds[0]),
                    -((int) viewBounds[1])
            };
            // returns touches that are on the view area
            for (ScreenTouch screenTouch : screenTouches) {
                if (!screenTouch.isConsumed()
                        && screenTouch.getAction() != ScreenTouch.Action.EXITED
                        && view.contains(screenTouch.getX(), screenTouch.getY())) {
                    // copies the given screenTouch
                    ScreenTouch copiedScreenTouch = ScreenTouch.copy(screenTouch, touchOffset[0], touchOffset[1]);
                    // consumes the copied screenTouch
                    copiedScreenTouch.consume();
                    result.add(copiedScreenTouch);

                    // consumes the original screenTouch if necessary
                    if (consumeTouches) {
                        screenTouch.consume();
                    }
                }
            }
        }

        return result.toArray(new ScreenTouch[0]);
    }

    /**
     * Copies and consumes the given screen touches.
     *
     * @param view          a not null {@link View}
     * @param screenTouches the screen touches to copy and consume
     * @return a new list filled with the copied (and consumed) touches, adjusted to the boundaries of the view
     * @throws NullPointerException if {@code view == null || screenTouches == null}
     */

    public static ScreenTouch[] copyAndConsumeTouches(View view, ScreenTouch... screenTouches) {
        Objects.requireNonNull(screenTouches);
        Objects.requireNonNull(view);

        ScreenTouch[] result = {};
        if (view.isVisible()) {
            result = new ScreenTouch[screenTouches.length];

            // calculates the screenTouch offset
            float[] viewBounds = view.getBounds();
            int[] touchOffset = {
                    -((int) viewBounds[0]),
                    -((int) viewBounds[1])
            };
            for (int i = 0; i < screenTouches.length; i++) {
                ScreenTouch screenTouch = screenTouches[i];
                // consumes the screenTouch
                screenTouch.consume();
                // copies it
                ScreenTouch copiedScreenTouch = ScreenTouch.copy(screenTouch, touchOffset[0], touchOffset[1]);
                copiedScreenTouch.consume();
                // populates the result
                result[i] = copiedScreenTouch;
            }
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
     * Makes the given Transform Object suitable for clip region operation.
     * <br>
     * Time complexity: T(1)
     * <br>
     * Space complexity: O(1)
     *
     * @throws NullPointerException if {@code view == null || targetTransform == null}
     */

    public static void makeTransformForClipRegion(View view, float scaleX, float scaleY,
                                                  Transform targetTransform) {
        Objects.requireNonNull(targetTransform);
        Objects.requireNonNull(view);

        float[] bounds = view.getBounds();
        float width = view.getWidth();
        float height = view.getHeight();

        // updates the target Transform object
        targetTransform
                .setTranslation(bounds[0] + bounds[2] / 2f, bounds[1] + bounds[3] / 2f)
                .setScale(scaleX * width, scaleY * height)
                .setRotation(bounds[4]);
    }

    /**
     * Calculates the View position on the x-axis
     *
     * @param xContainer     the left top container corner position on the x-axis
     * @param containerWidth the container width
     * @param xDist          the View distance from the container center on the x-axis
     * @param yDist          the View distance from the container center on the y-axis
     * @param radians        the View rotation in radians
     * @return the View position on the x-axis
     */

    public static float getPositionOnX(float xContainer, float containerWidth,
                                       float xDist, float yDist, float radians) {
        return xContainer + 0.5f * containerWidth + rotateX(xDist, yDist, radians);
    }

    /**
     * Calculates the View position on the y-axis
     *
     * @param yContainer      the left top container corner position on the y-axis
     * @param containerHeight the container height
     * @param xDist           the View distance from the container center on the x-axis
     * @param yDist           the View distance from the container center on the y-axis
     * @param radians         the View rotation in radians
     * @return the View position on the y-axis
     */

    public static float getPositionOnY(float yContainer, float containerHeight,
                                       float xDist, float yDist, float radians) {
        return yContainer + 0.5f * containerHeight + rotateY(xDist, yDist, radians);
    }

    /**
     * Build a rounded rectangle.
     *
     * @see Geometries#rect(Geometry, int, float, float, float, float, float)
     */

    public static void buildRect(Geometry geometry, float width, float height, float radius) {
        Geometries.rect(geometry, Geometries.STD_VERT, radius, width / height);
    }
}
