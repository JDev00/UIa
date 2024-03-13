package uia.physical;

import uia.core.*;
import uia.core.paint.Paint;
import uia.core.basement.Callable;
import uia.core.basement.Message;
import uia.core.shape.Geometry;
import uia.core.shape.Shape;
import uia.physical.callbacks.CallbackStore;
import uia.physical.message.EventKeyMessage;
import uia.physical.message.EventTouchScreenMessage;
import uia.physical.message.MessageStore;
import uia.physical.theme.Theme;
import uia.physical.utility.ComponentUtility;
import uia.utility.Geometries;
import uia.core.basement.Callback;
import uia.core.ui.View;
import uia.core.ui.Graphics;
import uia.core.ui.callbacks.*;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static uia.utility.MathUtility.*;

/**
 * UIa standard {@link View} implementation
 */

public final class Component implements View {
    private final Paint paint;
    private final Shape shape;
    private java.util.function.Consumer<Geometry> geometryBuilder;
    private final Callable callable;

    private final String id;
    private final float[] expanse = {1f, 1f, 1f, 1f, 0.125f};
    private final float[] container;
    private final float[] dimension = new float[2];
    private final float[] maxDimension = {0, 0};

    private boolean over = false;
    private boolean focus = false;
    private boolean visible = true;
    private boolean consumeScreenTouch = true;
    private boolean consumeKey = true;

