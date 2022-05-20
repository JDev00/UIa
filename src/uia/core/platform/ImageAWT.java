package uia.core.platform;

import uia.core.policy.Image;

/**
 * AWT image implementation
 */

public class ImageAWT implements Image {
    private java.awt.Image image;

    public ImageAWT() {
        image = null;
    }

    @Override
    public void setNative(Object o) {
        if (o instanceof java.awt.Image)
            image = (java.awt.Image) o;
    }

    @Override
    public java.awt.Image getNative() {
        return image;
    }
}
