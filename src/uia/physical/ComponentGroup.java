package uia.physical;

import uia.core.Shape;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ScreenPointer;
import uia.core.ui.Graphic;
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

public final class ComponentGroup extends WrapperView implements ViewGroup {
    private final List<View> views;

    private final Shape clipShape;

    private final float[] cBound = {0f, 0f, 0f, 0f};

    private boolean clip = true;

    public ComponentGroup(View view) {
        super(view);

        views = new ArrayList<>();

        clipShape = new Shape();
        clipShape.setGeometry(getGeometry());
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
    public void add(int i, View view) {
        if (!views.contains(view)) views.add(i, view);
    }

    @Override
    public void add(View... views) {
        for (View i : views) {
            add(size(), i);
        }
    }

    @Override
    public void remove(View view) {
        views.remove(view);
    }

    @Override
    public void removeAll() {
        views.clear();
        Arrays.fill(cBound, 0);
    }

    private final List<ScreenPointer> iScreenPointers = new ArrayList<>();

    @Override
    public void dispatch(DISPATCHER dispatcher, Object data) {
        switch (dispatcher) {
            case MESSAGE:
                super.dispatch(dispatcher, data);
                for (int i = views.size() - 1; i >= 0; i--) {
                    views.get(i).dispatch(dispatcher, data);
                }
                break;

            case KEY:
                if (isVisible()) {
                    for (int i = views.size() - 1; i >= 0; i--) {
                        views.get(i).dispatch(dispatcher, data);
                    }
                }
                super.dispatch(dispatcher, data);
                break;

            case POINTERS:
                List<ScreenPointer> screenPointers = (List<ScreenPointer>) data;

                iScreenPointers.clear();

                if (isVisible()) {
                    screenPointers.forEach(p -> {
                        if (!clip || ComponentGroup.this.contains(p.getX(), p.getY())) iScreenPointers.add(p);
                    });
                }

                for (int i = views.size() - 1; i >= 0; i--) {
                    views.get(i).dispatch(dispatcher, iScreenPointers);
                }

                super.dispatch(dispatcher, data);
                break;

            default:
                super.dispatch(dispatcher, data);
                break;
        }
    }

    private void updateGroupFocus(View parent) {
        int i = 0, size = views.size();
        while (i < size && !views.get(i).isOnFocus()) i++;
        if (i < size) requestFocus(parent.isOnFocus());
    }

    /**
     * Update group boundaries
     */

    private void updateGroupBounds() {
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);

            if (isVisible()) view.update(this);

            float[] bounds = view.bounds();
            float xi = bounds[0], yi = bounds[1];

            if (i == 0) {
                cBound[0] = cBound[2] = xi;
                cBound[1] = cBound[3] = yi;
            }
            if (xi < cBound[0]) cBound[0] = xi;
            if (yi < cBound[1]) cBound[1] = yi;
            if (xi > cBound[2]) cBound[2] = xi + bounds[2];
            if (yi > cBound[3]) cBound[3] = yi + bounds[3];
        }

        cBound[2] = cBound[2] - cBound[0];
        cBound[3] = cBound[3] - cBound[1];
    }

    /**
     * Update clip shape position, dimension and rotation
     */

    private void updateClipShape() {
        float[] bounds = bounds();

        clipShape.setPosition(bounds[0] + bounds[2] / 2f, bounds[1] + bounds[3] / 2f);
        clipShape.setDimension(getWidth(), getHeight());
        clipShape.setRotation(bounds[4]);
    }

    @Override
    public void update(View parent) {
        super.update(parent);

        updateGroupFocus(parent);
        updateGroupBounds();
        updateClipShape();
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        if (isVisible()) {

            if (clip) graphic.setClip(clipShape);

            for (View i : views) {
                i.draw(graphic);
            }

            if (clip) graphic.restoreClip();
        }
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
    public View get(String id) {
        for (View i : views) {
            if (i.getID().equals(id)) return i;
        }
        return null;
    }

    private final float[] copyBounds = new float[5];

    @Override
    public float[] boundsContent() {
        System.arraycopy(cBound, 0, copyBounds, 0, cBound.length);
        return copyBounds;
    }

    @Override
    public Iterator<View> iterator() {
        return views.iterator();
    }
}
