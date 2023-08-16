package uia.physical.ui.wrapper;

import uia.core.architecture.Event;
import uia.core.architecture.Graphic;
import uia.core.Geom;
import uia.core.Key;
import uia.core.Paint;
import uia.core.Pointer;
import uia.core.architecture.ui.View;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * WrapperView enables the creation of complex graphical widgets.
 */

public abstract class WrapperView implements View {
    private final View view;

    public WrapperView(View view) {
        this.view = view;
    }

    @Override
    public void setGeom(BiConsumer<View, Geom> builder, boolean inTimeBuilding) {
        view.setGeom(builder, inTimeBuilding);
    }

    @Override
    public Geom getGeom() {
        return view.getGeom();
    }

    @Override
    public void setPaint(Paint paint) {
        view.setPaint(paint);
    }

    @Override
    public Paint getPaint() {
        return view.getPaint();
    }

    @Override
    public void addEvent(Event<View, ?> event) {
        view.addEvent(event);
    }

    @Override
    public void removeEvent(Event<View, ?> event) {
        view.removeEvent(event);
    }

    @Override
    public void updateEvent(Class<? extends Event> type, Object o) {
        view.updateEvent(type, o);
    }

    @Override
    public void setPosition(float x, float y) {
        view.setPosition(x, y);
    }

    @Override
    public void setDimension(float x, float y) {
        view.setDimension(x, y);
    }

    @Override
    public void setRotation(float radians) {
        view.setRotation(radians);
    }

    @Override
    public void setFocus(boolean focus) {
        view.setFocus(focus);
    }

    @Override
    public void setVisible(boolean visible) {
        view.setVisible(visible);
    }

    @Override
    public void dispatch(List<Pointer> pointers) {
        view.dispatch(pointers);
    }

    @Override
    public void dispatch(Key key) {
        view.dispatch(key);
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
    public float[] bounds() {
        return view.bounds();
    }

    @Override
    public float[] desc() {
        return view.desc();
    }

    @Override
    public boolean contains(float x, float y) {
        return view.contains(x, y);
    }

    @Override
    public String getID() {
        return view.getID();
    }

    @Override
    public boolean isVisible() {
        return view.isVisible();
    }

    @Override
    public boolean isFocused() {
        return view.isFocused();
    }

    @Override
    public void setConsumer(CONSUMER consumer, boolean enableConsumer) {
        view.setConsumer(consumer, enableConsumer);
    }

    @Override
    public void setColliderPolicy(boolean aabb) {
        view.setColliderPolicy(aabb);
    }

    /**
     * @return the internal {@link View}
     */

    public View getView() {
        return view;
    }
}
