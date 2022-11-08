package uia.core.animator;

import uia.core.animator.policy.AnimatorPath;
import uia.structure.BInt;

/**
 * Standard {@link AnimatorPath} implementation
 */

public class StdAnimatorPath implements AnimatorPath {
    private final BInt buffer;

    public StdAnimatorPath() {
        buffer = new BInt(4);
    }

    @Override
    public StdAnimatorPath clear() {
        buffer.clear();
        return this;
    }

    @Override
    public StdAnimatorPath add(int i) {
        buffer.add(i);
        return this;
    }

    @Override
    public StdAnimatorPath remove(int i) {
        buffer.remove(i);
        return this;
    }

    @Override
    public StdAnimatorPath swap(int i, int j) {
        int size = buffer.size();

        if (i >= 0 && i < size
                && j >= 0 && j < size) {
            int temp = buffer.get(i);
            buffer.set(i, buffer.get(j));
            buffer.set(j, temp);
        }

        return this;
    }

    @Override
    public StdAnimatorPath set(AnimatorPath animatorPath) {
        if (animatorPath != null) {
            buffer.clear();

            for (int i = 0; i < animatorPath.size(); i++) {
                buffer.add(animatorPath.get(i));
            }
        }
        return this;
    }

    @Override
    public StdAnimatorPath reverse() {
        for (int i = 0; i < buffer.size() / 2; i++) {
            swap(i, buffer.size() - 1 - i);
        }
        return this;
    }

    @Override
    public int size() {
        return buffer.size();
    }

    @Override
    public int get(int i) {
        return i >= 0 && i < buffer.size() ? buffer.get(i) : -1;
    }
}
