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

    private boolean built = false;

    public Image(String path) {
        width = 0f;
        height = 0f;
        this.path = path;
    }

    /**
     * Set the native Image object
     */

    public void setNative(Object o, float width, float height) {
        this.width = width;
        this.height = height;
        nativeImage = o;
        built = o != null;
    }

    /**
     * Load a new Image
     *
     * @param path the image path
     */

    public Image load(String path) {
        if (path != null) {
            this.path = path;
            built = false;
        }
        return this;
    }

    /**
     * @return true if the native object has been built
     */

    public boolean isBuilt() {
        return built;
    }

    /**
     * @return the native Image object
     */

    public Object getNative() {
        return nativeImage;
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

    /**
     * @return the image's path
     */

    public String getPath() {
        return path;
    }
}
