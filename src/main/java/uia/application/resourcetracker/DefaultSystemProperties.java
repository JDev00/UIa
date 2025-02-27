package uia.application.resourcetracker;

/**
 * Collection of known system properties.
 */

public enum DefaultSystemProperties {
    FPS(0), FRAME_COUNT(1), CPU(2), RAM(3);

    private final int id;

    DefaultSystemProperties(int id) {
        this.id = id;
    }

    /**
     * @return the property ID
     */

    public int getID() {
        return id;
    }
}