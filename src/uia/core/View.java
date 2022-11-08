package uia.core;

import uia.core.animator.policy.Animator;
import uia.core.event.*;
import uia.core.event.Event;
import uia.core.animator.StdAnimator;
import uia.core.event.EventQueue;
import uia.core.platform.independent.shape.Figure;
import uia.core.platform.independent.shape.Shape;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.policy.*;
import uia.utils.Collider;
import uia.utils.Timer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static uia.utils.TrigTable.*;

/**
 * Primitive UI object designed for inheritance and start point for every UI component.
 * <br>
 * A View is made up of 4 essential components:
 * <br>
 * 1) a {@link Shape} object used to define the View's shape, anyway it could be null;
 * <br>
 * 2) a {@link Paint} object used to define the graphic aspects;
 * <br>
 * 3) an {@link EventQueue} used to store events;
 * <br>
 * 4) an {@link Animator} used to animate some View's features.
 * <br>
 * Some additional points:
 * <br>
 * 1) a View is considered not visible only when it is explicitly declared through {@link View#setVisible(boolean)};
 * <br>
 * 2) focus is handled automatically by the framework, but for convenience there are 2 methods to control it manually:
 * {@link View#requestFocus()} and {@link View#removeFocus()};
 * <br>
 * 3) a View, by default, scales and translates according to screen resize but
 * this functionality can be controlled with {@link View#setPositionAdjustment(boolean)}
 * and {@link View#setDimensionAdjustment(boolean)}.
 * <br>
 * If position/dimension without transformations are needed, you could use {@link #boundsStatic()}.
 */

public class View {

    /**
     * The ANIMATOR_POS constant identifies the {@link Animator} used to animate the view's position
     */
    public static final int ANIMATOR_POS = 0;

    /**
     * The ANIMATOR_DIM constant identifies the {@link Animator} used to animate the view's dimension
     */
    public static final int ANIMATOR_DIM = 1;

    /**
     * The ANIMATOR_ROT constant identifies the {@link Animator} used to animate the view's rotation
     */
    public static final int ANIMATOR_ROT = 2;

    /* Input */

    private final PointerEvent pointerEvent;

    private Key key;

    private boolean consumePointer = true;
    private boolean consumeKey = true;

    /* Behaviour */

    private Context context;

    private Shape shape;

    private Consumer<Shape> dynShape;

    private Paint paint;

    private EventQueue<Event<View>> eventQueue;

    private final Animator[] animator;

    private final float[] bounds = new float[4];
    private final float[] boundsStatic = new float[4];

    private float rot;
    private float xCenter;
    private float yCenter;
    private float xScale = 1f;
    private float yScale = 1f;

    /**
     * View's ratio.
     * <br>
     * 1) ratio[0]: x-position scaling according to {@link Context#screenRatioX()}
     * <br>
     * 2) ratio[1]: y-position scaling according to {@link Context#screenRatioY()}
     * <br>
     * 3) ratio[2]: x-dimension scaling according to {@link Context#screenRatioX()}
     * <br>
     * 4) ratio[3]: y-dimension scaling according to {@link Context#screenRatioY()}
     */
    private final float[] ratio = {1f, 1f, 1f, 1f};

    private float expansionTime = 0.125f;
    private float xExpanseLimit = 1.015f;
    private float yExpanseLimit = 1.015f;
    private float xExpanse = 1f;
    private float yExpanse = 1f;

    private boolean over = false;
    private boolean focus = false;
    private boolean visible = true;
    private boolean adjustPosition = true;
    private boolean adjustDimension = true;
    private boolean useAABB = false;

    public View(Context context, float x, float y, float width, float height) {
        this.context = Context.validate(context);

        boundsStatic[0] = x;
        boundsStatic[1] = y;
        boundsStatic[2] = width;
        boundsStatic[3] = height;

        shape = new Shape(Figure.STD_VERT);

        dynShape = s -> Figure.rect(s, Figure.STD_VERT, Figure.STD_ROUND);

        paint = new Paint(255, 255, 255);

        pointerEvent = new PointerEvent();

        eventQueue = new StdEventQueue<>();

        animator = new Animator[]{new StdAnimator(), new StdAnimator(), new StdAnimator()};

        updateBounds();
    }

