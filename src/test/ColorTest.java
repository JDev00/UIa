import org.junit.jupiter.api.Test;

import uia.core.rendering.color.Color;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Color unit tests.
 */

class ColorTest {

    @Test
    void fourChannelColorShouldBeCreated() {
        // act
        int red = 255;
        int green = 0;
        int blue = 112;
        int alpha = 100;
        Color color = Color.createColor(red, green, blue, alpha);

        // verify
        int[] expectedChannels = {red, green, blue, alpha};
        assertArrayEquals(expectedChannels, color.getRGBA());
    }

    @Test
    void ColorShouldBeCreatedFromHexValue() {
        // setup
        String hexColor = "#ff03C604";
        Color color = Color.createColor(hexColor);

        // verify
        int[] expectedChannels = {255, 3, 198, 4};
        assertArrayEquals(expectedChannels, color.getRGBA());
    }

    @Test
    void differentColorObjectsWithTheSameStateShouldHaveTheSameHashcode() {
        // setup
        Color color1 = Color.createColor("0x000000");
        Color color2 = Color.createColor(0, 0, 0);

        // verify
        int hashcodeColor1 = color1.hashCode();
        int hashcodeColor2 = color2.hashCode();
        assertEquals(hashcodeColor1, hashcodeColor2);
    }

    @Test
    void ColorShouldBeCloned() {
        // setup
        Color color = Color.createColor(255, 0, 0);

        // act
        Color clonedColor = color.clone();

        // verify
        int[] colorChannels = color.getRGBA();
        int[] clonedColorChannels = clonedColor.getRGBA();
        assertArrayEquals(colorChannels, clonedColorChannels);
    }
}
