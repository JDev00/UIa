package test.core;

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
     * Check that the subject is equal to the given value
     *
     * @throws RuntimeException if {@code subject != value}
     */

    public void toBeEqual(Object value) {
        if ((subject == null && value != null) || !subject.equals(value)) {
            throw new RuntimeException("expected " + subject + " to be " + value + " but it wasn't");
        } else {
            passedAssertions++;
        }
    }

    /*
     * Check that the subject is equal to the given value
     *
     * @throws RuntimeException if {@code subject != value}
     *

    public void toBeEqual(Object value, int timeoutMillis) {
        int elapsed_millis = 0;
        while (elapsed_millis < timeoutMillis && ((subject == null && value != null) || !subject.equals(value))) {
            elapsed_millis += 10;
            try {
                Thread.sleep(10);
            } catch (Exception ignored) {
            }
        }

        if (elapsed_millis >= timeoutMillis) {
            throw new RuntimeException("expected '" + subject + "' to be '" + value + "' but it wasn't");
        } else {
            passedAssertions++;
        }
    }*/

    /**
     * @return true if this TestAssertions passed all assertions
     */

    public boolean passed() {
        return passedAssertions >= expectedAssertions;
    }
}
