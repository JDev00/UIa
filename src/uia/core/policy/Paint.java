package uia.core.policy;

import java.util.List;

/**
 * Paint ADT.
 * <br>
 * <b>Note that Paint primarily acts as a bridge between API framework and native platform.</b>
 */

public interface Paint extends Bundle {

    /**
     * TYPE class provides a list of all the native Paint objects.
     * It is used to classify the given Paint and, if necessary, to convert it for another Context.
     */
    enum TYPE {LINEAR, GRADIENT, GRADIENT_LINEAR, GRADIENT_RADIAL, OTHER}

    /**
     * @return the Paint TYPE or null if no native Paint has been set
     * @see TYPE
     */

    TYPE getType();

    /**
     * @return the colors composition
     */

    List<float[]> getColors();
}
