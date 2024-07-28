package uia.core.rendering.color;

import uia.utility.MathUtility;

import java.util.Arrays;

/**
 * The Color Object stores an RGBA-based color.
 */

public final class Color {
    private final int[] channels;

    private Color(int red, int green, int blue, int alpha) {
        channels = new int[]{red, green, blue, alpha};
    }

    @Override
    public String toString() {
        return "Color{" +
                "red=" + channels[0] +
                ", green=" + channels[1] +
                ", blue=" + channels[2] +
                ", alpha=" + channels[3] +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Arrays.equals(channels, color.channels);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(channels);
    }

    @Override
    public Color clone() {
        return createColor(getRed(), getGreen(), getBlue(), getAlpha());
    }

    /**
     * @return the red color channel
     */

    public int getRed() {
        return channels[0];
    }

    /**
     * @return the green color channel
     */

    public int getGreen() {
        return channels[1];
    }

    /**
     * @return the blue color channel
     */

    public int getBlue() {
        return channels[2];
    }

    /**
     * @return the alpha color channel
     */

    public int getAlpha() {
        return channels[3];
    }

    /**
     * @return the color as a new array made of:
     * <ul>
     *     <li>red color channel;</li>
     *     <li>green color channel;</li>
     *     <li>blue color channel;</li>
     *     <li>alpha color channel;</li>
     * </ul>
     */

    public int[] getRGBA() {
        return new int[]{
                getRed(),
                getGreen(),
                getBlue(),
                getAlpha()
        };
    }

    /**
     * @return the correct color channel value
     */

    private static int getChannelValue(int value) {
        return MathUtility.constrain(value, 0, 255);
    }

    /**
     * Creates a new Color.
     *
     * @param red   the red color channel value between [0, 255]
     * @param green the green color channel value between [0, 255]
     * @param blue  the blue color channel value between [0, 255]
     * @param alpha the alpha color channel value between [0, 255]
     * @return a new {@link Color}
     */

    public static Color createColor(int red, int green, int blue, int alpha) {
        return new Color(
                getChannelValue(red),
                getChannelValue(green),
                getChannelValue(blue),
                getChannelValue(alpha)
        );
    }

    /**
     * Creates a new Color.
     *
     * @param red   the red color channel value between [0, 255]
     * @param green the green color channel value between [0, 255]
     * @param blue  the blue color channel value between [0, 255]
     * @return a new {@link Color}
     */

    public static Color createColor(int red, int green, int blue) {
        return createColor(red, green, blue, 255);
    }

    /**
     * Creates a new Color.
     *
     * @param grey the grey color channel value between [0, 255]
     * @return a new {@link Color}
     */

    public static Color createColor(int grey) {
        return createColor(grey, grey, grey, 255);
    }

    /**
     * @param hex a not null hexadecimal number with uppercase chars
     * @return the decimal representation of the given hex
     */

    private static int hexToDecimal(String hex) {
        String digits = "0123456789ABCDEF";
        int val = 0;
        for (int i = 0; i < hex.length(); i++) {
            char c = hex.charAt(i);
            int d = digits.indexOf(c);
            val = 16 * val + d;
        }
        return val;
    }

    /**
     * Creates a new Color based on the specified hex value.
     *
     * @param hex a hex value
     * @return a new Color or null if the specified hex is not valid
     */

    public static Color createColor(String hex) {
        Color result = null;
        try {
            hex = hex.replace("0x", "")
                    .replace("#", "")
                    .toUpperCase();
            int length = hex.length();
            int color = hexToDecimal(hex);

            if (length == 6) {
                result = createColor(
                        ColorChannel.getChannelValue(color, ColorChannel.GREEN),
                        ColorChannel.getChannelValue(color, ColorChannel.BLUE),
                        ColorChannel.getChannelValue(color, ColorChannel.ALPHA)
                );
            } else if (length == 8) {
                result = createColor(
                        ColorChannel.getChannelValue(color, ColorChannel.RED),
                        ColorChannel.getChannelValue(color, ColorChannel.GREEN),
                        ColorChannel.getChannelValue(color, ColorChannel.BLUE),
                        ColorChannel.getChannelValue(color, ColorChannel.ALPHA)
                );
            }
        } catch (Exception ignored) {
            //
        }
        return result;
    }
}
