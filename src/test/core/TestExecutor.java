package test.core;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * TestExecutor is responsible for executing a test suite.
 */

public class TestExecutor {

    /**
     * Helper function. Extracts the method annotated with the specified annotation.
     *
     * @param annotation the annotation to search for
     * @param methods    the class methods
     * @return the annotated method or null if no method is found
     * @throws NullPointerException if {@code annotation == null || methods == null}
     */

    private static Method getMethodAnnotatedWith(Class<? extends Annotation> annotation, Method[] methods) {
        Objects.requireNonNull(annotation);
        Objects.requireNonNull(methods);

        Method result = null;
        int i = 0;
        while (i < methods.length && result == null) {
            Method currentMethod = methods[i];
            if (currentMethod.isAnnotationPresent(annotation)) {
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
            Method beforeEachMethod = getMethodAnnotatedWith(BeforeEachTest.class, methods);
            Method afterEachMethod = getMethodAnnotatedWith(AfterEachTest.class, methods);

            for (Method method : methods) {
                if (method.isAnnotationPresent(Test.class) && !method.isAnnotationPresent(Skip.class)) {

                    // executes the before each test method
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

                    // executes the after each test method
                    if (afterEachMethod != null) {
                        try {
                            afterEachMethod.invoke(testSuite);
                        } catch (Exception ignored) {
                            System.out.println("Method marked with @AfterEachTest can't take arguments");
                        }
                    }
                }
            }

            // collects the test results
            System.out.println("TEST passed: " + result[0] + "/" + result[1]);
            System.exit(0);
        }).start();
    }
}
