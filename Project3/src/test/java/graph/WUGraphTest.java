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
  int w1, w2; // weights

  @Before
  public void setUp() {
    graph = new WUGraph();
    v1 = 42;
    v2 = 7;
    v3 = "Hello";
    v4 = "Graphs are SO FUN!";
    v5 = "Too many strings";
    w1 = 2;
    w2 = 5;
  }

  @After
  public void tearDown() {
    graph = null;
  }

  @Test
  public void vertexCount_emptyGraph() {
    assertEquals("vertexCount failed on empty graph.", 0, graph.vertexCount());
  }

  @Test
  public void vertexCount_nonEmptyGraph() {
    graph.addVertex(v1);
    assertEquals("vertexCount failed on 1-vertex graph.", 1, graph.vertexCount());
    graph.addVertex(v2);
    assertEquals("vertexCount failed on 2-vertex graph.", 2, graph.vertexCount());
  }

  @Test
  public void vertexCount_afterRemoveVertex() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.removeVertex(v2);
    assertEquals("vertexCount failed on 2-vertex graph.", 1, graph.vertexCount());
    graph.removeVertex(v1);
    assertEquals("vertexCount failed on 1-vertex graph.", 0, graph.vertexCount());
  }

  @Test
  public void vertexCount_afterRemoveEdge() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v2, w1);
    graph.removeEdge(v1, v2);
    assertEquals("vertexCount failed after removing an edge.", 2, graph.vertexCount());
  }

  @Test
  public void edgeCount_emptyGraph() {
    assertEquals("edgeCount failed on empty graph.", 0, graph.edgeCount());
  }

  @Test
  public void edgeCount_nonEmptyGraph() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v2, w1);
    assertEquals("edgeCount failed on 1-edge graph.", 2, graph.edgeCount());
    graph.addEdge(v1, v1, w1);
    assertEquals("edgeCount failed on 2-edge graph (1 self-edge).", 3, graph.edgeCount());
  }

  @Test
  public void edgeCount_afterRemoveVertex() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addVertex(v3);
    graph.addVertex(v4);
    graph.addEdge(v1, v2, w1);
    graph.addEdge(v1, v1, w2);
    graph.addEdge(v1, v3, w2);
    graph.addEdge(v3, v4, w1);
    graph.addEdge(v4, v4, w1);
    graph.removeVertex(v2);
    assertEquals("edgeCount failed after removing vertex with 1 edge.", 6, graph.edgeCount());
    graph.removeVertex(v1);
    assertEquals("edgeCount failed after removing vertex with 2 edges (1 self-edge).", 3,
        graph.edgeCount());
    graph.removeVertex(v3);
    assertEquals("edgeCount failed after removing vertex with 1 edge.", 1, graph.edgeCount());
    graph.removeVertex(v4);
    assertEquals("edgeCount failed after removing vertex with 1 self-edge.", 0, graph.edgeCount());
  }

  @Test
  public void edgeCount_afterRemoveEdge() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v2, w1);
    graph.addEdge(v1, v1, w1);
    graph.removeEdge(v2, v1);
    assertEquals("edgeCount failed after removing 1 edge, 1 self-edge remaining.", 1,
        graph.edgeCount());
    graph.removeEdge(v1, v1);
    assertEquals("edgeCount failed after removing last edge in graph.", 0, graph.edgeCount());
  }

  @Test
  public void getVertices_emptyGraph() {
    assertEquals("getVertices failed on empty graph.", "[]", Arrays.toString(graph.getVertices()));
  }

  @Test
  public void getVertices_nonEmptyGraph() {
    graph.addVertex(v1);
    assertEquals("getVertices failed on 1-vertex graph.", "[42]", Arrays.toString(graph.getVertices()));
    graph.addVertex(v2);
    graph.addVertex(v3);
    assertEquals("getVertices failed on 3-vertex graph.", "[42, 7, Hello]",
        Arrays.toString(graph.getVertices()));
  }

  @Test
  public void getVertices_afterRemoveVertex() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addVertex(v3);
    graph.removeVertex(v2);
    assertEquals("getVertices failed on 3-vertex graph.", "[42, Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(v1);
    assertEquals("getVertices failed on 2-vertex graph.", "[Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(v3);
    assertEquals("getVertices failed on 1-vertex graph.", "[]", Arrays.toString(graph.getVertices()));
  }

  @Test
  public void getVertices_afterRemoveEdge() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addVertex(v3);
    graph.addEdge(v1, v2, w1);
    graph.addEdge(v1, v1, w2);
    graph.addEdge(v1, v3, w2);
    graph.removeEdge(v1, v1);
    assertEquals("getVertices failed after removing an edge.", "[42, 7, Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeEdge(v2, v1);
    assertEquals("getVertices failed after removing an edge.", "[42, 7, Hello]",
        Arrays.toString(graph.getVertices()));
  }

  @Test
  public void addVertex_vertexAlreadyInGraph() {
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
  public void isVertex_notInGraph() {
    assertFalse("isVertex failed on vertex not in graph.", graph.isVertex(v1));
    assertFalse("isVertex failed on vertex not in graph.", graph.isVertex(null));
  }

  @Test
  public void isVertex_normalOperation() {
    graph.addVertex(v1);
    assertTrue("isVertex failed on 1-vertex graph.", graph.isVertex(v1));
    graph.addVertex(v2);
    assertTrue("isVertex failed on 2-vertex graph.", graph.isVertex(v2));
  }

  @Test
  public void isVertex_afterRemoveVertex() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.removeVertex(v2);
    assertFalse("isVertex failed on 2-vertex graph.", graph.isVertex(v2));
    graph.removeVertex(v1);
    assertFalse("isVertex failed on 1-vertex graph.", graph.isVertex(v1));
  }

  @Test
  public void degree_normalOperation() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v2, w1);
    assertEquals("degree failed after adding edge.", 1, graph.degree(v1));
    assertEquals("degree failed after adding edge.", 1, graph.degree(v2));
    graph.addEdge(v2, v2, w1);
    assertEquals("degree failed after adding self-edge.", 1, graph.degree(v1));
    assertEquals("degree failed after adding self-edge.", 2, graph.degree(v2));
  }

  @Test
  public void degree_notInGraph() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    assertEquals("degree failed on vertex with no edges.", 0, graph.degree(v1));
  }

  @Test
  public void degree_afterRemoveVertex() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addVertex(v3);
    graph.addEdge(v1, v2, w1);
    graph.addEdge(v2, v2, w1);
    graph.addEdge(v2, v3, w1);
    graph.removeVertex(v1);
    assertEquals("degree failed after removing vertex.", 2, graph.degree(v2));
    graph.removeVertex(v2);
    assertEquals("degree failed after removing vertex.", 0, graph.degree(v2));
  }

  @Test
  public void degree_afterRemoveEdge() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addVertex(v3);
    graph.addEdge(v1, v2, w1);
    graph.addEdge(v2, v2, w1);
    graph.addEdge(v2, v3, w1);
    graph.removeEdge(v1, v2);
    assertEquals("degree failed after removing edge.", 0, graph.degree(v1));
    assertEquals("degree failed after removing edge.", 2, graph.degree(v2));
    graph.removeEdge(v2, v2);
    assertEquals("degree failed after removing self-edge.", 1, graph.degree(v2));
    assertEquals("degree failed after removing self-edge.", 1, graph.degree(v3));
    graph.removeEdge(v2, v3);
    assertEquals("degree failed after removing edge.", 0, graph.degree(v2));
    assertEquals("degree failed after removing edge.", 0, graph.degree(v3));
  }

  @Test
  public void isVertex_afterRemoveEdge() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v1, w1);
    graph.addEdge(v1, v2, w2);
    graph.removeEdge(v1, v2);
    assertTrue("isVertex failed on 2-vertex graph.", graph.isVertex(v2));
    assertTrue("isVertex failed on 2-vertex graph.", graph.isVertex(v1));
  }

  @Test
  public void removeVertex_vertexNotInGraph() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addVertex(v3);
    graph.removeVertex(v4);
    assertEquals("removeVertex failed on vertex not in graph.", 3, graph.vertexCount());
    assertEquals("removeVertex failed on vertex not in graph.", "[42, 7, Hello]",
        Arrays.toString(graph.getVertices()));
  }

  @Test
  public void removeVertex_normalOperation() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addVertex(v3);
    graph.removeVertex(v1);
    assertEquals("removeVertex failed to remove vertex in 3-vertex graph.", "[7, Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(v1);
    assertEquals("removeVertex failed on already removed vertex.", "[7, Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(v3);
    assertEquals("removeVertex failed to remove vertex in 2-vertex graph.", "[7]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(v2);
    assertEquals("removeVertex failed to remove vertex in 1-vertex graph.", "[]",
        Arrays.toString(graph.getVertices()));
    assertEquals("removeVertex failed to remove vertex in 1-vertex graph.", 0, graph.vertexCount());
  }

  @Test
  public void addEdge_vertexNotInGraph() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v3, v4, w1);
    assertEquals("addEdge failed by adding edge with vertices not in graph.", 0, graph.edgeCount());
    graph.addEdge(v2, v3, w1);
    assertEquals("addEdge failed by adding edge with vertex not in graph.", 0, graph.edgeCount());
    graph.addEdge(v3, v1, w1);
    assertEquals("addEdge failed by adding edge with vertex not in graph.", 0, graph.edgeCount());
  }

  // try to add edge already in graph in order (u, v) and (v, u)
  @Test
  public void addEdge_edgeAlreadyInGraph() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v2, w1);
    graph.addEdge(v1, v2, w1);
    assertTrue("addEdge failed by modifying edge already added.", graph.isEdge(v1, v2));
    assertEquals("addEdge failed by modifying edge already added.", 2, graph.edgeCount());
    graph.addEdge(v2, v1, w1);
    assertEquals("addEdge failed by modifying edge already added.", 2, graph.edgeCount());
    graph.addEdge(v1, v1, w2);
    graph.addEdge(v1, v1, w1);
    assertEquals("addEdge failed by modifying edge already added.", 3, graph.edgeCount());
  }

  @Test
  public void addEdge_updateWeight() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v2, w1);
    assertEquals("addEdge failed to set weight.", w1, graph.weight(v1, v2));
    graph.addEdge(v2, v1, w2);
    assertEquals("addEdge failed to update weight.", w2, graph.weight(v1, v2));
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
  public void removeEdge_notInGraph() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v1, w1);
    graph.removeEdge(v1, v2);
    assertEquals("removeEdge failed on edge not in graph.", 1, graph.edgeCount());
    graph.removeEdge(v3, v4);
    assertEquals("removeEdge failed on edge not in graph.", 1, graph.edgeCount());
  }

  @Test
  public void isEdge_notInGraph() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    assertFalse("isEdge failed on edge not in graph.", graph.isEdge(v1, v2));
  }

  @Test
  public void isEdge_afterRemoveVertex() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v2, w1);
    graph.addEdge(v2, v2, w1);
    graph.removeVertex(v1);
    assertFalse("isEdge failed after removing vertex", graph.isEdge(v1, v2));
    graph.removeVertex(v2);
    assertFalse("isEdge failed after removing vertex", graph.isEdge(v2, v2));
  }

  @Test
  public void isEdge_afterRemoveEdge() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v2, w1);
    graph.addEdge(v2, v2, w1);
    graph.removeEdge(v1, v2);
    assertFalse("isEdge failed after removing edge", graph.isEdge(v1, v2));
    graph.removeEdge(v2, v2);
    assertFalse("isEdge failed after removing edge", graph.isEdge(v2, v2));
  }

  @Test
  public void isEdge_normalOperation() {
    graph.addVertex(v1);
    graph.addVertex(v2);
    graph.addEdge(v1, v2, w1);
    assertTrue("isEdge failed after adding edge.", graph.isEdge(v1, v2));
    assertTrue("isEdge failed with vertex order swapped.", graph.isEdge(v2, v1));
    graph.addEdge(v2, v2, w1);
    assertTrue("isEdge failed after adding self-edge.", graph.isEdge(v2, v2));
  }

  @Test
  public void weight_notInGraph() {
    // TODO: finish up later
  }

}
