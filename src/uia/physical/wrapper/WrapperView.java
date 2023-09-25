package uia.physical.wrapper;

import uia.core.basement.Callback;
import uia.core.ui.Graphic;
import uia.core.Geometry;
import uia.core.Paint;
import uia.core.ui.View;

import java.util.function.Consumer;

/**
 * WrapperView enables the creation of complex graphical widgets.
 */

public abstract class WrapperView implements View {
    private final View view;

    public WrapperView(View view) {
        this.view = view;
    }

    /**
     * @return the attached {@link View}
     */

    public View getView() {
        return view;
    }

    @Override
    public void addCallback(Callback<?> callback) {
        view.addCallback(callback);
    }

    @Override
    public void removeCallback(Callback<?> callback) {
        view.removeCallback(callback);
    }

    @Override
    public void notifyCallbacks(Class<? extends Callback> type, Object data) {
        view.notifyCallbacks(type, data);
    }

    @Override
    public void sendMessage(Object message, String destID) {
        view.sendMessage(message, destID);
    }

    @Override
    public void buildGeometry(Consumer<Geometry> builder, boolean inTimeBuilding) {
        view.buildGeometry(builder, inTimeBuilding);
    }

    @Override
    public Paint getPaint() {
        return view.getPaint();
    }

    @Override
    public Geometry getGeometry() {
        return view.getGeometry();
    }

    @Override
    public void setVisible(boolean visible) {
        view.setVisible(visible);
    }

    @Override
    public boolean isVisible() {
        return view.isVisible();
    }

    @Override
    public void requestFocus(boolean request) {
        view.requestFocus(request);
    }

    @Override
    public boolean isOnFocus() {
        return view.isOnFocus();
    }

    @Override
    public void setColliderPolicy(COLLIDER_POLICY policy) {
        view.setColliderPolicy(policy);
    }

    @Override
    public void setPosition(float x, float y) {
        view.setPosition(x, y);
    }

    @Override
    public void setDimension(float width, float height) {
        view.setDimension(width, height);
    }

    @Override
    public void setRotation(float radians) {
        view.setRotation(radians);
    }

    @Override
    public void setConsumer(CONSUMER consumer, boolean enableConsumer) {
        view.setConsumer(consumer, enableConsumer);
    }

    @Override
    public void dispatch(DISPATCHER dispatcher, Object data) {
        view.dispatch(dispatcher, data);
    }

    @Override
    public void update(View parent) {
        view.update(parent);
    }

    @Override
    public void draw(Graphic graphic) {
        view.draw(graphic);
    }

    @Override
    public float getWidth() {
        return view.getWidth();
    }

    @Override
    public float getHeight() {
        return view.getHeight();
    }

    @Override
    public float[] bounds() {
        return view.bounds();
    }

    @Override
    public boolean contains(float x, float y) {
        return view.contains(x, y);
    }

    @Override
    public String getID() {
        return view.getID();
    }
}
