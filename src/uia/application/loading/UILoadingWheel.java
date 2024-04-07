package uia.application.loading;

import uia.physical.component.WrapperView;
import uia.utility.MathUtility;
import uia.core.shape.Geometry;
import uia.utility.Geometries;
import uia.core.ui.View;

/**
 * Standard UIa component.
 * <br>
 * UILoadingWheel represents a standard spinning wheel component.
 */

public final class UILoadingWheel extends WrapperView {
    public static final float THICKNESS = 0.04f;
    public static final float VELOCITY = 0.05f;

    private float rotation = 0f;

    public UILoadingWheel(View view, float velocity, float thickness) {
        super(view);
        setColliderPolicy(ColliderPolicy.AABB);
        setGeometry(g -> {
            if (Math.abs(rotation) >= MathUtility.TWO_PI) {
                rotation = 0f;
            }
            Geometry arc = Geometries.arc(g, 50, rotation, thickness);
            Geometry.rotate(arc, rotation);
            rotation += velocity;
        }, true);
    }
}
