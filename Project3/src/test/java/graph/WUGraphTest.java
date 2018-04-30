package graph;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class WUGraphTest {
  private WUGraph graph;
  private final static int V_1 = 42;
  private final static int V_2 = 7;
  private final static String V_3 = "Hello";
  private final static String V_4 = "Graphs are SO FUN!";
  private final static int W_1 = 2;
  private final static int W_2 = 5;

  @Before
  public void setUp() {
    graph = new WUGraph();
  }

  @Test
  public void vertexCount_emptyGraph() {
    assertEquals("vertexCount failed on empty graph.", 0, graph.vertexCount());
  }

  @Test
  public void vertexCount_nonEmptyGraph() {
    graph.addVertex(V_1);
    assertEquals("vertexCount failed on 1-vertex graph.", 1, graph.vertexCount());
    graph.addVertex(V_2);
    assertEquals("vertexCount failed on 2-vertex graph.", 2, graph.vertexCount());
  }

  @Test
  public void vertexCount_afterRemoveVertex() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.removeVertex(V_2);
    assertEquals("vertexCount failed on 2-vertex graph.", 1, graph.vertexCount());
    graph.removeVertex(V_1);
    assertEquals("vertexCount failed on 1-vertex graph.", 0, graph.vertexCount());
  }

  @Test
  public void vertexCount_afterRemoveEdge() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    graph.removeEdge(V_1, V_2);
    assertEquals("vertexCount failed after removing an edge.", 2, graph.vertexCount());
  }

  @Test
  public void edgeCount_emptyGraph() {
    assertEquals("edgeCount failed on empty graph.", 0, graph.edgeCount());
  }

  @Test
  public void edgeCount_nonEmptyGraph() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    assertEquals("edgeCount failed on 1-edge graph.", 1, graph.edgeCount());
    graph.addEdge(V_1, V_1, W_1);
    assertEquals("edgeCount failed on 2-edge graph.", 2, graph.edgeCount());
  }

  @Test
  public void edgeCount_afterRemoveVertex() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addVertex(V_3);
    graph.addVertex(V_4);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_1, V_1, W_2);
    graph.addEdge(V_1, V_3, W_2);
    graph.addEdge(V_3, V_4, W_1);
    graph.addEdge(V_4, V_4, W_1);
    graph.removeVertex(V_2);
    assertEquals("edgeCount failed after removing vertex with 1 edge.", 4, graph.edgeCount());
    graph.removeVertex(V_1);
    assertEquals("edgeCount failed after removing vertex with 2 edges.", 2,
        graph.edgeCount());
    graph.removeVertex(V_3);
    assertEquals("edgeCount failed after removing vertex with 1 edge.", 1, graph.edgeCount());
    graph.removeVertex(V_4);
    assertEquals("edgeCount failed after removing vertex with 1 edge.", 0, graph.edgeCount());
  }

  @Test
  public void edgeCount_afterRemoveEdge() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_1, V_1, W_1);
    graph.removeEdge(V_1, V_2);
    assertEquals("edgeCount failed after removing 1 edge, 1 edge remaining.", 1, graph.edgeCount());
    graph.removeEdge(V_1, V_1);
    assertEquals("edgeCount failed after removing last edge in graph.", 0, graph.edgeCount());
  }

  @Test
  public void getVertices_emptyGraph() {
    assertEquals("getVertices failed on empty graph.", "[]", Arrays.toString(graph.getVertices()));
  }

  @Test
  public void getVertices_nonEmptyGraph() {
    graph.addVertex(V_1);
    assertEquals("getVertices failed on 1-vertex graph.", "[42]", Arrays.toString(graph.getVertices()));
    graph.addVertex(V_2);
    graph.addVertex(V_3);
    assertEquals("getVertices failed on 3-vertex graph.", "[42, 7, Hello]",
        Arrays.toString(graph.getVertices()));
  }

  @Test
  public void getVertices_afterRemoveVertex() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addVertex(V_3);
    graph.removeVertex(V_2);
    assertEquals("getVertices failed on 3-vertex graph.", "[42, Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(V_1);
    assertEquals("getVertices failed on 2-vertex graph.", "[Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(V_3);
    assertEquals("getVertices failed on 1-vertex graph.", "[]", Arrays.toString(graph.getVertices()));
  }

  @Test
  public void getVertices_afterRemoveEdge() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addVertex(V_3);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_1, V_1, W_2);
    graph.addEdge(V_1, V_3, W_2);
    graph.removeEdge(V_1, V_1);
    assertEquals("getVertices failed after removing an edge.", "[42, 7, Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeEdge(V_1, V_2);
    assertEquals("getVertices failed after removing an edge.", "[42, 7, Hello]",
        Arrays.toString(graph.getVertices()));
  }

  @Test
  public void addVertex_vertexAlreadyInGraph() {
    graph.addVertex(V_1);
    assertEquals("addVertex failed on empty graph.", 1, graph.vertexCount());
    assertEquals("getVertices failed on 1-vertex graph.", "[42]",
        Arrays.toString(graph.getVertices()));

    graph.addVertex(V_1);
    assertEquals("addVertex failed trying to add same object vertex.", 1, graph.vertexCount());
    assertEquals("getVertices failed trying to add same object vertex.", "[42]",
        Arrays.toString(graph.getVertices()));
  }

  @Test
  public void isVertex_notInGraph() {
    assertFalse("isVertex failed on vertex not in graph.", graph.isVertex(V_1));
    assertFalse("isVertex failed on vertex not in graph.", graph.isVertex(null));
  }

  @Test
  public void isVertex_normalOperation() {
    graph.addVertex(V_1);
    assertTrue("isVertex failed on 1-vertex graph.", graph.isVertex(V_1));
    graph.addVertex(V_2);
    assertTrue("isVertex failed on 2-vertex graph.", graph.isVertex(V_2));
  }

  @Test
  public void isVertex_afterRemoveVertex() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.removeVertex(V_2);
    assertFalse("isVertex failed on 2-vertex graph.", graph.isVertex(V_2));
    graph.removeVertex(V_1);
    assertFalse("isVertex failed on 1-vertex graph.", graph.isVertex(V_1));
  }

  @Test
  public void degree_normalOperation() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    assertEquals("degree failed after adding edge.", 1, graph.degree(V_1));
    assertEquals("degree failed after adding edge.", 1, graph.degree(V_2));
    graph.addEdge(V_2, V_2, W_1);
    assertEquals("degree failed after adding edge.", 1, graph.degree(V_1));
    assertEquals("degree failed after adding edge.", 2, graph.degree(V_2));
  }

  @Test
  public void degree_notInGraph() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    assertEquals("degree failed on vertex with no edges.", 0, graph.degree(V_1));
  }

  @Test
  public void degree_afterRemoveVertex() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addVertex(V_3);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_2, V_2, W_1);
    graph.addEdge(V_2, V_3, W_1);
    graph.removeVertex(V_1);
    assertEquals("degree failed after removing vertex.", 2, graph.degree(V_2));
    graph.removeVertex(V_2);
    assertEquals("degree failed after removing vertex.", 0, graph.degree(V_2));
  }

  @Test
  public void degree_afterRemoveEdge() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addVertex(V_3);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_2, V_2, W_1);
    graph.addEdge(V_2, V_3, W_1);
    graph.removeEdge(V_1, V_2);
    assertEquals("degree failed after removing edge.", 0, graph.degree(V_1));
    assertEquals("degree failed after removing edge.", 2, graph.degree(V_2));
    graph.removeEdge(V_2, V_2);
    assertEquals("degree failed after removing edge.", 1, graph.degree(V_2));
    assertEquals("degree failed after removing edge.", 1, graph.degree(V_3));
    graph.removeEdge(V_2, V_3);
    assertEquals("degree failed after removing edge.", 0, graph.degree(V_2));
    assertEquals("degree failed after removing edge.", 0, graph.degree(V_3));
  }

  @Test
  public void getNeighbors_notInGraph() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    assertNull("getNeighbors failed on vertex with no edges.", graph.getNeighbors(V_1));
    assertNull("getNeighbors failed on vertex not in graph.", graph.getNeighbors(V_3));
  }

  @Test
  public void getNeighbors_normalOperation() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_2, V_2, W_2);
    assertEquals("getNeighbors neighborList failed on vertex with 1 edge.", "[7]",
        Arrays.toString(graph.getNeighbors(V_1).neighborList));
    assertEquals("getNeighbors weightList failed on vertex with 1 edge.", "[2]",
        Arrays.toString(graph.getNeighbors(V_1).weightList));
    assertEquals("getNeighbors neighborList failed on vertex with 2 edges.", "[42, 7]",
        Arrays.toString(graph.getNeighbors(V_2).neighborList));
    assertEquals("getNeighbors weightList failed on vertex with 2 edges.", "[2, 5]",
        Arrays.toString(graph.getNeighbors(V_2).weightList));
  }

  @Test
  public void getNeighbors_afterRemoveVertex() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addVertex(V_3);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_2, V_2, W_2);
    graph.addEdge(V_2, V_3, W_1);
    graph.removeVertex(V_1);
    assertEquals("getNeighbors neighborList failed on vertex with 2 edges.", "[7, Hello]",
        Arrays.toString(graph.getNeighbors(V_2).neighborList));
    assertEquals("getNeighbors weightList failed on vertex with 2 edges.", "[5, 2]",
        Arrays.toString(graph.getNeighbors(V_2).weightList));
    graph.removeVertex(V_3);
    assertEquals("getNeighbors neighborList failed on vertex with 1 edge.", "[7]",
        Arrays.toString(graph.getNeighbors(V_2).neighborList));
    assertEquals("getNeighbors weightList failed on vertex with 1 edge.", "[5]",
        Arrays.toString(graph.getNeighbors(V_2).weightList));
  }

  @Test
  public void getNeighbors_afterRemoveEdge() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_2, V_2, W_2);
    graph.removeEdge(V_1, V_2);
    assertEquals("getNeighbors neighborList failed on vertex with 1 edge.", "[7]",
        Arrays.toString(graph.getNeighbors(V_2).neighborList));
    assertEquals("getNeighbors weightList failed on vertex with 1 edge.", "[5]",
        Arrays.toString(graph.getNeighbors(V_2).weightList));
    graph.removeEdge(V_2, V_2);
    assertNull("getNeighbors failed on vertex with no edges.", graph.getNeighbors(V_2));
  }

  @Test
  public void isVertex_afterRemoveEdge() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_1, W_1);
    graph.addEdge(V_1, V_2, W_2);
    graph.removeEdge(V_1, V_2);
    assertTrue("isVertex failed on 2-vertex graph.", graph.isVertex(V_2));
    assertTrue("isVertex failed on 2-vertex graph.", graph.isVertex(V_1));
  }

  @Test
  public void removeVertex_vertexNotInGraph() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addVertex(V_3);
    graph.removeVertex(V_4);
    assertEquals("removeVertex failed on vertex not in graph.", 3, graph.vertexCount());
    assertEquals("removeVertex failed on vertex not in graph.", "[42, 7, Hello]",
        Arrays.toString(graph.getVertices()));
  }

  @Test
  public void removeVertex_normalOperation() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addVertex(V_3);
    graph.removeVertex(V_1);
    assertEquals("removeVertex failed to remove vertex in 3-vertex graph.", "[7, Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(V_1);
    assertEquals("removeVertex failed on already removed vertex.", "[7, Hello]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(V_3);
    assertEquals("removeVertex failed to remove vertex in 2-vertex graph.", "[7]",
        Arrays.toString(graph.getVertices()));
    graph.removeVertex(V_2);
    assertEquals("removeVertex failed to remove vertex in 1-vertex graph.", "[]",
        Arrays.toString(graph.getVertices()));
    assertEquals("removeVertex failed to remove vertex in 1-vertex graph.", 0, graph.vertexCount());
  }

  @Test
  public void addEdge_vertexNotInGraph() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_3, V_4, W_1);
    assertEquals("addEdge failed by adding edge with vertices not in graph.", 0, graph.edgeCount());
    graph.addEdge(V_2, V_3, W_1);
    assertEquals("addEdge failed by adding edge with vertex not in graph.", 0, graph.edgeCount());
    graph.addEdge(V_3, V_1, W_1);
    assertEquals("addEdge failed by adding edge with vertex not in graph.", 0, graph.edgeCount());
  }

  // try to add edge already in graph in order (u, v) and (v, u)
  @Test
  public void addEdge_edgeAlreadyInGraph() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_1, V_2, W_1);
    assertTrue("addEdge failed by modifying edge already added.", graph.isEdge(V_1, V_2));
    assertEquals("addEdge failed by modifying edge already added.", 1, graph.edgeCount());
    graph.addEdge(V_2, V_1, W_1);
    assertEquals("addEdge failed by modifying edge already added.", 1, graph.edgeCount());
    graph.addEdge(V_1, V_1, W_2);
    graph.addEdge(V_1, V_1, W_1);
    assertEquals("addEdge failed by modifying edge already added.", 2, graph.edgeCount());
  }

  @Test
  public void addEdge_updateWeight() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    assertEquals("addEdge failed to set weight on edge.", W_1, graph.weight(V_1, V_2));
    graph.addEdge(V_2, V_1, W_2);
    assertEquals("addEdge failed to update weight on edge.", W_2, graph.weight(V_1, V_2));
    graph.addEdge(V_2, V_2, W_1);
    assertEquals("addEdge failed to set weight on self-edge.", W_1, graph.weight(V_2, V_2));
    graph.addEdge(V_2, V_2, W_2);
    assertEquals("addEge failed to update weight on self-edge.", W_2, graph.weight(V_2, V_2));
  }

  @Test
  public void addEdge_normalOperation() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addVertex(V_3);
    graph.addEdge(V_1, V_2, 3);
    assertTrue("isEdge failed on (V_1, V_2) which is an edge.", graph.isEdge(V_1, V_2));
    assertTrue("isEdge failed on (V_2, V_1) which is an edge.", graph.isEdge(V_2, V_1));
    assertFalse("isEdge failed on (V_1, V_3) which is not an edge.", graph.isEdge(V_1, V_3));
    assertFalse("isEdge failed on (V_3, V_1) which is not an edge.", graph.isEdge(V_3, V_1));
  }

  @Test
  public void removeEdge_notInGraph() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_1, W_1);
    graph.removeEdge(V_1, V_2);
    assertEquals("removeEdge failed on edge not in graph.", 1, graph.edgeCount());
    graph.removeEdge(V_3, V_4);
    assertEquals("removeEdge failed on edge not in graph.", 1, graph.edgeCount());
  }

  // try to remove edge in order (u, v) and (v, u)
  @Test
  public void removeEdge_normalOperation() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_2, V_2, W_1);
    graph.removeEdge(V_1, V_2);
    assertFalse("removeEdge failed to remove edge.", graph.isEdge(V_1, V_2));
    graph.addEdge(V_1, V_2, W_1);
    graph.removeEdge(V_2, V_1);
    assertFalse("removeEdge failed to remove edge.", graph.isEdge(V_2, V_1));
  }

  @Test
  public void isEdge_notInGraph() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    assertFalse("isEdge failed on edge not in graph.", graph.isEdge(V_1, V_2));
  }

  @Test
  public void isEdge_afterRemoveVertex() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_2, V_2, W_1);
    graph.removeVertex(V_1);
    assertFalse("isEdge failed after removing vertex", graph.isEdge(V_1, V_2));
    graph.removeVertex(V_2);
    assertFalse("isEdge failed after removing vertex", graph.isEdge(V_2, V_2));
  }

  @Test
  public void isEdge_afterRemoveEdge() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    graph.addEdge(V_2, V_2, W_1);
    graph.removeEdge(V_1, V_2);
    assertFalse("isEdge failed after removing edge", graph.isEdge(V_1, V_2));
    graph.removeEdge(V_2, V_2);
    assertFalse("isEdge failed after removing self-edge", graph.isEdge(V_2, V_2));
  }

  @Test
  public void isEdge_normalOperation() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    assertTrue("isEdge failed after adding edge.", graph.isEdge(V_1, V_2));
    assertTrue("isEdge failed with vertex order swapped.", graph.isEdge(V_2, V_1));
    graph.addEdge(V_2, V_2, W_1);
    assertTrue("isEdge failed after adding self-edge.", graph.isEdge(V_2, V_2));
  }

  @Test
  public void weight_notInGraph() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    assertEquals("weight failed on edge not in graph.", 0, graph.weight(V_1, V_1));
  }

  @Test
  public void weight_normalOperation() {
    graph.addVertex(V_1);
    graph.addVertex(V_2);
    graph.addEdge(V_1, V_2, W_1);
    assertEquals("weight failed on edge in graph.", W_1, graph.weight(V_1, V_2));
    graph.addEdge(V_2, V_1, 0);
    assertEquals("weight failed after changing weight on edge in graph.", 0, graph.weight(V_1, V_2));
  }
}
