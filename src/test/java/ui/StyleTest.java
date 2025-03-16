package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uia.core.ui.style.Style;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StyleTest {
    Style style;

    @BeforeEach
    void beforeEach() {
        style = new Style();
    }

    // happy paths
    @Test
    void aNewAttributeShouldBeAddedToTheStyle() {
        // act
        String attributeName = "subcomponents-background-color";
        int attributeValue = 1;
        style.setAttribute(attributeName, attributeValue);

        // verify
        int result = style.getAttribute(attributeName);
        assertEquals(attributeValue, result);
    }

    @Test
    void anExistingAttributeShouldBeUpdated() {
        // setup
        String attributeName = "empty-attribute";
        int attributeValue = 1;
        style.setAttribute(attributeName, attributeValue);

        // act
        List<Integer> newAttributeValue = new ArrayList<>();
        style.setAttribute(attributeName, newAttributeValue);

        // verify
        Object result = style.getAttribute(attributeName);
        assertEquals(newAttributeValue, result);
    }

    @Test
    void differentAttributesShouldBeAdded() {
        // act
        // adds the first attribute
        String attributeName1 = "attribute-1";
        boolean attributeValue1 = true;
        style.setAttribute(attributeName1, attributeValue1);
        // adds the second attribute
        String attributeName2 = "1-attribute";
        long attributeValue2 = 10L;
        style.setAttribute(attributeName2, attributeValue2);

        // verify
        Object result1 = style.getAttribute(attributeName1);
        Object result2 = style.getAttribute(attributeName2);
        assertEquals(attributeValue1, result1);
        assertEquals(attributeValue2, result2);
    }

    // sad paths
    @Test
    void addingAnInvalidAttributeShouldThrowAnError() {
        // act and verify
        assertThrows(NullPointerException.class, () -> style.setAttribute(null, 1));
        assertThrows(NullPointerException.class, () -> style.setAttribute("attribute-1", null));
    }

    @Test
    void gettingTheValueOfAnInvalidAttributeShouldThrowAnError() {
        // act and verify
        assertThrows(NullPointerException.class, () -> style.getAttribute(null));
        assertThrows(IllegalArgumentException.class, () -> style.getAttribute("attribute-1"));
    }
}
