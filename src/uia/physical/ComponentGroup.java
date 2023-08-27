package uia.physical;

import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.Key;
import uia.core.Pointer;
import uia.core.basement.Graphic;
import uia.physical.wrapper.WrapperView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Implementation of {@link ViewGroup}.
 * <br>
 * Note that the first View to handle events is the last added one.
 */

public final class ComponentGroup extends WrapperView implements ViewGroup, Iterable<View> {
    private final List<View> views;

    private final float[] contentBound = {0f, 0f, 0f, 0f};

    private boolean clip = true;

    public ComponentGroup(View view) {
        super(view);

        views = new ArrayList<>();
    }

    @Override
    public void setClip(boolean clipRegion) {
        clip = clipRegion;
    }

    @Override
    public boolean hasClip() {
        return clip;
    }

    @Override
    public void add(View... views) {
        for (View i : views) {
            add(size(), i);
        }
    }

    @Override
    public void add(int i, View view) {
        if (!views.contains(view)) views.add(i, view);
    }

    @Override
    public void remove(View view) {
        views.remove(view);
    }

    @Override
    public void removeAll() {
        views.clear();
        Arrays.fill(contentBound, 0);
    }

    /**
     * Change the visibility of this group and of all the attached views
     *
     * @param visible true to set this group visible
     */

    @Override
    public void setVisible(boolean visible) {
        for (int i = views.size() - 1; i >= 0; i--) {
            views.get(i).setVisible(visible);
        }

        super.setVisible(visible);
    }

    @Override
    public void dispatch(Object message, String sourceID, String destID) {
        super.dispatch(message, sourceID, destID);

        for (int i = 0; i < size(); i++) {
            get(i).dispatch(message, sourceID, destID);
        }
    }

    @Override
    public void dispatch(Key key) {
        for (int i = views.size() - 1; i >= 0; i--) {
            views.get(i).dispatch(key);
        }
        super.dispatch(key);
    }

    private final List<Pointer> iPointers = new ArrayList<>();

    @Override
    public void dispatch(List<Pointer> pointers) {
        iPointers.clear();

        if (clip) {
            pointers.forEach(p -> {
                if (ComponentGroup.this.contains(p.getX(), p.getY())) iPointers.add(p);
            });
        } else {
            iPointers.addAll(pointers);
        }

        for (int i = views.size() - 1; i >= 0; i--) {
            views.get(i).dispatch(iPointers);
        }

        super.dispatch(pointers);
    }

    private final float[] params = new float[8];

    @Override
    public void update(View parent) {
        super.update(parent);

        // enable the group's focus if, at least, one view is on focus
        int i = 0, size = views.size();
        while (i < size && !views.get(i).isFocused()) i++;
        if (i < size) setFocus(parent.isFocused());

        Arrays.fill(params, 0);

        for (View view : views) {
            view.update(this);

            float[] bounds = view.bounds();
            float xi = bounds[0];
            float yi = bounds[1];

            // update bounds along x-axis
            if (xi < params[0]) {
                params[0] = xi;
                params[1] = 0.5f * bounds[2];
            }
            if (xi > params[4]) {
                params[4] = xi;
                params[5] = 0.5f * bounds[2];
            }

            // update bounds along y-axis
            if (yi < params[2]) {
                params[2] = yi;
                params[3] = 0.5f * bounds[3];
            }
            if (yi > params[6]) {
                params[6] = xi;
                params[7] = 0.5f * bounds[3];
            }
        }

        // update limits
        contentBound[0] = params[0] - params[1];
        contentBound[1] = params[2] - params[3];
        contentBound[2] = params[4] + params[5];
        contentBound[3] = params[6] + params[7];
    }

    @Override
    public void draw(Graphic graphic) {
        if (clip) graphic.setClip(getGeom());

        super.draw(graphic);

        for (View i : views) {
            i.draw(graphic);
        }

        if (clip) graphic.restoreClip();
    }

    @Override
    public int size() {
        return views.size();
    }

    @Override
    public View get(int i) {
        return views.get(i);
    }

    @Override
    public View get(String name) {
        for (View i : views) {
            if (i.getID().equals(name)) return i;
        }
        return null;
    }

    @Override
    public Iterator<View> iterator() {
        return views.iterator();
    }

    @Override
    public float[] boundsContent() {
        return contentBound;
    }
}
