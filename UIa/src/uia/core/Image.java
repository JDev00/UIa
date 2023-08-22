package uia.core;

/**
 * Platform image abstraction.
 * <br>
 * The Image class acts as an adapter: the native image must be set with {@link #setNative(Object, float, float)}
 */

public class Image {

    /**
     * Mode defines how the image will be rendered:
     * <ul>
     *     <li>LEFT: the image is rendered according to its top left corner;</li>
     *     <li>CENTER: the image is rendered according to its center;</li>
     *     <li>RIGHT: the image is rendered according to its bottom right corner</li>
     * </ul>
     */

    public enum MODE {LEFT, CENTER, RIGHT}

    private float width;
    private float height;
    private MODE mode;
    private String path;

    private Object nativeImage;

    public Image() {
        width = 0f;
        height = 0f;
        mode = MODE.CENTER;
    }

    /**
     * Invalidate this Image and force the system to rebuild its state
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
     * Set the Image render MODE
     *
     * @param mode a not null {@link MODE}
     */

    public Image setMode(MODE mode) {
        if (mode != null) this.mode = mode;
        return this;
    }

    /**
     * @return the image's {@link MODE}
     */

    public MODE getMode() {
        return mode;
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
