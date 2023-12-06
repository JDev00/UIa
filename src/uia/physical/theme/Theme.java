package uia.physical.theme;

import uia.core.Paint;

/**
 * Collection of colors
 */

public interface Theme {
    Paint.Color TRANSPARENT = createColor(0, 0, 0, 1);

    // gray
    Paint.Color BLACK = createColor(0);
    Paint.Color WHITE = createColor(255);
    Paint.Color WHITE_SMOKE = createColor(245);
    Paint.Color SNOW = createColor(255, 250, 250);
    Paint.Color IVORY = createColor(255, 255, 240);
    Paint.Color DARK_GRAY = createColor(66);
    Paint.Color DARK_SLATE_GRAY = createColor(47, 79, 79);
    Paint.Color LIGHT_GRAY = createColor(211);
    Paint.Color SILVER = createColor(192);

    // red
    Paint.Color RED = createColor(255, 0, 0);
    Paint.Color DARK_RED = createColor(139, 0, 0);
    Paint.Color CHOCOLATE = createColor(210, 105, 30);
    Paint.Color LIGHT_CORAL = createColor(240, 128, 128);

    // green
    Paint.Color LIME = createColor(0, 255, 0);
    Paint.Color GREEN = createColor(0, 128, 0);
    Paint.Color GREEN_YELLOW = createColor(173, 255, 47);
    Paint.Color TEAL = createColor(0, 128, 128);

    // blue
    Paint.Color BLUE = createColor(0, 0, 255);
    Paint.Color ROYAL_BLUE = createColor(65, 105, 225);
    Paint.Color MIDNIGHT_BLUE = createColor(25, 25, 112);
    Paint.Color NAVY = createColor(0, 0, 128);

    /**
     * Creates a new Color
     */

    static Paint.Color createColor(int r, int g, int b) {
        return new Paint.Color(r, g, b);
    }

    /**
     * Creates a new Color
     */

    static Paint.Color createColor(int gray) {
        return new Paint.Color(gray);
    }

    /**
     * Creates a new Color
     */

    static Paint.Color createColor(int r, int g, int b, int a) {
        return new Paint.Color(r, g, b, a);
    }
}
