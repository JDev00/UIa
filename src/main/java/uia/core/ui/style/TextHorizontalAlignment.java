package uia.core.ui.style;

import java.util.Objects;

/**
 * The TextHorizontalAlignment collects the horizontal alignment for text.
 */

public enum TextHorizontalAlignment {
    LEFT, CENTER, RIGHT;

    /**
     * Maps the specified {@link TextHorizontalAlignment} value to an integer.
     *
     * @return a number between {0,1,2} where:
     * <ul>
     *     <li>LEFT = 0</li>
     *     <li>CENTER = 1</li>
     *     <li>RIGHT = 2</li>
     * </ul>
     * @throws NullPointerException if {@code textHorizontalAlignment == null}
     */

    public static int map(TextHorizontalAlignment textHorizontalAlignment) {
        Objects.requireNonNull(textHorizontalAlignment);
        switch (textHorizontalAlignment) {
            case LEFT:
                return 0;
            case CENTER:
                return 1;
            default:
                return 2;
        }
    }
}
