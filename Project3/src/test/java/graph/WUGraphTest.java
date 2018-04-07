package graph;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class WUGraphTest {
  private WUGraph graph;
  int v1, v2;
  String v3, v4, v5; // vertices
  int w1; // weights

  @Before
  public void setUp() {
    graph = new WUGraph();
    v1 = 42;
    v2 = 7;
    v3 = "Hello";
    v4 = "Graphs are SO FUN!";
    v5 = "Too many strings";
    w1 = 2;
  }

  @After
  public void tearDown() {
    graph = null;
  }

  @Test
  public void vertexCount() {
    // test vertexCount after addVertex
    assertEquals("vertexCount failed on empty graph.", 0, graph.vertexCount());
    graph.addVertex(v1);
    assertEquals("vertexCount failed on 1-vertex graph.", 1, graph.vertexCount());
    graph.addVertex(v2);
    assertEquals("vertexCount failed on 2-vertex graph.", 2, graph.vertexCount());

    // test vertexCount after removeVertex
    graph.removeVertex(v2);
    assertEquals("vertexCount failed on 2-vertex graph.", 1, graph.vertexCount());
    graph.removeVertex(v1);
    assertEquals("vertexCount failed on 1-vertex graph.", 0, graph.vertexCount());

    // TODO: test vertexCount after removeEdge
  }

  @Test
  public void edgeCount() {
    // test edgeCount after addEdge
    assertEquals("edgeCount failed on empty graph.", 0, graph.edgeCount());
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v2, w1);
    assertEquals("edgeCount failed on 1-edge graph.", 2, graph.edgeCount());
    graph.addEdge(v1, v1, w1);
    assertEquals("edgeCount failed on 2-edge graph (1 self-edge).", 3, graph.edgeCount());

    // test edgeCount after removeVertex
    graph.removeEdge(v2, v1);
    assertEquals("edgeCount failed after removing 1 edge, 1 self-edge remaining.", 1,
        graph.edgeCount());
    graph.removeEdge(v1, v1);
    assertEquals("edgeCount failed after removing last edge in graph.", 0, graph.edgeCount());

    // TODO: test edgeCount after removeEdge
  }

  @Test
  public void getVertices() {
    // test getVertices after addVertex
    assertEquals("getVertices failed on empty graph.", "[]", Arrays.toString(graph.getVertices()));
    graph.addVertex(v1);
    assertEquals("getVertices failed on 1-vertex graph.", "[42]", Arrays.toString(graph.getVertices()));
    graph.addVertex(v2);
    graph.addVertex(v3);
    assertEquals("getVertices failed on 3-vertex graph.", "[42, 7, Hello]",
        Arrays.toString(graph.getVertices()));

    // test getVertices after removeVertex
    graph.removeVertex(v2);
    assertEquals("getVertices failed on 3-vertex graph.", "[42, Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(v1);
    assertEquals("getVertices failed on 2-vertex graph.", "[Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(v3);
    assertEquals("getVertices failed on 1-vertex graph.", "[]", Arrays.toString(graph.getVertices()));

    // TODO: test getVertices after removeEdge
  }

  @Test
  public void addVertex_sameObject() {
    graph.addVertex(v1);
    assertEquals("addVertex failed on empty graph.", 1, graph.vertexCount());
    assertEquals("getVertices failed on 1-vertex graph.", "[42]",
        Arrays.toString(graph.getVertices()));

    graph.addVertex(v1);
    assertEquals("addVertex failed trying to add same object vertex.", 1, graph.vertexCount());
    assertEquals("getVertices failed trying to add same object vertex.", "[42]",
        Arrays.toString(graph.getVertices()));
  }

  @Test
  public void isVertex() {
    // test isVertex after addVertex
    assertFalse("isVertex failed on empty graph.", graph.isVertex(v1));
    assertFalse("isVertex failed on empty graph.", graph.isVertex(null));
    graph.addVertex(v1);
    assertTrue("isVertex failed on 1-vertex graph.", graph.isVertex(v1));
    graph.addVertex(v2);
    assertTrue("isVertex failed on 2-vertex graph.", graph.isVertex(v2));
    assertFalse("isVertex failed on vertex not in graph.", graph.isVertex(v3));

    // test isVertex after removeVertex
    graph.removeVertex(v2);
    assertFalse("isVertex failed on 2-vertex graph.", graph.isVertex(v2));
    graph.removeVertex(v1);
    assertFalse("isVertex failed on 1-vertex graph.", graph.isVertex(v1));

    // TODO: test isVertex after removeEdge
  }

  @Test
  public void removeVertex() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addVertex(v3);
    graph.addVertex(v4);
    graph.removeVertex(v5);
    assertEquals("removeVertex failed on vertex not in graph.", 4, graph.vertexCount());
    assertEquals("removeVertex failed on vertex not in graph.", "[42, 7, Hello, Graphs are SO FUN!]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(v1);
    assertEquals("removeVertex failed to remove vertex in 4-vertex graph.",
        "[7, Hello, Graphs are SO FUN!]", Arrays.toString(graph.getVertices()));
    graph.removeVertex(v1);
    assertEquals("removeVertex failed on already removed vertex.", "[7, Hello, Graphs are SO FUN!]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(v3);
    assertEquals("removeVertex failed to remove vertex in 3-vertex graph.", "[7, Graphs are SO FUN!]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(v4);
    assertEquals("removeVertex failed to remove vertex in 2-vertex graph.", "[7]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(v2);
    assertEquals("removeVertex failed to remove vertex in 1-vertex graph.", "[]",
        Arrays.toString(graph.getVertices()));
    assertEquals("removeVertex failed to remove vertex in 1-vertex graph.", 0, graph.vertexCount());
  }

  @Test
  public void addEdge_normalOperation() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addVertex(v3);
    graph.addEdge(v1, v2, 3);
    assertTrue("isEdge failed on (v1, v2) which is an edge.", graph.isEdge(v1, v2));
    assertTrue("isEdge failed on (v2, v1) which is an edge.", graph.isEdge(v2, v1));
    assertFalse("isEdge failed on (v1, v3) which is not an edge.", graph.isEdge(v1, v3));
    assertFalse("isEdge failed on (v3, v1) which is not an edge.", graph.isEdge(v3, v1));
  }

  @Test
  public void addEdge_changeWeight() {

  }

  @Test
  public void isEdge_notInGraph() {
    int v1 = 42;
    int v2 = 7;
    assertFalse("isEdge failed on empty graph.", graph.isEdge(v1, v2));
    assertFalse("isEdge failed on empty graph.", graph.isEdge(v2, v1));
    assertFalse("isEdge failed on empty graph.", graph.isEdge(v1, v1));
  }

  @Test
  public void isEdge_notAnEdge() {
    int v1 = 42;
    int v2 = 7;
    graph.addVertex(v1);
    graph.addVertex(v2);
    assertFalse("isEdge failed on (v1, v2) which is not an edge.", graph.isEdge(v1, v2));
    assertFalse("isEdge failed on (v1, v2) which is not an edge.", graph.isEdge(v2, v1));
    assertFalse("isEdge failed on (v1, v1) which is not an edge.", graph.isEdge(v1, v1));
  }

  @Test
  public void removeEdge_normalOperation() {
    int v1 = 42;
    int v2 = 7;
    int v3 = 5;
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addVertex(v3);
    graph.addEdge(v1, v2, 3);
    graph.removeEdge(v1, v2);
    assertFalse("removeEdge failed on (v1, v2) which is an edge.", graph.isEdge(v1, v2));
    graph.addEdge(v1, v2, 3);
    assertTrue("addEdge failed on (v1, v2) which is an edge.", graph.isEdge(v1, v2));
  }
}
