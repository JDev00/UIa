package uia.core;

import uia.core.animator.Animator;
import uia.core.event.*;
import uia.core.event.Event;
import uia.core.animator.LinearAnimator;
import uia.core.utility.KeyEnc;
import uia.core.utility.Pointer;
import uia.core.figure.Figure;
import uia.core.figure.RectSmooth;
import uia.utils.Timer;
import uia.utils.Utils;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

import static java.lang.Math.*;

/**
 * Primitive UI object <b>designed for inheritance</b>, and start point for every UI component, used to
 * render and animate a shape (smooth rect by default) and handling events.
 * <br>
 * Some additional points:
 * <br>
 * 1) a view is always considered visible (even when its dimension is (0,0)) except when user/programmer explicitly declare it
 * not visible through the use of {@link View#setVisible(boolean)}
 * <br>
 * 2) a view is never on focus by default. Focus is handled automatically by the framework, but for convenience there are 2 method to
 * try to handle it manually:
 * <br>
 * &nbsp;&nbsp;a) use {@link View#requestFocus()} to put it on focus
 * <br>
 * &nbsp;&nbsp;b) use {@link View#removeFocus()} to remove focus
 * <br>
 * 3) {@link View#update()} method is designed to be overridden, in addition it is always executed regardless of the view state (visible/not visible)
 * <br>
 * 4) <b>view's animators work only if it is visible</b>
 * <br>
 * 5) view automatically scales and translates according to screen resize.
 * This functionality could create some problems for user defined widget. If you need absolute position or dimension, use:
 * <br>
 * &nbsp;&nbsp;a) {@link View#nativePx()} or {@link View#nativePy()} to access position without artifacts
 * <br>
 * &nbsp;&nbsp;a) {@link View#nativeDx()} or {@link View#nativeDy()} to access dimension without artifacts
 */

// @Add Figure rotation
// @Test
public class View {

    /**
     * Animator types
     */

    public enum ANIMATOR_TYPE {POS, DIM, ROT}

    private static final int ANIMATOR_POS = 0;
    private static final int ANIMATOR_DIM = 1;
    private static final int ANIMATOR_ROT = 2;

    /*
     * Mouse attributes, Mouse handler and key management
     */

    private boolean consumePointer = true;
    private boolean consumeKey = true;

    private final MotionEvent motionTouch;

    private KeyEnc keyEnc;

    /*
     * Animator & events handler
     */

    private final Animator[] animator;

    private final EventHandler<View> eventHandler;

    /*
     * Graphics
     */

    private Figure figure;

    private Color color;

    private Image image;

    /*
     * Behaviour
     */

    private Context context;

    private float px;
    private float py;
    private float dx;
    private float dy;
    private float rot;

    private float xCenter;
    private float yCenter;
    private float xScale = 1f;
    private float yScale = 1f;
    private float xLowerBound;
    private float yLowerBound;
    private float xUpperBound;
    private float yUpperBound;

    private float expansionTime = 0.125f;
    private float xExpanseLimit = 1.01f;
    private float yExpanseLimit = 1.01f;
    private float xExpanse = 1f;
    private float yExpanse = 1f;

    private float xScaleImg = 1f;
    private float yScaleImg = 1f;

    private boolean over = false;
    private boolean focus = false;
    private boolean visible = true;
    private boolean imgVisible = true;
    private boolean autoAdjustment = true;

    public View(Context context,
                float px, float py,
                float dx, float dy) {
        this.context = Context.validateContext(context);

        this.px = px;
        this.py = py;
        this.dx = dx;
        this.dy = dy;

        updateBounds();

        motionTouch = new MotionEvent();

        eventHandler = new EventHandler<>();

        figure = RectSmooth.create(5, 0.1f);

        animator = new Animator[]{
                new LinearAnimator(),
                new LinearAnimator(),
                new LinearAnimator()
        };

        color = Color.WHITE;
    }

    /*
     *
     * Context
     *
     */

    /**
     * Set the view {@link Context}
     *
     * @param context a not null Context
     */

    void setContext(Context context) {
        if (context != null)
            this.context = context;
    }

    /*
     *
     * Events
     *
     */

    /**
     * Add a new event
     *
     * @param event a not null {@link Event}
     */

    public final void addEvent(Event<View> event) {
        eventHandler.add(event);
    }

