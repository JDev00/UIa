package uia.core.Paint;

import uia.utility.Utility;

import java.util.Arrays;
import java.util.Objects;

/**
 * Color has the responsibility to store an RGBA color.
 */

public final class Color {

    /**
     * Color channel
     */

    enum ColorChannel {
        RED, GREEN, BLUE, ALPHA;

        /**
         * @return the offset used to extract the color channel
         */

        public int getOffset() {
            switch (this) {
                case RED:
                    return 24;
                case GREEN:
                    return 16;
                case BLUE:
                    return 8;
                default:
                    return 0;
            }
        }

        /**
         * @param color        a hex color
         * @param colorChannel a not null color channel to extract
         * @return the color channel value as an integer
         * @throws NullPointerException if {@code channel == null}
         */

        public static int getChannelValue(int color, ColorChannel colorChannel) {
            return (color >> colorChannel.getOffset()) & 0xFF;
        }
    }

    private final int[] channels;

    private Color(int r, int g, int b, int a) {
        channels = new int[]{r, g, b, a};
    }

    @Override
    public String toString() {
        return "Color{" +
                "channels=" + Arrays.toString(channels) +
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
        return Utility.constrain(value, 0, 255);
    }

    /**
     * Creates a new Color
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
     * Creates a new Color
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
     * Creates a new Color
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
     * Creates a new Color based on the specified hex value
     *
     * @param hex a not null hex value
     * @return a new {@link Color} or null if the specified hex is malformed
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
        }
        return result;
    }

    /**
     * Copies the specified color
     *
     * @param color a {@link Color} to copy
     * @return a new copy of the specified color
     */

    public static Color copy(Color color) {
        return createColor(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                color.getAlpha()
        );
    }

    /**
     * Checks whether the specified colour channels are the same as the specified channels.
     *
     * @param color a not null color to control
     * @param red   the color channel red value
     * @param green the green color channel value
     * @param blue  the blue color channel value
     * @param alpha the alpha color channel value
     * @return true if the color channels are equal to the given ones
     * @throws NullPointerException if {@code color == null}
     */

    public static boolean equals(Color color, int red, int green, int blue, int alpha) {
        Objects.requireNonNull(color);
        return color.getRed() == red
                && color.getGreen() == green
                && color.getBlue() == blue
                && color.getAlpha() == alpha;
    }
}
