package uia.application.ui.component;

import uia.core.rendering.geometry.Geometry;
import uia.core.basement.message.Message;
import uia.core.rendering.Graphics;
import uia.core.basement.Callback;
import uia.core.ui.style.Style;
import uia.core.ui.View;

/**
 * Helper class. Wraps a View implementation.
 */

public abstract class WrapperView implements View {
    private final View view;

    protected WrapperView(View view) {
        this.view = view;
    }

    /**
     * @return the wrapped {@link View}
     */

    @SuppressWarnings("unchecked")
    public <T extends View> T getView() {
        return (T) view;
    }

    @Override
    public long registerCallback(Callback<?> callback) {
        return view.registerCallback(callback);
    }

    @Override
    public void unregisterCallback(long callbackID) {
        view.unregisterCallback(callbackID);
    }

    @Override
    public void notifyCallbacks(Class<? extends Callback> type, Object data) {
        view.notifyCallbacks(type, data);
    }

    @Override
    public int numberOfCallbacks() {
        return view.numberOfCallbacks();
    }

    @Override
    public Style getStyle() {
        return view.getStyle();
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
    public void setColliderPolicy(ColliderPolicy policy) {
        view.setColliderPolicy(policy);
    }

    @Override
    public void setInputConsumer(InputConsumer inputConsumer, boolean enableInputConsumer) {
        view.setInputConsumer(inputConsumer, enableInputConsumer);
    }

    @Override
    public void sendMessage(Message message) {
        view.sendMessage(message);
    }

    @Override
    public void readMessage(Message message) {
        view.readMessage(message);
    }

    @Override
    public void update(View parent) {
        view.update(parent);
    }

    @Override
    public void draw(Graphics graphics) {
        view.draw(graphics);
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
    public float[] getBounds() {
        return view.getBounds();
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
