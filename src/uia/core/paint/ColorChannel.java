package uia.core.paint;

/**
 * Color channels.
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