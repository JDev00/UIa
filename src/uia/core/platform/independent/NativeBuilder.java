package uia.core.platform.independent;

/**
 * NativeBuilder class is used to handle the directives of a native object creation
 */

public abstract class NativeBuilder {
    private boolean toBuild = true;

    /**
     * Request to build the native object
     */

    public void request() {
        toBuild = true;
    }

    /**
     * Deny the building of the native object
     */

    public void deny() {
        toBuild = false;
    }

    /**
     * @return true if the native object has to be built
     */

    public boolean hasToBeBuilt() {
        return toBuild;
    }
}
