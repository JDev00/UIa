package systemTracker;

import uia.application.metrics.DefaultSystemProperties;
import uia.application.metrics.monitorable.Monitorable;
import uia.application.metrics.SystemProperty;
import uia.application.metrics.SystemTracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class SystemTrackerTest {
    Monitorable<Integer> customProperty = new SystemProperty<>();
    SystemTracker tracker = SystemTracker.getInstance();
    int propertyID = -1;

    @AfterEach
    void afterEach() {
        try {
            tracker.unregisterProperty(propertyID);
        } catch (Exception ignored) {
            // ignored
        }
    }

    // happy paths

    @Test
    void systemPropertyShouldBeUpdatedAndItsValueAccessed() {
        // act
        int fpsPropertyID = DefaultSystemProperties.FPS.getID();
        int propertyNewValue = 120;
        tracker.updatePropertyValue(fpsPropertyID, propertyNewValue);

        // verify
        assertEquals((Integer) propertyNewValue, tracker.getPropertyValue(fpsPropertyID));
    }

    @Test
    void customPropertyShouldBeUpdatedAndItsValueAccessed() {
        // setup
        propertyID = tracker.registerProperty(customProperty);

        // act
        String propertyNewValue = "hello!";
        tracker.updatePropertyValue(propertyID, propertyNewValue);

        // verify
        assertEquals(propertyNewValue, tracker.getPropertyValue(propertyID));
    }

    // sad paths

    @Test
    void anErrorShouldBeThrownWhenTryingToAccessTheValueOfAnUnregisteredProperty() {
        // act and verify
        int unregisteredPropertyID = -1;
        assertThrows(NoSuchElementException.class, () -> tracker.getPropertyValue(unregisteredPropertyID));
    }

    @Test
    void anErrorShouldBeThrownWhenTryingToRegisterAPropertyTwice() {
        // setup
        propertyID = tracker.registerProperty(customProperty);

        // act and verify
        // registers the property twice
        assertThrows(IllegalArgumentException.class, () -> tracker.registerProperty(customProperty));
    }

    @Test
    void anErrorShouldBeThrownWhenTryingToUnregisterAnUnregisteredProperty() {
        // act and verify
        assertThrows(NoSuchElementException.class, () -> tracker.unregisterProperty(-1));
    }
}
