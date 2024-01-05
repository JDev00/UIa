package test.core;

import java.lang.reflect.Method;

/**
 * To describe
 */

public class TestExecutor {

    /**
     * Executes the specified test suite
     *
     * @param testSuite a not null test suite to execute
     */

    public static void runTests(Object testSuite) {
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
