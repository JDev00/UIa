package uia.application.animator;

/**
 * Animator ADT.
 */

public interface Animator {

    /**
     * Sets an animator path.
     *
     * @param animatorPath an {@link AnimatorPath}
     * @throws NullPointerException if {@code animatorPath == null}
     */

    Animator setPath(AnimatorPath animatorPath);

    /**
     * @return the {@link AnimatorPath}
     */

    AnimatorPath getPath();

    /**
     * Pauses this animator.
     */

    void pause();

    /**
     * @return true if this animator is paused
     */

    boolean isPaused();

    /**
     * Resumes this animator.
     */

    void resume();

    /**
     * Restarts this animator.
     */

    void restart();

    /**
     * @return true if this animator has reached the last node
     */

    boolean hasFinished();

    /**
     * Updates this animator.
     */

    void update();

    /**
     * @return the position on the x-axis of the moving node
     */

    float x();

    /**
     * @return the position on the y-axis of the moving node
     */

    float y();
}