    /**
     * Set the View's Context
     *
     * @param c a not null {@link Context}
     */

    public void setContext(Context c) {
        if (c != null && !context.equals(c)) context = c;
    }

    /**
     * Set the View's Shape and set {@link #setShape(Consumer, boolean)} to null
     *
     * @param shape a {@link Shape}; it could be null
     */

    public void setShape(Shape shape) {
        this.shape = shape;
        dynShape = null;
    }

    /**
     * Set the Shape's structure
     *
     * @param builder     a {@link Consumer} used to build the Shape's structure; it could be null
     * @param dynamically true to rebuild the Shape's structure every time the View is rendered
     */

    public void setShape(Consumer<Shape> builder, boolean dynamically) {
        if (dynamically) {
            dynShape = builder;
        } else {
            dynShape = null;
            if (builder != null) builder.accept(shape);
        }
    }

    /**
     * Set the View's Paint
     *
     * @param paint a not null {@link Paint}
     */

    public void setPaint(Paint paint) {
        if (paint != null) this.paint = paint;
    }

    /*
     *
     * Events
     *
     */

    /**
     * Call the specified event's type
     *
     * @param type  a not null event's type
     * @param state the event's state
     * @return true if the operation succeed
     * @see Event
     */

    protected boolean callEvent(Class<?> type, int state) {
        return EventQueue.accessEvent(eventQueue, type, e -> e.apply(View.this, state));
    }

    /**
     * Set a new EventQueue and keep the attached events
     *
     * @param eventQueue a new not null {@link EventQueue}
     */

    public void setEventQueue(EventQueue<Event<View>> eventQueue) {
        if (eventQueue != null && !eventQueue.equals(this.eventQueue)) {
            List<Event<View>> list = new ArrayList<>();

            this.eventQueue.forEach(list::add);
            this.eventQueue = eventQueue;

            for (Event<View> i : list) {
                this.eventQueue.addEvent(i);
            }
        }
    }

    /**
     * @return the attached {@link EventQueue}
     */

    public EventQueue<Event<View>> getEventQueue() {
        return eventQueue;
    }

    /*
     *
     * Animator
     *
     */

    /**
     * Replace an animator with a new one
     *
     * @param i        the position of the animator to replace
     * @param animator a not null {@link Animator}
     */

    public final void setAnimator(int i, Animator animator) {
        if (animator != null && i >= ANIMATOR_POS && i <= ANIMATOR_ROT)
            this.animator[i] = animator;
    }

    /**
     * @return the {@link Animator} responsible for view's position animation
     */

    public final Animator getAnimatorPos() {
        return animator[ANIMATOR_POS];
    }

    /**
     * @return the {@link Animator} responsible for view's dimension animation
     */

    public final Animator getAnimatorDim() {
        return animator[ANIMATOR_DIM];
    }

    /**
     * @return the {@link Animator} responsible for view's rotation animation
     */

    public final Animator getAnimatorRot() {
        return animator[ANIMATOR_ROT];
    }

    /*
     *
     * View
     *
     */

    private final float[] dim = {0f, 0f};

    /**
     * Update View's bounds.
     * <br>
     * Time required: T(1);
     * <br>
     * Space required: O(1).
     */

    private void updateBounds() {
        float rot = rotation();
        float cos = cos(rot);
        float sin = sin(rot);

        float xs = xExpanse * ratio[2] * xScale;
        float ys = yExpanse * ratio[3] * yScale;
        float xsr = boundX(xs, ys, cos, sin) / boundX(1, 1, cos, sin);
        float ysr = boundY(xs, ys, cos, sin) / boundY(1, 1, cos, sin);
        float width = max(0, xsr * (boundsStatic[2] + animator[ANIMATOR_DIM].x()));
        float height = max(0, ysr * (boundsStatic[3] + animator[ANIMATOR_DIM].y()));

        // update dimension
        dim[0] = width;
        dim[1] = height;

        // update view's bounds according to its rotation
        bounds[0] = xCenter + ratio[0] * (boundsStatic[0] + animator[ANIMATOR_POS].x());
        bounds[1] = yCenter + ratio[1] * (boundsStatic[1] + animator[ANIMATOR_POS].y());
        bounds[2] = boundX(width, height, cos, sin);
        bounds[3] = boundY(width, height, cos, sin);
    }

