package uia.physical.ui.wrapper;

import uia.core.architecture.ui.View;
import uia.core.architecture.ui.ViewGroup;

public class WrapperViewGroup extends WrapperView implements ViewGroup {
    private ViewGroup viewGroup;

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
    public float[] boundsContent() {
        return viewGroup.boundsContent();
    }
}
