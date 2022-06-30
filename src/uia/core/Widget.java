package uia.core;

import uia.core.policy.Context;
import uia.core.policy.Render;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/**
 * Widget bridge object.
 * <br>
 * This bridge is intended to simplify the creation of custom widgets without worry about internal details.
 * <br>
 * A top-down approach has been adopted, meaning that, the first View to receive pointers and keys, is the one on top.
 * <br>
 * <b>Note that View's Context will be updated only in rendering time.</b>
 */

public abstract class Widget extends View implements Iterable<View> {
    private final List<View> views;

    public Widget(Context context,
                  float px, float py,
                  float dx, float dy) {
        super(context, px, py, dx, dy);

        views = new ArrayList<>(1);
    }

    /**
     * Add a new View to this Widget
     *
     * @param view a not null {@link View} or its subclass
     */

    public final void add(View view) {
        if (view != null)
            views.add(view);
    }

    /**
     * Remove all views handled by widget or its subclasses
     */

    public void clear() {
        views.clear();
    }

    /**
     * Remove a View from this Widget
     *
     * @param i the position of the {@link View} to remove
     */

    public final void remove(int i) {
        if (i >= 0 && i < views.size())
            views.remove(i);
    }

    /**
     * @return the number of handled views
     */

    public final int size() {
        return views.size();
    }

    /**
     * @param view a not null {@link View}
     * @return the position of the given View
     */

    public final int indexOf(View view) {
        return views.indexOf(view);
    }

    /**
     * @param i the position of the {@link View} to return
     * @return the specified View or null
     */

    public final View get(int i) {
        return (i >= 0 && i < views.size()) ? views.get(i) : null;
    }

    /**
     * @return the last {@link View} or null
     */

    public final View peek() {
        int size = views.size();
        return size > 0 ? views.get(size - 1) : null;
    }

    /**
     * Modify a specified View
     *
     * @param i        the position of the View to modify
     * @param consumer a function used to modify the View
     */

    // @Test
    public final <T extends View> void modify(int i, Consumer<T> consumer) {
        View v = get(i);

        if (v != null)
            consumer.accept((T) v);
    }

    @Override
    protected void focusDispatcher(boolean gained) {
        for (int i = views.size() - 1; i >= 0; i--) {
            views.get(i).focusDispatcher(gained);
        }

        super.focusDispatcher(gained);
    }

    @Override
    protected void pointerDispatcher(boolean update) {
        for (int i = views.size() - 1; i >= 0; i--) {
            View.updatePointerDispatcher(views.get(i), update);
        }

        super.pointerDispatcher(update);
    }

    @Override
    protected void keyDispatcher(boolean update) {
        for (int i = views.size() - 1; i >= 0; i--) {
            View.updateKeyDispatcher(views.get(i), update);
        }

        super.keyDispatcher(update);
    }

    @Override
    protected void postDraw(Render render) {
        super.postDraw(render);

        float px = px();
        float py = py();
        Context context = getContext();

        for (View i : views) {

            // Update Views Context
            if (!context.equals(i.getContext()))
                i.setContext(context);

            i.setCenter(px, py);
            i.draw(render);
        }
    }

    @Override
    public Iterator<View> iterator() {
        return views.iterator();
    }
}
