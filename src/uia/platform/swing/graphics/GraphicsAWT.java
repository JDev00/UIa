package uia.platform.swing.graphics;

import uia.core.ui.primitives.shape.Transform;
import uia.core.ui.primitives.color.Color;
import uia.core.ui.primitives.font.Font;
import uia.core.ui.primitives.Image;
import uia.physical.theme.Theme;
import uia.utility.MathUtility;
import uia.core.ui.Graphics;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;
import java.awt.BasicStroke;
import java.awt.geom.Path2D;
import java.awt.Graphics2D;
import java.util.*;

// TODO: to improve performance

/**
 * {@link Graphics} implementation based on Java AWT.
 */

public class GraphicsAWT implements Graphics {
    private final Supplier<Graphics2D> graphics2DSupplier;

    private final Deque<java.awt.Shape> clipPaths;
    private final Path2D clipPath;
    private final Path2D shapePath;

    private float shapeBorderWidth = 0;
    private Color shapeBorderColor = null;
    private Color shapeColor = Theme.WHITE;
    private Color textColor = Theme.BLACK;

    public GraphicsAWT(Supplier<Graphics2D> graphics2DSupplier) {
        clipPaths = new ArrayDeque<>();

        clipPath = new Path2D.Float();

        shapePath = new Path2D.Float();

        this.graphics2DSupplier = graphics2DSupplier;
    }

    /**
     * Helper function. Returns the platform graphics.
     */

    private Graphics2D getGraphics() {
        return graphics2DSupplier.get();
    }

    @Override
    public void dispose() {
        getGraphics().dispose();
    }

    // shape

    @Override
    public Graphics setShapeBorderWidth(float lineWidth) {
        if (lineWidth < 0) {
            throw new IllegalArgumentException("lineWidth can not be < 0");
        }
        this.shapeBorderWidth = lineWidth;

        // spike - to improve performance
        Graphics2D graphics = getGraphics();
        BasicStroke awtStroke = new BasicStroke(lineWidth);
        graphics.setStroke(awtStroke);
        //
        return this;
    }

    @Override
    public Graphics setShapeColor(Color color) {
        if (color != null) {
            this.shapeColor = color;
            this.shapeBorderColor = color;

            // spike - to improve performance
            Graphics2D graphics = getGraphics();
            java.awt.Color awtColor = GraphicsAWTUtility.createColor(color);
            graphics.setColor(awtColor);
            //
        }
        return this;
    }

    @Override
    public Graphics setShapeColor(Color color, Color borderColor) {
        if (color != null) {
            this.setShapeColor(color);
            this.shapeBorderColor = borderColor == null ? color : borderColor;
        }
        return this;
    }

    /**
     * Helper function. Draws the given Path on this Graphics.
     */

    private void drawPath(Path2D path) {
        Graphics2D graphics = getGraphics();
        graphics.fill(path);

        if (shapeBorderWidth > 0) {
            java.awt.Paint previousPaint = graphics.getPaint();
            // spike - to improve performance
            java.awt.Paint awtStrokeColor = GraphicsAWTUtility.createColor(shapeBorderColor);
            if (awtStrokeColor == null) {
                awtStrokeColor = GraphicsAWTUtility.createColor(shapeColor);
            }
            //

            graphics.setPaint(awtStrokeColor);
            graphics.draw(path);
            graphics.setPaint(previousPaint);
        }
    }

    @Override
    public Graphics drawShape(Transform transform, int length, float... vertices) {
        GraphicsAWTUtility.buildShape(transform, length, vertices, shapePath);
        drawPath(shapePath);
        return this;
    }

    @Override
    public Graphics setClip(Transform transform, int length, float... vertices) {
        Graphics2D graphics = getGraphics();
        if (vertices != null) {
            GraphicsAWTUtility.buildShape(transform, length, vertices, clipPath);
            graphics.clip(clipPath);
        } else {
            graphics.setClip(null);
        }
        clipPaths.addLast(graphics.getClip());
        return this;
    }

    @Override
    public Graphics restoreClip() {
        Graphics2D graphics = getGraphics();
        try {
            clipPaths.removeLast();
            graphics.setClip(clipPaths.peekLast());
        } catch (Exception error) {
            graphics.setClip(null);
        }
        return this;
    }

    // text

    @Override
    public Graphics setFont(Font font) {
        // spike - to improve performance
        java.awt.Font awtFont = GraphicsAWTUtility.createFont(font);
        //
        Graphics2D graphics = getGraphics();
        graphics.setFont(awtFont);
        return this;
    }

    @Override
    public Graphics setTextColor(Color color) {
        if (color != null) {
            textColor = color;
        }
        return this;
    }

    @Override
    public Graphics drawText(char[] data, int offset, int length, float x, float y, float rotation) {
        boolean rotated = Float.compare(rotation % MathUtility.TWO_PI, 0f) != 0;
        AffineTransform previousMatrix = null;
        Graphics2D graphics = getGraphics();

        if (rotated) {
            previousMatrix = graphics.getTransform();
            graphics.rotate(rotation, x, y);
        }

        java.awt.Paint previousPaint = graphics.getPaint();

        // spike - to improve performance
        java.awt.Color awtTextColor = GraphicsAWTUtility.createColor(textColor);
        graphics.setColor(awtTextColor);
        //

        graphics.drawChars(data, offset, length, (int) x, (int) y);
        graphics.setPaint(previousPaint);

        if (rotated) {
            graphics.setTransform(previousMatrix);
        }
        return this;
    }

    private final java.awt.Image fakeImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

    @Override
    public Graphics drawImage(Image image, float x, float y, float width, float height, float rotation) {
        Graphics2D graphics = getGraphics();

        if (!image.isValid()) {
            GraphicsAWTUtility.createImage(image, fakeImage);
        }

        boolean rotated = Float.compare(rotation % MathUtility.TWO_PI, 0f) != 0;
        AffineTransform previousMatrix = null;

        if (rotated) {
            previousMatrix = graphics.getTransform();
            graphics.rotate(rotation, x, y);
        }

        float offset = 0.5f;
        graphics.drawImage((java.awt.Image) image.getNative(),
                (int) (x - offset * width),
                (int) (y - offset * height),
                (int) width,
                (int) height, null);

        if (rotated) {
            graphics.setTransform(previousMatrix);
        }
        return this;
    }
}
