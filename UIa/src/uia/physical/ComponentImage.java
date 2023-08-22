package uia.physical;

import uia.core.ui.View;
import uia.core.Image;
import uia.core.basement.Graphic;
import uia.physical.wrapper.WrapperView;

import static uia.utils.TrigTable.*;

/**
 * Component designed to draw an Image on screen.
 */

public final class ComponentImage extends WrapperView {
    private final float[] imgBounds = {0f, 0f, 1f, 1f, 0f};

    private final Image image;

    public ComponentImage(View view) {
        super(view);

        image = new Image();
    }

    /**
     * Set the image relative position
     *
     * @param x the position along x-axis
     * @param y the position along y-axis
     */

    public void setImagePosition(float x, float y) {
        imgBounds[0] = x;
        imgBounds[1] = y;
    }

    /**
     * Set the image relative dimension
     *
     * @param x the dimension along x-axis
     * @param y the dimension along y-axis
     */

    public void setImageDimension(float x, float y) {
        imgBounds[2] = x;
        imgBounds[3] = y;
    }

    /**
     * Set the image rotation in radians
     *
     * @param radians the rotation expressed in radians
     */

    public void setImageRotation(float radians) {
        imgBounds[4] = radians;
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        if (isVisible()) {
            float[] desc = desc();
            float[] bounds = bounds();

            float x_off = imgBounds[0] * desc[0];
            float y_off = imgBounds[1] * desc[1];
            float cos = cos(imgBounds[4]);
            float sin = sin(imgBounds[4]);

            graphic.drawImage(image,
                    bounds[0] + bounds[2] / 2f + rotX(x_off, y_off, cos, sin),
                    bounds[1] + bounds[3] / 2f + rotY(x_off, y_off, cos, sin),
                    imgBounds[2] * desc[0],
                    imgBounds[3] * desc[1],
                    bounds[4] + imgBounds[4]);
        }
    }

    private final float[] imgBoundsCopy = new float[imgBounds.length];

    /**
     * @return the image's bounds
     */

    public float[] boundsImage() {
        System.arraycopy(imgBounds, 0, imgBoundsCopy, 0, imgBoundsCopy.length);
        return imgBoundsCopy;
    }

    /**
     * @return the associated {@link Image}
     */

    public Image getImage() {
        return image;
    }
}
