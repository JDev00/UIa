package uia.physical;

import uia.core.Geometry;
import uia.core.Paint;
import uia.core.basement.Callback;
import uia.core.ui.Graphic;
import uia.core.ui.View;

import java.util.function.Consumer;

/**
 * ComponentRoot is a simple View used as root for user interface tree components.
 */

public final class ComponentRoot implements View {
    private final float[] bounds = new float[5];
    private boolean focus = true;

    private final String unsupported_error = "method not supported by ROOT View!";

    @Override
    public void addCallback(Callback<?> callback) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public void removeCallback(Callback<?> callback) {
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
    public void setConsumer(CONSUMER consumer, boolean enableConsumer) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public void dispatch(DISPATCHER dispatcher, Object data) {
        throw new UnsupportedOperationException(unsupported_error);
    }

    @Override
    public void buildGeometry(Consumer<Geometry> builder, boolean inTimeBuilding) {
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
    public void setColliderPolicy(COLLIDER_POLICY policy) {
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
    public boolean contains(float x, float y) {
        return false;
    }

    @Override
    public String getID() {
        return "ROOT";
    }
}
