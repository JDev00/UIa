package uia.application.metrics;

/**
 * Collection of known metrics for the SystemMetrics.
 */

public enum DefaultSystemMetrics {
    FPS(0), FRAME_COUNT(1), CPU(2), RAM(3);

    private final int id;

    DefaultSystemMetrics(int id) {
        this.id = id;
    }

    /**
     * @return the ID this metric
     */

    public int getID() {
        return id;
    }
}