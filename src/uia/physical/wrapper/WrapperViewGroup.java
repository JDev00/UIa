package uia.physical.wrapper;

import uia.core.ui.View;
import uia.core.ui.ViewGroup;

import java.util.Iterator;

public abstract class WrapperViewGroup extends WrapperView implements ViewGroup {
    private final ViewGroup viewGroup;

    public WrapperViewGroup(ViewGroup viewGroup) {
        super(viewGroup);
        this.viewGroup = viewGroup;
    }

    @Override
    public void setClip(boolean clipRegion) {
        viewGroup.setClip(clipRegion);
    }

    @Override
    public boolean hasClip() {
        return viewGroup.hasClip();
    }

    @Override
    public void add(View... views) {
        viewGroup.add(views);
    }

    @Override
    public void add(int i, View view) {
        viewGroup.add(i, view);
    }

    @Override
    public void remove(View view) {
        viewGroup.remove(view);
    }

    @Override
    public void removeAll() {
        viewGroup.removeAll();
    }

    @Override
    public int size() {
        return viewGroup.size();
    }

    @Override
    public View get(int i) {
        return viewGroup.get(i);
    }

    @Override
    public View get(String id) {
        return viewGroup.get(id);
    }

    @Override
    public int indexOf(View view) {
        return viewGroup.indexOf(view);
    }

    @Override
    public float[] boundsContent() {
        return viewGroup.boundsContent();
    }

    @Override
    public Iterator<View> iterator() {
        return viewGroup.iterator();
    }
}
