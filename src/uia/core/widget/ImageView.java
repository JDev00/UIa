package uia.core.widget;

import uia.core.View;
import uia.core.platform.independent.Image;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;
import uia.utils.Utils;

/**
 * UI component designed to draw an Image
 */

public class ImageView extends View {
    private float xTrans = 0f;
    private float yTrans = 0f;
    private float xScale = 1f;
    private float yScale = 1f;
    private float iRot = 0f;

    private Image image;

    public ImageView(Context context, float x, float y, float width, float height) {
        super(context, x, y, width, height);
    }

    /**
     * Set an Image to this View
     *
     * @param image an {@link Image}; it could be null
     */

    public void setImage(Image image) {
        this.image = image;
    }

    /**
     * Set the image's translation respect to the View's center and relative its dimension
     *
     * @param x the translation along x-axis
     * @param y the translation along y-axis
     */

    public void setImageTranslation(float x, float y) {
        xTrans = x;
        yTrans = y;
    }

    /**
     * Set the image's scale relative to this View
     *
     * @param x the scale along x-axis expressed between [0,1]
     * @param y the scale along y-axis expressed between [0,1]
     */

    public void setImageScale(float x, float y) {
        xScale = Utils.constrain(x, 0, 1);
        yScale = Utils.constrain(y, 0, 1);
    }

    /**
     * Set the image's rotation
     *
     * @param rot the rotation expressed in radians
     */

    public void setImageRotation(float rot) {
        iRot = rot;
    }

    @Override
    protected void draw(Graphic graphic) {
        super.draw(graphic);

        if (image != null) {
            float w = width();
            float h = height();
            graphic.drawImage(image, x() + xTrans * w, y() + yTrans * h, xScale * w, yScale * h, iRot);
        }
    }

    /**
     * @return the image's translation along x-axis
     */

    public float getImageXTranslation() {
        return xTrans;
    }

    /**
     * @return the image's translation along y-axis
     */

    public float getImageYTranslation() {
        return yTrans;
    }

    /**
     * @return the image's scale along x-axis
     */

    public float getImageXScale() {
        return xScale;
    }

    /**
     * @return the image's scale along y-axis
     */

    public float getImageYScale() {
        return yScale;
    }

    /**
     * @return the image's rotation
     */

    public float getImageRotation() {
        return iRot;
    }

    /**
     * @return the associated {@link Image}
     */

    public Image getImage() {
        return image;
    }
}
