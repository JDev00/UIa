package uia.physical;

import uia.core.shape.Shape;
import uia.core.basement.Message;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ScreenTouch;
import uia.core.ui.Graphics;
import uia.physical.message.EventKeyMessage;
import uia.physical.message.EventTouchScreenMessage;
import uia.physical.message.Messages;

import java.util.*;

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
        Arrays.fill(cBound, 0);
    }

    /**
     * Helper function. Dispatches a message to the group children.
     */

    private void dispatchMessageToViews(Message message) {
        for (int i = views.size() - 1; i >= 0; i--) {
            views.get(i).readMessage(message);
        }
    }

    /**
     * Helper function. Dispatch Key event message to the group children.
     */

    private void dispatchKeyMessage(Message message) {
        if (isVisible()) {
            for (int i = views.size() - 1; i >= 0; i--) {
                views.get(i).readMessage(message);
            }
        }
    }

    private final List<ScreenTouch> screenTouches = new ArrayList<>();

    /**
     * Helper function. Dispatches screenTouch event to the group children.
     */

    private void dispatchScreenEventMessage(Message message) {
        screenTouches.clear();

        List<ScreenTouch> tempScreenTouches = message.getPayload();
        if (isVisible()) {
            tempScreenTouches.forEach(screenTouch -> {
                ScreenTouch currentTouch = screenTouch;

                if (clip && !contains(screenTouch.getX(), screenTouch.getY())) {
                    currentTouch = ScreenTouch.copy(screenTouch, 0, 0);
                    currentTouch.consume();
                }
                screenTouches.add(currentTouch);
            });
        }

        Message outMessage = Messages.newScreenEventMessage(screenTouches, message.getRecipient());
        for (int i = views.size() - 1; i >= 0; i--) {
            views.get(i).readMessage(outMessage);
        }
    }

    @Override
    public void readMessage(Message message) {
        if (message instanceof EventTouchScreenMessage) {
            dispatchScreenEventMessage(message);
            super.readMessage(message);
        } else if (message instanceof EventKeyMessage) {
            dispatchKeyMessage(message);
            super.readMessage(message);
        } else {
            super.readMessage(message);
            dispatchMessageToViews(message);
        }
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
        System.arraycopy(cBound, 0, copyBounds, 0, cBound.length);
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
