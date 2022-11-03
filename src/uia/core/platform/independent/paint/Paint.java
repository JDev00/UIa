package uia.core.platform.independent.paint;

import uia.core.platform.independent.NativeBuilder;
import uia.utils.Utils;

/**
 * Paint is used to set geometry properties like: color, strokes and strokes width
 */

public class Paint extends NativeBuilder {
    private final int[] color;
    private final int[] colorStrokes;

    private int strokeWidth = 1;

    private Object nativeColor;
    private Object nativeStrokeColor;
    private Object nativeStrokeWidth;

    private boolean strokes = false;

    public Paint(int r, int g, int b, int a) {
        color = new int[]{r, g, b, a};
        colorStrokes = new int[]{0, 0, 0};
    }

    public Paint(int r, int g, int b) {
        this(r, g, b, 255);
    }

    /**
     * Set the native objects
     *
     * @param nativeColor       the native color object
     * @param nativeStrokeColor the native stroke color object
     * @param nativeStrokeWidth the native stroke width object
     */

    public void setNative(Object nativeColor, Object nativeStrokeColor, Object nativeStrokeWidth) {
        this.nativeColor = nativeColor;
        this.nativeStrokeColor = nativeStrokeColor;
        this.nativeStrokeWidth = nativeStrokeWidth;

        if (nativeColor == null) {
            request();
        } else {
            deny();
        }
    }

    /**
     * Set a new GREY color
     *
     * @param grey a value between [0,255] used to express the grey's intensity
     */

    public Paint setColor(int grey) {
        return setColor(grey, grey, grey, 255);
    }

    /**
     * Set a new RGB color
     *
     * @param r the red channel between [0,255]
     * @param g the green channel between [0,255]
     * @param b the blue channel between [0,255]
     */

    public Paint setColor(int r, int g, int b) {
        return setColor(r, g, b, 255);
    }

    /**
     * Set a new RGBA color
     *
     * @param r the red channel between [0,255]
     * @param g the green channel between [0,255]
     * @param b the blue channel between [0,255]
     * @param a the alpha channel between [0,255]
     */

    public Paint setColor(int r, int g, int b, int a) {
        int oC0 = color[0];
        int oC1 = color[1];
        int oC2 = color[2];
        int oC3 = color[3];
        color[0] = Utils.constrain(r, 0, 255);
        color[1] = Utils.constrain(g, 0, 255);
        color[2] = Utils.constrain(b, 0, 255);
        color[3] = Utils.constrain(a, 0, 255);
        if (color[0] != oC0 || color[1] != oC1 || color[2] != oC2 || color[3] != oC3) {
            request();
        }
        return this;
    }

    /**
     * Enable or disable the stroke rendering
     *
     * @param strokes true to enable stroke
     */

    public Paint enableStrokes(boolean strokes) {
        this.strokes = strokes;
        return this;
    }

    /**
     * Set a new strokes GREY color
     *
     * @param grey a value between [0,255] used to express the grey's intensity
     */

    public Paint setStrokesColor(int grey) {
        return setStrokesColor(grey, grey, grey);
    }

    /**
     * Set a new strokes' color
     *
     * @param r the red channel between [0,255]
     * @param g the green channel between [0,255]
     * @param b the blue channel between [0,255]
     */

    public Paint setStrokesColor(int r, int g, int b) {
        int oC0 = colorStrokes[0];
        int oC1 = colorStrokes[1];
        int oC2 = colorStrokes[2];
        colorStrokes[0] = Utils.constrain(r, 0, 255);
        colorStrokes[1] = Utils.constrain(g, 0, 255);
        colorStrokes[2] = Utils.constrain(b, 0, 255);
        if (colorStrokes[0] != oC0 || colorStrokes[1] != oC1 || colorStrokes[2] != oC2) {
            request();
        }
        return this;
    }

    /**
     * Set the strokes thickness
     *
     * @param val a value greater than zero
     */

    public Paint setStrokesWidth(int val) {
        if (val > 0 && strokeWidth != val) {
            strokeWidth = val;
            request();
        }
        return this;
    }

    /**
     * @return the strokes thickness
     */

    public int getStrokesWidth() {
        return strokeWidth;
    }

    /**
     * @return true if strokes are enabled
     */

    public boolean hasStrokes() {
        return strokes;
    }

    private final int[] colorCopy = {0, 0, 0, 0};

    /**
     * @return the geometry's color
     */

    public int[] getColor() {
        System.arraycopy(color, 0, colorCopy, 0, 4);
        return colorCopy;
    }

    private final int[] colorStrokeCopy = {0, 0, 0};

    /**
     * @return the strokes' color
     */

    public int[] getColorStrokes() {
        System.arraycopy(colorStrokes, 0, colorStrokeCopy, 0, 3);
        return colorStrokeCopy;
    }

    /**
     * @return if exists, the native color, otherwise null
     */

    public Object getNativeColor() {
        return nativeColor;
    }

    /**
     * @return if exists, the native strokes color, otherwise null
     */

    public Object getNativeStrokesColor() {
        return nativeStrokeColor;
    }

    /**
     * @return if exists, the native strokes' thickness, otherwise null
     */

    public Object getNativeStrokesWidth() {
        return nativeStrokeWidth;
    }
}
