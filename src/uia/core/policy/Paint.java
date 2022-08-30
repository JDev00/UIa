package uia.core.policy;

import java.util.List;

/**
 * Paint ADT.
 * <br>
 * <b>Note that Paint primarily acts as a bridge between API framework and native platform.</b>
 */

public interface Paint extends Bundle {

    /**
     * TYPE class provides a list of all the native Paint objects.
     * It is used to classify the given Paint and, if necessary, to convert it for another Context.
     */
    enum TYPE {LINEAR, GRADIENT, GRADIENT_LINEAR, GRADIENT_RADIAL, OTHER}

    /**
     * Enable or disable the stroke color
     *
     * @param strokeColor true to enable stroke color
     */

    void enableStrokeColor(boolean strokeColor);

    /**
     * @return true if this Paint has a stroke color
     */

    boolean hasStrokeColor();

    /**
     * Set the stroke color.
     * <br>
     * Note that this method <b>does not automatically enable</b> {@link #enableStrokeColor(boolean)} method
     *
     * @param r the red channel between [0,255]
     * @param g the green channel between [0,255]
     * @param b the blue channel between [0,255]
     */

    void setStrokeColor(float r, float g, float b);

    /**
     * @return the native color object used for stroke color representation
     */

    Object getStrokeNative();

    /**
     * @return the Paint TYPE or null if no native Paint has been set yet
     * @see TYPE
     */

    TYPE getType();

    /**
     * @return the composition of each color handled by this Paint
     */

    List<float[]> getColors();
}
