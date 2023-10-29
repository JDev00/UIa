package uia.physical;

import uia.core.Geometry;
import uia.core.Paint;
import uia.core.basement.Callback;
import uia.core.ui.Graphic;
import uia.core.ui.View;

/**
 * ComponentHiddenRoot is a simple View designed to be used as a root for user interface tree components.
 * <br>
 * <br>
 * <b>Important:</b> do not use it directly. It is designed to be used within a Context-specific implementation.
 */

public final class ComponentHiddenRoot implements View {
    private final float[] bounds = new float[5];
    private boolean focus = true;

    private final String unsupported_error = "method not supported by ROOT View!";

    @Override
    public void registerCallback(Callback<?> callback) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public void unregisterCallback(Callback<?> callback) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public void notifyCallbacks(Class<? extends Callback> type, Object o) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public void setVisible(boolean visible) {
        throw new UnsupportedOperationException(unsupported_error);
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
    public void setConsumer(Consumer consumer, boolean enableConsumer) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public void dispatch(Dispatcher dispatcher, Object data) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public void buildGeometry(java.util.function.Consumer<Geometry> builder, boolean inTimeBuilding) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public Paint getPaint() {
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
    public void setRotation(float radians) {
    }

    @Override
    public void sendMessage(Object message, String destID) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public void update(View parent) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public void draw(Graphic graphic) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public void setColliderPolicy(ColliderPolicy policy) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public float[] bounds() {
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
    public float getRotation() {
        return bounds[4];
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
