package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uia.core.rendering.geometry.Geometry;

import static org.junit.jupiter.api.Assertions.*;

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
    void aListOfVerticesShouldBeAddedToTheGeometry() {
        // setup
        geometry.addVertex(0f, 0f);

        // act
        geometry.addVertices(0f, 0.5f, 0.25f, 0.4f);

        // verify
        float[] expectedVertices = {0f, 0f, 0f, 0.5f, 0.25f, 0.4f};
        assertArrayEquals(expectedVertices, geometry.getVertices());
    }

    @Test
    void tryingToAddAnInvalidListOfVertexShouldThrowAnException() {
        // act & verify
        assertThrows(IllegalArgumentException.class, () -> {
            geometry.addVertices(0f);
        });
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

    @Test
    void tryingToRemoveAnInvalidVertexShouldThrowAnException() {
        // setup
        geometry.addVertex(0f, 0f);

        // act & verify
        assertThrows(IndexOutOfBoundsException.class, () -> geometry.removeVertex(1));
    }

    @Test
    void vertexValuesShouldBeChanged() {
        // setup
        geometry.addVertex(0f, 0f);

        // act
        geometry.setVertex(0, 1f, -1f);

        // verify
        float[] expectedVertices = {0.5f, -0.5f};
        assertArrayEquals(expectedVertices, geometry.getVertices());
    }

    @Test
    void tryingToModifyAnInvalidVertexShouldThrowAnException() {
        // act & verify
        assertThrows(IndexOutOfBoundsException.class, () -> geometry.setVertex(1, 0f, 0f));
    }
}
