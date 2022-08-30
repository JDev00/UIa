package uia.core.animator;

import uia.core.event.Event;
import uia.core.event.EventHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Standard animator implementation
 */

public class LinearAnimator implements Animator {
    private final List<Node> nodes;

    private final EventHandler<Animator> eventHandler;

    private AnimatorPath animatorPath;

    private final Node movable;

    private int curr;

    private float ix = 0;
    private float iy = 0;

    private boolean pause = false;
    private boolean firstStep = true;

    public LinearAnimator() {
        nodes = new ArrayList<>(2);

        eventHandler = new EventHandler<>();

        animatorPath = new SimplePath();

        movable = Node.create();
    }

    @Override
    public void addEvent(Event<Animator> event) {
        eventHandler.add(event);
    }

    @Override
    public void setPath(AnimatorPath animatorPath) {
        this.animatorPath = Objects.requireNonNull(animatorPath);
    }

    @Override
    public void restart() {
        curr = 0;
        pause = false;
        firstStep = true;

        if (animatorPath.size() > 0) {
            movable.set(nodes.get(animatorPath.get(curr)));
        }
    }

    @Override
    public void pause() {
        pause = true;
    }

    @Override
    public void resume() {
        pause = false;
    }

    @Override
    public void clear() {
        curr = 0;

        firstStep = true;

        nodes.clear();

        animatorPath.clear();
    }

    @Override
    public void addNode(int i, float x, float y, float seconds) {
        nodes.add(i, Node.create(x, y, seconds));
        animatorPath.add(i);
    }

    @Override
    public void setNodes(Animator animator) {
        clear();

        for (int i = 0; i < animator.size(); i++) {
            addNode(animator.getX(i), animator.getY(i), animator.getSeconds(i));
        }
    }

    @Override
    public void addNode(float x, float y, float seconds) {
        addNode(nodes.size(), x, y, seconds);
    }

    @Override
    public void setNode(int i, float x, float y, float seconds) {
        if (i >= 0 && i < nodes.size()) nodes.get(i).set(x, y, seconds);
    }

    @Override
    public void removeNode(int i) {
        nodes.remove(i);
        animatorPath.remove(i);
    }

    @Override
    public void update() {
        if (!pause && curr < animatorPath.size()) {
            Node next = nodes.get(animatorPath.get(curr));


            if (firstStep) {
                firstStep = false;
                ix = Node.getSteps(Node.getDistX(movable.x, next.x), next.seconds);
                iy = Node.getSteps(Node.getDistY(movable.y, next.y), next.seconds);
            }


            if (movable.nearTo(next, Math.abs(ix) + Math.abs(iy))) {
                movable.set(next.x, next.y);

                eventHandler.update(NodeEv.class, this, NodeEv.NEXT);

                curr++;

                // Calculates step for the next node
                if (curr < animatorPath.size()) {
                    Node nextNext = nodes.get(animatorPath.get(curr));
                    ix = Node.getSteps(Node.getDistX(next.x, nextNext.x), nextNext.seconds);
                    iy = Node.getSteps(Node.getDistY(next.y, nextNext.y), nextNext.seconds);
                } else {
                    eventHandler.update(NodeEv.class, this, NodeEv.LAST);
                }
            } else {
                movable.add(ix, iy);
            }
        }
    }

    @Override
    public String toString() {
        return "LinearAnimator{" +
                nodes.toString() +
                ", current=" + movable +
                '}';
    }

    @Override
    public int size() {
        return nodes.size();
    }

    @Override
    public int current() {
        return curr;
    }

    @Override
    public float x() {
        return movable.x;
    }

    @Override
    public float y() {
        return movable.y;
    }

    @Override
    public float getX(int i) {
        return nodes.get(i).x;
    }

    @Override
    public float getY(int i) {
        return nodes.get(i).y;
    }

    @Override
    public float getSeconds(int i) {
        return nodes.get(i).seconds;
    }

    @Override
    public boolean isPaused() {
        return pause;
    }

    @Override
    public boolean isFinished() {
        return curr == nodes.size();
    }

    @Override
    public AnimatorPath getPath() {
        return animatorPath;
    }
}
