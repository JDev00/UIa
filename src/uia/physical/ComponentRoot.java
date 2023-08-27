package uia.physical;

import uia.core.Geom;
import uia.core.Key;
import uia.core.Paint;
import uia.core.Pointer;
import uia.core.basement.Event;
import uia.core.basement.Graphic;
import uia.core.ui.View;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * ComponentRoot is a simple View used as root for user interface tree components.
 */

public final class ComponentRoot implements View {
    private final float[] bounds = new float[5];
    private final float[] desc = new float[3];
    private boolean focus = true;

    @Override
    public void addEvent(Event<?> event) {
    }

    @Override
    public void removeEvent(Event<?> event) {
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
    public void buildGeom(BiConsumer<View, Geom> builder, boolean inTimeBuilding) {
    }


    @Override
    public Geom getGeom() {
        return null;
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

        desc[0] = x;
        desc[1] = y;
    }

    @Override
    public void setRotation(float radians) {
    }

    @Override
    public void sendMessage(Object message, String destID) {
    }

    @Override
    public void dispatch(Object message, String sourceID, String destID) {
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
    public float[] desc() {
        return desc;
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