    /**
     * Update the specified set of events
     *
     * @param event the event type
     * @param state the event state
     */

    protected final void updateEvent(Class<?> event, int state) {
        eventHandler.update(event, this, state);
    }

    /**
     * @return the event handler used by this view
     */

    public final EventHandler<View> getEventHandler() {
        return eventHandler;
    }

    /*
     *
     * Animator
     *
     */

    private int mapAnimator(ANIMATOR_TYPE animatorType) {
        switch (animatorType) {
            case POS:
                return ANIMATOR_POS;
            case DIM:
                return ANIMATOR_DIM;
            case ROT:
                return ANIMATOR_ROT;
            default:
                return -1;
        }
    }

    /**
     * Set up the specified animator
     *
     * @param animatorType the animator type; take a look at {@link ANIMATOR_TYPE}
     * @param setup        a function with the animation code
     */

    public final void setupAnimator(ANIMATOR_TYPE animatorType, Consumer<Animator> setup) {
        if (setup != null && animatorType != null)
            setup.accept(animator[mapAnimator(animatorType)]);
    }

    /**
     * Replace an animator with a new one
     *
     * @param animatorType the animator to replace
     * @param animator     a not null {@link Animator}
     */

    public final void setAnimator(ANIMATOR_TYPE animatorType, Animator animator) {
        if (animator != null && animatorType != null)
            this.animator[mapAnimator(animatorType)] = animator;
    }

    /**
     * @param animatorType the animator type; take a look at {@link ANIMATOR_TYPE}
     * @return if exists the animator otherwise null
     */

    public final Animator getAnimator(ANIMATOR_TYPE animatorType) {
        return (animatorType == null) ? null : animator[mapAnimator(animatorType)];
    }

    /*
     *
     * Native
     *
     */

    /**
     * Set an <u>offset position</u>.
     * <b>Method used to translate view behind the scene without use setPos() and translate() methods.</b>
     *
     * @param x the offset position along x-axis
     * @param y the offset position along y-axis
     */

    final void setCenter(float x, float y) {
        xCenter = x;
        yCenter = y;
    }

    /**
     * Set the position of this view
     *
     * @param x the new position along x-axis
     * @param y the new position along y-axis
     */

    public void setPos(float x, float y) {
        px = x;
        py = y;
    }

    /**
     * Translate this view
     *
     * @param x the translation along x-axis
     * @param y the translation along y-axis
     */

    public void translate(float x, float y) {
        px += x;
        py += y;
    }

    /**
     * Update view bounds
     */

    private void updateBounds() {
        xLowerBound = xScale * dx;
        yLowerBound = yScale * dy;
        xUpperBound = xScale * dx * xExpanseLimit;
        yUpperBound = yScale * dy * yExpanseLimit;
    }

    /**
     * Set a static dimension for this view
     *
     * @param x the new dimension along x-axis; if {@code x < 0}, x will be set to 0
     * @param y the new dimension along y-axis; if {@code y < 0}, y will be set to 0
     */

    public void setDim(float x, float y) {
        dx = max(0, x);
        dy = max(0, y);

        updateBounds();
    }

    /**
     * Scale this view
     *
     * @param x the scale along x-axis; if {@code x < 0}, x will be set to 0
     * @param y the scale along y-axis; if {@code y < 0}, y will be set to 0
     */

    public void scale(float x, float y) {
        dx *= max(0, x);
        dy *= max(0, y);

        updateBounds();
    }

    /**
     * Set the scale of this view
     *
     * @param x the scale along x-axis; if {@code x < 0}, x will be set to 0
     * @param y the scale along y-axis; if {@code y < 0}, y will be set to 0
     */

    public void setScale(float x, float y) {
        xScale = max(0, x);
        yScale = max(0, y);

        updateBounds();
    }

    /**
     * Set the rotation of this view
     *
     * @param radians the radians used to rotate this view
     */

    public void setRot(float radians) {
        rot = radians;
    }

    /**
     * Sum the given radians to the current rotation
     *
     * @param radians the radians used to rotate this view
     */

    public void rotate(float radians) {
        rot += radians;
    }

    /**
     * Set the view expansion animation.
     * <b>The animation starts when user is over this view.</b>
     *
     * @param x a value between [0, 1] used to define expansion along x-axis
     * @param y a value between [0, 1] used to define expansion along y-axis
     */

