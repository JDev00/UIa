package uia.core.platform.independent;

import uia.core.platform.policy.Graphic;

/**
 * Image bridge implementation.
 * <br>
 * The image creation is a Graphic responsibility, as consequence of that, different policies
 * could be adopted to load/create an Image.
 * More details can be found inside the corresponding {@link Graphic} implementation.
 */

public class Image extends NativeBuilder {
    private float width;
    private float height;

    private String path;

    private Object nativeImage;

    public Image(String path) {
        width = 0f;
        height = 0f;
        this.path = path;
        request();
    }

    /**
     * Load a new Image
     *
     * @param path the image path
     */

    public Image load(String path) {
        if (path != null) {
            this.path = path;
            request();
        }
        return this;
    }

    public void setNative(Object o, float width, float height) {
        this.width = width;
        this.height = height;

        nativeImage = o;

        if (o == null) {
            request();
        } else {
            deny();
        }
    }

    public Object getNative() {
        return nativeImage;
    }

    /**
     * @return the image's dimension along x-axis
     */

    public float width() {
        return width;
    }

    /**
     * @return the image's dimension along y-axis
     */

    public float height() {
        return height;
    }

    /**
     * @return the image's path
     */

    public String getPath() {
        return path;
    }
}
