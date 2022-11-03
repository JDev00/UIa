package uia.core.platform.implementation;

import uia.core.platform.policy.FontMetrics;

/**
 * FontMetrics implementation
 */

public class FontMetricsAWT implements FontMetrics {
    private final java.awt.FontMetrics fontMetrics;

    public FontMetricsAWT(java.awt.FontMetrics fontMetrics) {
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
    public float getHeight() {
        return fontMetrics.getHeight();
    }

    @Override
    public float getWidth(char c) {
        return fontMetrics.charWidth(c);
    }

    @Override
    public float getWidth(byte[] in, int off, int len) {
        return fontMetrics.bytesWidth(in, off, len);
    }

    @Override
    public float getWidth(char[] in, int off, int len) {
        return fontMetrics.charsWidth(in, off, len);
    }

    @Override
    public float getWidth(String in) {
        return fontMetrics.stringWidth(in);
    }

    @Override
    public Object getNative() {
        return fontMetrics;
    }
}
