package uia.core.platform.independent.paint;

public class PaintGradient extends Paint {
    private float fraction = 1f;
    private final float[] bounds;
    private int[] color2;

    public PaintGradient(int r, int g, int b, int a) {
        super(r, g, b, a);
        bounds = new float[]{0f, 0f, 1f, 1f};
        color2 = new int[]{0, 0, 0, 255};
    }

    public PaintGradient(int r, int g, int b) {
        this(r, g, b, 255);
    }

    public PaintGradient setFraction(float fraction) {
        this.fraction = Math.max(0, fraction);
        return this;
    }

    public PaintGradient setBounds(float xLower, float yLower,
                                   float xUpper, float yUpper) {
        bounds[0] = xLower;
        bounds[1] = yLower;
        bounds[2] = xUpper;
        bounds[3] = yUpper;
        return this;
    }

    public PaintGradient setColor2(int r, int g, int b, int a) {
        if (color2 == null) color2 = new int[4];
        color2[0] = r;
        color2[1] = g;
        color2[2] = b;
        color2[3] = a;
        return this;
    }

    public PaintGradient setColor2(int r, int g, int b) {
        return setColor2(r, g, b, 255);
    }

    public float getFraction() {
        return fraction;
    }

    public float[] getBounds() {
        return bounds;
    }

    public int[] getColor2() {
        return color2;
    }
}
