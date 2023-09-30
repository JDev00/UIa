package test.artefacts;

public class TestValidation {

    /**
     * Assert that the given message is equal to the answer.
     * <br>
     * If message and answer are null, then the assertion will be True.
     */

    public static void assertThat(Object message, Object answer) {
        System.out.print("TEST result: ");

        if (message == answer) {
            System.out.println("PASSED");
            System.exit(0);
        } else {
            System.out.println("FAILED");
            System.exit(1);
        }
    }
}