    public Component(String id, float x, float y, float width, float height) {
        this.id = id;

        container = new float[]{x, y, width, height, 0f};

        callable = new CallbackStore(4);

        paint = new Paint().setColor(Theme.WHITE);

        shape = new Shape();
        Geometries.rect(shape.getGeometry());
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

    /**
     * Sets the Component maximum width.
     *
     * @param maxWidth the component maximum width in pixels
     * @return this Component
     */

    public Component setMaxWidth(float maxWidth) {
        maxDimension[0] = Math.max(0, maxWidth);
        return this;
    }

    /**
     * Sets the Component maximum height.
     *
     * @param maxHeight the component maximum height in pixels
     * @return this Component
     */

    public Component setMaxHeight(float maxHeight) {
        maxDimension[1] = Math.max(0, maxHeight);
        return this;
    }

    @Override
    public void registerCallback(Callback<?> callback) {
        callable.registerCallback(callback);
    }

    @Override
    public void unregisterCallback(Callback<?> callback) {
        callable.unregisterCallback(callback);
    }

    @Override
    public void notifyCallbacks(Class<? extends Callback> type, Object data) {
        callable.notifyCallbacks(type, data);
    }

    @Override
    public void setGeometry(java.util.function.Consumer<Geometry> builder, boolean inTimeBuilding) {
        if (builder != null) {
            builder.accept(shape.getGeometry());
            geometryBuilder = inTimeBuilding ? builder : null;
        }
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    @Override
    public Geometry getGeometry() {
        return shape.getGeometry();
    }

    @Override
    public void setPosition(float x, float y) {
        container[0] = x;
        container[1] = y;
    }

    @Override
    public void setDimension(float width, float height) {
        container[2] = max(0, width);
        container[3] = max(0, height);

        // TODO: is it correct?
        if (parent != null) {
            updateShape(parent);
        }
    }

    @Override
    public void setRotation(float radians) {
        container[4] = radians % TWO_PI;
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
    public void setColliderPolicy(ColliderPolicy policy) {
        shape.setColliderPolicy(policy);
    }

    @Override
    public void setConsumer(Consumer consumer, boolean enableConsumer) {
        switch (consumer) {
            case SCREEN_TOUCH:
                consumeScreenTouch = enableConsumer;
                break;
            case KEY:
                consumeKey = enableConsumer;
                break;
        }
    }

    @Override
    public void sendMessage(Message message) {
        MessageStore.getInstance().add(message);
    }

    /**
     * Helper function. Notifies screenTouch listeners with the specified screen touches.
     *
     * @param insideTouches the screen touches within the view area
     * @param globalTouches all screen touches
     */

    private void notifyScreenTouchListeners(List<ScreenTouch> insideTouches,
                                            List<ScreenTouch> globalTouches) {
        if (!insideTouches.isEmpty()) {
            // request focus
            for (int i = 0; i < insideTouches.size() && !focus; i++) {
                if (insideTouches.get(i).getAction() == ScreenTouch.Action.PRESSED) {
                    requestFocus(true);
                }
            }
            // invoke mouse enter or mouse hover callback
            if (!over) {
                over = true;
                notifyCallbacks(OnMouseEnter.class, insideTouches);
            } else {
                notifyCallbacks(OnMouseHover.class, insideTouches);
            }
            // invoke click callback
            insideTouches.forEach(touch -> {
                if (touch.getAction() == ScreenTouch.Action.CLICKED) {
                    notifyCallbacks(OnClick.class, insideTouches);
                }
            });
        } else {
            // remove focus
            for (int i = 0; i < globalTouches.size() && focus; i++) {
                if (globalTouches.get(i).getAction() == ScreenTouch.Action.PRESSED) {
                    requestFocus(false);
                }
            }
            // invoke mouse exit callback
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
        if (message instanceof EventTouchScreenMessage.Lock) {
            if (id.equals(message.getRecipient())) {
                List<ScreenTouch> screenTouches = message.getPayload();
                List<ScreenTouch> localTouches = ComponentUtility.copyAndConsumeTouches(
                        this, screenTouches
                );
                notifyScreenTouchListeners(localTouches, screenTouches);
            }
        } else if (message instanceof EventTouchScreenMessage) {
            List<ScreenTouch> screenTouches = message.getPayload();
            List<ScreenTouch> localTouches = ComponentUtility.getAndConsumeTouchesOnViewArea(
                    this, screenTouches, consumeScreenTouch
            );
            notifyScreenTouchListeners(localTouches, screenTouches);
        } else if (message instanceof EventKeyMessage) {
            ComponentUtility.notifyKeyListeners(
                    this, message.getPayload(), consumeKey
            );
        } else {
            ComponentUtility.notifyMessageListeners(this, message);
        }
    }

    /**
     * Helper function. Updates shape.
     *
     * @param parent the view parent
     */

    private void updateShape(View parent) {
        float[] bounds = parent.getBounds();
        float width = parent.getWidth();
        float height = parent.getHeight();
        float xDist = width * (container[0] - 0.5f);
        float yDist = height * (container[1] - 0.5f);
        float rot = bounds[4];

        // TODO: adjust auto-resize when component rotates
        float componentWidth = container[2] * width;
        float componentHeight = container[3] * height;
        if (maxDimension[0] > 0) componentWidth = Math.min(maxDimension[0], componentWidth);
        if (maxDimension[1] > 0) componentHeight = Math.min(maxDimension[1], componentHeight);
        dimension[0] = expanse[0] * componentWidth;
        dimension[1] = expanse[1] * componentHeight;

        shape.setPosition(
                View.getPositionOnX(bounds[0], bounds[2], xDist, yDist, rot),
                View.getPositionOnY(bounds[1], bounds[3], xDist, yDist, rot)
        );
        shape.setDimension(dimension[0], dimension[1]);
        shape.setRotation(rot + container[4]);
    }

    /**
     * Updates the expansion animation.
     */

    private void updateAnimation() {
        float frameFactor = 60f * expanse[4];
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

    private View parent;

    @Override
    public void update(View parent) {
        this.parent = parent;

        if (visible) {
            updateAnimation();
            updateShape(parent);
            if (geometryBuilder != null) {
                geometryBuilder.accept(shape.getGeometry());
            }
        }
    }

    @Override
    public void draw(Graphics graphics) {
        if (visible) {
            graphics.setPaint(paint);
            graphics.drawShape(shape);
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

    @Override
    public float getRotation() {
        return container[4];
    }

    @Override
    public float[] getBounds() {
        return shape.getBounds();
    }

    @Override
    public boolean contains(float x, float y) {
        return shape.contains(x, y);
    }

    @Override
    public String getID() {
        return id;
    }
}
