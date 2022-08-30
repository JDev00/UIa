package uia.core.widget.math.struct;

import uia.structure.vector.Vec;

/**
 * Data structure used to store one point with its description
 */

public class VecS extends Vec {
    public String data;

    public VecS(float x, float y) {
        super(x, y);
    }

    public VecS(float x, float y, String data) {
        super(x, y);
        this.data = data;
    }

    /**
     * Updates the data contents
     *
     * @param data a string
     */

    public void setData(String data) {
        this.data = data;
    }

    public void set(float x, float y, String data) {
        this.x = x;
        this.y = y;
        this.data = data;
    }

    @Override
    public String toString() {
        return "VecS{x = " + x + ", y=" + y +
                ",data='" + data + '\'' +
                '}';
    }

    /**
     * Creates a new instance of this class
     *
     * @param v a value to set to x and y
     */

    public static VecS create(float v) {
        return new VecS(v, v, null);
    }

    /**
     * Creates a new instance of this class
     *
     * @param v    a value to set to x and y
     * @param data a string used to describe this point
     */

    public static VecS create(float v, String data) {
        return new VecS(v, v, data);
    }

    /**
     * Creates a new instance of this class
     *
     * @param vec a vec whose parameters will be copy to the new vec
     */

    public static VecS create(Vec vec) {
        return new VecS(vec.x, vec.y, null);
    }

    /**
     * Creates a new instance of this class
     *
     * @param vecS a vec whose parameters will be copy to the new vec
     */

    public static VecS create(VecS vecS) {
        return new VecS(vecS.x, vecS.y, vecS.data);
    }
}