    public void setExpansion(float x, float y) {
        xExpanseLimit = 1f + Utils.constrain(x, 0, 1);
        yExpanseLimit = 1f + Utils.constrain(y, 0, 1);

        updateBounds();
    }

    /**
     * Reset the expanse animation
     */

    public void resetAnimation() {
        xExpanse = 1f;
        yExpanse = 1f;
    }

    /**
     * Set the required time to complete the expansion animation
     *
     * @param seconds the seconds required to end up the animation; if {@code time < 0}, time will be set to 0
     */

    public void setExpansionTime(float seconds) {
        expansionTime = max(0, seconds);
    }

    /**
     * Set the {@link Figure} to render
     *
     * @param figure a figure; it could be null
     */

    public void setFigure(Figure figure) {
        this.figure = figure;
    }

    /**
     * Set the figure color
     *
     * @param color a not null color
     */

    public void setColor(Color color) {
        if (color != null)
            this.color = color;
    }

    /**
     * @return the figure color
     */

    public final Color getColor() {
        return color;
    }

    /**
     * Request to this view to put on focus, that it means that it will be allowed to interact with user.
     * <b>Note that this operation will be done only if the view is visible and <u>isn't already on focus</u>.</b>
     */

    public void requestFocus() {
        if (!focus && isVisible()) {
            focus = true;
            updateEvent(State.class, State.FOCUS_GAINED);
        }
    }

    /**
     * Remove focus from this view.
     * <b>Note that this operation will be done only if this view is on focus.</b>
     */

    public void removeFocus() {
        if (focus) {
            focus = false;
            updateEvent(State.class, State.FOCUS_LOST);
        }
    }

    /**
     * Display or hide this view.
     * <b>Note that when view is set to be not visible, focus will be lost.
     * In particular, {@link View#removeFocus()} will be automatically invoked.</b>
     *
     * @param isVisible true to set this view visible
     */

    public void setVisible(boolean isVisible) {
        visible = isVisible;

        if (!visible) {
            over = false;
            removeFocus();
        }
    }

    /**
     * Overlay functionality, when enabled, acts as a wall for pointers.
     * This functionality, <u>enabled by default</u>, is useful when multiple views overlay. The view on top
     * consumes pointers inside its area with the consequence that the rest of views can't use the same pointers to interact with user.
     *
     * @param overlay true to consume pointers
     */

    public void enableOverlay(boolean overlay) {
        consumePointer = overlay;
    }

    /**
     * Key consumer functionality, <u>enabled by default</u>, consumes the last keyEvent that go through this view with the consequence that
     * all other views (inside a page) can't process the keyEvent.
     * <b>This functionality has been studied to allow just one view at a time to accept keys from keyboard.</b>
     *
     * @param consumeKey true to consume keyEvent
     */

    public void enableKeyConsumer(boolean consumeKey) {
        this.consumeKey = consumeKey;
    }

    /**
     * Enable or disable the auto-adjustment functionality.
     * <b>Auto-Adjustment is the ability of a view to change its position and dimension according to screen resize.</b>
     *
     * @param autoAdjustment true to enable auto-adjustment functionality
     */

    public void enableAutoAdjustment(boolean autoAdjustment) {
        this.autoAdjustment = autoAdjustment;
    }

    /**
     * Display or hide the image
     *
     * @param enableImage true to display the image
     */

    public void setImageVisible(boolean enableImage) {
        imgVisible = enableImage;
    }

    /**
     * Set an image to this view
     *
     * @param image an {@link Image} to render
     */

    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Scale image
     *
     * @param x a value between [0, 1] used to scale along x-axis
     * @param y a value between [0, 1] used to scale along y-axis
     */

    public void setScaleImg(float x, float y) {
        xScaleImg = Utils.constrain(x, 0, 1);
        yScaleImg = Utils.constrain(y, 0, 1);
    }

    /*
     *
     * Focus dispatch
     *
     */

    /**
     * Method automatically invoked by the framework when Window gains or loses focus.
     */

    protected void focusDispatcher(boolean gained) {
        if (!gained)
            removeFocus();
    }

    /*
     *
     * Mouse dispatcher & motion event
     *
     */

