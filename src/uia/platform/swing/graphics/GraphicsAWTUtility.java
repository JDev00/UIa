package uia.platform.swing.graphics;

import uia.core.ui.primitives.shape.GeometryUtility;
import uia.core.ui.primitives.shape.Transform;
import uia.core.ui.primitives.color.Color;
import uia.core.ui.primitives.font.Font;
import uia.core.ui.primitives.Image;

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
     * Helper function. Populates a Path object suitable for AWT.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(1).
     *
     * @param transform the Transform object used to transform the given vertices; it could be null
     */

    public static void buildShape(Transform transform, int length, float[] vertices, Path2D targetPath) {
        Objects.requireNonNull(targetPath);

        targetPath.reset();

        if (length > 0) {
            boolean firstVertex = true;
            float[] transformedVertex = {0f, 0f};

            for (int i = 0; i < length; i++) {
                float vertexX = vertices[2 * i];
                float vertexY = vertices[2 * i + 1];

                // calculates the transformed vertex
                if (transform == null) {
                    transformedVertex[0] = vertexX;
                    transformedVertex[1] = vertexY;
                } else {
                    GeometryUtility.computeTransformedVertex(transform, vertexX, vertexY, transformedVertex);
                }

                float newVertexX = transformedVertex[0];
                float newVertexY = transformedVertex[1];
                if (firstVertex) {
                    firstVertex = false;
                    targetPath.moveTo(newVertexX, newVertexY);
                } else {
                    targetPath.lineTo(newVertexX, newVertexY);
                }
            }

            targetPath.closePath();
        }
    }
}
