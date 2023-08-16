package platform.awt;

import uia.core.Font;
import uia.core.Image;
import uia.core.Geom;
import uia.core.Paint;
import uia.core.architecture.Graphic;
import uia.utils.TrigTable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

/**
 * AWT {@link Graphic} implementation
 */

public class GraphicAWT implements Graphic {
    private Graphics2D g;
    private Path2D customPath;

    private Paint paint;
    private Font font;

    public GraphicAWT() {
        customPath = new Path2D.Float();

        paint = new Paint();

        font = new Font("Arial", Font.STYLE.ITALIC, Font.FONT_SIZE_DESKTOP);
    }

    @Override
    public void setNative(Object... o) {
        try {
            g = (Graphics2D) o[0];
            setFont(font);
        } catch (Exception ignored) {
        }
    }

    /**
     * @return always null
     */

    @Override
    public Object getNative() {
        return null;
    }

    @Override
    public void dispose() {
        g.dispose();
    }

    private final Stack<Geom> stack = new Stack<>();
    private Geom clip = null;
    private final Path2D clipPath = new Path2D.Float();

    private void buildGeometry(Geom geom, Path2D path) {
        path.reset();

        for (int i = 0; i < geom.vertices(); i++) {
            Geom.Vertex vertex = geom.get(i);

            if (vertex.beginGeom) {
                path.moveTo(vertex.x, vertex.y);
            } else {
                path.lineTo(vertex.x, vertex.y);
            }
        }

        path.closePath();
    }

    @Override
    public void setClip(Geom geom) {
        clip = stack.push(geom);

        if (geom != null) {
            buildGeometry(geom, clipPath);
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
                buildGeometry(clip, clipPath);
                g.setClip(clipPath);
                return;
            }
        }

        clip = null;
        g.setClip(null);
    }

    @Override
    public void setFont(Font font) {
        this.font = font;

        // build the native font when necessary
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

            font.setNative(fontNative, new Metrics(new Canvas().getFontMetrics(fontNative)));
        }

        g.setFont((java.awt.Font) font.getNative());
    }

    @Override
    public void setPaint(Paint p) {
        paint = p;

        if (!p.isValid() || !(p.getNativeColor() instanceof java.awt.Paint)) {
            p.setNative(
                    new Color(paint.getRed(), paint.getGreen(), paint.getBlue(), paint.getAlpha()),
                    new Color(paint.getStrokeRed(), paint.getStrokeGreen(), paint.getStrokeBlue()),
                    new BasicStroke(paint.getStrokeWidth()));
        }

        g.setPaint((java.awt.Paint) p.getNativeColor());
        g.setStroke((Stroke) p.getNativeStrokeWidth());
    }

    @Override
    public void openShape(float x, float y) {
        customPath.moveTo(x, y);
    }

    @Override
    public void vertex(float x, float y) {
        customPath.lineTo(x, y);
    }

    @Override
    public void closeShape() {
        customPath.closePath();

        g.fill(customPath);

        // set stroke color
        if (paint.hasStroke()) {
            // old paint
            java.awt.Paint oPaint = g.getPaint();

            g.setPaint((java.awt.Paint) paint.getNativeStrokeColor());
            g.draw(customPath);

            // restore old paint
            g.setPaint(oPaint);
        }

        customPath = new Path2D.Float();
    }

    @Override
    public void drawText(char[] data, int offset, int length, float x, float y, float rotation) {// @test
        boolean rotate = Float.compare(rotation % TrigTable.TWO_PI, 0f) != 0;

        AffineTransform oldXForm = null;

        if (rotate) {
            oldXForm = g.getTransform();
            g.rotate(rotation, x, y);
        }

        g.drawChars(data, offset, length, (int) x, (int) y);

        if (rotate) g.setTransform(oldXForm);
    }

    private final java.awt.Image puppetImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

    @Override
    public void drawImage(Image img, float x, float y, float width, float height, float rotation) {
        if (!img.isValid() || !(img.getNative() instanceof java.awt.Image)) {
            img.setNative(puppetImage, puppetImage.getWidth(null), puppetImage.getHeight(null));

            new Thread(() -> {
                try {
                    java.awt.Image image = ImageIO.read(new File(img.getPath()));
                    img.setNative(image, image.getWidth(null), image.getHeight(null));
                } catch (Exception e) {
                    img.setNative(null, 0f, 0f);
                }
            }).start();
        }

        boolean rotate = Float.compare(rotation % TrigTable.TWO_PI, 0f) != 0;

        AffineTransform oldXForm = null;

        if (rotate) {
            oldXForm = g.getTransform();
            g.rotate(rotation, x, y);
        }

        g.drawImage((java.awt.Image) img.getNative(),
                (int) (x - width / 2f), (int) (y - height / 2f), (int) width, (int) height, null);

        if (rotate) g.setTransform(oldXForm);
    }

    @Override
    public void drawGeometry(Geom geom) {
        if (geom.vertices() > 0) {
            customPath.reset();

            for (int i = 0; i < geom.vertices(); i++) {
                Geom.Vertex vertex = geom.get(i);

                if (vertex.beginGeom) {
                    openShape(vertex.x, vertex.y);
                } else {
                    vertex(vertex.x, vertex.y);
                }
            }

            closeShape();
        }
    }

    // FontMetrics implementation

    private static class Metrics implements uia.core.FontMetrics {
        private final java.awt.FontMetrics fontMetrics;

        public Metrics(java.awt.FontMetrics fontMetrics) {
            this.fontMetrics = fontMetrics;
        }

        @Override
        public float getAscent() {
            return fontMetrics.getAscent();
        }

        @Override
        public float getDescent() {
            return fontMetrics.getDescent();
        }

        @Override
        public float getLeading() {
            return fontMetrics.getLeading();
        }

        @Override
        public float getWidth(char c) {
            return fontMetrics.charWidth(c);
        }

        @Override
        public float getWidth(char[] in, int off, int len) {
            return fontMetrics.charsWidth(in, off, len);
        }
    }
}
