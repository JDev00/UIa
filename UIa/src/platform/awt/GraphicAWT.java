package platform.awt;

import uia.core.*;
import uia.core.Font;
import uia.core.Image;
import uia.core.Paint;
import uia.core.basement.Graphic;
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
    private final Path2D customPath;

    private final java.awt.Image fakeImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

    public GraphicAWT() {
        customPath = new Path2D.Float();
    }

    @Override
    public void setNative(Object... o) {
        g = (Graphics2D) o[0];
    }

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

        g.setFont((java.awt.Font) font.getNative());
    }

    private Paint paint = new Paint();

    @Override
    public void setPaint(Paint p) {
        paint = p;

        if (!p.isValid() || !(p.getNativeColor() instanceof java.awt.Paint)) {
            p.setNative(
                    new Color(p.getRed(), p.getGreen(), p.getBlue(), p.getAlpha()),
                    new Color(p.getStrokeRed(), p.getStrokeGreen(), p.getStrokeBlue()),
                    new BasicStroke(p.getStrokeWidth()));
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

        customPath.reset();
    }

    @Override
    public void drawText(char[] data, int offset, int length, float x, float y, float rotation) {
        boolean rotate = Float.compare(rotation % TrigTable.TWO_PI, 0f) != 0;

        AffineTransform oldXForm = null;

        if (rotate) {
            oldXForm = g.getTransform();
            g.rotate(rotation, x, y);
        }

        g.drawChars(data, offset, length, (int) x, (int) y);

        if (rotate) g.setTransform(oldXForm);
    }

    @Override
    public void drawImage(Image img, float x, float y, float width, float height, float rotation) {
        if (!img.isValid() || !(img.getNative() instanceof java.awt.Image)) {
            img.setNative(fakeImage, 1f, 1f);

            new Thread(() -> {
                try {
                    java.awt.Image image = ImageIO.read(new File(img.getPath()));
                    img.setNative(image, image.getWidth(null), image.getHeight(null));
                } catch (Exception ignored) {
                }
            }).start();
        }

        boolean rotate = Float.compare(rotation % TrigTable.TWO_PI, 0f) != 0;

        AffineTransform oldXForm = null;

        if (rotate) {
            oldXForm = g.getTransform();
            g.rotate(rotation, x, y);
        }

        float offset = 0;
        if (img.getMode() == Image.MODE.CENTER) {
            offset = 0.5f;
        } else if (img.getMode() == Image.MODE.RIGHT) {
            offset = 1f;
        }

        g.drawImage((java.awt.Image) img.getNative(),
                (int) (x - offset * width), (int) (y - offset * height),
                (int) width, (int) height, null);

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
}
