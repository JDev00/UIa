package uia.physical;

import uia.core.*;
import uia.core.basement.Message;
import uia.physical.message.EventKeyMessage;
import uia.physical.message.EventTouchScreenMessage;
import uia.physical.message.GenericMessage;
import uia.physical.message.MessageStore;
import uia.utility.GeometryFactory;
import uia.core.basement.Callback;
import uia.core.ui.View;
import uia.core.ui.Graphic;
import uia.core.ui.callbacks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static uia.utility.TrigTable.*;

/**
 * UIa built-in {@link View} implementation.
 */

public final class Component implements View {
    private final Paint paint;
    private final Shape shape;
    private final List<Callback> callbacks;
    private java.util.function.Consumer<Geometry> geometryBuilder;

    private final float[] expanse = {1f, 1f, 1f, 1f, 0.125f};
    private final float[] container;
    private final float[] dimension = new float[2];

    private final String id;

    private boolean over = false;
    private boolean focus = false;
    private boolean visible = true;
    private boolean consumePointer = true;
    private boolean consumeKey = true;

    public Component(String id, float x, float y, float width, float height,
                     float xExpansion, float yExpansion) {
        this.id = id;

        container = new float[]{x, y, width, height, 0f};

        callbacks = new ArrayList<>(4);

        paint = new Paint().setColor(new Paint.Color(255));

        shape = new Shape();
        GeometryFactory.rect(shape.getGeometry());

        setExpanseLimit(xExpansion, yExpansion);
    }

    public Component(String id, float x, float y, float width, float height) {
        this(id, x, y, width, height, 1f, 1f);
    }

    /**
     * By default, Component creates a responsive View.
     * It means that, when user is inside its area, Component will expand its dimension.
     * <br>
     * With this method you can set the maximum upper expansion limits.
     *
     * @param x the maximum (relative) value along x-axis
     * @param y the maximum (relative) value along x-axis
     * @return this Component
     */

    public Component setExpanseLimit(float x, float y) {
        expanse[2] = x;
        expanse[3] = y;
        return this;
    }

    @Override
    public void registerCallback(Callback<?> callback) {
        if (!callbacks.contains(callback)) callbacks.add(callback);
    }

    @Override
    public void unregisterCallback(Callback<?> callback) {
        callbacks.remove(callback);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void notifyCallbacks(Class<? extends Callback> type, Object data) {
        try {
            String name = type.getName();

            callbacks.forEach(callback -> {
                Class<?> current = callback.getClass();
                while (current != null) {
                    Class<?>[] interfaces = current.getInterfaces();
                    for (Class<?> i : interfaces) {
                        if (name.equals(i.getName())) {
                            callback.update(data);
                        }
                    }
                    current = current.getSuperclass();
                }
            });
        } catch (Exception ignored) {
        }
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
                consumePointer = enableConsumer;
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
     * Helper function. Read the given message and invoke the callbacks.
     */

    private void readMessage(Message message) {
        String sender = message.getSender();
        String recipient = message.getRecipient();
        if (Objects.equals(id, recipient) || (recipient == null && !id.equals(sender))) {
            notifyCallbacks(OnMessageReceived.class, message);
        }
    }

    private final List<ScreenTouch> curScreenTouches = new ArrayList<>();

    /**
     * Helper function. Read the given screen touches and invoke the callbacks.
     */

    private void readScreenTouches(List<ScreenTouch> screenTouches) {
        float[] bounds = shape.bounds();
        int[] offset = {-(int) (bounds[0]), -(int) (bounds[1])};

        curScreenTouches.clear();

        if (visible) {
            screenTouches.forEach(p -> {
                if (!p.isConsumed()
                        && p.getAction() != ScreenTouch.Action.EXITED
                        && contains(p.getX(), p.getY())) {
                    ScreenTouch screenTouch = p.copy();
                    screenTouch.consume();
                    screenTouch.translate(offset[0], offset[1]);
                    curScreenTouches.add(screenTouch);
                    // consume pointer when necessary
                    if (consumePointer) p.consume();
                }
            });
        }

        if (!curScreenTouches.isEmpty()) {
            // request focus when necessary
            for (int i = 0; i < curScreenTouches.size() && !focus; i++) {
                if (curScreenTouches.get(i).getAction() == ScreenTouch.Action.PRESSED) requestFocus(true);
            }

            // notify hover when necessary
            if (!over) {
                over = true;
                notifyCallbacks(OnMouseEnter.class, curScreenTouches);
            } else {
                notifyCallbacks(OnMouseHover.class, curScreenTouches);
            }

            // notify click when necessary
            curScreenTouches.forEach(p -> {
                if (p.getAction() == ScreenTouch.Action.CLICKED) notifyCallbacks(OnClick.class, curScreenTouches);
            });
        } else {
            // remove focus when necessary
            for (int i = 0; i < screenTouches.size() && focus; i++) {
                if (screenTouches.get(i).getAction() == ScreenTouch.Action.PRESSED) requestFocus(false);
            }

            // notify pointer exit when necessary
            if (over) {
                over = false;
                if (visible) notifyCallbacks(OnMouseExit.class, new ArrayList<ScreenTouch>(0));
            }
        }
    }

    /**
     * Helper function. Read the given Key and invoke the callbacks.
     */

    private void readKey(Key key) {
        if (!key.isConsumed() && visible && focus) {
            if (consumeKey) {
                key.consume();
            }
            switch (key.getAction()) {
                case PRESSED:
                    notifyCallbacks(OnKeyPressed.class, key);
                    break;
                case TYPED:
                    notifyCallbacks(OnKeyTyped.class, key);
                    break;
                case RELEASED:
                    notifyCallbacks(OnKeyReleased.class, key);
                    break;
            }
        }
    }

    @Override
    public void dispatchMessage(Message message) {
        if (message instanceof GenericMessage) {
            readMessage(message);
        }
        if (message instanceof EventTouchScreenMessage) {
            readScreenTouches(message.getPayload());
        }
        if (message instanceof EventKeyMessage) {
            readKey(message.getPayload());
        }
    }

    /**
     * Update shape position, dimension and rotation
     *
     * @param bounds a View's bounds
     */

    private void updateShape(float[] bounds, float width, float height) {
        float xDist = width * (container[0] - 0.5f);
        float yDist = height * (container[1] - 0.5f);
        float rot = bounds[4];

        // TODO: adjust auto-resize when component rotates
        dimension[0] = expanse[0] * container[2] * width;
        dimension[1] = expanse[1] * container[3] * height;

        shape.setPosition(
                View.getPositionOnX(bounds[0], bounds[2], xDist, yDist, rot),
                View.getPositionOnY(bounds[1], bounds[3], xDist, yDist, rot)
        );
        shape.setDimension(dimension[0], dimension[1]);
        shape.setRotation(rot + container[4]);
    }

    /**
     * Update the expansion animation
     */

    private void updateExpansionAnimation() {
        float div = 60f * expanse[4];
        float xq = (expanse[2] - 1f) / div;
        float yq = (expanse[3] - 1f) / div;

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

            if (!parent.isOnFocus() && focus) {
                requestFocus(false);
            }

            updateExpansionAnimation();
            updateShape(parent.bounds(), parent.getWidth(), parent.getHeight());

            if (geometryBuilder != null) {
                geometryBuilder.accept(shape.getGeometry());
            }
        }
    }

    @Override
    public void draw(Graphic graphic) {
        if (visible) {
            graphic.setPaint(paint);
            graphic.drawShape(shape);
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
    public float[] bounds() {
        return shape.bounds();
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
