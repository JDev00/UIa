import org.junit.jupiter.api.Test;

import uia.core.rendering.font.Font;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Font unit tests.
 */

class TestFont {

    @Test
    void differentFontWithTheSameStateShouldHaveTheSameHashcode() {
        // setup
        Font.FontStyle fontStyle = Font.FontStyle.PLAIN;
        String fontName = "Arial";
        float fontSize = 21f;
        Font font1 = new Font(fontName, fontStyle, fontSize);
        Font font2 = new Font(fontName, fontStyle, fontSize);

        // verify
        int hashcodeFont1 = font1.hashCode();
        int hashcodeFont2 = font2.hashCode();
        assertEquals(hashcodeFont1, hashcodeFont2);
    }
}
