package uia.physical.group;

import uia.core.shape.Shape;
import uia.core.basement.Message;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.Graphics;
import uia.physical.component.WrapperView;
import uia.physical.message.EventKeyMessage;
import uia.physical.message.EventTouchScreenMessage;
import uia.physical.group.utility.GroupUtility;

import java.util.*;

/**
 * Implementation of {@link ViewGroup}.
 * <br>
 * Implementation choices:
 * <ul>
 *     <li>
 *         the clipping functionality is enabled at build time. If you want to disable it, use the {@link #setClip(boolean)}
 *         method;
 *     </li>
 *     <li>
 *         the last added View is the first to handle events.
 *     </li>
 * </ul>
 */

public final class ComponentGroup extends WrapperView implements ViewGroup {
    private final List<View> views;

    private final Shape clipShape;

    private final float[] contentBounds = {0f, 0f, 0f, 0f};

    private boolean clip = true;

    public ComponentGroup(View view) {
        super(view);

        views = new ArrayList<>();

        clipShape = new Shape();
        clipShape.setGeometry(getGeometry());
    }

    @Override
    public void requestFocus(boolean request) {
        super.requestFocus(request);

        if (!request) {
            // removes children focus
            for (View view : this) {
                view.requestFocus(false);
            }
        }
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);

        if (!visible) {
            // removes children focus
            for (View view : this) {
                view.requestFocus(false);
            }
        }
    }

    @Override
    public void readMessage(Message message) {
        if (message instanceof EventTouchScreenMessage) {
            GroupUtility.dispatchScreenTouchMessageToChildren(this, message);
            super.readMessage(message);
        } else if (message instanceof EventKeyMessage) {
            GroupUtility.dispatchKeyMessageToChildren(this, message);
            super.readMessage(message);
        } else {
            super.readMessage(message);
            GroupUtility.dispatchMessageToChildren(this, message);
        }
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
    public boolean insert(int index, View view) {
        Objects.requireNonNull(view);
        if (index < 0 || index > size()) {
            throw new IndexOutOfBoundsException("the given index is out of bounds for the group with ID = " + getID());
        }

        boolean result = false;
        if (!views.contains(view)) {
            views.add(index, view);
            result = true;
        }
        return result;
    }

    @Override
    public boolean remove(View view) {
        return views.remove(view);
    }

    @Override
    public void removeAll() {
        views.clear();
        Arrays.fill(contentBounds, 0);
    }

    /**
     * Helper function. Update group boundaries.
     */

    private void updateGroupBounds() {
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);

            if (isVisible()) view.update(this);

            float[] bounds = view.getBounds();
            float xi = bounds[0], yi = bounds[1];

            if (i == 0) {
                contentBounds[0] = contentBounds[2] = xi;
                contentBounds[1] = contentBounds[3] = yi;
            }
            if (xi < contentBounds[0]) contentBounds[0] = xi;
            if (yi < contentBounds[1]) contentBounds[1] = yi;
            if (xi + bounds[2] > contentBounds[2]) contentBounds[2] = xi + bounds[2];
            if (yi + bounds[3] > contentBounds[3]) contentBounds[3] = yi + bounds[3];
        }

        contentBounds[2] -= contentBounds[0];
        contentBounds[3] -= contentBounds[1];
    }

    /**
     * Helper function. Update clip shape position, dimension and rotation.
     */

    private void updateClipShape() {
        float[] bounds = getBounds();
        clipShape.setPosition(bounds[0] + bounds[2] / 2f, bounds[1] + bounds[3] / 2f);
        clipShape.setDimension(getWidth(), getHeight());
        clipShape.setRotation(bounds[4]);
    }

    @Override
    public void update(View parent) {
        super.update(parent);
        updateGroupBounds();
        updateClipShape();
    }

    @Override
    public void draw(Graphics graphics) {
        super.draw(graphics);

        if (isVisible()) {
            if (clip) {
                graphics.setClip(clipShape);
            }
            for (View i : views) {
                i.draw(graphics);
            }
            if (clip) {
                graphics.restoreClip();
            }
        }
    }

    @Override
    public Iterator<View> iterator() {
        return views.iterator();
    }

    private final float[] copyBounds = new float[5];

    @Override
    public float[] boundsContent() {
        System.arraycopy(contentBounds, 0, copyBounds, 0, contentBounds.length);
        return copyBounds;
    }

    @Override
    public int size() {
        return views.size();
    }

    @Override
    public int indexOf(View view) {
        return views.indexOf(view);
    }

    @Override
    public View get(int index) {
        View result = null;
        if (index >= 0 && index < size()) {
            result = views.get(index);
        }
        return result;
    }

    @Override
    public View get(String viewID) {
        for (View view : views) {
            if (view.getID().equals(viewID)) {
                return view;
            }
        }
        return null;
    }
}
