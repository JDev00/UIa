package uia.physical.group;

import uia.physical.group.utility.GroupLayoutUtility;
import uia.physical.message.EventTouchScreenMessage;
import uia.physical.group.utility.GroupUtility;
import uia.physical.message.EventKeyMessage;
import uia.physical.component.WrapperView;
import uia.core.basement.Message;
import uia.core.ui.ViewGroup;
import uia.core.shape.Shape;
import uia.core.ui.Graphics;
import uia.core.ui.View;

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

    private float[] boundaries = {0f, 0f, 0f, 0f};

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
        Arrays.fill(boundaries, 0);
    }

    /**
     * Helper function. Updates views and their boundaries.
     */

    private void updateGroup() {
        if (isVisible()) {
            for (View view : views) {
                view.update(this);
            }
        }

        // updates boundaries
        boundaries = GroupLayoutUtility.measureBoundaries(views);
    }

    /**
     * Helper function. Updates clip shape position, dimension and rotation.
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
        updateGroup();
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
        System.arraycopy(boundaries, 0, copyBounds, 0, boundaries.length);
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
