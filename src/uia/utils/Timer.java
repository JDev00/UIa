package uia.utils;

/**
 * Object used to track time
 */

public class Timer {
    private long init;
    private long end;

    public Timer() {
        end = 0;
        init = System.currentTimeMillis();
    }

    /**
     * Resets the elapsed time to 0
     */

    public void reset() {
        init = System.currentTimeMillis();
    }

    /**
     * Resets the initial time
     *
     * @param init the start time
     */

    public void reset(long init) {
        this.init = init;
    }

    /**
     * @return the start time
     */

    public long init() {
        return init;
    }

    /**
     * @return the last time
     */

    public long end() {
        return end;
    }

    /**
     * @return the elapsed time in seconds
     */

    public float seconds() {
        end = System.currentTimeMillis();
        return 0.001f * (end - init);
    }

    /**
     * Check if this timer is over a specified time
     *
     * @param seconds the seconds to check for
     * @return true if the timer is over the specified seconds
     */

    public boolean isOver(float seconds) {
        return seconds() >= seconds;
    }
}
