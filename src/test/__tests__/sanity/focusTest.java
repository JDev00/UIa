package test.__tests__.sanity;

import test.__tests__.utility.TestUtility;
import uia.physical.group.ComponentGroup;
import uia.physical.component.Component;
import uia.core.ui.callbacks.OnFocus;
import uia.core.context.Context;
import uia.physical.theme.Theme;
import uia.core.ui.style.Style;
import uia.core.ui.ViewGroup;
import uia.core.ui.View;

/**
 * Sanity test.
 */

public final class focusTest {

    private focusTest() {
    }

    /**
     * Creates a mocked group to test focus behaviour.
     */

    private static ViewGroup createMockGroup() {
        ViewGroup result = new ComponentGroup(
                new Component("GROUP_0", 0.5f, 0.5f, 1f, 1f)
        );
        result.getStyle().setBackgroundColor(Theme.LIGHT_GRAY);
        createBorderWhenFocused(result);

        ViewGroup internalGroup1 = new ComponentGroup(
                new Component("GROUP_1", 0.5f, 0.5f, 0.75f, 0.75f)
        );
        internalGroup1.getStyle().setBackgroundColor(Theme.SILVER);
        createBorderWhenFocused(internalGroup1);

        ViewGroup internalGroup2 = new ComponentGroup(
                new Component("GROUP_2", 0.5f, 0.5f, 0.75f, 0.75f)
        );
        internalGroup2.getStyle().setBackgroundColor(Theme.DARK_GRAY);
        createBorderWhenFocused(internalGroup2);

        View child1 = new Component("CHILD_1", 1f, 0.5f, 0.5f, 0.5f);
        child1.getStyle().setBackgroundColor(Theme.NAVY);
        createBorderWhenFocused(child1);

        View child2 = new Component("CHILD_2", 0f, 0f, 0.5f, 0.5f);
        child2.getStyle().setBackgroundColor(Theme.ROYAL_BLUE);
        createBorderWhenFocused(child2);

        // GROUP_0 contains GROUP_1 contains GROUP_2 contains CHILD_1 and CHILD_2
        ViewGroup.insert(internalGroup2, child1, child2);
        ViewGroup.insert(internalGroup1, internalGroup2);
        ViewGroup.insert(result, internalGroup1);

        return result;
    }

    private static void createBorderWhenFocused(View view) {
        view.getStyle().setBorderColor(Theme.BLACK);
        view.registerCallback((OnFocus) focus -> {
            Style style = view.getStyle();
            style.setBorderWidth(focus ? 10 : 0);
        });
    }

    public static void main(String[] args) {
        ViewGroup mockedGroup = createMockGroup();

        Context context = TestUtility.createMockContext();
        context.setView(mockedGroup);
    }
}
