package uia.physical.ui;

import uia.core.architecture.ui.ViewGroup;
import uia.core.Image;
import uia.core.architecture.Graphic;
import uia.physical.ui.wrapper.WrapperView;

/**
 * Component designed to draw an Image on screen.
 */

public final class ComponentImage extends WrapperView {
    /**
     * imgBounds defines the image bounds inside this component
     */
    public final float[] imgBounds = {0f, 0f, 1f, 1f, 0f};

    private Image image;

    public ComponentImage(ViewGroup view) {
        super(view);
    }

    /**
     * Set an Image to this View
     *
     * @param image an {@link Image}; it could be null
     */

    public void setImage(Image image) {
        this.image = image;
    }

    @Override
    public void draw(Graphic graphic) {
        super.draw(graphic);

        if (isVisible() && image != null) {
            float[] bounds = bounds();
            graphic.drawImage(image,
                    bounds[0] + imgBounds[0] * bounds[2],
                    bounds[1] + imgBounds[1] * bounds[3],
                    imgBounds[2] * bounds[2],
                    imgBounds[3] * bounds[3],
                    imgBounds[4]);
        }
    }

    /**
     * @return the associated {@link Image}
     */

    public Image getImage() {
        return image;
    }
}
