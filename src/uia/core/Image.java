package uia.core;

/**
 * Platform image abstraction.
 * <br>
 * The Image class acts as an adapter: the native image must be set with {@link #setNative(Object, float, float)}
 */

public class Image {
    private float width;
    private float height;

    private String path;

    private Object nativeImage;

    public Image(String path) {
        width = 0f;
        height = 0f;
        this.path = path;
    }

    /**
     * Invalidate this Image and force the system to rebuild its state
     */

    public void invalidate() {
        nativeImage = null;
    }

    /**
     * @return true if this Paint has a native color set
     */

    public boolean isValid() {
        return nativeImage != null;
    }

    /**
     * Set the native Image object
     */

    public void setNative(Object o, float width, float height) {
        this.width = width;
        this.height = height;
        nativeImage = o;
    }

    /**
     * @return the native Image object
     */

    public Object getNative() {
        return nativeImage;
    }

    /**
     * Load a new Image
     *
     * @param path the image path
     */

    public Image load(String path) {
        if (path != null) {
            this.path = path;
            invalidate();
        }
        return this;
    }

    /**
     * @return the image's path
     */

    public String getPath() {
        return path;
    }

    /**
     * @return the image's width
     */

    public float width() {
        return width;
    }

    /**
     * @return the image's height
     */

    public float height() {
        return height;
    }
}
