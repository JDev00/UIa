package test.core;

import java.lang.reflect.Method;

/**
 * To describe
 */

public class TestExecutor {

    /**
     * Helper function. Extracts the method marked with {@link BeforeEachTest}
     *
     * @param methods a not null array of class methods
     * @return the marked method or, if it does not exist, null
     */

    private static Method getBeforeEachTest(Method[] methods) {
        Method result = null;
        int i = 0;
        while (i < methods.length && result == null) {
            Method currentMethod = methods[i];
            if (currentMethod.isAnnotationPresent(BeforeEachTest.class)) {
                result = currentMethod;
            }
            i++;
        }
        return result;
    }

    /**
     * Executes the specified test suite
     *
     * @param testSuite a not null test suite to execute
     */

    public static void runTests(Object testSuite) {
        int[] result = {0, 0};

        new Thread(() -> {
            Method[] methods = testSuite.getClass().getMethods();

            Method beforeEachMethod = getBeforeEachTest(methods);

            for (Method method : methods) {
                if (method.isAnnotationPresent(Test.class) && !method.isAnnotationPresent(Skip.class)) {

                    if (beforeEachMethod != null) {
                        try {
                            beforeEachMethod.invoke(testSuite);
                        } catch (Exception ignored) {
                            System.out.println("Method marked with @BeforeEachTest can't take arguments");
                        }
                    }

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
