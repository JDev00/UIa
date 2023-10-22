package test.artefacts;

public class TestUtils {

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
                testcase.run(new TestAssertion());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Execute the specified test suite
     *
     * @param testSuite a not null {@link TestSuite} to execute
     */

    public static void runTestSuite(TestSuite testSuite) {
        int[] tests = {0, 0};

        new Thread(() -> {
            for (TestCase testCase : testSuite) {
                try {
                    testCase.run(new TestAssertion());
                    tests[0]++;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tests[1]++;
            }
            System.out.println("TEST passed: " + tests[0] + "/" + tests[1]);
            System.exit(0);
        }).start();
    }
}
