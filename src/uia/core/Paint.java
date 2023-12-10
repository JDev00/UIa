package uia.core;

import uia.utility.Utility;

import java.util.Arrays;
import java.util.Objects;

/**
 * Paint is used to set the entity color, stroke color and stroke width.
 * <br>
 * The Paint class acts as an adapter, the native objects are stored inside it.
 */

public class Paint {
    public static final int NO_STROKE = 0;

    private final int[] color = {255, 255, 255, 255};
    private final int[] colorStroke = {0, 0, 0};
    private int strokeWidth = NO_STROKE;

    private final Object[] natives = new Object[3];

    @Override
    public String toString() {
        return "Paint{" +
                "color=" + Arrays.toString(color) +
                ", colorStroke=" + Arrays.toString(colorStroke) +
                ", strokeWidth=" + strokeWidth +
                ", natives=" + Arrays.toString(natives) +
                '}';
    }

    /**
     * Invalidate this Paint and force the system to rebuild its state
     */

    public void invalidate() {
        natives[0] = null;
    }

    /**
     * @return true if this Paint has a native color set
     */

    public boolean isValid() {
        return natives[0] != null;
    }

    /**
     * Set the native uia.application.platform objects
     *
     * @param nativeColor       the native uia.application.platform color
     * @param nativeStrokeColor the native uia.application.platform stroke color
     * @param nativeStrokeWidth the native uia.application.platform stroke width
     */

    public void setNative(Object nativeColor, Object nativeStrokeColor, Object nativeStrokeWidth) {
        natives[0] = nativeColor;
        natives[1] = nativeStrokeColor;
        natives[2] = nativeStrokeWidth;
    }

    /**
     * @return the native color
     */

    public Object getNativeColor() {
        return natives[0];
    }

    /**
     * @return the native stroke color
     */

    public Object getNativeStrokeColor() {
        return natives[1];
    }

    /**
     * @return the native stroke width
     */

    public Object getNativeStrokeWidth() {
        return natives[2];
    }

    /**
     * Set color and stroke of the given Paint to this one.
     * <br>
     * <i>This operation doesn't modify native objects.</i>
     *
     * @param paint a not null {@link Paint} to copy
     */

    public void set(Paint paint) {
        if (paint != null) {
            System.arraycopy(paint.color, 0, color, 0, color.length);
            System.arraycopy(paint.colorStroke, 0, colorStroke, 0, colorStroke.length);
            strokeWidth = paint.strokeWidth;
            invalidate();
        }
    }

    /**
     * Set color
     *
     * @param r the red channel between [0,255]
     * @param g the green channel between [0,255]
     * @param b the blue channel between [0,255]
     * @param a the alpha channel between [0,255]
     */

    private Paint setColor(int r, int g, int b, int a) {
        if (color[0] != r || color[1] != g || color[2] != b || color[3] != a) {
            color[0] = Utility.constrain(r, 0, 255);
            color[1] = Utility.constrain(g, 0, 255);
            color[2] = Utility.constrain(b, 0, 255);
            color[3] = Utility.constrain(a, 0, 255);
            invalidate();
        }
        return this;
    }

    /**
     * Set stroke color
     *
     * @param r the red channel between [0,255]
     * @param g the green channel between [0,255]
     * @param b the blue channel between [0,255]
     */

    private Paint setStrokeColor(int r, int g, int b) {
        if (colorStroke[0] != r || colorStroke[1] != g || colorStroke[2] != b) {
            colorStroke[0] = Utility.constrain(r, 0, 255);
            colorStroke[1] = Utility.constrain(g, 0, 255);
            colorStroke[2] = Utility.constrain(b, 0, 255);
            invalidate();
        }
        return this;
    }

    /**
     * Set color
     *
     * @param color a not null {@link Color}
     * @throws NullPointerException if {@code color == null}
     */

    public Paint setColor(Color color) {
        int[] channel = color.channel;
        return setColor(channel[0], channel[1], channel[2], channel[3]);
    }

    /**
     * @return the red channel
     */

    public int getRed() {
        return color[0];
    }

    /**
     * @return the green channel
     */

    public int getGreen() {
        return color[1];
    }

    /**
     * @return the blue channel
     */

    public int getBlue() {
        return color[2];
    }

