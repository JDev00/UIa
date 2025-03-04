package uia.application.ui.component;

import uia.application.message.systemessages.ScreenTouchMessage;
import uia.application.message.messagingsystem.LockedMessage;
import uia.application.ui.component.utility.ComponentUtility;
import uia.application.message.store.GlobalMessageStore;
import uia.application.message.systemessages.KeyMessage;
import uia.core.rendering.geometry.GeometryCollection;
import uia.core.rendering.geometry.GeometryUtility;
import uia.core.basement.message.MessageStore;
import uia.application.events.CallbackStore;
import uia.core.rendering.geometry.Geometry;
import uia.core.ui.primitives.ScreenTouch;
import uia.core.basement.message.Message;
import uia.core.rendering.Transform;
import uia.core.rendering.Graphics;
import uia.core.basement.Callback;
import uia.core.basement.Callable;
import uia.core.ui.primitives.Key;
import uia.core.ui.callbacks.*;
import uia.core.ui.style.Style;
import uia.core.ui.View;

import java.util.function.Consumer;
import java.util.ArrayList;
import java.util.Objects;

import static java.lang.Math.*;

/**
 * UIa standard {@link View} implementation.
 */

public final class Component implements View {
    private ColliderPolicy colliderPolicy = ColliderPolicy.SAT;
    private final Callable callable;
    private final Transform transform;
    private final Geometry geometry;
    private final Style style;

    private final String id;
    private final float[] expanse = {1f, 1f, 1f, 1f, 0.185f};
    private final float[] dimension = new float[2];

    private int previousGeometryBuilderHashcode = -1;

    private boolean over = false;
    private boolean focus = false;
    private boolean visible = true;
    private boolean consumeScreenTouch = true;
    private boolean consumeKey = true;

    public Component(String id, float x, float y, float width, float height) {
        this.id = id;

        style = new Style()
                .setDimension(width, height)
                .setPosition(x, y);

        callable = new CallbackStore(4);

        transform = new Transform();

        geometry = GeometryCollection.rect(new Geometry());
    }

    /**
     * By default, Component creates a responsive View.
     * It means that, when user is within its area, Component will expand its dimension.
     * <br>
     * With this method you can set the maximum upper expansion limits.
     *
     * @param x the maximum (relative) value on the x-axis
     * @param y the maximum (relative) value on the x-axis
     * @return this Component
     */

    public Component setExpanseLimit(float x, float y) {
        expanse[2] = x;
        expanse[3] = y;
        return this;
    }

    @Override
    public long registerCallback(Callback<?> callback) {
        return callable.registerCallback(callback);
    }

    @Override
    public void unregisterCallback(long callbackID) {
        callable.unregisterCallback(callbackID);
    }

    @Override
    public void notifyCallbacks(Class<? extends Callback> type, Object data) {
        callable.notifyCallbacks(type, data);
    }

    @Override
    public int numberOfCallbacks() {
        return callable.numberOfCallbacks();
    }

    @Override
    public Style getStyle() {
        return style;
    }

    @Override
    public Geometry getGeometry() {
        return geometry;
    }

    @Override
    public void requestFocus(boolean request) {
        if (request) {
            if (!focus && visible) {
                focus = true;
                notifyCallbacks(OnFocus.class, true);
            }
        } else if (focus) {
            focus = false;
            notifyCallbacks(OnFocus.class, false);
        }
    }

