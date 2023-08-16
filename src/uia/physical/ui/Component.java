package uia.physical.ui;

import uia.physical.Figure;
import uia.core.architecture.Event;
import uia.core.architecture.ui.View;
import uia.core.Geom;
import uia.core.Paint;
import uia.core.architecture.Graphic;
import uia.core.Key;
import uia.core.Pointer;
import uia.core.architecture.ui.event.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static uia.utils.TrigTable.*;

/**
 * Implementation of {@link View}
 */

public final class Component implements View {
    private final Geom geom;
    private Paint paint;
    private final List<Event> events = new ArrayList<>(2);

    private BiConsumer<View, Geom> geomBuilder;

    /**
     * Parameters required to make a geometry expansion animation.
     * <ul>
     *     <li>expanse[0,1] = current expansion's values;</li>
     *     <li>expanse[2,3] = expansion's upper limits along x and y axis;</li>
     *     <li>expanse[4] = the seconds required to complete the animation.</li>
     * </ul>
     */
    public final float[] expanse = {1f, 1f, 1.015f, 1.015f, 0.125f};

    private final float[] container;
    private final float[] bounds;
    private final float[] desc;

    private final String id;

    private boolean over = false;
    private boolean focus = false;
    private boolean visible = true;
    private boolean collider_aabb = false;
    private boolean consumePointer = true;
    private boolean consumeKey = true;

    public Component(String id, float x, float y, float width, float height) {
        this.id = id;

        container = new float[]{x, y, width, height, 0f};
        bounds = new float[container.length];
        desc = new float[3];

        paint = new Paint().setColor(255);

        geom = Figure.rect(new Geom());
    }

    /**
     * Set the expanse upper limits
     */

    public Component setExpanseLimit(float x, float y) {
        expanse[2] = x;
        expanse[3] = y;
        return this;
    }

    @Override
    public void addEvent(Event<View, ?> event) {
        if (!events.contains(event)) events.add(event);
    }

    @Override
    public void removeEvent(Event<View, ?> event) {
        events.remove(event);
    }

    @Override
    public void updateEvent(Class<? extends Event> type, Object data) {
        access(events, type, e -> e.update(Component.this, data));
    }

    @Override
    public void setPosition(float x, float y) {
        container[0] = x;
        container[1] = y;
    }

    @Override
    public void setDimension(float x, float y) {
        container[2] = max(0, x);
        container[3] = max(0, y);
    }

    @Override
    public void setRotation(float radians) {
        container[4] = radians % TWO_PI;
    }

    @Override
    public void setFocus(boolean request) {
        if (request) {
            if (!focus && isVisible()) {
                focus = true;
                updateEvent(EventFocus.class, true);
            }
        } else if (focus) {
            focus = false;
            updateEvent(EventFocus.class, false);
        }
    }

