package test.core;

/**
 * TestAssertion has the responsibility to check if a specified Object is equal to another one.
 * In addition, it registers all the passed checks.
 */

public class TestAssertion {
    private Object subject;
    private int passedAssertions = 0;
    private int expectedAssertions = 1;

    /**
     * Set the expected assertions to be true
     *
     * @param assertions the number of assertions to pass to consider this test passed
     * @return this TestAssertion
     */

    public TestAssertion assertions(int assertions) {
        expectedAssertions = Math.max(1, assertions);
        return this;
    }

    /**
     * Set an Object to control
     *
     * @param subject the Object to control; it could be null
     * @return this TestAssertion
     */

    public TestAssertion expect(Object subject) {
        this.subject = subject;
        return this;
    }

    /**
     * Check that the subject is equal to the specified value
     *
     * @throws RuntimeException if {@code subject != value}
     */

    public void toBe(Object value) {
        if ((subject == null && value != null) || !subject.equals(value)) {
            throw new RuntimeException("expected " + subject + " to be " + value + " but it wasn't");
        } else {
            passedAssertions++;
        }
    }

    /**
     * @return true if this TestAssertions passed all assertions
     */

    public boolean passed() {
        return passedAssertions == expectedAssertions;
    }
}