    /**
     * @return the alpha channel
     */

    public int getAlpha() {
        return color[3];
    }

    /**
     * Set the stroke color
     *
     * @param color a not null {@link Color}
     * @throws NullPointerException if {@code color == null}
     */

    public Paint setStrokeColor(Color color) {
        int[] channel = color.channel;
        return setStrokeColor(channel[0], channel[1], channel[2]);
    }

    /**
     * @return the stroke red channel
     */

    public int getStrokeRed() {
        return colorStroke[0];
    }

    /**
     * @return the stroke green channel
     */

    public int getStrokeGreen() {
        return colorStroke[1];
    }

    /**
     * @return the stroke blue channel
     */

    public int getStrokeBlue() {
        return colorStroke[2];
    }

    /**
     * Set the stroke width
     *
     * @param strokeWidth a value {@code >= 0}
     */

    public Paint setStrokeWidth(int strokeWidth) {
        if (this.strokeWidth != strokeWidth) {
            this.strokeWidth = Math.max(strokeWidth, NO_STROKE);
            invalidate();
        }
        return this;
    }

    /**
     * @return the stroke width
     */

    public int getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * @return true if {@link #getStrokeWidth()} > 0
     */

    public boolean hasStroke() {
        return strokeWidth != NO_STROKE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Paint paint = (Paint) o;
        return strokeWidth == paint.strokeWidth
                && Arrays.equals(color, paint.color)
                && Arrays.equals(colorStroke, paint.colorStroke);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(strokeWidth);
        result = 31 * result + Arrays.hashCode(color);
        result = 31 * result + Arrays.hashCode(colorStroke);
        return result;
    }

    /**
     * Checks if the given Paints have the same color
     *
     * @param paint1 a not null {@link Paint}
     * @param paint2 a not null {@link Paint}
     * @return true if the specified Paints have the same color
     * @throws NullPointerException if {@code paint1 == null or paint2 == null}
     */

    public static boolean haveTheSameColor(Paint paint1, Paint paint2) {
        Objects.requireNonNull(paint1);
        Objects.requireNonNull(paint2);
        return paint1.getRed() == paint2.getRed()
                && paint1.getGreen() == paint2.getGreen()
                && paint1.getBlue() == paint2.getBlue()
                && paint1.getAlpha() == paint2.getAlpha();
    }

    /**
     * Color is used to represent an RGB based color format.
     */

    public static class Color {

        /**
         * Color's channels (red, green, blue and alpha)
         */

        enum CHANNEL {
            R, G, B, A;

            /**
             * @return the offset used to extract color's channel
             */

            public int getOffset() {
                switch (this) {
                    case R:
                        return 24;
                    case G:
                        return 16;
                    case B:
                        return 8;
                    default:
                        return 0;
                }
            }
        }

        private final int[] channel;

        public Color(int r, int g, int b, int a) {
            channel = new int[]{r, g, b, a};
        }

        public Color(int grey) {
            this(grey, grey, grey, 255);
        }

        public Color(int r, int g, int b) {
            this(r, g, b, 255);
        }

        public Color(String hex) {
            this(0);

            try {
                hex = hex.replace("0x", "").replace("#", "").toUpperCase();
                int length = hex.length();
                int color = getDecimal(hex);

                if (length == 6) {
                    channel[0] = getChannel(color, CHANNEL.G);
                    channel[1] = getChannel(color, CHANNEL.B);
                    channel[2] = getChannel(color, CHANNEL.A);
                } else if (length == 8) {
                    channel[0] = getChannel(color, CHANNEL.R);
                    channel[1] = getChannel(color, CHANNEL.G);
                    channel[2] = getChannel(color, CHANNEL.B);
                    channel[3] = getChannel(color, CHANNEL.A);
                }
            } catch (Exception ignored) {
            }
        }

        /**
         * @param hex a not null hexadecimal number with uppercase chars
         * @return the decimal representation of the given hex
         */

        public static int getDecimal(String hex) {
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
         * @param color   a hex color
         * @param channel a not null color's channel to extract
         * @return the color's channel value as an integer
         * @throws NullPointerException if {@code channel == null}
         */

        public static int getChannel(int color, CHANNEL channel) {
            return (color >> channel.getOffset()) & 0xFF;
        }
    }
}
