package uia.core.platform;

import uia.core.policy.Font;

import java.awt.*;

/**
 * AWT Font implementation
 */

public class FontAWT implements Font {
    private java.awt.Font font;
    private FontMetrics fontMetrics;

    public FontAWT() {
        font = null;
        fontMetrics = null;
    }

    @Override
    public void setNative(Object o) {
        if (o instanceof java.awt.Font) {
            font = (java.awt.Font) o;
            fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
        }
    }

    @Override
    public java.awt.Font getNative() {
        return font;
    }

    @Override
    public void setSize(float size) {
        if (size > 0) {
            font = font.deriveFont(size);
            fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
        }
    }

    @Override
    public FontMetrics getMetrics() {
        return fontMetrics;
    }

    @Override
    public float getSize() {
        return font.getSize();
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
    public float getLineHeight() {
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
}