    /**
     * Set an offset position
     *
     * @param x the offset position along x-axis
     * @param y the offset position along y-axis
     */

    public void setCenter(float x, float y) {
        xCenter = x;
        yCenter = y;
        updateBounds();
    }

    /**
     * Set an offset scale
     *
     * @param x the offset scale along x-axis
     * @param y the offset scale along y-axis
     */

    public void setScale(float x, float y) {
        xScale = max(0, x);
        yScale = max(0, y);
        updateBounds();
    }

    /**
     * Set the View's position
     *
     * @param x the new position along x-axis
     * @param y the new position along y-axis
     */

    public void setPosition(float x, float y) {
        boundsStatic[0] = x;
        boundsStatic[1] = y;
        updateBounds();
    }

    /**
     * Translate this View
     *
     * @param x the translation along x-axis
     * @param y the translation along y-axis
     */

    public void translate(float x, float y) {
        boundsStatic[0] += x;
        boundsStatic[1] += y;
        updateBounds();
    }

    /**
     * Set a static dimension for this View
     *
     * @param x the new dimension along x-axis; if {@code x < 0}, x will be set to 0
     * @param y the new dimension along y-axis; if {@code y < 0}, y will be set to 0
     */

    public void setDimension(float x, float y) {
        boundsStatic[2] = max(0, x);
        boundsStatic[3] = max(0, y);
        updateBounds();
    }

    /**
     * Scale this View
     *
     * @param x the scale along x-axis; if {@code x < 0}, x will be set to 0
     * @param y the scale along y-axis; if {@code y < 0}, y will be set to 0
     */

    public void scale(float x, float y) {
        boundsStatic[2] *= max(0, x);
        boundsStatic[3] *= max(0, y);
        updateBounds();
    }

    /**
     * Set the View's rotation
     *
     * @param radians the amount of radians used to rotate this View
     */

    public void setRotation(float radians) {
        rot = radians % TWO_PI;
        updateBounds();
    }

    /**
     * Sum the given radians to the current rotation
     *
     * @param radians the amount of radians used to rotate this View
     */

    public void rotate(float radians) {
        rot += radians;
        if (Math.abs(rot) >= TWO_PI) rot %= TWO_PI;
        updateBounds();
    }

    /**
     * Reset the expansion
     */

    public void resetExpansion() {
        xExpanse = 1f;
        yExpanse = 1f;
        updateBounds();
    }

    /**
     * Set the expansion limits
     *
     * @param x a value {@code >= 0} used to define expansion along x-axis
     * @param y a value {@code >= 0} used to define expansion along y-axis
     */

    public void setExpansion(float x, float y) {
        xExpanseLimit = 1f + max(0, x);
        yExpanseLimit = 1f + max(0, y);
        updateBounds();
    }

    /**
     * Set the required time to complete the expansion
     *
     * @param seconds the amount of seconds ({@code >= 0}) required to complete the expansion
     */

    public void setExpansionTime(float seconds) {
        expansionTime = max(0, seconds);
    }

    /**
     * Request to this View to put on focus. It means that it will be allowed to interact with user.
     * Note that this operation will be done only if the View is visible and <u>isn't already on focus</u>.
     */

    public void requestFocus() {
        if (!focus && isVisible()) {
            focus = true;
            callEvent(Event.class, Event.FOCUS_GAINED);
        }
    }

    /**
     * Remove focus from this View.
     * <br>
     * Note that this operation will be done only if this View is on focus.
     */

    public void removeFocus() {
        if (focus) {
            focus = false;
            callEvent(Event.class, Event.FOCUS_LOST);
        }
    }

    /**
     * Display or hide this View
     *
     * @param isVisible true to set this View visible
     */

