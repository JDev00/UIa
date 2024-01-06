package uia.physical;

import uia.core.shape.Shape;
import uia.core.basement.Message;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ScreenTouch;
import uia.core.ui.Graphic;
import uia.physical.message.EventKeyMessage;
import uia.physical.message.EventTouchScreenMessage;
import uia.physical.message.Messages;

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
    public boolean insert(int i, View view) {
        boolean result = false;
        if (!views.contains(view)) {
            views.add(i, view);
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
        Arrays.fill(cBound, 0);
    }

    /**
     * Helper function. Dispatch a message to the group children
     */

    private void dispatchMessageToViews(Message message) {
        for (int i = views.size() - 1; i >= 0; i--) {
            views.get(i).dispatchMessage(message);
        }
    }

    /**
     * Helper function. Dispatch Key event message to the group children.
     */

    private void dispatchKeyMessage(Message message) {
        if (isVisible()) {
            for (int i = views.size() - 1; i >= 0; i--) {
                views.get(i).dispatchMessage(message);
            }
        }
    }

    private final List<ScreenTouch> screenTouches = new ArrayList<>();

    /**
     * Helper function. Dispatch screen event to the group children.
     */

    private void dispatchScreenEventMessage(Message message) {
        screenTouches.clear();

        if (isVisible()) {
            List<ScreenTouch> tempScreenTouches = message.getPayload();
            tempScreenTouches.forEach(screenTouch -> {
                if (!clip || ComponentGroup.this.contains(screenTouch.getX(), screenTouch.getY())) {
                    screenTouches.add(screenTouch);
                }
            });
        }

        Message outMessage = Messages.newScreenEventMessage(screenTouches, message.getRecipient());
        for (int i = views.size() - 1; i >= 0; i--) {
            views.get(i).dispatchMessage(outMessage);
        }
    }

    @Override
    public void dispatchMessage(Message message) {
        if (message instanceof EventTouchScreenMessage) {
            dispatchScreenEventMessage(message);
            super.dispatchMessage(message);
        } else if (message instanceof EventKeyMessage) {
            dispatchKeyMessage(message);
            super.dispatchMessage(message);
        } else {
            super.dispatchMessage(message);
            dispatchMessageToViews(message);
        }
    }

    /**
     * Helper function. Update children focus.
     */

    private void updateGroupFocus(View parent) {
        int i = 0, size = views.size();
        while (i < size && !views.get(i).isOnFocus()) i++;
        if (i < size) requestFocus(parent.isOnFocus());
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
                cBound[0] = cBound[2] = xi;
                cBound[1] = cBound[3] = yi;
            }
            if (xi < cBound[0]) cBound[0] = xi;
            if (yi < cBound[1]) cBound[1] = yi;
            if (xi + bounds[2] > cBound[2]) cBound[2] = xi + bounds[2];
            if (yi + bounds[3] > cBound[3]) cBound[3] = yi + bounds[3];
        }

        cBound[2] -= cBound[0];
        cBound[3] -= cBound[1];
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
        updateGroupFocus(parent);
        updateGroupBounds();
        updateClipShape();
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        if (isVisible()) {
            if (clip) {
                graphic.setClip(clipShape);
            }
            for (View i : views) {
                i.draw(graphic);
            }
            if (clip) {
                graphic.restoreClip();
            }
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

    @Override
    public int indexOf(View view) {
        return views.indexOf(view);
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
