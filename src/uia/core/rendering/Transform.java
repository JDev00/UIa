package uia.core.rendering;

import uia.utility.MathUtility;

import java.util.Arrays;

/**
 * Transform is responsible for keeping geometric transformations.
 * <br>
 * The transformations handled are:
 * <ul>
 *     <li>translation</li>
 *     <li>scale</li>
 *     <li>rotation</li>
 * </ul>
 */

public final class Transform {
    private final float[] translation;
    private final float[] scale;
    private float rotation;

    public Transform() {
        translation = new float[]{0f, 0f};
        scale = new float[]{1f, 1f};
        rotation = 0f;
    }

    @Override
    public String toString() {
        return "Transform{translation=" + Arrays.toString(translation) +
                ", scale=" + Arrays.toString(scale) +
                ", rotation=" + rotation +
                '}';
    }

    /**
     * Sets the translation.
     *
     * @param x the translation on the x-axis
     * @param y the translation on the y-axis
     * @return this Transform
     */

    public Transform setTranslation(float x, float y) {
        translation[0] = x;
        translation[1] = y;
        return this;
    }

    /**
     * @return the translation on the x-axis
     */

    public float getTranslationX() {
        return translation[0];
    }

    /**
     * @return the translation on the y-axis
     */

    public float getTranslationY() {
        return translation[1];
    }

    /**
     * Sets the scale.
     *
     * @param x the scale on the x-axis
     * @param y the scale on the y-axis
     * @return this Transform
     * @throws IllegalArgumentException if {@code x < 0 || y < 0}
     */

    public Transform setScale(float x, float y) {
        if (x < 0) {
            throw new IllegalArgumentException("the scale on the x-axis must be greater than or equal to 0");
        }
        if (y < 0) {
            throw new IllegalArgumentException("the scale on the y-axis must be greater than or equal to 0");
        }

        scale[0] = x;
        scale[1] = y;
        return this;
    }

    /**
     * @return the scale on the x-xis
     */

    public float getScaleX() {
        return scale[0];
    }

    /**
     * @return the scale on the y-xis
     */

    public float getScaleY() {
        return scale[1];
    }

    /**
     * Sets the rotation.
     *
     * @param radians the rotation in radians
     * @return this Transform
     */

    public Transform setRotation(float radians) {
        rotation = radians % MathUtility.TWO_PI;
        return this;
    }

    /**
     * @return the rotation in radians
     */

    public float getRotation() {
        return rotation;
    }
}
