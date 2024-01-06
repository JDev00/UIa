package uia.physical;

import uia.core.*;
import uia.core.Paint.Paint;
import uia.core.basement.Callable;
import uia.core.basement.Message;
import uia.core.shape.Geometry;
import uia.core.shape.Shape;
import uia.physical.callbacks.CallbackStore;
import uia.physical.message.EventKeyMessage;
import uia.physical.message.EventTouchScreenMessage;
import uia.physical.message.MessageStore;
import uia.physical.theme.Theme;
import uia.utility.Geometries;
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
    private boolean consumePointer = true;
    private boolean consumeKey = true;

    public Component(String id, float x, float y, float width, float height,
                     float xExpansion, float yExpansion) {
        this.id = id;

        container = new float[]{x, y, width, height, 0f};

        callable = new CallbackStore(4);

        paint = new Paint().setColor(Theme.WHITE);

        shape = new Shape();
        Geometries.rect(shape.getGeometry());

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

    /**
     * Set Component maximum width
     *
     * @param maxWidth the component maximum width in pixels
     * @return this Component
     */

    public Component setMaxWidth(float maxWidth) {
        maxDimension[0] = Math.max(0, maxWidth);
        return this;
    }

    /**
     * Set Component maximum height
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

        // TODO: approfondire
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

    private List<ScreenTouch> extractScreenTouches(List<ScreenTouch> screenTouches) {
        List<ScreenTouch> out = new ArrayList<>(2);
        if (visible) {
            float[] bounds = shape.getBounds();
            int[] offset = {-(int) (bounds[0]), -(int) (bounds[1])};
            screenTouches.forEach(p -> {
                if (!p.isConsumed()
                        && p.getAction() != ScreenTouch.Action.EXITED
                        && contains(p.getX(), p.getY())) {
                    ScreenTouch screenTouch = p.copy();
                    screenTouch.consume();
                    screenTouch.translate(offset[0], offset[1]);
                    out.add(screenTouch);
                    // consume pointer when necessary
                    if (consumePointer) p.consume();
                }
            });
        }
        return out;
    }

    private List<ScreenTouch> extractLockedScreenTouches(List<ScreenTouch> screenTouches) {
        List<ScreenTouch> out = new ArrayList<>(2);
        if (visible) {
            float[] bounds = shape.getBounds();
            int[] offset = {-(int) (bounds[0]), -(int) (bounds[1])};
            screenTouches.forEach(p -> {
                p.consume();
                ScreenTouch screenTouch = p.copy();
                screenTouch.consume();
                screenTouch.translate(offset[0], offset[1]);
                out.add(screenTouch);
            });
        }
        return out;
    }

    /**
     * Helper function. Update screen touch callbacks according to the specified screen touches.
     *
     * @param internalTouches the screen touches inside the view area
     * @param globalTouches   all the window screen touches
     */

    private void updateScreenTouchCallbacks(List<ScreenTouch> internalTouches,
                                            List<ScreenTouch> globalTouches) {
        if (!internalTouches.isEmpty()) {
            // request focus
            for (int i = 0; i < internalTouches.size() && !focus; i++) {
                if (internalTouches.get(i).getAction() == ScreenTouch.Action.PRESSED) requestFocus(true);
            }
            // invoke mouse enter or mouse hover callback
            if (!over) {
                over = true;
                notifyCallbacks(OnMouseEnter.class, internalTouches);
            } else {
                notifyCallbacks(OnMouseHover.class, internalTouches);
            }
            // invoke click callback
            internalTouches.forEach(p -> {
                if (p.getAction() == ScreenTouch.Action.CLICKED) notifyCallbacks(OnClick.class, internalTouches);
            });
        } else {
            // remove focus
            for (int i = 0; i < globalTouches.size() && focus; i++) {
                if (globalTouches.get(i).getAction() == ScreenTouch.Action.PRESSED) requestFocus(false);
            }
            // invoke mouse exit callback
            if (over) {
                over = false;
                if (visible) notifyCallbacks(OnMouseExit.class, new ArrayList<ScreenTouch>(0));
            }
        }
    }

    /**
     * Helper function. Update key callbacks according to the specified key.
     */

    private void updateKeyCallbacks(Key key) {
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

    @Override
    public void dispatchMessage(Message message) {
        try {
            List<ScreenTouch> screenTouches;
            if (id.equals(message.getRecipient()) &&
                    message instanceof EventTouchScreenMessage.Lock) {
                screenTouches = extractLockedScreenTouches(message.getPayload());
            } else {
                screenTouches = extractScreenTouches(message.getPayload());
            }
            updateScreenTouchCallbacks(screenTouches, message.getPayload());
        } catch (Exception ignored) {
        }
        if (message instanceof EventKeyMessage) {
            updateKeyCallbacks(message.getPayload());
        } else {
            readMessage(message);
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

    private View parent;

    @Override
    public void update(View parent) {
        this.parent = parent;

        if (visible) {

            if (!parent.isOnFocus() && focus) {
                requestFocus(false);
            }

            updateExpansionAnimation();
            updateShape(parent);

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
