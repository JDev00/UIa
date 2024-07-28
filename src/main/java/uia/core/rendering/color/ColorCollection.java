package uia.core.rendering.color;

/**
 * The ColorCollection is a collection of some of the known colours.
 */

public final class ColorCollection {

    private ColorCollection() {
    }

    public static final Color TRANSPARENT = Color.createColor(0, 0, 0, 1);

    // gray
    public static final Color BLACK = Color.createColor(0);
    public static final Color WHITE = Color.createColor(255);
    public static final Color WHITE_SMOKE = Color.createColor(245);
    public static final Color SNOW = Color.createColor(255, 250, 250);
    public static final Color IVORY = Color.createColor(255, 255, 240);
    public static final Color DARK_GRAY = Color.createColor(66);
    public static final Color DARK_SLATE_GRAY = Color.createColor(47, 79, 79);
    public static final Color LIGHT_GRAY = Color.createColor(211);
    public static final Color SILVER = Color.createColor(192);

    // red
    public static final Color RED = Color.createColor(255, 0, 0);
    public static final Color DARK_RED = Color.createColor(139, 0, 0);
    public static final Color CHOCOLATE = Color.createColor(210, 105, 30);
    public static final Color LIGHT_CORAL = Color.createColor(240, 128, 128);
    public static final Color PINK = Color.createColor(255, 192, 203);

    // green
    public static final Color LIME = Color.createColor(0, 255, 0);
    public static final Color GREEN = Color.createColor(0, 128, 0);
    public static final Color GREEN_YELLOW = Color.createColor(173, 255, 47);
    public static final Color TEAL = Color.createColor(0, 128, 128);

    // blue
    public static final Color BLUE = Color.createColor(0, 0, 255);
    public static final Color ROYAL_BLUE = Color.createColor(65, 105, 225);
    public static final Color MIDNIGHT_BLUE = Color.createColor(25, 25, 112);
    public static final Color NAVY = Color.createColor(0, 0, 128);
}
