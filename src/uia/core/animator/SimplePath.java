package uia.core.animator;

import uia.structure.list.BufferInt;

public class SimplePath implements AnimatorPath {
    private final BufferInt buffer;

    public SimplePath() {
        buffer = new BufferInt(4);
    }

    @Override
    public void clear() {
        buffer.clear();
    }

    @Override
    public void add(int i) {
        buffer.add(i);
    }

    @Override
    public void remove(int i) {
        buffer.remove(i);
    }

    @Override
    public void swap(int i, int j) {
        int size = buffer.size();

        if (i >= 0 && i < size
                && j >= 0 && j < size) {
            int temp = buffer.getIntAt(i);
            buffer.set(i, buffer.getIntAt(j));
            buffer.set(j, temp);
        }
    }

    @Override
    public void set(AnimatorPath animatorPath) {
        if (animatorPath != null) {
            buffer.clear();

            for (int i = 0; i < animatorPath.size(); i++) {
                buffer.add(animatorPath.get(i));
            }
        }
    }

    @Override
    public void reverse() {
        for (int i = 0; i < buffer.size() / 2; i++) {
            swap(i, buffer.size() - 1 - i);
        }
    }

    @Override
    public int size() {
        return buffer.size();
    }

    @Override
    public int get(int i) {
        return i >= 0 && i < buffer.size() ? buffer.getIntAt(i) : -1;
    }
}