    public void setVisible(boolean isVisible) {
        visible = isVisible;

        if (!visible) {
            over = false;
            removeFocus();
        }
    }

    /**
     * Pointer consumer functionality consumes pointers that are inside the View's area.
     * <br>
     * In a multiple View scenario, a View consumes pointers inside its area with the consequence
     * that the consumed pointers can't be propagated through the rest of the views.
     * When views intersects, the user will interact only with the view on top.
     *
     * @param consumePointer true to enable pointer consumer
     */

    public void setPointerConsumer(boolean consumePointer) {
        this.consumePointer = consumePointer;
    }

    /**
     * Key consumer functionality consumes the last {@link Key} that go through this view with the consequence that
     * the rest of views can't process the Key.
     * <br>
     * This functionality has been provided to allow just one view at a time to process keys from keyboard
     *
     * @param consumeKey true to enable key consumer
     */

    public void setKeyConsumer(boolean consumeKey) {
        this.consumeKey = consumeKey;
    }

    /**
     * Enable or disable the position adjustment functionality.
     * <br>
     * Position adjustment is the ability of a view to change its position according to screen resize
     *
     * @param adjustment true to enable position adjustment functionality
     */

    public void setPositionAdjustment(boolean adjustment) {
        adjustPosition = adjustment;
    }

    /**
     * Enable or disable the dimension adjustment functionality.
     * <br>
     * Dimension adjustment is the ability of a view to change its dimension according to screen resize
     *
     * @param adjustment true to enable dimension adjustment functionality
     */

    public void setDimensionAdjustment(boolean adjustment) {
        adjustDimension = adjustment;
    }

    /**
     * Enable or disable the AABB technique to check if a point is inside the View's Area
     *
     * @param useAABB true to use AABB technique
     */

    public void setAABBContainer(boolean useAABB) {
        this.useAABB = useAABB;
    }

    private int clickCount = 0;

    /**
     * Update pointers and pointer's events
     *
     * @param update true to update pointer's events
     */

    protected void updatePointers(boolean update) {
        // Fix it. It must work with multiple pointers
        Pointer p = context.pointers().get(0);

        boolean compute = update && isVisible() && !p.isConsumed();

        if (compute
                && p.getAction() != Pointer.EXITED
                && contains(p.getX(), p.getY())) {

            // update PointerEvent
            pointerEvent.update(p, (int) (-bounds[0] + bounds[2] / 2f), (int) (-bounds[1] + bounds[3] / 2f));

            // consume pointers
            if (consumePointer) p.consume();

            // put on focus and update FOCUS_GAINED event
            if (!focus && p.getAction() == Pointer.PRESSED) {
                focus = true;
                callEvent(Event.class, Event.FOCUS_GAINED);
            }

            if (!over) {// update POINTER_ENTER event
                over = true;
                callEvent(Event.class, Event.POINTER_ENTER);
            } else {    // update POINTER_HOVER event
                callEvent(Event.class, Event.POINTER_HOVER);
            }

            // update POINTER_CLICK event
            if (pointerEvent.getCumulativeClicks() != clickCount) {
                clickCount = pointerEvent.getCumulativeClicks();
                callEvent(Event.class, Event.POINTER_CLICK);
            }
        } else {
            // remove focus
            if (focus && p.getAction() == Pointer.PRESSED) removeFocus();

            // update POINTER_LEAVE event
            if (over) {
                over = false;
                if (compute || (p.isConsumed() && p.getAction() == Pointer.EXITED)) {
                    callEvent(Event.class, Event.POINTER_LEAVE);
                }
                pointerEvent.clear();
            }
        }
    }

    /**
     * Call this method only inside an {@link Event}
     *
     * @return the view's {@link PointerEvent}
     */

    public final PointerEvent getPointerEvent() {
        return pointerEvent;
    }

    /**
     * Update keys and key's events
     *
     * @param update true to update key's events
     */

