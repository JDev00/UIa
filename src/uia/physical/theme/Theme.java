package uia.physical.theme;

import uia.core.Paint;

/**
 * Collection of colors
 */

public interface Theme {
    Paint.Color TRANSPARENT = createColor(0, 0, 0, 1);
    Paint.Color WHITE = createColor(255);
    Paint.Color BLACK = createColor(0);
    Paint.Color RED = createColor(255, 0, 0);
    Paint.Color GREEN = createColor(0, 255, 0);
    Paint.Color BLUE = createColor(0, 0, 255);

    Paint.Color DARK_GREY = createColor(66);
    Paint.Color DARK_SLATE_GRAY = createColor(47, 79, 79);

    Paint.Color LIGHT_GREY = createColor(211);

    Paint.Color SILVER = createColor(192);
    Paint.Color IVORY = createColor(255, 255, 240);

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
