package uia.core.ui.style;

import uia.core.rendering.geometry.Geometries;
import uia.core.rendering.geometry.Geometry;
import uia.core.rendering.color.Color;
import uia.core.rendering.font.Font;
import uia.physical.ui.Theme;

import java.util.function.Consumer;
import java.util.Objects;

import static uia.utility.MathUtility.TWO_PI;
import static java.lang.Math.max;

/**
 * Style is responsible for storing the UI component graphical settings.
 */

public final class Style {
    private Color backgroundColor;
    private Color borderColor;
    private Color textColor;
    private float borderWidth = 0;

    // text
    private TextHorizontalAlignment textHorizontalAlignment;
    private TextVerticalAlignment textVerticalAlignment;
    private Font font;

    // geometry
    private Consumer<Geometry> geometryBuilder;
    private boolean buildGeometryDynamically = false;

    // positioning
    private final float[] minDimension = {0f, 0f};
    private final float[] maxDimension = {0f, 0f};
    private final float[] container = {0f, 0f, 0f, 0f, 0f};

    public Style() {
        backgroundColor = Theme.WHITE;
        textColor = Theme.BLACK;
        borderColor = null;

        textHorizontalAlignment = TextHorizontalAlignment.CENTER;
        textVerticalAlignment = TextVerticalAlignment.TOP;

        font = Font.createDesktopFont(Font.FontStyle.PLAIN);

        geometryBuilder = Geometries::rect;
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

    // geometry

    /**
     * Sets the component geometry builder.
     *
     * @param geometryBuilder a function used to create the geometry
     * @param dynamicBuilding true to force the rebuilding of the geometry on each element refresh
     * @return this Style
     * @throws NullPointerException if {@code geometryBuilder == null}
     */

    public Style setGeometry(Consumer<Geometry> geometryBuilder, boolean dynamicBuilding) {
        Objects.requireNonNull(geometryBuilder);
        this.buildGeometryDynamically = dynamicBuilding;
        this.geometryBuilder = geometryBuilder;
        return this;
    }

    /**
     * @return the component geometry builder
     */

    public Consumer<Geometry> getGeometryBuilder() {
        return geometryBuilder;
    }

    /**
     * @return true if geometry must be rebuilt on each element refresh
     */

    public boolean isGeometryToBeBuiltDynamically() {
        return this.buildGeometryDynamically;
    }

    // margin & padding

    public Style setMargin() {
        throw new UnsupportedOperationException();
    }

    public Style setPadding() {
        throw new UnsupportedOperationException();
    }

    // positioning

    /**
     * Sets the component position.
     *
     * @param x the component position on the x-axis
     * @param y the component position on the y-axis
     * @return this Style
     */

    public Style setPosition(float x, float y) {
        container[0] = x;
        container[1] = y;
        return this;
    }

    /**
     * Translates the component.
     *
     * @param x the translation on the x-axis
     * @param y the translation on the y-axis
     * @return this Style
     */

    public Style translate(float x, float y) {
        container[0] += x;
        container[1] += y;
        return this;
    }

    /**
     * @return the component position on the x-axis
     */

    public float getX() {
        return container[0];
    }

    /**
     * @return the component position on the y-axis
     */

    public float getY() {
        return container[1];
    }

    // dimensioning

    /**
     * Sets the minimum component width in pixels.
     *
     * @param width the minimum component width
     * @return this Style
     */

    public Style setMinWidth(float width) {
        minDimension[0] = Math.max(0, width);
        return this;
    }

    /**
     * @return the minimum component width in pixels
     */

    public float getMinWidth() {
        return minDimension[0];
    }

    /**
     * Sets the minimum component height in pixels.
     *
     * @param height the minimum component height
     * @return this Style
     */

    public Style setMinHeight(float height) {
        minDimension[1] = Math.max(0, height);
        return this;
    }

    /**
     * @return the minimum component height in pixels
     */

    public float getMiHeight() {
        return minDimension[1];
    }

    /**
     * Sets the maximum component width in pixels.
     *
     * @param width the maximum component width
     * @return this Style
     */

    public Style setMaxWidth(float width) {
        maxDimension[0] = Math.max(0f, width);
        return this;
    }

    /**
     * @return the maximum component width in pixels
     */

    public float getMaxWidth() {
        return maxDimension[0];
    }

    /**
     * Sets the maximum component height in pixels.
     *
     * @param height the maximum component height
     * @return this Style
     */

    public Style setMaxHeight(float height) {
        maxDimension[1] = Math.max(0f, height);
        return this;
    }

    /**
     * @return the maximum component height in pixels
     */

    public float getMaxHeight() {
        return maxDimension[1];
    }

    /**
     * Sets the component dimension.
     *
     * @param width  the component width
     * @param height the component height
     * @return this Style
     */

    public Style setDimension(float width, float height) {
        container[2] = max(0, width);
        container[3] = max(0, height);
        return this;
    }

    /**
     * Scales the component.
     *
     * @param x the scale on the x-axis
     * @param y the scale on the y-axis
     * @return this Style
     */

    public Style scale(float x, float y) {
        container[2] *= max(0, x);
        container[3] *= max(0, y);
        return this;
    }

    /**
     * @return the component width. Note that offsetWidth won't change as consequence of a rotation.
     */

    public float getOffsetWidth() {
        return container[2];
    }

    /**
     * @return the component height. Note that offsetHeight won't change as consequence of a rotation.
     */

    public float getOffsetHeight() {
        return container[3];
    }

    // rotation

    /**
     * Set the component rotation.
     *
     * @param radians the rotation in radians
     * @return this Style
     */

    public Style setRotation(float radians) {
        container[4] = radians % TWO_PI;
        return this;
    }

    /**
     * Rotates the component by the given amount.
     *
     * @param radians the rotation in radians
     * @return this Style
     */


    public Style rotate(float radians) {
        container[4] += (radians % TWO_PI);
        return this;
    }

    /**
     * @return the component rotation in radians
     */

    public float getRotation() {
        return container[4];
    }
}