    protected void updateKeys(boolean update) {
        Key k = context.getKey();

        if (update && isFocused() && !k.isConsumed()) {
            key = k;
            if (consumeKey) k.consume();

            switch (k.getAction()) {
                case Key.PRESSED:
                    callEvent(Event.class, Event.KEY_PRESSED);
                    break;

                case Key.RELEASED:
                    callEvent(Event.class, Event.KEY_RELEASED);
                    break;

                case Key.TYPED:
                    callEvent(Event.class, Event.KEY_TYPED);
                    break;
            }
        }
    }

    /**
     * Call this method only inside an {@link Event}
     *
     * @return the last handled {@link Key}
     */

    public final Key getKey() {
        return key;
    }

    /**
     * Update the view's state
     */

    protected void update() {
        // if context isn't on focus, remove view's focus
        if (!context.isOnFocus() && focus) removeFocus();

        float xq = (xExpanseLimit - 1f) / (60f * expansionTime);
        float yq = (yExpanseLimit - 1f) / (60f * expansionTime);

        // update expansion
        if (over) {
            xExpanse = min(xExpanseLimit, xExpanse + xq);
            yExpanse = min(yExpanseLimit, yExpanse + yq);
        } else {
            xExpanse = max(1, xExpanse - xq);
            yExpanse = max(1, yExpanse - yq);
        }

        // update position according to screen resize
        if (adjustPosition) {
            ratio[0] = context.screenRatioX();
            ratio[1] = context.screenRatioY();
        } else {
            ratio[0] = ratio[1] = 1f;
        }

        // update dimension according to screen resize
        if (adjustDimension) {
            ratio[2] = context.screenRatioX();
            ratio[3] = context.screenRatioY();
        } else {
            ratio[2] = ratio[3] = 1f;
        }

        // update animators
        for (Animator i : animator) {
            i.update();
        }

        updateBounds();
    }

    /**
     * Draw this View on the given Graphic
     *
     * @param graphic a not null {@link Graphic} instance
     */

    protected void draw(Graphic graphic) {
        if (shape != null) {

            // build shape dynamically
            if (dynShape != null) dynShape.accept(shape);

            graphic.setPaint(paint);
            shape.setPosition(bounds[0], bounds[1]);
            shape.setDimension(dim[0], dim[1]);
            shape.setRotation(rotation());
            shape.draw(graphic);
        }
    }

    /*
     *
     * Update view's state. Test functionality.
     *
     */

    /**
     * Update the given View
     *
     * @param view   a not null {@link View}
     * @param update true to update {@link #updatePointers(boolean)}
     * @return true if the given View has been updated; false otherwise
     */

    public static boolean updatePointers(View view, boolean update) {
        if (view != null) {
            view.updatePointers(update && view.isVisible());
            return true;
        }
        return false;
    }

    /**
     * Update the given View
     *
     * @param view   a not null {@link View}
     * @param update true to update {@link #updateKeys(boolean)}
     * @return true if the given View has been updated; false otherwise
     */

    public static boolean updateKeys(View view, boolean update) {
        if (view != null) {
            view.updateKeys(update && view.isVisible());
            return true;
        }
        return false;
    }

    /**
     * Update the given View
     *
     * @param view a not null {@link View}
     * @return true if the given View has been updated; false otherwise
     */

    public static boolean update(View view) {
        if (view != null && view.isVisible()) {
            view.update();
            return true;
        }
        return false;
    }

    /**
     * Use the given Graphic to draw the given View
     *
     * @param view    a not null {@link View}
     * @param graphic a not null {@link Graphic}
     * @return true if the given View has been drawn; false otherwise
     */

    public static boolean draw(View view, Graphic graphic) {
        if (view != null && view.isVisible()) {
            view.draw(graphic);
            return true;
        }
        return false;
    }

    /*
     *
     * Getters
     *
     */

    /**
     * @return the View's Context
     */

    public Context getContext() {
        return context;
    }

    /**
     * @return the View's Paint
     */

    public Paint getPaint() {
        return paint;
    }

    /**
     * @return the View's Shape
     */

    public Shape getShape() {
        return shape;
    }

    /**
     * @return the View's position along x-axis
     */

    public float x() {
        return bounds[0];
    }

    /**
     * @return the View's position along y-axis
     */

    public float y() {
        return bounds[1];
    }

    /**
     * @return the View's dimension along x-axis
     */

