package test.__tests__.sanity;

import test.__tests__.utility.TestUtility;
import uia.core.paint.Paint;
import uia.core.ui.View;
import uia.core.ui.ViewGroup;
import uia.core.ui.callbacks.OnFocus;
import uia.core.ui.context.Context;
import uia.physical.component.Component;
import uia.physical.group.ComponentGroup;
import uia.physical.theme.Theme;

/**
 * Sanity test.
 */

public class focusTest {

    /**
     * Creates a mocked group to test focus behaviour.
     */

    private static ViewGroup createMockGroup() {
        ViewGroup result = new ComponentGroup(
                new Component("GROUP_0", 0.5f, 0.5f, 1f, 1f)
        );
        result.getPaint().setColor(Theme.LIGHT_GRAY);
        result.registerCallback((OnFocus) focus -> {
            Paint paint = result.getPaint();
            paint.setStrokeWidth(focus ? 10 : 0);
        });

        ViewGroup internalGroup1 = new ComponentGroup(
                new Component("GROUP_1", 0.5f, 0.5f, 0.75f, 0.75f)
        );
        internalGroup1.getPaint().setColor(Theme.SILVER);
        internalGroup1.registerCallback((OnFocus) focus -> {
            Paint paint = internalGroup1.getPaint();
            paint.setStrokeWidth(focus ? 10 : 0);
        });

        ViewGroup internalGroup2 = new ComponentGroup(
                new Component("GROUP_2", 0.5f, 0.5f, 0.75f, 0.75f)
        );
        internalGroup2.getPaint().setColor(Theme.DARK_GRAY);
        internalGroup2.registerCallback((OnFocus) focus -> {
            Paint paint = internalGroup2.getPaint();
            paint.setStrokeWidth(focus ? 10 : 0);
        });

        View child1 = new Component("CHILD_1", 1f, 0.5f, 0.5f, 0.5f);
        child1.getPaint().setColor(Theme.NAVY);
        child1.registerCallback((OnFocus) focus -> {
            Paint paint = child1.getPaint();
            paint.setStrokeWidth(focus ? 10 : 0);
        });

        View child2 = new Component("CHILD_2", 0f, 0f, 0.5f, 0.5f);
        child2.getPaint().setColor(Theme.ROYAL_BLUE);
        child2.registerCallback((OnFocus) focus -> {
            Paint paint = child2.getPaint();
            paint.setStrokeWidth(focus ? 10 : 0);
        });

        // GROUP_0 contains GROUP_1 contains GROUP_2 contains CHILD_1 and CHILD_2
        ViewGroup.insert(internalGroup2, child1, child2);
        ViewGroup.insert(internalGroup1, internalGroup2);
        ViewGroup.insert(result, internalGroup1);

        return result;
    }

    public static void main(String[] args) {
        ViewGroup mockedGroup = createMockGroup();

        Context context = TestUtility.createMockContext();
        context.setView(mockedGroup);
    }
}
