package uia.core.platform.implementation;

import uia.core.platform.independent.Font;
import uia.core.platform.independent.Image;
import uia.core.platform.independent.shape.Shape;
import uia.core.platform.independent.paint.Paint;
import uia.core.platform.independent.paint.PaintGradient;
import uia.core.platform.policy.Context;
import uia.core.platform.policy.Graphic;
import uia.utils.TrigTable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

/**
 * AWT render implementation
 */

public class GraphicAWT implements Graphic {
    private Graphics2D g;
    private Path2D customPath;
    private final java.awt.Image puppetImage;

    private Paint paint;
    private Font font;

    public GraphicAWT() {
        g = null;

        customPath = new Path2D.Float();

        puppetImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

        paint = new Paint(255, 255, 255);

        font = new Font("Arial", Font.STYLE.ITALIC, 18f);
    }

    @Override
    public void setNative(Object o) {
        if (o instanceof Graphics2D) {
            g = (Graphics2D) o;
            setFont(font);
        }
    }

    @Override
    public Graphics2D getNative() {
        return g;
    }

    @Override
    public void dispose() {
        g.dispose();
    }

    @Override
    public void finalize() {
        g.finalize();
    }

    private final int[] clipBounds = new int[4];

    @Override
    public int[] getClipBounds() {
        Rectangle bounds = g.getClipBounds();
        clipBounds[0] = bounds.x;
        clipBounds[1] = bounds.y;
        clipBounds[2] = bounds.width;
        clipBounds[3] = bounds.height;
        return clipBounds;
    }

    private Shape clip = null;
    private final Path2D clipPath = new Path2D.Float();
    private final Shape.Geometry geometry = (x, y, b) -> {
        if (b) {
            clipPath.moveTo(x, y);
        } else {
            clipPath.lineTo(x, y);
        }
    };

    private final Stack<Shape> stack = new Stack<>();

    @Override
    public void setClip(Shape p) {
        clip = stack.push(p);

        if (p != null) {
            clipPath.reset();
            p.copy(geometry, false);
            clipPath.closePath();

            g.clip(clipPath);
        } else {
            g.setClip(null);
        }
    }

    @Override
    public void restoreClip() {
        if (!stack.isEmpty()) {
            stack.pop();

            if (!stack.isEmpty()) {
                clip = stack.peek();

                clipPath.reset();
                clip.copy(geometry, false);
                clipPath.closePath();

                g.setClip(clipPath);
                return;
            }
        }

        clip = null;
        g.setClip(null);
    }

    @Override
    public Shape getClip() {
        return clip;
    }

    @Override
    public boolean hitClip(int x, int y, int width, int height) {
        return g.hitClip(x, y, width, height);
    }

    private final int[] clipBounds2 = new int[4];

    @Override
    public int[] getClipBounds(int[] r) {
        if (r != null && r.length == 4) {
            Rectangle bounds = g.getClipBounds(new Rectangle(r[0], r[1], r[2], r[3]));
            clipBounds2[0] = bounds.x;
            clipBounds2[1] = bounds.y;
            clipBounds2[2] = bounds.width;
            clipBounds2[3] = bounds.height;
            return clipBounds2;
        }

        return null;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;

        // build the native font when necessary
        if (font.hasToBeBuilt() || !(font.getNative() instanceof java.awt.Font)) {
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

            font.setNative(fontNative, new FontMetricsAWT(new Canvas().getFontMetrics(fontNative)));
        }

        g.setFont((java.awt.Font) font.getNative());
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public void setPaint(Paint p) {
        if (p != null) {
            paint = p;
            cachedPaint = null;

            if (p.hasToBeBuilt() || !(p.getNativeColor() instanceof java.awt.Paint)) {
                int[] c = p.getColor();
                int[] cs = paint.getColorStrokes();
                p.setNative(
                        new Color(c[0], c[1], c[2], c[3]),
                        new Color(cs[0], cs[1], cs[2]),
                        new BasicStroke(p.getStrokesWidth()));
            }

            // set paint
            g.setPaint((java.awt.Paint) p.getNativeColor());

            // set stroke width
            g.setStroke((Stroke) p.getNativeStrokesWidth());
        }
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        g.drawLine((int) x1, (int) y1, (int) x2, (int) y2);
    }

    @Override
    public void drawText(String str, float x, float y) {
        g.drawString(str, x, y);
    }

    @Override
    public void drawText(char[] data, int offset, int length, float x, float y) {
        g.drawChars(data, offset, length, (int) x, (int) y);
    }

    @Override
    public void drawImage(Image img, float x, float y, float rotation) {
        this.drawImage(img, x, y, img.width(), img.height(), rotation);
    }

    @Override
    public void drawImage(Image img, float x, float y, float width, float height, float rotation) {
        if (img.hasToBeBuilt() || !(img.getNative() instanceof java.awt.Image)) {
            img.setNative(puppetImage, puppetImage.getWidth(null), puppetImage.getHeight(null));

            Context.runTask(() -> {// load this image in an async way
                try {
                    java.awt.Image image = ImageIO.read(new File(img.getPath()));
                    img.setNative(image, image.getWidth(null), image.getHeight(null));
                } catch (IOException ignored) {
                    img.setNative(null, 0f, 0f);
                }
            });
        }

        boolean rotate = Float.compare(rotation % TrigTable.TWO_PI, 0f) != 0;

        if (rotate) g.rotate(rotation, x, y);
        g.drawImage((java.awt.Image) img.getNative(),
                (int) (x - width / 2f), (int) (y - height / 2f), (int) width, (int) height, null);
        if (rotate) g.rotate(0f);
    }

    @Override
    public void vertexBegin(float x, float y) {
        customPath.moveTo(x, y);
    }

    @Override
    public void vertex(float x, float y) {
        customPath.lineTo(x, y);
    }

    private java.awt.Paint cachedPaint = null;

    @Override
    public void closeShape() {
        customPath.closePath();

        if (paint instanceof PaintGradient) {

            if (cachedPaint == null) {
                PaintGradient pg = (PaintGradient) paint;

                int[] c1 = pg.getColor();
                int[] c2 = pg.getColor2();
                float[] bounds = pg.getBounds();

                Rectangle2D r = customPath.getBounds2D();

                float width = (float) r.getWidth();
                float height = (float) r.getHeight();

                cachedPaint = new GradientPaint(
                        (float) r.getX() + bounds[0] * width,
                        (float) r.getY() + bounds[1] * height, new Color(c1[0], c1[1], c1[2], c1[3]),
                        bounds[2] * width,
                        bounds[3] * height, new Color(c2[0], c2[1], c2[2], c2[3])
                );
            }

            g.setPaint(cachedPaint);
        }

        g.fill(customPath);

        // set stroke color
        if (paint != null && paint.hasStrokes()) {
            // old paint
            java.awt.Paint oPaint = g.getPaint();

            g.setPaint((java.awt.Paint) paint.getNativeStrokesColor());
            g.draw(customPath);

            // restore old paint
            g.setPaint(oPaint);
        }

        // Test!
        customPath = new Path2D.Float();
    }
}