    public float width() {
        return bounds[2];
    }

    /**
     * @return the View's dimension along y-axis
     */

    public float height() {
        return bounds[3];
    }

    /**
     * @return rotation in radians
     */

    public float rotation() {
        return rot + animator[ANIMATOR_ROT].x();
    }

    private final float[] boundsMaximum = new float[4];
    private final float[] boundsStaticCopy = new float[4];

    /**
     * @return the static View's bounds
     */

    public final float[] boundsStatic() {
        System.arraycopy(boundsStatic, 0, boundsStaticCopy, 0, 4);
        return boundsStaticCopy;
    }

    /**
     * @return the maximum View's bounds
     */

    public final float[] boundsMax() {// Test
        boundsMaximum[0] = bounds[0];
        boundsMaximum[1] = bounds[1];
        boundsMaximum[2] = xExpanseLimit * bounds[2] / xExpanse;
        boundsMaximum[3] = yExpanseLimit * bounds[3] / yExpanse;
        return boundsMaximum;
    }

    /**
     * @return true if at least one pointer is inside the View's area
     */

    public boolean arePointersOver() {
        return over;
    }

    /**
     * @return true if pointer consumer functionality is enabled
     */

    public boolean hasPointerConsumer() {
        return consumePointer;
    }

    /**
     * @return true if key consumer functionality is enabled
     */

    public boolean hasKeyConsumer() {
        return consumeKey;
    }

    /**
     * @return true if position adjustment functionality is enabled
     */

    public boolean hasPositionAdjustment() {
        return adjustPosition;
    }

    /**
     * @return true if dimension adjustment functionality is enabled
     */

    public boolean hasDimensionAdjustment() {
        return adjustDimension;
    }

    /**
     * @return true if AABB technique is enabled
     */

    public boolean hasAABBTechnique() {
        return useAABB;
    }

    /**
     * @return true if this View is visible
     */

    public boolean isVisible() {
        return visible;
    }

    /**
     * @return true if this View is on focus
     */

    public boolean isFocused() {
        return focus;
    }

    /**
     * @return true if this View has a not null Shape
     */

    public boolean hasShape() {
        return shape != null;
    }

    /**
     * To check if the given point is inside the View's area, two policies are adopted
     * according to the Shape's state:
     * <br>
     * 1) if Shape is null, then an AABB collider is used;
     * <br>
     * 2) if Shape isn't null, will be used the native Shape's method {@link Shape#contains(float, float)}.
     *
     * @param x the point's position along x-axis
     * @param y the point's position along y-axis
     * @return true if this View contains the given point
     */

    public boolean contains(float x, float y) {
        return (shape == null || useAABB)
                ? Collider.AABB(bounds[0], bounds[1], bounds[2], bounds[3], x, y, 1, 1)
                : shape.contains(x, y);
    }

    /**
     * Pointers handler used to track clicks, buttons and actions
     */

    public static final class PointerEvent {
        public static final int NULL = Integer.MAX_VALUE;

        private Pointer pointer;

        private final Timer durationTimer;
        private final Timer pressedTimer;

        private int clicks = 0;
        private int cumulativeClicks = 0;

        private int xMouse = NULL;
        private int yMouse = NULL;
        private int xStart = NULL;
        private int yStart = NULL;
        private int xStartPressed = NULL;
        private int yStartPressed = NULL;

        private float pressedTime = 0f;
        private float durationTime = 0f;

        private PointerEvent() {
            pointer = null;
            durationTimer = new Timer();
            pressedTimer = new Timer();
        }

        /**
         * Clear this handler
         */

        private void clear() {
            pointer = null;

            xStart = yStart = NULL;
            xMouse = yMouse = NULL;
            xStartPressed = yStartPressed = NULL;

            clicks = 0;

            pressedTime = 0f;
            durationTime = 0f;
        }

        /**
         * Compute a pointer and its attributes
         *
         * @param e    the pointer used by this view
         * @param xOff the pointer position offset along x-axis
         * @param yOff the pointer position offset along y-axis
         */

