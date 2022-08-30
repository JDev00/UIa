package uia.core.policy;

/**
 * Bundle ADT used as bridge between multiple platforms/implementations
 */

public interface Bundle {

    /**
     * Encapsulate the native object
     *
     * @param o a not null object
     */

    void setNative(Object o);

    /**
     * @return the native object
     */

    Object getNative();
}
