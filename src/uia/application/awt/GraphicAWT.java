package uia.application.awt;

import uia.core.Font;
import uia.core.Geometry;
import uia.core.Image;
import uia.core.Paint;
import uia.core.Shape;
import uia.core.ui.Graphic;
import uia.utility.TrigTable;

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

/**
 * {@link Graphic} implementation based on Java AWT
 */

public class GraphicAWT implements Graphic {
    private Graphics2D graphics;
    private final Path2D customPath;

    private Paint paint;

    public GraphicAWT() {
        customPath = new Path2D.Float();

        paint = new Paint();
    }

    @Override
    public void setNativeGraphic(Object nativeGraphic) {
        graphics = (Graphics2D) Objects.requireNonNull(nativeGraphic);
    }

    @Override
    public void dispose() {
        graphics.dispose();
    }

    /**
     * Helper function.
     * <br>
     * Time required: T(n)
     * <br>
     * Space required: O(1).
     */

    private void buildPathAndStoreInto(Shape shape, Path2D targetPath) {
        targetPath.reset();

        Geometry geometry = shape.getGeometry();
        Shape.TransformedVertex target = new Shape.TransformedVertex();

        for (int i = 0; i < geometry.vertices(); i++) {
            shape.transformAndStoreInto(geometry.get(i), target);
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

    private final Stack<Shape> clipStack = new Stack<>();
    private Shape clipShape = null;
    private final Path2D clipPath = new Path2D.Float();

    @Override
    public void setClip(Shape shape) {
        clipShape = clipStack.push(shape);

        if (shape != null) {
            buildPathAndStoreInto(shape, clipPath);
            graphics.clip(clipPath);
        } else {
            graphics.setClip(null);
        }
    }

    @Override
    public void restoreClip() {
        if (!clipStack.isEmpty()) {
            clipStack.pop();

            if (!clipStack.isEmpty()) {
                clipShape = clipStack.peek();
                buildPathAndStoreInto(clipShape, clipPath);
                graphics.setClip(clipPath);
                return;
            }
        }

        clipShape = null;
        graphics.setClip(null);
    }

    /**
     * Helper function.
     */

    private void analyzeFont(Font font) {
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

            font.setNative(fontNative, (off, len, in) -> metrics.charsWidth(in, off, len),
                    metrics.getAscent(), metrics.getDescent(), metrics.getLeading());
        }
    }

    @Override
    public void setFont(Font font) {
        analyzeFont(font);
        graphics.setFont((java.awt.Font) font.getNative());
    }

    /**
     * Helper function.
     */

    private void analyzePaint(Paint paint) {
        if (!paint.isValid() || !(paint.getNativeColor() instanceof java.awt.Paint)) {
            paint.setNative(
                    new Color(paint.getRed(), paint.getGreen(), paint.getBlue(), paint.getAlpha()),
                    new Color(paint.getStrokeRed(), paint.getStrokeGreen(), paint.getStrokeBlue()),
                    new BasicStroke(paint.getStrokeWidth()));
        }
    }

    @Override
    public void setPaint(Paint paint) {
        this.paint = paint;

        analyzePaint(paint);
        graphics.setPaint((java.awt.Paint) paint.getNativeColor());
        graphics.setStroke((Stroke) paint.getNativeStrokeWidth());
    }

    @Override
    public void drawShape(Shape shape) {
        buildPathAndStoreInto(shape, customPath);

        graphics.fill(customPath);

        // set stroke color
        if (paint.hasStroke()) {
            // old paint
            java.awt.Paint oPaint = graphics.getPaint();

            graphics.setPaint((java.awt.Paint) paint.getNativeStrokeColor());
            graphics.draw(customPath);

            // restore old paint
            graphics.setPaint(oPaint);
        }
    }

    @Override
    public void drawText(char[] data, int offset, int length, float x, float y, float rotation) {
        boolean rotated = Float.compare(rotation % TrigTable.TWO_PI, 0f) != 0;
        AffineTransform oldXForm = null;

        if (rotated) {
            oldXForm = graphics.getTransform();
            graphics.rotate(rotation, x, y);
        }

        graphics.drawChars(data, offset, length, (int) x, (int) y);

        if (rotated) {
            graphics.setTransform(oldXForm);
        }
    }

    private final java.awt.Image fakeImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

    /**
     * Helper function.
     */

    private void analyzeImage(Image img) {
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
        analyzeImage(img);

        boolean rotated = Float.compare(rotation % TrigTable.TWO_PI, 0f) != 0;
        AffineTransform oldXForm = null;

        if (rotated) {
            oldXForm = graphics.getTransform();
            graphics.rotate(rotation, x, y);
        }

        float offset = 0.5f;
        graphics.drawImage((java.awt.Image) img.getNative(),
                (int) (x - offset * width), (int) (y - offset * height),
                (int) width, (int) height, null);

        if (rotated) {
            graphics.setTransform(oldXForm);
        }
    }
}