    private int tClicks = 0;

    /**
     * Mouse and event handler invoked automatically by the framework.
     * <b>Note that when view is not visible no event is executed.<b>
     * <b>If a null pointer is passed in, focus will be automatically lost.</b>
     *
     * @param p      a {@link Pointer}; if null, focus will be lost
     * @param update true to update touch dispatcher
     */

    protected void touchDispatcher(Pointer p, boolean update) {
        boolean compute = update
                && (p != null && !p.isConsumed())
                && isVisible();

        if (compute
                && p.getAction() != MouseEvent.MOUSE_EXITED
                && containsPoint(p.getX(), p.getY())) {

            // update motion event
            motionTouch.update(p, (int) (-px() + 0.5f * dx()), (int) (-py() + 0.5f * dy()));

            // Consume pointer when consumeTouch is enabled
            if (consumePointer)
                p.consume();

            // Put on focus and update FOCUS_GAINED
            if (!focus && p.getAction() == MouseEvent.MOUSE_PRESSED) {
                focus = true;
                updateEvent(State.class, State.FOCUS_GAINED);
            }

            // Update ENTER
            if (!over) {
                over = true;
                updateEvent(Mouse.class, Mouse.ENTER);
            } else {
                // Update HOVER
                updateEvent(Mouse.class, Mouse.HOVER);
            }

            // Update CLICK
            if (motionTouch.getCumulativeClicks() != tClicks) {
                tClicks = motionTouch.getCumulativeClicks();
                updateEvent(Mouse.class, Mouse.CLICK);
            }
        } else {

            // Remove focus and update FOCUS_LOST
            if (focus && (p == null || p.getAction() == MouseEvent.MOUSE_PRESSED)) {
                focus = false;
                updateEvent(State.class, State.FOCUS_LOST);
            }

            // Update LEAVE
            if (over) {
                over = false;

                if (compute)
                    updateEvent(Mouse.class, Mouse.LEAVE);

                motionTouch.clear();
            }
        }
    }

    /**
     * <b>Invoke this method only inside a {@link Mouse} event or its subclass.</b>
     *
     * @return the {@link MotionEvent} of this view
     */

    public final MotionEvent getMotionEvent() {
        return motionTouch;
    }

    /*
     *
     * Key dispatcher & keys
     *
     */

    /**
     * Key and event handler invoked automatically by the framework.
     * <b>Note that when view is not visible no event is executed.<b>
     *
     * @param k      the last system handled {@link KeyEnc}
     * @param update true to update key dispatcher
     */

    protected void keyDispatcher(KeyEnc k, boolean update) {
        if (update
                && (k != null && !k.isConsumed())
                && isVisible()
                && isFocused()) {
            keyEnc = k;

            if (consumeKey)
                k.consume();

            eventHandler.update(Key.class, this, k.getAction());
        }
    }

    /**
     * <b>Call this method only inside a {@link Key} event or its subclass.</b>
     *
     * @return the {@link KeyEnc} handled by this view
     */

    public final KeyEnc getKey() {
        return keyEnc;
    }

    /*
     *
     * View render
     *
     */

    /**
     * Method invoked automatically before {@link View#preDraw(Graphics2D)}.
     * <b>Note that it will be invoked even when view is not visible.</b>
     */

    protected void update() {

    }

    /**
     * Method invoked automatically before view rendering.
     * <b>Note that it will be invoked only when view is visible.</b>
     */

    protected void preDraw(Graphics2D canvas) {

    }

    /**
     * Method invoked automatically after view rendering.
     * <b>Note that it will be invoked only when view is visible.</b>
     */

    protected void postDraw(Graphics2D canvas) {

    }

    private float xFac;
    private float yFac;

    /**
     * Draw this view on screen
     *
     * @param canvas a not null {@link Graphics2D}
     */