        private void update(Pointer e, int xOff, int yOff) {
            pointer = e;

            int x = e.getX() + xOff;
            int y = e.getY() + yOff;

            if (xStart == NULL && yStart == NULL) {
                xStart = x;
                yStart = y;
                // reset only here!
                durationTimer.reset();

                // only if pointer is already pressed and enters this view
                if (e.getAction() == Pointer.DRAGGED) {
                    xStartPressed = x;
                    yStartPressed = y;
                    pressedTimer.reset();
                }
            }

            xMouse = x;
            yMouse = y;
            durationTime = durationTimer.seconds();

            switch (e.getAction()) {
                case Pointer.PRESSED:
                    if (e.getPressedButtonSize() == 0) {
                        xStartPressed = x;
                        yStartPressed = y;
                        pressedTimer.reset();
                    }
                    break;
                case Pointer.RELEASED:
                case Pointer.DRAGGED:
                    pressedTime = pressedTimer.seconds();
                    break;
                case Pointer.CLICKED:
                    clicks++;
                    cumulativeClicks++;
                    break;
            }
        }

        /**
         * @return pointer's position along x-axis
         */

        public int getX() {
            return xMouse;
        }

        /**
         * @return pointer's position along y-axis
         */

        public int getY() {
            return yMouse;
        }

        /**
         * @return the last handled action
         */

        public int getAction() {
            return pointer.getAction();
        }

        /**
         * If no button has been used, it returns 0
         *
         * @return the button used to perform the action
         */

        public int getActionButton() {
            return pointer.getButton();
        }

        /**
         * @return the amount of currently pressed buttons
         */

        public int getPressedButtonSize() {
            return pointer.getPressedButtonSize();
        }

        /**
         * @param id the button id between {1,2,3}
         * @return true if the given button is currently pressed
         */

        public boolean isButtonPressed(int id) {
            return pointer.isButtonPressed(id);
        }

        /**
         * @return the number of clicks
         */

        public int getClicks() {
            return clicks;
        }

        /**
         * @return the number of clicks since the handler creation
         */

        public int getCumulativeClicks() {
            return cumulativeClicks;
        }

        /**
         * @return the distance along x-axis between mouse pointer and the first mouse entry
         */

        public float getDistX() {
            return xMouse - xStart;
        }

        /**
         * @return the distance along y-axis between mouse pointer and the first mouse entry
         */

        public float getDistY() {
            return yMouse - yStart;
        }

        /**
         * Note that if no button is pressed {@link PointerEvent#NULL} is returned
         *
         * @return the distance along x-axis between mouse pointer and the first pressed position
         */

        public float getDistXPressed() {
            return getPressedButtonSize() > 0 ? (xMouse - xStartPressed) : NULL;
        }

        /**
         * Note that if no button is pressed {@link PointerEvent#NULL} is returned
         *
         * @return the distance along y-axis between mouse pointer and the first pressed position
         */

        public float getDistYPressed() {
            return getPressedButtonSize() > 0 ? (yMouse - yStartPressed) : NULL;
        }

        /**
         * @return the elapsed seconds from the beginning of this event
         */

        public float getTime() {
            return durationTime;
        }

        /**
         * @return the seconds spent in mouse pressing
         */

        public float getTimePressed() {
            return pressedTime;
        }

        /**
         * @return true if this handler is currently used
         */

        public boolean isUsed() {
            return xMouse != NULL && yMouse != NULL;
        }

        /**
         * @return the mouse wheeling value
         */

        public int getWheelRotation() {
            return pointer.getWheelRotation();
        }

        /**
         * @return true if mouse is wheeling
         */

        public boolean isMouseWheeling() {
            return pointer.isWheeling();
        }

        /**
         * @return true if mouse is pressed
         */

        public boolean isMousePressed() {
            return getPressedButtonSize() > 0;
        }

        /**
         * @return true if a button has been released
         */

        public boolean isMouseReleased() {
            return pointer.getAction() == pointer.RELEASED;
        }

        /**
         * @return true if all buttons have been released
         */

        public boolean areButtonsReleased() {
            return isMouseReleased() && getPressedButtonSize() == 0;
        }
    }
}
