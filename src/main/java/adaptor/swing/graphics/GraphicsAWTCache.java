package adaptor.swing.graphics;

import uia.core.rendering.color.Color;
import uia.core.rendering.font.Font;

import java.util.HashMap;
import java.util.Map;
import java.awt.*;

/**
 * GraphicsAWTCache is responsible for caching objects required by the AWT Graphics component.
 */

public class GraphicsAWTCache {
    private final Map<Integer, java.awt.Color> colorCache;
    private final Map<Integer, java.awt.Font> fontCache;
    private final Map<Float, BasicStroke> strokeCache;

    public GraphicsAWTCache() {
        strokeCache = new HashMap<>();
        colorCache = new HashMap<>();
        fontCache = new HashMap<>();
    }

    @Override
    public String toString() {
        return "GraphicsAWTCache{colorCache=" + colorCache.size() +
                ", fontCache=" + fontCache.size() +
                ", strokeCache=" + strokeCache.size() +
                '}';
    }

    // color cache

    /**
     * Caches the specified Color.
     * <br>
     * <br>
     * Time complexity:
     * <ul>
     *     <li>average case: O(1)</li>
     *     <li>worst case: O(n).</li>
     * </ul>
     *
     * @param color a Color to cache
     */

    public void cacheColor(Color color) {
        int colorKey = color.hashCode();
        colorCache.computeIfAbsent(colorKey, key -> GraphicsAWTUtility.createColor(color));
    }

    /**
     * @return the native color associated to the specified Color
     */

    public java.awt.Color getNativeColor(Color color) {
        int colorKey = color.hashCode();
        return colorCache.get(colorKey);
    }

    /**
     * Caches and returns the specified Color. If the Color is already cached, it immediately
     * returns the native color.
     * <br>
     * <br>
     * Time complexity:
     * <ul>
     *     <li>average case: O(1)</li>
     *     <li>worst case: O(n).</li>
     * </ul>
     *
     * @param color the color to be cached
     * @return the native cached Color
     */

    public java.awt.Color cacheAndGetNativeColor(Color color) {
        cacheColor(color);
        return getNativeColor(color);
    }

    // font cache

    /**
     * Caches the specified Font.
     * <br>
     * <br>
     * Time complexity:
     * <ul>
     *     <li>average case: O(1)</li>
     *     <li>worst case: O(n).</li>
     * </ul>
     *
     * @param font a Font to be cached
     */

    public void cacheFont(Font font) {
        int fontKey = font.hashCode();
        // bugfix: rebuilds font data
        if (fontCache.containsKey(fontKey)) {
            java.awt.Font awtFont = getNativeFont(font);
            GraphicsAWTUtility.buildFontData(font, awtFont);
        }
        // creates and caches font if not already cached
        fontCache.computeIfAbsent(fontKey, key -> GraphicsAWTUtility.createFont(font));
    }

    /**
     * @return the native font associated to the specified Font
     */

    public java.awt.Font getNativeFont(Font font) {
        int fontKey = font.hashCode();
        return fontCache.get(fontKey);
    }

    /**
     * Caches and returns the specified font. If the font is already cached, it immediately
     * returns the native font.
     * <br>
     * <br>
     * Time complexity:
     * <ul>
     *     <li>average case: O(1)</li>
     *     <li>worst case: O(n).</li>
     * </ul>
     *
     * @param font the font to be cached
     * @return the native cached font
     */

    public java.awt.Font cacheAndGetNativeFont(Font font) {
        cacheFont(font);
        return getNativeFont(font);
    }

    // stroke cache

    /**
     * Caches the specified stroke value.
     * <br>
     * <br>
     * Time complexity:
     * <ul>
     *     <li>average case: O(1)</li>
     *     <li>worst case: O(n).</li>
     * </ul>
     *
     * @param strokeWidth the stroke value to be cached
     */

    public void cacheStroke(float strokeWidth) {
        strokeCache.computeIfAbsent(strokeWidth, key -> new BasicStroke(strokeWidth));
    }

    /**
     * @return the native stroke associated to the stroke value
     */

    public BasicStroke getNativeStroke(float strokeWidth) {
        return strokeCache.get(strokeWidth);
    }

    /**
     * Caches and returns the specified stroke. If the stroke is already cached, it immediately
     * returns the native stroke.
     * <br>
     * <br>
     * Time complexity:
     * <ul>
     *     <li>average case: O(1)</li>
     *     <li>worst case: O(n).</li>
     * </ul>
     *
     * @param strokeWidth the value of the stroke to be cached
     * @return the native cached stroke value
     */

    public BasicStroke cacheAndGetNativeStroke(float strokeWidth) {
        cacheStroke(strokeWidth);
        return getNativeStroke(strokeWidth);
    }
}
