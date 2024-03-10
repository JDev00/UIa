package uia.physical.utility;

import uia.core.basement.Message;
import uia.core.shape.Shape;
import uia.core.ui.View;
import uia.core.ui.callbacks.OnMessageReceived;

import java.util.Objects;

/**
 * Collection of utilities for View implementations.
 */
public class ComponentUtility {

    /**
     * Reads the message and invoke the appropriate callback.
     * <br>
     * The callback notification is invoked if:
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
     * @param message a not null {@link Message} to notify
     * @throws NullPointerException if {@code view == null || message == null}
     */

    public static void notifyMessageCallback(View view, Message message) {
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