    public final void draw(Graphics2D canvas) {
        // Execute the code before the drawing operation
        update();

        // Execute only if it is visible
        if (isVisible()) {

            // Update screen size factors
            xFac = autoAdjustment ? context.dxFactor() : 1;
            yFac = autoAdjustment ? context.dyFactor() : 1;

            // Update animators
            for (Animator i : animator) {
                i.update();
            }

            // Update the expanse animation
            float xq = (xExpanseLimit - 1f) / (60f * expansionTime);
            float yq = (yExpanseLimit - 1f) / (60f * expansionTime);

            if (over) {
                xExpanse = min(xExpanseLimit, xExpanse + xq);
                yExpanse = min(yExpanseLimit, yExpanse + yq);
            } else {
                xExpanse = max(1, xExpanse - xq);
                yExpanse = max(1, yExpanse - yq);
            }

            // Call preDraw before shape drawing
            preDraw(canvas);

            // Draw shape
            if (figure != null) {
                canvas.setColor(color);
                figure.setDim(dx(), dy());
                figure.setPos(px(), py());
                figure.draw(canvas);
            }

            // @Revision
            // Draw image
            if (image != null && imgVisible) {
                int ix = (int) (xScaleImg * dx());
                int iy = (int) (yScaleImg * dy());
                canvas.drawImage(image, (int) (px() - 0.5f * ix), (int) (py() - 0.5f * iy), ix, iy, null);
            }

            // Call postDraw after shape drawing
            postDraw(canvas);
        }
    }

    /**
     * @return the view's Context
     */

    public final Context getContext() {
        return context;
    }

    /**
     * @return position along x-axis
     */

    public final float px() {
        return xFac * px + xCenter + animator[ANIMATOR_POS].x();
    }

    /**
     * @return position along y-axis
     */

    public final float py() {
        return yFac * py + yCenter + animator[ANIMATOR_POS].y();
    }

    /**
     * @return view position along x-axis without scaling, offsets and animations
     */

    public final float nativePx() {
        return px;
    }

    /**
     * @return view position along y-axis without scaling, offsets and animations
     */

    public final float nativePy() {
        return py;
    }

    /**
     * @return dimension along x-axis
     */

    public final float dx() {
        return xExpanse * max(0, xFac * xScale * dx + animator[ANIMATOR_DIM].x());
    }

    /**
     * @return dimension along y-axis
     */

    public final float dy() {
        return yExpanse * max(0, yFac * yScale * dy + animator[ANIMATOR_DIM].y());
    }

    /**
     * @return view dimension along x-axis without scaling and animations
     */

    public final float nativeDx() {
        return dx;
    }

    /**
     * @return view dimension along y-axis without scaling and animations
     */

    public final float nativeDy() {
        return dy;
    }

    /**
     * @return rotation in radians
     */

    public final float rot() {
        return rot + animator[ANIMATOR_ROT].x();
    }

    /**
     * @return the minimum dimension along x-axis <b>without the dimension animator contribute</b>
     */

    public final float xLowerBound() {
        return xLowerBound;
    }

    /**
     * @return the minimum dimension along y-axis <b>without the dimension animator contribute</b>
     */

    public final float yLowerBound() {
        return yLowerBound;
    }

    /**
     * @return the maximum dimension along x-axis <b>without the dimension animator contribute</b>
     */

    public final float xUpperBound() {
        return xUpperBound;
    }

    /**
     * @return the maximum dimension along y-axis <b>without the dimension animator contribute</b>
     */

    public final float yUpperBound() {
        return yUpperBound;
    }

    /**
     * @return the image scale factor along x-axis
     */

    public final float imgScaleX() {
        return xScaleImg;
    }

    /**
     * @return the image scale factor along y-axis
     */

    public final float imgScaleY() {
        return yScaleImg;
    }

    /**
     * @return true if pointers are contained inside this view
     */

    public final boolean arePointersOver() {
        return over;
    }

    /**
     * @return true if overlay functionality is enabled
     */

    public final boolean hasOverlay() {
        return consumePointer;
    }

    /**
     * @return true if key consumer functionality is enabled
     */

    public final boolean hasKeyConsumer() {
        return consumeKey;
    }

    /**
     * @return true if auto-adjustment functionality is enabled
     */

    public final boolean hasAutoAdjustment() {
        return autoAdjustment;
    }

    /**
     * @return true if this view is visible
     */

    public final boolean isVisible() {
        return visible;
    }

    /**
     * @return true if image could be rendered
     */

    public final boolean isImageVisible() {
        return imgVisible;
    }

    /**
     * <b>Note that, when view is on focus, it must be visible.</b>
     *
     * @return true if this view is on focus
     */

    public final boolean isFocused() {
        return focus;
    }