    @Override
    public boolean isFocused() {
        return focus;
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
    public void setColliderPolicy(boolean aabb) {
        collider_aabb = aabb;
    }

    @Override
    public void setVisible(boolean isVisible) {
        visible = isVisible;

        if (!visible) {
            over = false;
            setFocus(false);
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void setGeom(BiConsumer<View, Geom> builder, boolean everyFrame) {
        if (builder != null) {

            if (!everyFrame) {
                geomBuilder = null;
                builder.accept(this, geom);
            } else {
                geomBuilder = builder;
            }
        }
    }

    @Override
    public Geom getGeom() {
        return geom;
    }

    @Override
    public void setPaint(Paint paint) {
        if (paint != null) this.paint = paint;
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    private final List<Pointer> curPointers = new ArrayList<>();

    @Override
    public void dispatch(List<Pointer> pointers) {
        boolean compute = pointers != null && isVisible();

        int xOff = (int) (-bounds[0] + bounds[2] / 2f);
        int yOff = (int) (-bounds[1] + bounds[3] / 2f);

        curPointers.clear();

        if (compute) {
            pointers.forEach(p -> {
                if (!p.isConsumed()
                        && p.getAction() != Pointer.ACTION.EXITED
                        && contains(p.getX(), p.getY())) {
                    p.translate(xOff, yOff);
                    curPointers.add(p);
                    // consume pointer when necessary
                    if (consumePointer) p.consume();
                }
            });
        }

        if (!curPointers.isEmpty()) {
            // update focus when necessary
            if (!focus) {
                int i = 0;
                while (i < curPointers.size() && curPointers.get(i).getAction() != Pointer.ACTION.PRESSED) i++;
                if (i < curPointers.size()) setFocus(true);
            }

            // update over when necessary
            if (!over) {
                over = true;
                updateEvent(EventEnter.class, curPointers);
            } else {
                updateEvent(EventHover.class, curPointers);
            }

            // update click when necessary
            curPointers.forEach(p -> {
                if (p.getAction() == Pointer.ACTION.CLICKED) updateEvent(EventClick.class, curPointers);
            });

            // restore pointers to original position
            for (Pointer i : curPointers) {
                i.translate(-xOff, -yOff);
            }
        } else {
            // remove focus when necessary
            if (focus) {
                int i = 0;
                while (i < pointers.size() && pointers.get(i).getAction() != Pointer.ACTION.PRESSED) i++;
                if (i < pointers.size()) setFocus(false);
            }

            // update exit when necessary
            if (over) {
                over = false;
                if (compute) updateEvent(EventExit.class, new ArrayList<Pointer>(0));
            }
        }
    }

    @Override
    public void dispatch(Key key) {
        if (!key.isConsumed() && isFocused() && isVisible()) {

            if (consumeKey) key.consume();

            switch (key.getAction()) {
                case PRESSED:
                    updateEvent(EventKeyPressed.class, key);
                    break;

                case TYPED:
                    updateEvent(EventKeyTyped.class, key);
                    break;

                case RELEASED:
                    updateEvent(EventKeyReleased.class, key);
                    break;
            }
        }
    }

    /**
     * Update expansion animation
     */

    private void updateExpansion() {
        float div = 60f * expanse[4];
        float xq = (expanse[2] - 1f) / div;
        float yq = (expanse[3] - 1f) / div;

        // update expansion
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

            // if parent isn't on focus, remove view's focus
            if (!parent.isFocused() && focus) setFocus(false);

            // update expansion animation
            updateExpansion();

            // update bounds & geometry
            float[] p_bounds = parent.bounds();

            // geometry variables
            float[] p_dim = parent.desc();
            float d_x = (container[0] - 0.5f) * p_dim[0];
            float d_y = (container[1] - 0.5f) * p_dim[1];
            float d_rot = p_bounds[4];
            desc[0] = expanse[0] * container[2] * p_dim[0];
            desc[1] = expanse[1] * container[3] * p_dim[1];
            desc[2] = container[4];

            // bounds variables
            float b_rot = container[4] + p_bounds[4];
            float b_w = boundX(desc[0], desc[1], cos(b_rot), sin(b_rot));
            float b_h = boundY(desc[0], desc[1], cos(b_rot), sin(b_rot));

            // update geometry
            geom.x = p_bounds[0] + p_bounds[2] / 2f + rotX(d_x, d_y, cos(d_rot), sin(d_rot));
            geom.y = p_bounds[1] + p_bounds[3] / 2f + rotY(d_x, d_y, cos(d_rot), sin(d_rot));
            geom.scaleX = desc[0] / 2f;
            geom.scaleY = desc[1] / 2f;
            geom.rotation = b_rot;

            // update bounds
            bounds[0] = geom.x - b_w / 2f;
            bounds[1] = geom.y - b_h / 2f;
            bounds[2] = b_w;
            bounds[3] = b_h;
            bounds[4] = b_rot;
        }
    }

    @Override
    public void draw(Graphic graphic) {
        if (visible) {

            if (geomBuilder != null) geomBuilder.accept(this, geom);

            graphic.setPaint(paint);
            graphic.drawGeometry(geom);

            // for debugging purposes. Highlight view's boundaries
            /*graphic.setPaint(new Paint().setColor(0, 255, 0, 100));
            graphic.openShape(bounds[0], bounds[1]);
            graphic.vertex(bounds[0] + bounds[2], bounds[1]);
            graphic.vertex(bounds[0] + bounds[2], bounds[1] + bounds[3]);
            graphic.vertex(bounds[0], bounds[1] + bounds[3]);
            graphic.closeShape();*/
        }
    }

    private final float[] boundsCopy = new float[5];
    private final float[] descCopy = new float[3];

    @Override
    public float[] bounds() {
        System.arraycopy(bounds, 0, boundsCopy, 0, bounds.length);
        return boundsCopy;
    }

    @Override
    public float[] desc() {
        System.arraycopy(desc, 0, descCopy, 0, desc.length);
        return descCopy;
    }

    @Override
    public boolean contains(float x, float y) {
        float xc = bounds[0] + bounds[2] / 2f;
        float yc = bounds[1] + bounds[3] / 2f;

        return collider_aabb
                ? intersects(x, y, 1, 1, xc, yc, bounds[2], bounds[3])
                : geom.contains(x, y);
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

    public static <T> void access(List<T> list, Class<?> type, Consumer<T> consumer) {
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
}
