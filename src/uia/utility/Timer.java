package uia.utility;

/**
 * Timer representation
 */

public class Timer {
    private long init;
    private long end;

    public Timer() {
        init = System.currentTimeMillis();
        end = 0;
    }

    /**
     * Reset the elapsed time since the previous reset
     */

    public void reset() {
        init = System.currentTimeMillis();
    }

    /**
     * @return the elapsed seconds since the previous reset
     */

    public float seconds() {
        end = System.currentTimeMillis();
        return 0.001f * (end - init);
    }
}
