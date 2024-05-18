package uia.core;

import uia.core.paint.Color;
import uia.core.paint.Paint;
import uia.physical.theme.Theme;

/**
 * Style is responsible for storing the UI component graphical settings.
 */

public final class Style {
    private final Paint paint;
    private Font font;

    public Style() {
        paint = new Paint();
        font = Font.createDesktopFont(Font.Style.PLAIN);
    }

    // color

    public Style setBackgroundColor(Color color) {
        paint.setColor(color);
        return this;
    }

    public Style setBorderColor(Color color) {
        paint.setStrokeColor(color);
        return this;
    }

    public Style setTextColor(Color color) {
        paint.setTextColor(color);
        return this;
    }

    // temp

    public Paint getPaint() {
        return paint;
    }

    // font

    public Style setFontName(String name) {
        font.setName(name);
        return this;
    }

    public Style setFontStyle(Font.Style fontStyle) {
        font.setStyle(fontStyle);
        return this;
    }

    public Style setFontSize(float size) {
        font.setSize(size);
        return this;
    }

    // temp

    public Font getFont() {
        return font;
    }

    // geometry style

    public Style setBorderWidth(int borderWidth) {
        paint.setStrokeWidth(borderWidth);
        return this;
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

    public static void main(String[] args) {
        Style style = new Style();
        style.setBorderColor(Theme.GREEN_YELLOW)
                .setFontName("ciao!")
                .setFontSize(10f)
                .setMaxWidth(10f)
                .setPosition(10f, 10f)
                .translate(20f, 0f);
    }
}
