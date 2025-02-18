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
     * Converts the given hexadecimal number to an RGBA color.
     *
     * @param hex a not null hexadecimal number
     * @return the RGBA color
     * @throws IllegalArgumentException if the provided hex number is not valid
     */

    private static int[] fromHexToRGBA(String hex) {
        // 1. normalizes the hex number
        String normalizedHex = hex
                .replace("0x", "")
                .replace("#", "")
                .toLowerCase();
        if (normalizedHex.length() == 6) {
            normalizedHex += "ff";
        }

        // 2. extracts the RGBA color
        if (normalizedHex.length() == 8) {
            int rawColor = Integer.parseUnsignedInt(normalizedHex, 16);
            return new int[]{
                    (rawColor >> 24) & 0xFF,
                    (rawColor >> 16) & 0xFF,
                    (rawColor >> 8) & 0xFF,
                    rawColor & 0xFF
            };
        } else {
            throw new IllegalArgumentException("Invalid hexadecimal number");
        }
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
            int[] rgba = fromHexToRGBA(hex);
            result = createColor(rgba[0], rgba[1], rgba[2], rgba[3]);
        } catch (Exception ignored) {
            //
        }
        return result;
    }
}
