package uia.platform.swing.graphics;

import uia.core.ui.primitives.shape.Geometry;
import uia.core.ui.primitives.shape.Shape;
import uia.core.ui.primitives.color.Color;
import uia.core.ui.primitives.font.Font;
import uia.core.Image;

import javax.imageio.ImageIO;
import java.awt.geom.Path2D;
import java.util.Objects;
import java.io.File;
import java.awt.*;

/**
 * GraphicsAWTUtility collects utilities for GraphicsAWT.
 */

public final class GraphicsAWTUtility {

    private GraphicsAWTUtility() {
    }

    // Color

    /**
     * Creates the AWT corresponding color.
     *
     * @param color the color used to create the AWT one
     * @return a new corresponding {@link java.awt.Color}
     */

    public static java.awt.Color createColor(Color color) {
        java.awt.Color result = null;
        if (color != null) {
            result = new java.awt.Color(
                    color.getRed(),
                    color.getGreen(),
                    color.getBlue(),
                    color.getAlpha());
        }
        return result;
    }

    // Font

    /**
     * Creates the AWT corresponding Font object.
     *
     * @param font the font used to create the AWT one
     * @return a new corresponding {@link java.awt.Font}
     */

    public static java.awt.Font createFont(Font font) {
        java.awt.Font result = null;

        switch (font.getStyle()) {
            case ITALIC:
                result = new java.awt.Font(font.getName(), java.awt.Font.ITALIC, (int) font.getSize());
                break;
            case BOLD:
                result = new java.awt.Font(font.getName(), java.awt.Font.BOLD, (int) font.getSize());
                break;
            case PLAIN:
                result = new java.awt.Font(font.getName(), java.awt.Font.PLAIN, (int) font.getSize());
                break;
        }

        FontMetrics metrics = new Canvas().getFontMetrics(result);
        font.buildFont(
                metrics.getAscent(),
                metrics.getDescent(),
                metrics.getLeading(),
                (off, len, text) -> metrics.charsWidth(text, off, len)
        );

        return result;
    }

    // Image

    /**
     * Creates the AWT corresponding Image object.
     *
     * @param image the image used to create the AWT one
     */

    public static void createImage(Image image, java.awt.Image fakeImage) {
        // sets a fake image while the specified one is loading
        image.setNative(fakeImage, 1, 1);

        new Thread(() -> {
            try {
                java.awt.Image awtImage = ImageIO.read(new File(image.getPath()));
                image.setNative(
                        awtImage,
                        awtImage.getWidth(null),
                        awtImage.getHeight(null)
                );
            } catch (Exception ignored) {
                //
            }
        }).start();
    }

    // Shape

    /**
     * Creates a Path object suitable for AWT.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(1).
     *
     * @param vertices   the vertices used to build the Path object
     * @param targetPath the target Path object
     * @throws NullPointerException if {@code vertices == null || targetPath == null}
     */

    public static void createPath(float[] vertices, Path2D targetPath) {
        Objects.requireNonNull(vertices);
        Objects.requireNonNull(targetPath);

        targetPath.reset();

        for (int i = 0; i < vertices.length; i += 2) {
            float x = vertices[i];
            float y = vertices[i + 1];
            if (targetPath.getCurrentPoint() == null) {
                targetPath.moveTo(x, y);
            } else {
                targetPath.lineTo(x, y);
            }
        }

        if (vertices.length > 0) {
            targetPath.closePath();
        }
    }

    /**
     * Creates a Path object suitable for AWT.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(1).
     *
     * @param shape      the Shape to build as a Path
     * @param targetPath the Path object used to contains the Shape points
     * @throws NullPointerException if {@code shape == null || targetPath == null}
     */

    public static void createPath(Shape shape, Path2D targetPath) {
        Objects.requireNonNull(shape);
        Objects.requireNonNull(targetPath);

        targetPath.reset();

        Geometry geometry = shape.getGeometry();
        Shape.TransformedVertex target = new Shape.TransformedVertex();

        if (geometry.vertices() > 0) {
            for (int i = 0; i < geometry.vertices(); i++) {
                Shape.transform(shape, geometry.get(i), target);
                float x = target.x;
                float y = target.y;
                if (target.primer) {
                    targetPath.moveTo(x, y);
                } else {
                    targetPath.lineTo(x, y);
                }
            }

            targetPath.closePath();
        }
    }
}
