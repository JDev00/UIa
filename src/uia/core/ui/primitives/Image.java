package uia.core.ui.primitives;

/**
 * Platform image abstraction.
 * <br>
 * The Image class acts as an adapter: the native image must be set with {@link #setNative(Object, int, int)}.
 */

public class Image {
    private int width;
    private int height;
    private String path;
    private Object nativeImage;

    public Image() {
        width = 0;
        height = 0;
    }

    @Override
    public String toString() {
        return "Image{" +
                "width=" + width +
                ", height=" + height +
                ", path='" + path + '\'' +
                ", nativeImage=" + nativeImage +
                '}';
    }

    /**
     * Invalidates this Image and force the system to rebuild its state
     */

    public void invalidate() {
        nativeImage = null;
    }

    /**
     * @return true if this Image is valid and hasn't to be rebuilt
     */

    public boolean isValid() {
        return nativeImage != null || path == null;
    }

    /**
     * Set the native image object
     */

    public void setNative(Object o, int width, int height) {
        this.width = width;
        this.height = height;
        nativeImage = o;
    }

    /**
     * @return the native image object
     */

    public Object getNative() {
        return nativeImage;
    }

    /**
     * Loads a new image
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
     * @return the image path
     */

    public String getPath() {
        return path;
    }

    /**
     * @return the image width
     */

    public int width() {
        return width;
    }

    /**
     * @return the image height
     */

    public int height() {
        return height;
    }
}