    @Override
    public boolean isOnFocus() {
        return focus;
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        if (!visible) {
            over = false;
            requestFocus(false);
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setColliderPolicy(ColliderPolicy colliderPolicy) {
        Objects.requireNonNull(colliderPolicy);
        this.colliderPolicy = colliderPolicy;
    }

    @Override
    public void setInputConsumer(InputConsumer inputConsumer, boolean enableInputConsumer) {
        if (inputConsumer == InputConsumer.SCREEN_TOUCH) {
            consumeScreenTouch = enableInputConsumer;
        } else if (inputConsumer == InputConsumer.KEY) {
            consumeKey = enableInputConsumer;
        }
    }

    @Override
    public void sendMessage(Message message) {
        MessageStore globalMessageStore = GlobalMessageStore.getInstance();
        globalMessageStore.add(message);
    }

    /**
     * Helper function. Notifies listeners with the specified screen touches.
     *
     * @param insideTouches the screen touches within the view area
     * @param globalTouches all screen touches
     */

    private void notifyScreenTouchListeners(ScreenTouch[] insideTouches,
                                            ScreenTouch[] globalTouches) {
        if (insideTouches.length > 0) {
            // requests focus
            for (int i = 0; i < insideTouches.length && !focus; i++) {
                if (insideTouches[i].getAction() == ScreenTouch.Action.PRESSED) {
                    requestFocus(true);
                }
            }
            // invokes the callback for the 'mouse enter' event
            if (!over) {
                over = true;
                notifyCallbacks(OnMouseEnter.class, insideTouches);
            } else {
                // invokes the callback for the 'mouse hover' event
                notifyCallbacks(OnMouseHover.class, insideTouches);
            }
            // invokes the callback when clicked on this view
            for (ScreenTouch screenTouch : insideTouches) {
                if (screenTouch.getAction() == ScreenTouch.Action.CLICKED) {
                    notifyCallbacks(OnClick.class, insideTouches);
                }
            }
        } else {
            // removes focus
            for (int i = 0; i < globalTouches.length && focus; i++) {
                if (globalTouches[i].getAction() == ScreenTouch.Action.PRESSED) {
                    requestFocus(false);
                }
            }
            // invokes the callback for the 'mouse exit' event
            if (over) {
                over = false;
                if (visible) {
                    notifyCallbacks(OnMouseExit.class, new ArrayList<ScreenTouch>(0));
                }
            }
        }
    }

    @Override
    public void readMessage(Message message) {
        if (message instanceof LockedMessage) {
            LockedMessage receivedMessage = (LockedMessage) message;
            String messageRecipient = receivedMessage.getRecipient();
            Message messagePayload = receivedMessage.getPayload();

            if (id.equals(messageRecipient)) {
                if (messagePayload instanceof ScreenTouchMessage) {
                    ScreenTouch[] screenTouches = messagePayload.getPayload();
                    ScreenTouch[] localTouches = ComponentUtility.copyAndConsumeTouches(this, screenTouches);
                    // notifies listeners
                    notifyScreenTouchListeners(localTouches, screenTouches);
                } else if (messagePayload instanceof KeyMessage) {
                    Key key = messagePayload.getPayload();
                    ComponentUtility.notifyKeyListeners(this, key, consumeKey);
                } else {
                    ComponentUtility.notifyMessageListeners(this, message);
                }
            }
        } else if (message instanceof ScreenTouchMessage) {
            ScreenTouch[] screenTouches = message.getPayload();
            ScreenTouch[] localTouches = ComponentUtility.getAndConsumeTouchesOnViewArea(this, consumeScreenTouch, screenTouches);
            // notifies listeners
            notifyScreenTouchListeners(localTouches, screenTouches);
        } else if (message instanceof KeyMessage) {
            ComponentUtility.notifyKeyListeners(this, message.getPayload(), consumeKey);
        } else {
            ComponentUtility.notifyMessageListeners(this, message);
        }
    }

    /**
     * Helper function. Updates View shape.
     *
     * @param parent the view parent
     */

    private void updateTransform(View parent) {
        float[] bounds = parent.getBounds();
        float width = parent.getWidth();
        float height = parent.getHeight();
        float xDist = width * (style.getX() - 0.5f);
        float yDist = height * (style.getY() - 0.5f);
        float rot = bounds[4];

        float componentWidth = style.getOffsetWidth() * width;
        float componentHeight = style.getOffsetHeight() * height;
        float maxWidth = style.getMaxWidth();
        float minWidth = style.getMinWidth();
        float maxHeight = style.getMaxHeight();
        float minHeight = style.getMiHeight();
        if (minWidth > 0f) {
            componentWidth = Math.max(minWidth, componentWidth);
        }
        if (maxWidth > 0f) {
            componentWidth = Math.min(maxWidth, componentWidth);
        }
        if (minHeight > 0f) {
            componentHeight = Math.max(minHeight, componentHeight);
        }
        if (maxHeight > 0f) {
            componentHeight = Math.min(maxHeight, componentHeight);
        }
        dimension[0] = expanse[0] * componentWidth;
        dimension[1] = expanse[1] * componentHeight;

        // updates geometry transformation
        transform
                .setTranslation(
                        ComponentUtility.getPositionOnX(bounds[0], bounds[2], xDist, yDist, rot),
                        ComponentUtility.getPositionOnY(bounds[1], bounds[3], xDist, yDist, rot)
                )
                .setRotation(rot + style.getRotation())
                .setScale(dimension[0], dimension[1]);
    }

    /**
     * Helper function. Updates View geometry.
     */

    private void updateGeometry() {
        Consumer<Geometry> geometryBuilder = style.getGeometryBuilder();
        int currentGeometryBuilderHashcode = geometryBuilder.hashCode();

        boolean isGeometryToBeBuilt = style.isGeometryToBeBuiltDynamically();
        boolean isBuilderDifferent = previousGeometryBuilderHashcode != currentGeometryBuilderHashcode;
        if (isGeometryToBeBuilt || isBuilderDifferent) {
            previousGeometryBuilderHashcode = currentGeometryBuilderHashcode;
            geometryBuilder.accept(geometry);
        }
    }

    /**
     * Helper function. Updates the expansion animation.
     */

    private void updateAnimation() {
        float frameFactor = 30f * expanse[4];
        float xq = (expanse[2] - 1f) / frameFactor;
        float yq = (expanse[3] - 1f) / frameFactor;
        if (over) {
            expanse[0] = min(expanse[2], expanse[0] + xq);
            expanse[1] = min(expanse[3], expanse[1] + yq);
        } else {
            expanse[0] = max(1, expanse[0] - xq);
            expanse[1] = max(1, expanse[1] - yq);
        }
    }

    @Override
    public void update(View parent) {
        if (visible) {
            updateAnimation();
            updateTransform(parent);
            updateGeometry();
        }
    }

    @Override
    public void draw(Graphics graphics) {
        if (visible) {
            graphics
                    .setShapeColor(style.getBackgroundColor())
                    .setShapeBorderColor(style.getBorderColor())
                    .setShapeBorderWidth(style.getBorderWidth())
                    .drawShape(transform, geometry.vertices(), geometry.toArray());
        }
    }

    @Override
    public float getWidth() {
        return dimension[0];
    }

    @Override
    public float getHeight() {
        return dimension[1];
    }

    private final float[] componentBounds = new float[5];

    @Override
    public float[] getBounds() {
        GeometryUtility.computeBounds(transform, componentBounds);
        return componentBounds;
    }

    @Override
    public boolean contains(float x, float y) {
        return GeometryUtility.contains(colliderPolicy, transform, geometry, x, y);
    }

    @Override
    public String getID() {
        return id;
    }
}
