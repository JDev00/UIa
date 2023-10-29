package test.core;

/**
 * Test case ADT.
 */

public interface TestCase {

    /**
     * Run this test case
     *
     * @param testAssertion a not null {@link TestAssertion}
     */

    void run(TestAssertion testAssertion);
}
