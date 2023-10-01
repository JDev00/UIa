package test.processing;

import processing.core.*;

import uia.core.*;
import uia.core.ui.Graphic;

public class GraphicProcessing implements Graphic {
    private PApplet pApplet;
    private PGraphics pGraphics;

    private final PImage fakeImage = new PImage(1, 1, PConstants.RGB);

    @Override
    public void setNative(Object data) {
        Object[] o = (Object[]) data;

        pGraphics = (PGraphics) o[0];
        pApplet = (PApplet) o[1];
    }

    @Override
    public Object getNative() {
        return pGraphics;
    }

    @Override
    public void dispose() {
        pGraphics.dispose();
    }

    @Override
    public void setClip(Shape shape) {
        // TODO: 18/08/2023 implement functionality
    }

    @Override
    public void restoreClip() {
        // TODO: 18/08/2023 implement functionality
    }

    @Override
    public void setFont(Font font) {
        if (!font.isValid() || !(font.getNative() instanceof PFont)) {
            PFont fontNative = pApplet.createFont(font.getName(), font.getSize(), true);
            font.setNative(fontNative, (off, len, in) -> {
                        float count = 0;
                        for (int i = 0; i < len; i++) {
                            count += fontNative.width(in[off + i]);
                        }
                        return font.getSize() * count;
                    },
                    font.getSize() * fontNative.ascent(),
                    font.getSize() * fontNative.descent(),
                    0.05f * font.getSize());
        }

        pGraphics.textFont((PFont) font.getNative());
    }

    @Override
    public void setPaint(Paint paint) {
        pGraphics.fill(paint.getRed(), paint.getGreen(), paint.getBlue(), paint.getAlpha());

        if (paint.hasStroke()) {
            pGraphics.stroke(paint.getStrokeRed(), paint.getStrokeGreen(), paint.getStrokeBlue());
        } else {
            pGraphics.noStroke();
        }

        pGraphics.strokeWeight(paint.getStrokeWidth());
    }

    @Override
    public void drawShape(Shape shape) {
        Geometry geometry = shape.getGeometry();
        Shape.TransformedVertex target = new Shape.TransformedVertex();

        for (int i = 0; i < geometry.vertices(); i++) {
            shape.transformAndStoreInto(geometry.get(i), target);
            float x = target.x;
            float y = target.y;

            if (target.primer) {
                pGraphics.beginShape();
                pGraphics.vertex(x, y);
            } else {
                pGraphics.vertex(x, y);
            }
        }

        pGraphics.endShape();
    }

    @Override
    public void drawText(char[] data, int offset, int length, float x, float y, float rotation) {
        try {
            pGraphics.textAlign(PConstants.LEFT);
            pGraphics.pushMatrix();
            pGraphics.translate(x, y);
            pGraphics.rotate(rotation);
            pGraphics.text(String.valueOf(data, offset, length), 0, 0);
            pGraphics.popMatrix();
        } catch (Exception ignored) {
        }
    }

    @Override
    public void drawImage(Image img, float x, float y, float width, float height, float rotation) {
        if (!img.isValid() || !(img.getNative() instanceof PImage)) {
            img.setNative(fakeImage, 1f, 1f);

            new Thread(() -> {
                PImage image = pApplet.loadImage(img.getPath());
                img.setNative(image, image.width, image.height);
            }).start();
        }

        float offset = 0;
        if (img.getMode() == Image.MODE.CENTER) {
            offset = 0.5f;
        } else if (img.getMode() == Image.MODE.RIGHT) {
            offset = 1f;
        }

        pGraphics.pushMatrix();
        pGraphics.translate(x, y);
        pGraphics.rotate(rotation);
        pGraphics.image((PImage) img.getNative(), -offset * width, -offset * height, width, height);
        pGraphics.popMatrix();
    }
}
