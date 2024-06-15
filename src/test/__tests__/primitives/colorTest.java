package test.__tests__.primitives;

import uia.core.ui.primitives.color.Color;
import test.core.TestAssertion;
import test.core.TestExecutor;
import test.core.Test;

/**
 * Color unit tests.
 */

public class colorTest {

    @Test
    public static void fourChannelColorShouldBeCreated(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        // setup
        int red = 255;
        int green = 0;
        int blue = 112;
        int alpha = 100;
        Color color = Color.createColor(red, green, blue, alpha);

        // verify
        testAssertion.expect(color.getRGBA()).toHaveValues(red, green, blue, alpha);
    }

    @Test
    public static void ColorShouldBeCreatedFromHexValue(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        // setup
        String hexColor = "#ff03C604";
        Color color = Color.createColor(hexColor);

        // verify
        testAssertion.expect(color.getRGBA()).toHaveValues(255, 3, 198, 4);
    }

    @Test
    public static void differentColorObjectsWithTheSameStateShouldHaveTheSameHashcode(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        // setup
        Color color1 = Color.createColor("0x000000");
        Color color2 = Color.createColor(0, 0, 0);

        // verify
        int hashcodeColor1 = color1.hashCode();
        int hashcodeColor2 = color2.hashCode();
        testAssertion.expect(hashcodeColor1).toBe(hashcodeColor2);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new colorTest());
    }
}
