package uia.core.animator.policy;

/**
 * Animator path ADT
 */

public interface AnimatorPath {

    /**
     * Remove all indices
     */

    AnimatorPath clear();

    /**
     * Add an index
     *
     * @param i an index
     */

    AnimatorPath add(int i);

    /**
     * Remove an index
     *
     * @param i the position of the index to remove
     */

    AnimatorPath remove(int i);

    /**
     * Swap two indices
     *
     * @param i the position of the first index to swap
     * @param j the position of the second index to swap
     */

    AnimatorPath swap(int i, int j);

    /**
     * Copy the given animator path and replaces the current one
     *
     * @param animatorPath the animator path to copy
     */

    AnimatorPath set(AnimatorPath animatorPath);

    /**
     * Reverse the list of indices of this path
     */

    AnimatorPath reverse();

    /**
     * @return the number of indices inside this path
     */

    int size();

    /**
     * @param i the position of the index to retrieve
     * @return the specified index or -1
     */

    int get(int i);
}