    /**
     * @return true if this view has a not null figure
     */

    public final boolean hasFigure() {
        return figure != null;
    }

    /**
     * @return true if this view has a not null image
     */

    public final boolean hasImage() {
        return image != null;
    }

    /**
     * @return true if this view contains the given point
     */

    public final boolean containsPoint(float x, float y) {
        return figure != null && figure.contains(x, y);
    }

    /**
     * @return true if this view contains the given point
     */

    public final boolean containsPoint(double x, double y) {
        return figure != null && figure.contains(x, y);
    }

    /**
     * @return the view's figure
     */

    public final Figure getFigure() {
        return figure;
    }

    /**
     * @return if exists, the view's image, otherwise null
     */

    public final Image getImage() {
        return image;
    }

    /**
     * Manually set a Context to the given view
     * <b>Designed to be used in widget implementation.</b>
     *
     * @param view    a not null view
     * @param context a not null Context
     */

    public static void updateContext(View view, Context context) {
        if (view != null)
            view.setContext(context);
    }

    /**
     * Manually update touch dispatcher of the given view.
     * <b>Designed to be used in widget implementation.</b>
     *
     * @param view   a not null view
     * @param p      a pointer
     * @param update true to update touch dispatcher
     */

    public static void updateTouchDispatcher(View view,
                                             Pointer p, boolean update) {
        if (view != null)
            view.touchDispatcher(p, update);
    }

    /**
     * Manually update key dispatcher of the given view.
     * <b>Designed to be used in widget implementation.</b>
     *
     * @param view   a not null view
     * @param k      the last system handled {@link KeyEnc}
     * @param update true to update key dispatcher
     */

    public static void updateKeyDispatcher(View view,
                                           KeyEnc k, boolean update) {
        if (view != null)
            view.keyDispatcher(k, update);
    }

    /*
     *
     * MotionEvent
     *
     */

    /**
     * Mouse motion handler used to track clicks, buttons and actions
     */

    public static final class MotionEvent {
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

        private MotionEvent() {
            pointer = null;
            durationTimer = new Timer();
            pressedTimer = new Timer();
        }

        /**
         * Clear motion event
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

                // Only if pointer is already pressed and enters this view
                if (e.getAction() == MouseEvent.MOUSE_DRAGGED) {
                    xStartPressed = x;
                    yStartPressed = y;
                    pressedTimer.reset();
                }
            }

            xMouse = x;
            yMouse = y;
            durationTime = durationTimer.secondsFloat();

            switch (e.getAction()) {

                case MouseEvent.MOUSE_PRESSED:
                    if (e.getPressedButtonSize() == 0) {
                        xStartPressed = x;
                        yStartPressed = y;
                        pressedTimer.reset();
                    }
                    break;

                case MouseEvent.MOUSE_RELEASED:
                case MouseEvent.MOUSE_DRAGGED:
                    pressedTime = pressedTimer.secondsFloat();
                    break;

                case MouseEvent.MOUSE_CLICKED:
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
         * If no button has been used, it returns {@link MouseEvent#NOBUTTON}
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
         * @param id the button id: {@see {@link MouseEvent#BUTTON1}, {@link MouseEvent#BUTTON2},{@link MouseEvent#BUTTON3}}
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
         * @return the number of clicks since the motion event creation
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
         * Note that if no button is pressed {@link MotionEvent#NULL} is returned
         *
         * @return the distance along x-axis between mouse pointer and the first pressed position
         */

        public float getDistXPressed() {
            return getPressedButtonSize() > 0 ? (xMouse - xStartPressed) : NULL;
        }

        /**
         * Note that if no button is pressed {@link MotionEvent#NULL} is returned
         *
         * @return the distance along y-axis between mouse pointer and the first pressed position
         */

        public float getDistYPressed() {
            return getPressedButtonSize() > 0 ? (yMouse - yStartPressed) : NULL;
        }

        /**
         * @return the elapsed seconds from the beginning of this motion event
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
         * @return true if this motion event is currently used
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
            return pointer.getAction() == MouseEvent.MOUSE_RELEASED;
        }

        /**
         * @return true if all buttons have been released
         */

        public boolean areButtonsReleased() {
            return isMouseReleased() && getPressedButtonSize() == 0;
        }
    }
}
