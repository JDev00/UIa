package uia.physical;

import uia.core.ui.View;
import uia.core.Image;
import uia.core.ui.Graphic;

/**
 * ComponentImage is designed to display an Image on a View.
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
     * @param x the position on x-axis
     * @param y the position on y-axis
     */

    public void setImagePosition(float x, float y) {
        imgBounds[0] = x;
        imgBounds[1] = y;
    }

    /**
     * Set the image relative dimension
     *
     * @param x the dimension on x-axis
     * @param y the dimension on y-axis
     */

    public void setImageDimension(float x, float y) {
        imgBounds[2] = x;
        imgBounds[3] = y;
    }

    /**
     * Set the image rotation
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
            float[] bounds = bounds();
            float xDist = imgBounds[0] * getWidth();
            float yDist = imgBounds[1] * getHeight();
            float rot = imgBounds[4];

            graphic.drawImage(image,
                    View.getPositionOnX(bounds[0], bounds[2], xDist, yDist, rot),
                    View.getPositionOnY(bounds[1], bounds[3], xDist, yDist, rot),
                    imgBounds[2] * getWidth() + 3,
                    imgBounds[3] * getHeight() + 3,
                    rot + bounds[4]);
        }
    }

    private final float[] imgBoundsCopy = new float[imgBounds.length];

    /**
     * @return the image bounds
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
