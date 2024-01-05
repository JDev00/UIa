package test.core;

/**
 * Collection of test utilities
 */

public class TestUtils {

    /**
     * Freezes the thread that calls this function for the specified amount of milliseconds
     *
     * @param millis the freeze time (> 0) in milliseconds
     */

    public static void wait(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
