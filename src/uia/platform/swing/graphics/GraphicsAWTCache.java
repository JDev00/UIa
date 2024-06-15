package uia.platform.swing.graphics;

import uia.core.ui.primitives.color.Color;
import uia.core.ui.primitives.font.Font;

import java.util.Hashtable;
import java.util.Map;

/**
 * Proof of concept.
 */

public class GraphicsAWTCache {
    private final Map<Integer, java.awt.Color> colorCache;
    private final Map<Integer, java.awt.Font> fontCache;

    public GraphicsAWTCache() {
        colorCache = new Hashtable<>();
        fontCache = new Hashtable<>();
    }

    @Override
    public String toString() {
        return "GraphicsAWTCache{colorCache=" + colorCache.size() +
                ", fontCache=" + fontCache.size() +
                '}';
    }

    /**
     * Caches the specified Color.
     * <br>
     * Time complexity: T(1)
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
     * Time complexity: T(1)
     *
     * @param color the color to be cached
     * @return the native cached Color
     */

    public java.awt.Color cacheAndGetNativeColor(Color color) {
        cacheColor(color);
        return getNativeColor(color);
    }

    /**
     * Caches the specified Font.
     * <br>
     * Time complexity: T(1)
     *
     * @param font a Font to be cached
     */

    public void cacheFont(Font font) {
        int fontKey = font.hashCode();
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
     * Time complexity: T(1)
     *
     * @param font the font to be cached
     * @return the native cached font
     */

    public java.awt.Font cacheAndGetNativeFont(Font font) {
        cacheFont(font);
        return getNativeFont(font);
    }
}
