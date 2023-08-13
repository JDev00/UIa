package uia.physical.ui;

import uia.core.Geom;
import uia.core.Key;
import uia.core.Paint;
import uia.core.Pointer;
import uia.core.architecture.Event;
import uia.core.architecture.Graphic;
import uia.core.architecture.ui.View;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * A RootView is a simple View used as root for user interface tree components.
 */

public final class RootView implements View {
    private final float[] bounds = new float[5];
    private boolean focus = true;

    @Override
    public void addEvent(Event<View, ?> event) {
    }

    @Override
    public void removeEvent(Event<View, ?> event) {
    }

    @Override
    public void updateEvent(Class<? extends Event> type, Object o) {
    }

    @Override
    public void setVisible(boolean visible) {
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public void setFocus(boolean request) {
        focus = request;
    }

    @Override
    public boolean isFocused() {
        return focus;
    }

    @Override
    public void setConsumer(CONSUMER consumer, boolean enableConsumer) {
    }

    @Override
    public void setColliderPolicy(boolean aabb) {
    }

    @Override
    public void setGeom(BiConsumer<View, Geom> builder, boolean inTimeBuilding) {
    }

    @Override
    public Geom getGeom() {
        return null;
    }

    @Override
    public void setPaint(Paint paint) {
    }

    @Override
    public Paint getPaint() {
        return null;
    }

    @Override
    public void setPosition(float x, float y) {
        bounds[0] = x;
        bounds[1] = y;
    }

    @Override
    public void setDimension(float x, float y) {
        bounds[2] = x;
        bounds[3] = y;
    }

    @Override
    public void setRotation(float radians) {
    }

    @Override
    public void dispatch(Key key) {
    }

    @Override
    public void dispatch(List<Pointer> pointers) {
    }

    @Override
    public void update(View parent) {
    }

    @Override
    public void draw(Graphic graphic) {
    }

    @Override
    public float[] bounds() {
        return bounds;
    }

    @Override
    public float[] boundsShape() {
        return bounds;
    }

    @Override
    public boolean contains(float x, float y) {
        return false;
    }

    @Override
    public String getID() {
        return "RootView";
    }
}
