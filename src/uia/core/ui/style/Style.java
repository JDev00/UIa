package uia.core.ui.style;

import uia.physical.theme.Theme;
import uia.core.paint.Color;
import uia.core.font.Font;

import java.util.Objects;

/**
 * Style is responsible for storing the UI component graphical settings.
 */

public final class Style {
    private Color backgroundColor;
    private Color borderColor;
    private Color textColor;

    // text
    private TextHorizontalAlignment textHorizontalAlignment;
    private TextVerticalAlignment textVerticalAlignment;
    private Font font;

    private float borderWidth = 0;

    public Style() {
        backgroundColor = Theme.WHITE;
        textColor = Theme.BLACK;
        borderColor = null;

        textHorizontalAlignment = TextHorizontalAlignment.CENTER;
        textVerticalAlignment = TextVerticalAlignment.TOP;

        font = Font.createDesktopFont(Font.FontStyle.PLAIN);
    }

    @Override
    public String toString() {
        return "Style {\nbackgroundColor=" + backgroundColor +
                ",\nborderColor=" + borderColor +
                ",\ntextColor=" + textColor +
                ",\ntextHorizontalAlignment=" + textHorizontalAlignment +
                ",\ntextVerticalAlignment=" + textVerticalAlignment +
                ",\nfont=" + font +
                ",\nborderWidth=" + borderWidth +
                "\n}";
    }

    /**
     * Changes this Style by applying the given style function.
     *
     * @param styleFunction a StyleFunction used to change this style
     * @return this Style
     * @throws NullPointerException if {@code styleFunction == null}
     */

    public Style applyStyleFunction(StyleFunction styleFunction) {
        Objects.requireNonNull(styleFunction);
        styleFunction.apply(this);
        return this;
    }

    /**
     * Sets the component background color.
     * <br>
     * Note: the specified color is not copied. As a result, side effects are allowed.
     *
     * @param color the background color
     * @return this Style
     * @throws NullPointerException if {@code color == null}
     */

    public Style setBackgroundColor(Color color) {
        Objects.requireNonNull(color);
        backgroundColor = color;
        return this;
    }

    /**
     * @return the component background color
     */

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    /**
     * Sets the component border color.
     * <br>
     * Note: the specified color is not copied. As a result, side effects are allowed.
     *
     * @param color the component border color; it could be null
     * @return this Style
     */

    public Style setBorderColor(Color color) {
        borderColor = color;
        return this;
    }

    /**
     * @return the component border color
     */

    public Color getBorderColor() {
        return borderColor;
    }

    /**
     * Sets the component text color.
     * <br>
     * Note: the specified color is not copied. As a result, side effects are allowed.
     *
     * @param color the component text color; it could be null
     * @return this Style
     */

    public Style setTextColor(Color color) {
        this.textColor = color;
        return this;
    }

    /**
     * @return the component text color
     */

    public Color getTextColor() {
        return this.textColor;
    }

    /**
     * Sets the component border width.
     *
     * @param borderWidth the component border width greater than or equal to zero
     * @throws IllegalArgumentException if {@code borderWidth < 0}
     */

    public Style setBorderWidth(float borderWidth) {
        if (borderWidth < 0) {
            throw new IllegalArgumentException("borderWidth can't be < 0");
        }
        this.borderWidth = borderWidth;
        return this;
    }

    /**
     * @return the component border width
     */

    public float getBorderWidth() {
        return borderWidth;
    }

    // text

    /**
     * Sets the horizontal text alignment.
     *
     * @param textAlignment one of {@link TextHorizontalAlignment}
     * @return this Style
     * @throws NullPointerException if {@code textAlignment == null}
     */

    public Style setTextAlignment(TextHorizontalAlignment textAlignment) {
        Objects.requireNonNull(textAlignment);
        textHorizontalAlignment = textAlignment;
        return this;
    }

    /**
     * @return the horizontal text alignment
     */

    public TextHorizontalAlignment getHorizontalTextAlignment() {
        return textHorizontalAlignment;
    }

    /**
     * Sets the vertical text alignment.
     *
     * @param textAlignment one of {@link TextVerticalAlignment}
     * @return this Style
     * @throws NullPointerException if {@code textAlignment == null}
     */

    public Style setTextAlignment(TextVerticalAlignment textAlignment) {
        Objects.requireNonNull(textAlignment);
        textVerticalAlignment = textAlignment;
        return this;
    }

    /**
     * @return the vertical text alignment
     */

    public TextVerticalAlignment getVerticalTextAlignment() {
        return textVerticalAlignment;
    }

    /**
     * Sets an external Font object as the font style.
     * <br>
     * <i>To be used with caution due to side effects.</i>
     *
     * @param font the font to be set
     * @return this Style
     * @throws NullPointerException if {@code font == null}
     */

    public Style setFont(Font font) {
        Objects.requireNonNull(font);
        this.font = font;
        return this;
    }

    /**
     * Sets the font name.
     *
     * @param name the font name
     * @return this Style
     */

    public Style setFontName(String name) {
        font.setName(name);
        return this;
    }

    /**
     * Sets the font style.
     *
     * @param fontStyle the font style
     * @return this Style
     */

    public Style setFontStyle(Font.FontStyle fontStyle) {
        font.setStyle(fontStyle);
        return this;
    }

    /**
     * Sets the font size.
     *
     * @param size the font size
     * @return this Style
     */

    public Style setFontSize(float size) {
        font.setSize(size);
        return this;
    }

    /**
     * @return the Font used by this style
     */

    public Font getFont() {
        return font;
    }

    // positioning

    public Style setMargin() {
        return this;
    }

    public Style setPosition(float x, float y) {
        return this;
    }

    public Style translate(float x, float y) {
        return this;
    }

    // dimensioning

    public Style setMinWidth(float width) {
        return this;
    }

    public Style setMinHeight(float height) {
        return this;
    }

    public Style setMaxWidth(float width) {
        return this;
    }

    public Style setMaxHeight(float height) {
        return this;
    }

    public Style setDimension(float x, float y) {
        return this;
    }

    public Style scale(float x, float y) {
        return this;
    }

    // rotation

    public Style setRotation(float angle) {
        return this;
    }

    public Style rotate(float angle) {
        return this;
    }

    //

    /**
     * Returns the bounding rect of the transformed UI element:
     * <ul>
     *     <li>the position on the x-axis of the left-top corner;</li>
     *     <li>the position on the y-axis of the left-top corner;</li>
     *     <li>width;</li>
     *     <li>height;</li>
     *     <li>left padding;</li>
     *     <li>top padding;</li>
     *     <li>right padding;</li>
     *     <li>bottom padding;</li>
     * </ul>
     */
    public float[] getBoundingRect(float[] array) {
        return array;
    }

    public float getOffsetWidth() {
        return 0f;
    }

    public float getOffsetHeight() {
        return 0f;
    }

    public float getRotation() {
        return 0f;
    }
}
