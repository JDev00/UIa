package uia.physical.component;

import uia.core.basement.Callback;
import uia.core.message.Message;
import uia.core.rendering.geometry.Geometry;
import uia.core.ui.style.Style;
import uia.core.rendering.Graphics;
import uia.core.ui.View;

/**
 * ComponentHiddenRoot is a simple View designed to be used as a root for user interface tree components.
 * <br>
 * <br>
 * <b>Important:</b> do not use it directly. It is designed to be used within a Context-specific implementation.
 */

public final class ComponentHiddenRoot implements View {
    private static final String UNSUPPORTED_ERROR = "method not supported by ROOT View!";

    private final float[] bounds = new float[5];
    private boolean focus = true;

    @Override
    public void registerCallback(Callback<?> callback) {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public void unregisterCallback(Callback<?> callback) {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public void notifyCallbacks(Class<? extends Callback> type, Object o) {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void requestFocus(boolean request) {
        focus = request;
    }

    @Override
    public boolean isOnFocus() {
        return focus;
    }

    @Override
    public void setInputConsumer(InputConsumer inputConsumer, boolean enableInputConsumer) {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public void sendMessage(Message message) {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public void readMessage(Message message) {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public Style getStyle() {
        return null;
    }

    @Override
    public Geometry getGeometry() {
        return null;
    }

    @Override
    public void setPosition(float x, float y) {
        bounds[0] = x;
        bounds[1] = y;
    }

    @Override
    public void setDimension(float width, float height) {
        bounds[2] = width;
        bounds[3] = height;
    }

    @Override
    public void update(View parent) {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public void draw(Graphics graphics) {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public void setColliderPolicy(ColliderPolicy policy) {
        throw new UnsupportedOperationException(UNSUPPORTED_ERROR);
    }

    @Override
    public float[] getBounds() {
        return bounds;
    }

    @Override
    public float getWidth() {
        return bounds[2];
    }

    @Override
    public float getHeight() {
        return bounds[3];
    }

    @Override
    public boolean contains(float x, float y) {
        return false;
    }

    @Override
    public String getID() {
        return "HIDDEN_ROOT";
    }
}
