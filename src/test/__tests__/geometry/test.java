package test.__tests__.geometry;

import uia.core.ui.primitives.shape.Geometry;

import test.core.*;

/**
 * Unit tests
 */

public class test {
    Geometry geometry = new Geometry();

    @BeforeEachTest
    public void beforeEachTest() {
        geometry.removeAllVertices();
    }

    @Test
    public void aVertexShouldBeAddedToTheGeometry(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        float xVertex = 0f;
        float yVertex = 0.5f;
        geometry.addVertex(xVertex, yVertex);

        testAssertion.expect(geometry.vertices()).toBe(1);
    }

    @Test
    public void aMalformedVertexShouldBeAddedToTheGeometry(TestAssertion testAssertion) {
        testAssertion.assertions(2);

        // setup
        float xVertex = 10f;
        float yVertex = -1.5f;
        geometry.addVertex(xVertex, yVertex);

        // verify
        testAssertion.expect(geometry.vertices()).toBe(1);
        float[] expectedVertex = {geometry.getX(0), geometry.getY(0)};
        testAssertion.expect(expectedVertex).toHaveValues(0.5f, -0.5f);
    }

    @Test
    public void aVertexShouldBeRemovedFromGeometry(TestAssertion testAssertion) {
        testAssertion.assertions(2);

        // setup
        geometry.addVertex(0f, 0f);
        geometry.addVertex(0.1f, 0.2f);
        geometry.addVertex(0.2f, -0.2f);
        geometry.addVertex(0.5f, -0.5f);

        // act
        geometry.removeVertex(0);
        geometry.removeVertex(1);

        // verify
        testAssertion.expect(geometry.vertices()).toBe(2);
        testAssertion.expect(geometry.toArray()).toHaveValues(
                0.1f, 0.2f,
                0.5f, -0.5f
        );
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new test());
    }
}
