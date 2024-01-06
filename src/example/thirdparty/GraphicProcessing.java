package example.thirdparty;

import processing.core.*;

import uia.core.*;
import uia.core.Paint.Color;
import uia.core.Paint.Paint;
import uia.core.shape.Geometry;
import uia.core.shape.Shape;
import uia.core.ui.Graphic;

import java.util.Objects;
import java.util.function.Supplier;

public class GraphicProcessing implements Graphic {
    private final Supplier<PApplet> pAppletSupplier;
    private final Supplier<PGraphics> pGraphicsSupplier;
    private final PImage fakeImage = new PImage(1, 1, PConstants.RGB);

    public GraphicProcessing(Supplier<PApplet> pAppletSupplier,
                             Supplier<PGraphics> pGraphicsSupplier) {
        this.pAppletSupplier = pAppletSupplier;
        this.pGraphicsSupplier = pGraphicsSupplier;
    }

    private PApplet getPApplet() {
        return pAppletSupplier.get();
    }

    private PGraphics getGraphics() {
        return pGraphicsSupplier.get();
    }

    @Override
    public void dispose() {
        getGraphics().dispose();
    }

    @Override
    public void setClip(Shape shape) {
    }

    @Override
    public void restoreClip() {
    }

    @Override
    public void setFont(Font font) {
        if (!font.isValid() || !(font.getNative() instanceof PFont)) {
            PFont fontNative = getPApplet().createFont(font.getName(), font.getSize(), true);
            font.setNative(fontNative,
                    font.getSize() * fontNative.ascent(),
                    font.getSize() * fontNative.descent(),
                    0.05f * font.getSize(),
                    (off, len, in) -> {
                        float count = 0;
                        for (int i = 0; i < len; i++) {
                            count += fontNative.width(in[off + i]);
                        }
                        return font.getSize() * count;
                    });
        }

        getGraphics().textFont((PFont) font.getNative());
    }

    @Override
    public void setPaint(Paint paint) {
        Color color = paint.getColor();
        Color strokeColor = paint.getStrokeColor();

        PGraphics graphics = getGraphics();
        graphics.fill(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());

        if (paint.hasStroke()) {
            graphics.stroke(strokeColor.getRed(), strokeColor.getGreen(), strokeColor.getBlue());
        } else {
            graphics.noStroke();
        }

        graphics.strokeWeight(paint.getStrokeWidth());
    }

    @Override
    public void drawShape(float... vertices) {
        Objects.requireNonNull(vertices);
        if (vertices.length % 2 != 0) {
            throw new IllegalArgumentException("vertices shape must be [x1,y1, x2,y2, x3,y3, ...]");
        }

        if (vertices.length > 0) {
            PGraphics graphics = getGraphics();
            graphics.beginShape();
            for (int i = 0; i < vertices.length; i += 2) {
                float x = vertices[i];
                float y = vertices[i + 1];
                graphics.vertex(x, y);
            }
            graphics.endShape();
        }
    }

    @Override
    public void drawShape(Shape shape) {
        PGraphics graphics = getGraphics();

        Geometry geometry = shape.getGeometry();
        Shape.TransformedVertex target = new Shape.TransformedVertex();

        for (int i = 0; i < geometry.vertices(); i++) {
            Shape.transform(shape, geometry.get(i), target);
            float x = target.x;
            float y = target.y;

            if (target.primer) {
                graphics.beginShape();
                graphics.vertex(x, y);
            } else {
                graphics.vertex(x, y);
            }
        }

        graphics.endShape();
    }

    @Override
    public void drawText(char[] data, int offset, int length, float x, float y, float rotation) {
        try {
            PGraphics graphics = getGraphics();
            graphics.textAlign(PConstants.LEFT);
            graphics.pushMatrix();
            graphics.translate(x, y);
            graphics.rotate(rotation);
            graphics.text(String.valueOf(data, offset, length), 0, 0);
            graphics.popMatrix();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void drawImage(Image img, float x, float y, float width, float height, float rotation) {
        if (!img.isValid() || !(img.getNative() instanceof PImage)) {
            img.setNative(fakeImage, 1, 1);

            new Thread(() -> {
                PImage image = getPApplet().loadImage(img.getPath());
                img.setNative(image, image.width, image.height);
            }).start();
        }

        float offset = 0.5f;
        PGraphics graphics = getGraphics();
        graphics.pushMatrix();
        graphics.translate(x, y);
        graphics.rotate(rotation);
        graphics.image((PImage) img.getNative(), -offset * width, -offset * height, width, height);
        graphics.popMatrix();
    }
}
