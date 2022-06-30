package uia.core.platform;

import uia.core.policy.*;
import uia.core.policy.Font;
import uia.core.policy.Image;
import uia.core.policy.Paint;

import java.awt.*;

/**
 * AWT render implementation
 */

public class RenderAWT implements Render {
    private Graphics2D g;

    private final Font font;

    private final Path path;

    private Paint paint;

    public RenderAWT() {
        g = null;

        font = new FontAWT();

        path = new PathAWT();
        path.setNative(null);

        paint = new PaintAWT();
    }

    @Override
    public void setNative(Object o) {
        if (o instanceof Graphics2D) {
            g = (Graphics2D) o;
            font.setNative(g.getFont());
        }
    }

    @Override
    public Graphics2D getNative() {
        return g;
    }

    @Override
    public void translate(int x, int y) {
        g.translate(x, y);
    }

    @Override
    public void translate(double tx, double ty) {
        g.translate(tx, ty);
    }

    @Override
    public void rotate(double theta) {
        g.rotate(theta);
    }

    @Override
    public void rotate(double theta, double x, double y) {
        g.rotate(theta, x, y);
    }

    @Override
    public void scale(double sx, double sy) {
        g.scale(sx, sy);
    }

    @Override
    public void shear(double shx, double shy) {
        g.shear(shx, shy);
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

    @Override
    public void clip(Path p) {
        if (p != null)
            g.clip((Shape) p.getNative());
    }

    @Override
    public void setClip(Path p) {
        if (p != null) {
            path.setNative(p.getNative());
            g.setClip((Shape) p.getNative());
        }
    }

    @Override
    public Path getClip() {
        return path;
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
        if (font != null) {
            java.awt.Font f = (java.awt.Font) font.getNative();
            this.font.setNative(f);
            g.setFont(f);
        }
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Override
    public void setPaint(Paint p) {
        if (p != null) {
            paint = p;
            g.setPaint((java.awt.Paint) p.getNative());
        }
    }

    @Override
    public Paint getPaint() {
        return paint;
    }

    @Override
    public void draw(Path path) {
        if (path != null) {
            g.fill((Shape) path.getNative());

            if (paint != null && paint.hasStrokeColor()) {
                g.setPaint((java.awt.Paint) paint.getStrokeNative());
                g.draw((Shape) path.getNative());
            }
        }
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void drawRect(int x, int y, int width, int height) {
        g.fillRect(x, y, width, height);
    }

    @Override
    public void drawRoundRect(int x, int y, int width, int height, int arcWidth, int arcHeight) {
        g.fillRoundRect(x, y, width, height, arcWidth, arcHeight);
    }

    @Override
    public void drawOval(int x, int y, int width, int height) {
        g.fillOval(x, y, width, height);
    }

    @Override
    public void drawArc(int x, int y, int width, int height, int startAngle, int arcAngle) {
        g.fillArc(x, y, width, height, startAngle, arcAngle);
    }

    @Override
    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        g.fillPolygon(xPoints, yPoints, nPoints);
    }

    @Override
    public void drawText(String str, int x, int y) {
        g.drawString(str, x, y);
    }

    @Override
    public void drawText(char[] data, int offset, int length, int x, int y) {
        g.drawChars(data, offset, length, x, y);
    }

    @Override
    public void drawImage(Image img, int x, int y) {
        g.drawImage((java.awt.Image) img.getNative(), x, y, null);
    }

    @Override
    public void drawImage(Image img, int x, int y, int width, int height) {
        g.drawImage((java.awt.Image) img.getNative(), x, y, width, height, null);
    }

    @Override
    public void dispose() {
        g.dispose();
    }

    @Override
    public void finalize() {
        g.finalize();
    }
}
