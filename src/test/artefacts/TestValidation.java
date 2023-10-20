package test.artefacts;

public class TestValidation {
    private Object subject;
    private int passedAssertions = 0;

    /**
     * Set an Object to control
     *
     * @param subject the Object to control; it could be null
     * @return the TestValidation instance
     */

    public TestValidation expect(Object subject) {
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
            throw new RuntimeException(value + " is not equal to " + subject);
        } else {
            passedAssertions++;
        }
    }

    /**
     * @return the number of passed assertions
     */

    public int getPassedAssertions() {
        return passedAssertions;
    }
}
