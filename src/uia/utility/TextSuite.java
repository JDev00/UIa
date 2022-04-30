package uia.core.utility;

import uia.core.Context;
import uia.structure.list.Buffer;
import uia.structure.list.BufferChar;
import uia.structure.list.BufferFloat;
import uia.structure.list.BufferInt;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;

/**
 * TextSuite is an object used to keep text metrics and attributes in a single place
 */

public final class TextSuite {

    public enum AlignX {
        LEFT, CENTER, RIGHT;

        public int getNumber() {
            if (equals(LEFT)) return 0;
            if (equals(CENTER)) return 1;
            return 2;
        }
    }

    public enum AlignY {TOP, CENTER}

    public static final int TEXT_SIZE = 24;

    public static final Font ARIAL = new Font("Arial", Font.PLAIN, TEXT_SIZE);
    public static final Font HELVETICA = new Font("Helvetica Neue", Font.PLAIN, TEXT_SIZE);
    public static final Font TIMES_NEW_ROMAN = new Font("Times new roman", Font.PLAIN, TEXT_SIZE);

    /*
     * Attributes
     */

    private Context context;

    private Font font;

    private Color color = Color.BLACK;

    private AlignX alignX = AlignX.CENTER;

    private AlignY alignY = AlignY.TOP;

    private float fontSize = TEXT_SIZE;

    /*
     * Metrics
     */

    private BufferFloat buffer;

    private int cachedSize;

    public TextSuite(Context context, int size) {
        this.context = Context.validateContext(context);

        font = ARIAL;

        cachedSize = size;

        buffer = new BufferFloat(size);

        updateBuffer();
    }

    /**
     * Updates the measure buffer
     */

    private void updateBuffer() {
        FontMetrics fontMetrics = getFontMetrics();

        buffer.clear();
        for (int i = 0; i < cachedSize; i++) {
            buffer.add(buffer.size(), fontMetrics.charWidth((char) i));
        }
    }

    /**
     * Set a {@link Context} to this TextSuite
     *
     * @param context a non-null Context
     */

    public void setContext(Context context) {
        if (context != null)
            this.context = context;
    }

    /**
     * Resizes the measure buffer
     *
     * @param size a value {@code >= 0}
     */

    public void resizeBuffer(int size) {
        cachedSize = Math.max(size, 0);
        updateBuffer();
    }

    /**
     * Copies the params of the given text info inside this one
     *
     * @param textInfo a non-null {@link TextSuite} instance
     */

    public void set(TextSuite textInfo) {
        if (textInfo != null) {
            font = textInfo.font;
            color = textInfo.color;
            alignX = textInfo.alignX;
            alignY = textInfo.alignY;
            fontSize = textInfo.fontSize;
            cachedSize = textInfo.cachedSize;
            buffer = textInfo.buffer;
        }
    }

    /**
     * Sets the text x-alignment
     *
     * @param alignX a non-null instance of {@link AlignX}
     */

    public void setAlignX(AlignX alignX) {
        if (alignX != null) {
            this.alignX = alignX;
        }
    }

    /**
     * Sets the text y-alignment
     *
     * @param alignY a non-null instance of {@link AlignY}
     */

    public void setAlignY(AlignY alignY) {
        if (alignY != null) {
            this.alignY = alignY;
        }
    }

    /**
     * Sets the text color
     *
     * @param color a non-null color
     */

    public void setColor(Color color) {
        if (color != null) {
            this.color = color;
        }
    }

    /**
     * Sets the text font
     *
     * @param font a non-null {@link Font} instance
     */

    public void setFont(Font font) {
        if (font != null) {
            this.font = font;

            fontSize = font.getSize();

            updateBuffer();
        }
    }

    /**
     * Sets the font size
     *
     * @param size a number greater than 0
     */

    public void setFontSize(float size) {
        if (size > 0) {
            font = createFont(font, size);

            fontSize = size;

            updateBuffer();
        }
    }

    /**
     * @return the font size
     */

    public float getFontSize() {
        return fontSize;
    }

    /**
     * @return the width of the given char
     */

    public float getWidth(char c) {
        return buffer.getFloatAt(c);
    }

    /**
     * @return the width of the given char of chars
     */

    public float getWidth(char[] c, int off, int len) {
        float tot = 0;

        for (int i = 0; i < len; i++) {
            tot += buffer.getFloatAt(c[off + i]);
        }

        return tot;
    }

    /**
     * Returns the char width in pixels
     *
     * @param s       a char to measure
     * @param metrics an instance of {@link FontMetrics} used to measure the char
     * @return the width in pixel of the char
     */

    public static float getWidth(char s, FontMetrics metrics) {
        return metrics.charWidth(s);
    }

    /**
     * Returns the text width in pixels
     *
     * @param input   a char array to measure
     * @param off     the char used to start the measure
     * @param len     the array's length to measure
     * @param metrics an instance of {@link FontMetrics} used to measure the text
     * @return the width in pixel of the text
     */

    public static float getWidth(char[] input, int off, int len, FontMetrics metrics) {
        return metrics.charsWidth(input, off, len);
    }

    /**
     * @return true if the given alignX is equal to this x-alignment
     */

    public final boolean isAlignX(AlignX alignX) {
        return this.alignX.equals(alignX);
    }

