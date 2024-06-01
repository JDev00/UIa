package uia.core.ui.style;

import java.util.Objects;

/**
 * The TextVerticalAlignment collects the vertical alignment for text.
 */

public enum TextVerticalAlignment {
    TOP, CENTER, BOTTOM;

    /**
     * Maps the specified {@link TextVerticalAlignment} value to an integer.
     *
     * @return a number between {0, 1, 2} where:
     * <ul>
     *     <li>TOP = 0</li>
     *     <li>CENTER = 1</li>
     *     <li>BOTTOM = 2</li>
     * </ul>
     * @throws NullPointerException if {@code textVerticalAlignment == null}
     */

    public static int map(TextVerticalAlignment textVerticalAlignment) {
        Objects.requireNonNull(textVerticalAlignment);
        switch (textVerticalAlignment) {
            case TOP:
                return 0;
            case CENTER:
                return 1;
            default:
                return 2;
        }
    }
}
