package uia.physical.theme;

import uia.core.Paint.Color;

/**
 * Collection of colors
 */

public interface Theme {
    Color TRANSPARENT = Color.createColor(0, 0, 0, 1);

    // gray
    Color BLACK = Color.createColor(0);
    Color WHITE = Color.createColor(255);
    Color WHITE_SMOKE = Color.createColor(245);
    Color SNOW = Color.createColor(255, 250, 250);
    Color IVORY = Color.createColor(255, 255, 240);
    Color DARK_GRAY = Color.createColor(66);
    Color DARK_SLATE_GRAY = Color.createColor(47, 79, 79);
    Color LIGHT_GRAY = Color.createColor(211);
    Color SILVER = Color.createColor(192);

    // red
    Color RED = Color.createColor(255, 0, 0);
    Color DARK_RED = Color.createColor(139, 0, 0);
    Color CHOCOLATE = Color.createColor(210, 105, 30);
    Color LIGHT_CORAL = Color.createColor(240, 128, 128);

    // green
    Color LIME = Color.createColor(0, 255, 0);
    Color GREEN = Color.createColor(0, 128, 0);
    Color GREEN_YELLOW = Color.createColor(173, 255, 47);
    Color TEAL = Color.createColor(0, 128, 128);

    // blue
    Color BLUE = Color.createColor(0, 0, 255);
    Color ROYAL_BLUE = Color.createColor(65, 105, 225);
    Color MIDNIGHT_BLUE = Color.createColor(25, 25, 112);
    Color NAVY = Color.createColor(0, 0, 128);
}
