package test.core;

import java.lang.annotation.*;

/**
 * Test is used to mark a class method as a unit test.
 * This allows the test framework to recognise and execute the unit test.
 *
 * @since 1.3.0
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Test {
}
