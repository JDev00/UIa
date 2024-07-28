import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uia.core.rendering.geometry.Geometry;

/**
 * Geometry unit tests.
 */

class GeometryTest {
    Geometry geometry = new Geometry();

    @BeforeEach
    public void beforeEachTest() {
        geometry.removeAllVertices();
    }

    @Test
    void aVertexShouldBeAddedToTheGeometry() {
        // act
        float xVertex = 0f;
        float yVertex = 0.5f;
        geometry.addVertex(xVertex, yVertex);

        // verify
        int expectedVertices = 1;
        assertEquals(expectedVertices, geometry.vertices());
    }

    @Test
    void aMalformedVertexShouldBeAddedToTheGeometry() {
        // act
        float xVertex = 10f;
        float yVertex = -1.5f;
        geometry.addVertex(xVertex, yVertex);

        // verify
        int expectedVertices = 1;
        assertEquals(geometry.vertices(), expectedVertices);
        float[] vertex = {geometry.getX(0), geometry.getY(0)};
        assertEquals(0.5f, vertex[0]);
        assertEquals(-0.5f, vertex[1]);
    }

    @Test
    void aVertexShouldBeRemovedFromGeometry() {
        // setup
        geometry.addVertex(0f, 0f);
        geometry.addVertex(0.1f, 0.2f);
        geometry.addVertex(0.2f, -0.2f);
        geometry.addVertex(0.5f, -0.5f);

        // act
        geometry.removeVertex(0);
        geometry.removeVertex(1);

        // verify
        int expectedVerticesNumber = 2;
        assertEquals(expectedVerticesNumber, geometry.vertices());
        float[] expectedVertices = {0.1f, 0.2f, 0.5f, -0.5f};
        assertArrayEquals(expectedVertices, geometry.getVertices());
    }
}