    /**
     * @return true if the given alignY is equal to this y-alignment
     */

    public final boolean isAlignY(AlignY alignY) {
        return this.alignY.equals(alignY);
    }

    /**
     * @return the amount of buffered chars
     */

    public int bufferSize() {
        return cachedSize;
    }

    /**
     * @return the text color
     */

    public Color getColor() {
        return color;
    }

    /**
     * @return the text font
     */

    public Font getFont() {
        return font;
    }

    /**
     * @return the font metrics associated to this font
     */

    public FontMetrics getFontMetrics() {
        return context.getFontMetrics(font);
    }

    /**
     * @return the text x-alignment
     */

    public AlignX getAlignX() {
        return alignX;
    }

    /**
     * @return the text y-alignment
     */

    public AlignY getAlignY() {
        return alignY;
    }

    /*
     *
     * Keys info
     *
     */

    /**
     * @param keyEvent  a keyEvent
     * @param character the char that will be used with ctrl to compose the special code
     * @return true if ctrl-X has been composed
     */

    public static boolean ctrlX(KeyEvent keyEvent, int character) {
        return keyEvent != null
                && (keyEvent.getKeyCode() == character)
                && ((keyEvent.getModifiers() & KeyEvent.CTRL_MASK) != 0);
    }

    /**
     * @return true if ctrl-c has been composed
     */

    public static boolean ctrlC(KeyEvent keyEvent) {
        return ctrlX(keyEvent, KeyEvent.VK_C);
    }

    /**
     * @return true if ctrl-v has been composed
     */

    public static boolean ctrlV(KeyEvent keyEvent) {
        return ctrlX(keyEvent, KeyEvent.VK_V);
    }

    /**
     * @return true if ctrl-A has been composed
     */

    public static boolean ctrlA(KeyEvent keyEvent) {
        return ctrlX(keyEvent, KeyEvent.VK_A);
    }

    /**
     * @param character the char that will be used with shift to compose the special code
     * @return true if shift-X has been composed
     */

    public static boolean shiftX(KeyEvent keyEvent, int character) {
        return (keyEvent.getKeyCode() == character) && ((keyEvent.getModifiers() & KeyEvent.SHIFT_MASK) != 0);
    }

    /*
     *
     * Text information
     *
     */

    /**
     * Creates a new {@link Font} from the given one
     *
     * @param font     the font used to derive the new font
     * @param textSize the font text size
     * @return a new {@link Font}
     * @throws NullPointerException if {@code font == null}
     */

    public static Font createFont(Font font, float textSize) {
        return Objects.requireNonNull(font).deriveFont(textSize);
    }

    /**
     * @param input   the string to control
     * @param metrics an instance of {@link FontMetrics} used to measure the text
     * @return the longest text's line in pixels
     */

    public static float longestLine(String input, FontMetrics metrics) {
        if (input == null || metrics == null) return 0f;

        int i = 0;
        float longest = 0f;
        char[] data = input.toCharArray();

        // First control
        for (int j = 0; j < data.length; j++) {

            if (data[j] == '\n') {
                float pixelLength = getWidth(data, i, j - i, metrics);

                i = j;

                if (pixelLength > longest) longest = pixelLength;
            }
        }

        // Last control
        float pixelLength = getWidth(data, i, data.length - i, metrics);
        if (pixelLength > longest) longest = pixelLength;

        return longest;
    }

    /*
     *
     * Text justify
     *
     */

    /**
     * Calculates the position of the break lines and store them inside a given buffer of lines
     * Time required for computation: T(n) = Theta(n)
     *
     * @param buffer    a non-null {@link BufferChar} instance
     * @param lines     a non-null {@link BufferInt} used to store break lines indices
     * @param textSuite a non-null {@link TextSuite} instance
     * @param width     a value {@code > 0} used to set the maximum line's width in pixel
     */

    public static void justify(BufferChar buffer, BufferInt lines, TextSuite textSuite, float width) {
        if (buffer == null || lines == null || textSuite == null || width < 0) return;

        char test = '\r';
        int gap = 0;
        float lineLength = 0;
        float subLength = 0;

        // remove the algorithm generated break lines. T(n) = Theta(n)
        buffer.remove(test);

        // clear lines
        lines.clear();

        // T(n) = Theta(n)
        for (int i = 0; i < buffer.size(); i++) {
            char t = buffer.getCharAt(i);
            float w = textSuite.getWidth(t);

            lineLength += w;
            subLength += w;

            if (t == '\n') {
                lineLength = subLength = 0;
            } else if (lineLength > width) {

                if (subLength < width) {
                    lines.add(gap + 1);
                    lineLength = subLength;
                } else {
                    lines.add(i);
                    lineLength = subLength = w;
                }
            }

            if (t == ' ') {
                gap = i;
                subLength = 0;
            }
        }

        // add to the buffer the algorithm generated lines. T(n) = Theta(n)
        buffer.add(Buffer.getData(lines), 0, lines.size(), test);

        lines.clear();

        // T(n) = Theta(n)
        for (int i = 0; i < buffer.size(); i++) {
            char t = buffer.getCharAt(i);

            if (t == '\n' || t == '\r') {
                lines.add(i);
            }
        }

        if (buffer.size() > 0) {
            lines.add(buffer.size());
        }
    }
}
