package uia.physical;

import uia.core.*;
import uia.physical.message.MessageStore;
import uia.utility.Figure;
import uia.core.basement.Callback;
import uia.core.ui.View;
import uia.core.ui.Graphic;
import uia.core.ui.callbacks.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static uia.utility.TrigTable.*;

/**
 * Implementation of {@link View}
 */

public final class Component implements View {
    private final Paint paint;
    private final Shape shape;
    private final List<Callback> callbacks = new ArrayList<>(4);
    private Consumer<Geometry> geomBuilder;

    private final float[] expanse = {1f, 1f, 1.015f, 1.015f, 0.125f};
    private final float[] container;

    private final String id;

    private boolean over = false;
    private boolean focus = false;
    private boolean visible = true;
    private boolean consumePointer = true;
    private boolean consumeKey = true;

    public Component(String id, float x, float y, float width, float height) {
        this.id = id;

        container = new float[]{x, y, width, height, 0f};

        paint = new Paint().setColor(new Paint.Color(255));

        shape = new Shape();
        Figure.rect(shape.getGeometry());
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
    public void addCallback(Callback<?> callback) {
        if (!callbacks.contains(callback)) callbacks.add(callback);
    }

    @Override
    public void removeCallback(Callback<?> callback) {
        callbacks.remove(callback);
    }

    @Override
    public void notifyCallbacks(Class<? extends Callback> type, Object data) {
        getInstance(callbacks, type, e -> e.onEvent(data));
    }

    @Override
    public void buildGeometry(Consumer<Geometry> builder, boolean inTimeBuilding) {
        if (builder != null) {
            builder.accept(shape.getGeometry());
            geomBuilder = inTimeBuilding ? builder : null;
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
    public void setColliderPolicy(COLLIDER_POLICY policy) {
        shape.setColliderPolicy(policy);
    }

    @Override
    public void setConsumer(CONSUMER consumer, boolean enableConsumer) {
        switch (consumer) {
            case POINTER:
                consumePointer = enableConsumer;
                break;
            case KEY:
                consumeKey = enableConsumer;
                break;
        }
    }

    @Override
    public void sendMessage(Object message, String destID) {
        MessageStore.getInstance().add(new Object[]{message, getID(), destID});
    }

    @Override
    public void dispatch(DISPATCHER dispatcher, Object data) {
        switch (dispatcher) {
            case MESSAGE:
                Object[] dataArray = (Object[]) data;
                dispatch((String) dataArray[2], (String) dataArray[1], dataArray[0]);
                break;
            case POINTERS:
                dispatch((List<ScreenPointer>) data);
                break;
            case KEY:
                dispatch((Key) data);
                break;
        }
    }

    private void dispatch(String destID, String sourceID, Object message) {
        if (id.equals(destID) || (destID == null && !id.equals(sourceID)))
            notifyCallbacks(OnMessageReceived.class, new Object[]{message, sourceID});
    }

    private final List<ScreenPointer> curScreenPointers = new ArrayList<>();

    private void dispatch(List<ScreenPointer> screenPointers) {
        float[] bounds = shape.bounds();
        int xOff = -(int) (bounds[0]);
        int yOff = -(int) (bounds[1]);

        curScreenPointers.clear();

        if (visible) {
            screenPointers.forEach(p -> {
                if (!p.isConsumed()
                        && p.getAction() != ScreenPointer.ACTION.EXITED
                        && contains(p.getX(), p.getY())) {
                    p.translate(xOff, yOff);
                    curScreenPointers.add(p);
                    // consume pointer when necessary
                    if (consumePointer) p.consume();
                }
            });
        }

        if (!curScreenPointers.isEmpty()) {
            // request focus when necessary
            for (int i = 0; i < curScreenPointers.size() && !focus; i++) {
                if (curScreenPointers.get(i).getAction() == ScreenPointer.ACTION.PRESSED) requestFocus(true);
            }

            // notify hover when necessary
            if (!over) {
                over = true;
                notifyCallbacks(OnMouseEnter.class, curScreenPointers);
            } else {
                notifyCallbacks(OnMouseHover.class, curScreenPointers);
            }

            // notify click when necessary
            curScreenPointers.forEach(p -> {
                if (p.getAction() == ScreenPointer.ACTION.CLICKED) notifyCallbacks(OnClick.class, curScreenPointers);
            });

            // restore pointers to original position
            for (ScreenPointer i : curScreenPointers) {
                i.translate(-xOff, -yOff);
            }
        } else {
            // remove focus when necessary
            for (int i = 0; i < screenPointers.size() && focus; i++) {
                if (screenPointers.get(i).getAction() == ScreenPointer.ACTION.PRESSED) requestFocus(false);
            }

            // notify pointer exit when necessary
            if (over) {
                over = false;
                if (visible) notifyCallbacks(OnMouseExit.class, new ArrayList<ScreenPointer>(0));
            }
        }
    }

    private void dispatch(Key key) {
        if (!key.isConsumed() && visible && focus) {

            if (consumeKey) key.consume();

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
     * Update shape position, dimension and rotation
     *
     * @param bounds a View's bounds
     */

    private void updateShape(float[] bounds) {
        float[] dimension_no_rot = dimensionWithoutRotation(bounds);
        float x = (container[0] - 0.5f) * dimension_no_rot[0];
        float y = (container[1] - 0.5f) * dimension_no_rot[1];

        shape.setPosition(
                bounds[0] + 0.5f * bounds[2] + rotX(x, y, cos(bounds[4]), sin(bounds[4])),
                bounds[1] + 0.5f * bounds[3] + rotY(x, y, cos(bounds[4]), sin(bounds[4]))
        );
        shape.setDimension(
                expanse[0] * container[2] * dimension_no_rot[0],
                expanse[1] * container[3] * dimension_no_rot[1]
        );
        shape.setRotation(container[4] + bounds[4]);
    }

    /**
     * Update expansion animation
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

            if (!parent.isOnFocus() && focus) requestFocus(false);

            updateExpansionAnimation();
            updateShape(parent.bounds());

            if (geomBuilder != null) geomBuilder.accept(shape.getGeometry());
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

    /**
     * Try to access the given type
     *
     * @param list     a not null {@link List}
     * @param type     the type to update
     * @param consumer a not null {@link Consumer} used to update the found type
     */

    // TODO: cambiare nome alla funzione
    private static <T> void getInstance(List<T> list, Class<?> type, Consumer<T> consumer) {
        try {
            String name = type.getName();

            list.forEach(t -> {
                Class<?> current = t.getClass();

                while (current != null) {
                    Class<?>[] interfaces = current.getInterfaces();

                    for (Class<?> i : interfaces) {
                        if (name.equals(i.getName())) consumer.accept(t);
                    }

                    current = current.getSuperclass();
                }
            });
        } catch (Exception ignored) {
        }
    }

    /**
     * Calculate width and height without rotation
     */

    public static float[] dimensionWithoutRotation(float[] bounds) {
        float cos = cos(bounds[4]);
        float sin = sin(bounds[4]);
        return new float[]{
                boundXneg(bounds[2], bounds[3], cos, sin),
                boundYneg(bounds[2], bounds[3], cos, sin)
        };
    }

    /**
     * Build a rounded rectangle
     */

    public static Geometry buildRect(Geometry geometry, float[] bounds, float radius) {
        float[] no_rot_dim = dimensionWithoutRotation(bounds);
        Figure.rect(geometry, Figure.STD_VERT, radius, no_rot_dim[0] / no_rot_dim[1]);
        return geometry;
    }
}
