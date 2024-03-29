package uia.application.desktop;

import uia.core.Font;
import uia.core.shape.Geometry;
import uia.core.Image;
import uia.core.paint.Paint;
import uia.core.shape.Shape;
import uia.core.ui.Graphics;
import uia.utility.MathUtility;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.Stroke;
import java.awt.Canvas;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.function.Supplier;

/**
 * {@link Graphics} implementation based on Java AWT.
 */

public class GraphicAWT implements Graphics {
    private final Supplier<Graphics2D> graphics2DSupplier;
    private final Path2D shapePath;

    private Paint paint;

    public GraphicAWT(Supplier<Graphics2D> graphics2DSupplier) {
        paint = new Paint();

        shapePath = new Path2D.Float();
        this.graphics2DSupplier = graphics2DSupplier;
    }

    /**
     * Helper function. Returns the native platform graphics.
     */

    private Graphics2D getGraphics() {
        return graphics2DSupplier.get();
    }

    @Override
    public void dispose() {
        getGraphics().dispose();
    }

    private final Stack<java.awt.Shape> clipStack = new Stack<>();
    private final Path2D clipPath = new Path2D.Float();

    @Override
    public void setClip(Shape shape) {
        Graphics2D graphics = getGraphics();
        if (shape != null) {
            buildPathAndStoreInto(shape, clipPath);
            graphics.clip(clipPath);
        } else {
            graphics.setClip(null);
        }
        clipStack.push(graphics.getClip());
    }

    @Override
    public void restoreClip() {
        Graphics2D graphics = getGraphics();
        try {
            clipStack.pop();
            graphics.setClip(clipStack.peek());
        } catch (Exception error) {
            graphics.setClip(null);
        }
    }

    /**
     * Helper function. Builds the given Font.
     */

    private void buildFont(Font font) {
        if (!font.isValid() || !(font.getNative() instanceof java.awt.Font)) {
            java.awt.Font fontNative = null;

            switch (font.getStyle()) {
                case ITALIC:
                    fontNative = new java.awt.Font(font.getName(), java.awt.Font.ITALIC, (int) font.getSize());
                    break;
                case BOLD:
                    fontNative = new java.awt.Font(font.getName(), java.awt.Font.BOLD, (int) font.getSize());
                    break;
                case PLAIN:
                    fontNative = new java.awt.Font(font.getName(), java.awt.Font.PLAIN, (int) font.getSize());
                    break;
            }

            FontMetrics metrics = new Canvas().getFontMetrics(fontNative);
            font.setNative(fontNative, metrics.getAscent(), metrics.getDescent(), metrics.getLeading(),
                    (off, len, in) -> metrics.charsWidth(in, off, len));
        }
    }

    @Override
    public void setFont(Font font) {
        buildFont(font);
        Graphics2D graphics = getGraphics();
        graphics.setFont((java.awt.Font) font.getNative());
    }

    /**
     * Helper function. Builds the given Paint.
     */

    private void buildPaint(Paint paint) {
        if (!paint.isValid() || !(paint.getNativeColor() instanceof java.awt.Paint)) {
            uia.core.paint.Color color = paint.getColor();
            uia.core.paint.Color strokeColor = paint.getStrokeColor();
            paint.setNative(
                    new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha()),
                    new Color(strokeColor.getRed(), strokeColor.getGreen(), strokeColor.getBlue()),
                    new BasicStroke(paint.getStrokeWidth()));
        }
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;

        buildPaint(paint);
        Graphics2D graphics = getGraphics();
        graphics.setPaint((java.awt.Paint) paint.getNativeColor());
        graphics.setStroke((Stroke) paint.getNativeStrokeWidth());
    }

    /**
     * Helper function. Draws the specified Path on screen.
     */

    private void drawShapeOnScreen(Path2D path) {
        Graphics2D graphics = getGraphics();
        graphics.fill(path);

        if (paint.hasStroke()) {
            java.awt.Paint previousPaint = graphics.getPaint();

            graphics.setPaint((java.awt.Paint) paint.getNativeStrokeColor());
            graphics.draw(path);

            graphics.setPaint(previousPaint);
        }
    }

    /**
     * Helper function. Builds a Shape suitable for AWT.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(1).
     */

    private void buildPathAndStoreInto(Shape shape, Path2D targetPath) {
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

    /**
     * Helper function. Builds a Shape suitable for AWT.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(1).
     */

    private void buildPathAndStoreInto(float[] vertices, Path2D targetPath) {
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

    @Override
    public void drawShape(float... vertices) {
        Objects.requireNonNull(vertices);
        if (vertices.length % 2 != 0) {
            throw new IllegalArgumentException("The array shape must be [x1,y1, x2,y2, x3,y3, ...]");
        }
        buildPathAndStoreInto(vertices, shapePath);
        drawShapeOnScreen(shapePath);
    }

    @Override
    public void drawShape(Shape shape) {
        buildPathAndStoreInto(shape, shapePath);
        drawShapeOnScreen(shapePath);
    }

    @Override
    public void drawText(char[] data, int offset, int length, float x, float y, float rotation) {
        Graphics2D graphics = getGraphics();
        boolean rotated = Float.compare(rotation % MathUtility.TWO_PI, 0f) != 0;
        AffineTransform previousMatrix = null;

        if (rotated) {
            previousMatrix = graphics.getTransform();
            graphics.rotate(rotation, x, y);
        }

        graphics.drawChars(data, offset, length, (int) x, (int) y);

        if (rotated) {
            graphics.setTransform(previousMatrix);
        }
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
                }
            }).start();
        }
    }

    @Override
    public void drawImage(Image img, float x, float y, float width, float height, float rotation) {
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
    }
}
