package uia.core.ui.style;

/**
 * StyleFunction is responsible for applying a list of changes to a Style object.
 */

public interface StyleFunction {

    /**
     * Applies the changes to the given Style.
     *
     * @param style a style to be modified
     * @throws NullPointerException if {@code style == null}
     */

    void apply(Style style);
}
