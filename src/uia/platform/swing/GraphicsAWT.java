package uia.platform.swing;

import uia.platform.swing.graphics.GraphicsAWTUtility;
import uia.physical.theme.Theme;
import uia.utility.MathUtility;
import uia.core.paint.Color;
import uia.core.shape.Shape;
import uia.core.ui.Graphics;
import uia.core.font.Font;
import uia.core.Image;

import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.function.Supplier;
import javax.imageio.ImageIO;
import java.awt.BasicStroke;
import java.awt.geom.Path2D;
import java.awt.Graphics2D;
import java.io.File;
import java.util.*;

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
    public Graphics setShapeBorderColor(Color color) {
        Objects.requireNonNull(color);
        this.shapeBorderColor = color;
        return this;
    }

    @Override
    public Graphics setShapeColor(Color color) {
        Objects.requireNonNull(color);
        this.shapeColor = color;

        // spike - to improve performance
        Graphics2D graphics = getGraphics();
        java.awt.Color awtColor = GraphicsAWTUtility.createAWTColor(color);
        graphics.setColor(awtColor);
        //
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
            java.awt.Paint awtStrokeColor = GraphicsAWTUtility.createAWTColor(shapeBorderColor);
            if (awtStrokeColor == null) {
                awtStrokeColor = GraphicsAWTUtility.createAWTColor(shapeColor);
            }
            //

            graphics.setPaint(awtStrokeColor);
            graphics.draw(path);
            graphics.setPaint(previousPaint);
        }
    }

    @Override
    public Graphics drawShape(float... vertices) {
        Objects.requireNonNull(vertices);
        if (vertices.length % 2 != 0) {
            throw new IllegalArgumentException("The array shape must be [x1,y1, x2,y2, x3,y3, ...]");
        }

        GraphicsAWTUtility.buildPath(vertices, shapePath);
        drawPath(shapePath);
        return this;
    }

    @Override
    public Graphics drawShape(Shape shape) {
        GraphicsAWTUtility.buildPath(shape, shapePath);
        drawPath(shapePath);
        return this;
    }

    @Override
    public void setClip(Shape shape) {
        Graphics2D graphics = getGraphics();
        if (shape != null) {
            GraphicsAWTUtility.buildPath(shape, clipPath);
            graphics.clip(clipPath);
        } else {
            graphics.setClip(null);
        }
        clipPaths.addLast(graphics.getClip());
    }

    @Override
    public void restoreClip() {
        Graphics2D graphics = getGraphics();
        try {
            clipPaths.removeLast();
            graphics.setClip(clipPaths.peekLast());
        } catch (Exception error) {
            graphics.setClip(null);
        }
    }

    // text

    @Override
    public Graphics setFont(Font font) {
        // spike - to improve performance
        java.awt.Font awtFont = GraphicsAWTUtility.createAWTFont(font);
        //
        Graphics2D graphics = getGraphics();
        graphics.setFont(awtFont);
        return this;
    }

    @Override
    public Graphics setTextColor(Color color) {
        Objects.requireNonNull(color);
        textColor = color;
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
        java.awt.Color awtTextColor = GraphicsAWTUtility.createAWTColor(textColor);
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

    /**
     * Helper function. Builds the given Image.
     */

    private void buildImage(Image img) {
        if (!img.isValid() || !(img.getNative() instanceof java.awt.Image)) {
            img.setNative(fakeImage, 1, 1);

            new Thread(() -> {
                try {
                    java.awt.Image image = ImageIO.read(new File(img.getPath()));
                    img.setNative(image, image.getWidth(null), image.getHeight(null));
                } catch (Exception ignored) {
                    //
                }
            }).start();
        }
    }

    @Override
    public Graphics drawImage(Image img, float x, float y, float width, float height, float rotation) {
        Graphics2D graphics = getGraphics();
        buildImage(img);

        boolean rotated = Float.compare(rotation % MathUtility.TWO_PI, 0f) != 0;
        AffineTransform previousMatrix = null;

        if (rotated) {
            previousMatrix = graphics.getTransform();
            graphics.rotate(rotation, x, y);
        }

        float offset = 0.5f;
        graphics.drawImage((java.awt.Image) img.getNative(),
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
