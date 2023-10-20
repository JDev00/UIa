package test.artefacts;

/**
 * Test case ADT.
 */

public interface TestCase {

    /**
     * Run this test case
     *
     * @return a not null {@link TestValidation}
     */

    TestValidation run();
}
