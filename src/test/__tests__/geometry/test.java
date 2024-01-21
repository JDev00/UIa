package test.__tests__.geometry;

import test.core.*;
import uia.core.shape.Geometry;

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
    public void shouldBePossibleToAddANewPrimerVertexToTheGeometry(TestAssertion testAssertion) {
        testAssertion.assertions(1);

        geometry.addVertex(0f, 0f);
        geometry.addVertex(0f, 0.5f, true);

        testAssertion.expect(geometry.vertices()).toBe(2);
    }

    @Test
    public void aMalformedVertexShouldBeAddedToTheGeometry(TestAssertion testAssertion) {
        testAssertion.assertions(3);

        float xVertex = 10f;
        float yVertex = -1.5f;
        geometry.addVertex(xVertex, yVertex);

        testAssertion.expect(geometry.vertices()).toBe(1);
        float[] expectedVertex = geometry.get(0).toArray();
        testAssertion.expect(expectedVertex).toHaveLength(2);
        testAssertion.expect(expectedVertex).toHaveValues(0.5f, -0.5f);
    }

    @Test
    public void aVertexShouldBeRemovableFromGeometry(TestAssertion testAssertion) {
        testAssertion.assertions(3);

        geometry.addVertex(0f, 0f);
        geometry.addVertex(0.1f, 0.2f);
        geometry.addVertex(0.2f, -0.2f);

        geometry.removeVertex(0);

        testAssertion.expect(geometry.vertices()).toBe(2);
        float[] expectedFirstVertex = geometry.get(0).toArray();
        testAssertion.expect(expectedFirstVertex).toHaveValues(0.1f, 0.2f);
        float[] expectedSecondVertex = geometry.get(1).toArray();
        testAssertion.expect(expectedSecondVertex).toHaveValues(0.2f, -0.2f);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new test());
    }
}
