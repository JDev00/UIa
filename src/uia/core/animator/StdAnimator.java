package uia.core.animator;

import uia.core.animator.policy.Animator;
import uia.core.animator.policy.AnimatorPath;
import uia.core.event.EventQueue;
import uia.core.event.StdEventQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Standard {@link Animator} implementation
 */

public class StdAnimator implements Animator {
    private final List<Node> nodes;

    private EventQueue<NodeEvent<Animator>> eventQueue;

    private AnimatorPath animatorPath;

    private final Node movable;

    private int curr;

    private float ix = 0;
    private float iy = 0;

    private boolean pause = false;
    private boolean firstStep = true;

    public StdAnimator() {
        nodes = new ArrayList<>(2);

        eventQueue = new StdEventQueue<>();

        animatorPath = new StdAnimatorPath();

        movable = new Node(0f, 0f, 0f);
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
    public StdAnimator setPath(AnimatorPath animatorPath) {
        this.animatorPath = Objects.requireNonNull(animatorPath);
        return this;
    }

    @Override
    public StdAnimator setEventQueue(EventQueue<NodeEvent<Animator>> eventQueue) {
        if (eventQueue != null && !eventQueue.equals(this.eventQueue)) {
            List<NodeEvent<Animator>> list = new ArrayList<>();

            this.eventQueue.apply(list::add);
            this.eventQueue = eventQueue;

            for (NodeEvent<Animator> i : list) {
                this.eventQueue.addEvent(i);
            }
        }
        return this;
    }

    @Override
    public StdAnimator clear() {
        curr = 0;
        firstStep = true;
        nodes.clear();
        animatorPath.clear();
        return this;
    }

    @Override
    public StdAnimator addNode(int i, float x, float y, float seconds) {
        nodes.add(i, new Node(x, y, seconds));
        animatorPath.add(i);
        return this;
    }

    @Override
    public StdAnimator addNode(float x, float y, float seconds) {
        addNode(nodes.size(), x, y, seconds);
        return this;
    }

    @Override
    public StdAnimator setNode(int i, float x, float y, float seconds) {
        if (i >= 0 && i < nodes.size()) nodes.get(i).set(x, y, seconds);
        return this;
    }

    @Override
    public StdAnimator setNodes(Animator animator) {
        clear();

        for (int i = 0; i < animator.size(); i++) {
            addNode(animator.getX(i), animator.getY(i), animator.getSeconds(i));
        }

        return this;
    }

    @Override
    public StdAnimator removeNode(int i) {
        if (i >= 0 && i < nodes.size()) {
            nodes.remove(i);
            animatorPath.remove(i);
        }
        return this;
    }

    protected void updateEvent(Class<?> event, int state) {
        String name = event.getName();

        eventQueue.apply(t -> {
            Class<?> current = t.getClass();

            while (current != null) {
                Class<?>[] interfaces = current.getInterfaces();

                for (Class<?> i : interfaces) {

                    if (name.equals(i.getName())) {
                        t.onEvent(StdAnimator.this, state);
                        return;
                    }
                }

                current = current.getSuperclass();
            }
        });
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

                updateEvent(NodeEvent.class, NodeEvent.NEXT_NODE);

                curr++;

                if (curr < animatorPath.size()) {// calculate the step for the next node
                    Node nextNext = nodes.get(animatorPath.get(curr));
                    ix = Node.getSteps(Node.getDistX(next.x, nextNext.x), nextNext.seconds);
                    iy = Node.getSteps(Node.getDistY(next.y, nextNext.y), nextNext.seconds);
                } else {
                    updateEvent(NodeEvent.class, NodeEvent.LAST_NODE);
                }
            } else {
                movable.add(ix, iy);
            }
        }
    }

    @Override
    public String toString() {
        return "StdAnimator{" + nodes + ", current=" + movable + '}';
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
    public EventQueue<NodeEvent<Animator>> getEventQueue() {
        return eventQueue;
    }

    @Override
    public AnimatorPath getPath() {
        return animatorPath;
    }
}
