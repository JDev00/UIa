package test.__tests__.primitives;

import uia.core.ui.primitives.font.Font;
import test.core.TestAssertion;
import test.core.TestExecutor;
import test.core.Test;

public class fontTest {

    @Test
    public static void differentFontWithTheSameStateShouldHaveTheSameHashcode(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        // setup
        Font.FontStyle fontStyle = Font.FontStyle.PLAIN;
        String fontName = "Arial";
        float fontSize = 21f;

        Font font1 = new Font(fontName, fontStyle, fontSize);
        Font font2 = new Font(fontName, fontStyle, fontSize);

        // verify
        int hashcodeFont1 = font1.hashCode();
        int hashcodeFont2 = font2.hashCode();
        testAssertion.expect(hashcodeFont1).toBe(hashcodeFont2);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new fontTest());
    }
}
