package test.core;

import java.lang.reflect.Array;
import java.util.Objects;

/**
 * TestAssertion is responsible for checking whether a specified expected object has the expected properties.
 */

public class TestAssertion {
    private Object expected;
    private int passedAssertions = 0;
    private int expectedAssertions = 1;

    /**
     * Sets the expected number of true assertions
     *
     * @param assertions the number of true assertions
     * @return this TestAssertion
     */

    public TestAssertion assertions(int assertions) {
        expectedAssertions = Math.max(1, assertions);
        return this;
    }

    /**
     * Sets the expected object
     *
     * @param expected the expected object; it could be null
     * @return this TestAssertion
     */

    public TestAssertion expect(Object expected) {
        this.expected = expected;
        return this;
    }

    /**
     * Checks if the expected object is equal to the specified value
     *
     * @throws IllegalArgumentException if {@code expected != value}
     */

    public void toBe(Object value) {
        if ((expected == null && value != null) || !expected.equals(value)) {
            throw new IllegalArgumentException("expected " + expected + " to be " + value + " but it wasn't");
        } else {
            passedAssertions++;
        }
    }

    /**
     * Checks whether the expected object contains the specified values.
     * <br>
     * A shallow control is operated on elements.
     *
     * @param values the values the expected object is supposed to contain
     * @throws IllegalArgumentException if {@code values.length > expected.length or values[i] != expected[i]}
     * @throws ClassCastException       if {@code expected is not an array}
     * @throws NullPointerException     if {@code values is null}
     */

    public void toHaveValues(Object... values) {
        Objects.requireNonNull(values);
        if (values.length > Array.getLength(expected)) {
            throw new IllegalArgumentException("have been specified more values than the expected ones");
        }
        if (!expected.getClass().isArray()) {
            throw new ClassCastException("expected " + expected + " to be an array but it wasn't");
        }
        for (int i = 0; i < values.length; i++) {
            Object subjectElement = Array.get(expected, i);
            if (!subjectElement.equals(values[i])) {
                throw new IllegalArgumentException("expected " + subjectElement + " to be " + values[i] + " but it wasn't");
            }
        }
        passedAssertions++;
    }

    /**
     * Checks if the expected object has the specified length.
     * <br>
     * Note that this method assumes that 'expected' is an array.
     *
     * @param length the number of elements (> 0) the expected is supposed to have
     * @throws IllegalArgumentException if {@code length < 0}
     * @throws ClassCastException       if {@code expected is not an array}
     */

    public void toHaveLength(int length) {
        if (length < 0) {
            throw new IllegalArgumentException("the specified 'length' must be >= 0");
        }
        if (!expected.getClass().isArray()) {
            throw new ClassCastException("expected " + expected + " to be an array but it wasn't");
        }
        int expectedElementsNumber = Array.getLength(expected);
        if (expectedElementsNumber != length) {
            throw new IllegalArgumentException("expected " + expected + "to have length " + length + " but it was " + expectedElementsNumber);
        }
        passedAssertions++;
    }

    /**
     * @return true if this TestAssertions has passed all the required assertions
     */

    public boolean passed() {
        return passedAssertions == expectedAssertions;
    }
}
