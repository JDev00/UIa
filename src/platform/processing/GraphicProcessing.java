package platform.processing;

import processing.core.*;

import uia.core.*;
import uia.core.architecture.Graphic;

public class GraphicProcessing implements Graphic {
    private PApplet pApplet;
    private PGraphics pGraphics;

    @Override
    public void setNative(Object... o) {
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

    //private final float[] bounds = new float[4];

    @Override
    public void setClip(Geom geom) {
        /*if (geom != null) {

            for (int i = 0; i < geom.vertices(); i++) {
                Geom.Vertex vertex = geom.get(i);

                if (vertex.x < bounds[0]) bounds[0] = vertex.x;
                if (vertex.x > bounds[2]) bounds[2] = vertex.x;

                if (vertex.y < bounds[1]) bounds[1] = vertex.y;
                if (vertex.y > bounds[3]) bounds[3] = vertex.y;
            }

            float w = PApplet.abs(bounds[2] - bounds[0]);
            float h = PApplet.abs(bounds[3] - bounds[1]);

            pGraphics.noClip();
            pGraphics.clip(geom.x - w / 2f, geom.y - h / 2f, w, h);
        } else {
            pGraphics.noClip();
        }*/
    }

    @Override
    public void restoreClip() {

    }

    @Override
    public void setFont(Font font) {
        if (!font.isBuilt() || !(font.getNative() instanceof PFont)) {
            PFont fontNative = pApplet.createFont(font.getName(), font.getSize(), true);
            font.setNative(fontNative, new FontMetricsProcessing(fontNative));
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

    private final float[] beginShape = {0f, 0f};

    @Override
    public void openShape(float x, float y) {
        beginShape[0] = x;
        beginShape[1] = y;

        pGraphics.beginShape();
        pGraphics.vertex(x, y);
    }

    @Override
    public void vertex(float x, float y) {
        pGraphics.vertex(x, y);
    }

    @Override
    public void closeShape() {
        pGraphics.vertex(beginShape[0], beginShape[1]);
        pGraphics.endShape();
    }

    @Override
    public void drawGeometry(Geom geom) {
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

    @Override
    public void drawText(char[] data, int offset, int length, float x, float y, float rotation) {
        // TODO: 21/07/2023 implement text rotation

        try {
            pGraphics.text(String.valueOf(data, offset, length), x, y);
        } catch (Exception ignored) {
        }
    }

    private final PImage puppetImage = new PImage(1, 1, PConstants.RGB);

    @Override
    public void drawImage(Image img, float x, float y, float width, float height, float rotation) {
        if (!img.isBuilt() || !(img.getNative() instanceof PImage)) {
            img.setNative(puppetImage, puppetImage.width, puppetImage.height);

            new Thread(() -> {// load this image in an async way
                PImage image = pApplet.loadImage(img.getPath());
                if (image != null) {
                    img.setNative(image, image.width, image.height);
                } else {
                    img.setNative(null, 0f, 0f);
                }
            }).start();
        }

        pGraphics.pushMatrix();
        pGraphics.rotate(rotation);
        pGraphics.image((PImage) img.getNative(), (x - width / 2f), (y - height / 2f), width, height);
        pGraphics.popMatrix();
    }

    //
    private static class FontMetricsProcessing implements FontMetrics {
        private final PFont font;

        public FontMetricsProcessing(PFont font) {
            this.font = font;
        }

        @Override
        public float getAscent() {
            return font.getSize() * font.ascent();
        }

        @Override
        public float getDescent() {
            return font.getSize() * font.descent();
        }

        @Override
        public float getLeading() {
            return font.getSize() / 2f;
        }

        @Override
        public float getWidth(char c) {
            return font.getSize() * font.width(c);
        }

        @Override
        public float getWidth(char[] in, int off, int len) {
            float count = 0;
            for (int i = 0; i < len; i++) {
                count += font.width(in[off + i]);
            }
            return font.getSize() * count;
        }
    }
}
