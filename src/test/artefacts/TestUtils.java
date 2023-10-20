package test.artefacts;

public class TestUtils {

    /**
     * Kill the process
     */

    public static void killProcess() {
        System.exit(1);
    }

    /**
     * Freeze the thread that calls this function for the specified amount of time
     *
     * @param millis the freeze time (> 0) in milliseconds
     */

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Execute the specified test case in a dedicated Thread
     *
     * @param testcase a not null {@link TestCase} to execute
     */

    public static void runTest(TestCase testcase) {
        new Thread(() -> {
            try {
                testcase.run();
            } catch (Exception e) {
                e.printStackTrace();
                killProcess();
            }
            System.exit(0);
        }).start();
    }
}
