package test.core;

import java.lang.reflect.Method;

public class TestUtils {

    /**
     * Freeze the thread that calls this function for the specified amount of time
     *
     * @param millis the freeze time (> 0) in milliseconds
     */

    public static void waitMillis(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Execute the specified test suite
     *
     * @param testSuite a not null Object with {@link Test} marked methods to execute
     */

    public static void runTestSuite(Object testSuite) {
        int[] result = {0, 0};

        new Thread(() -> {
            Method[] methods = testSuite.getClass().getMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(Test.class) && !method.isAnnotationPresent(Skip.class)) {
                    TestAssertion testAssertion = new TestAssertion();
                    try {
                        method.invoke(testSuite, testAssertion);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (testAssertion.passed()) {
                        result[0]++;
                    } else {
                        System.out.println(method.getName() + " failed");
                    }
                    result[1]++;
                }
            }
            System.out.println("TEST passed: " + result[0] + "/" + result[1]);
            System.exit(0);
        }).start();
    }
}
