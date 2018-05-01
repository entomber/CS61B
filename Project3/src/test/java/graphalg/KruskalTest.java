package graphalg;

import graph.WUGraph;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class KruskalTest {
  WUGraph g;
  WUGraph T;

  @Before
  public void setUp() throws Exception {
    g = new WUGraph();
  }

  @Test
  public void minSpanTree_noEdges() {
    g.addVertex(0);
    g.addVertex(1);
    T = Kruskal.minSpanTree(g);
    assertEquals("minSpanTree failed on no edge graph.", 0, T.edgeCount());
    assertTrue("minSpanTree failed on no edge graph.", T.isVertex(0));
    assertTrue("minSpanTree failed on no edge graph.", T.isVertex(1));
  }

  @Test
  public void minSpanTree_oneEdge() {
    g.addVertex(0);
    g.addVertex(1);
    g.addEdge(0, 1, 1);
    T = Kruskal.minSpanTree(g);
    assertTrue("minSpanTree failed on single edge graph", T.isEdge(0, 1));
  }

  @Test
  public void minSpanTree_multipleEdges() {
    g.addVertex(0);
    g.addVertex(1);
    g.addVertex(2);
    g.addVertex(3);
    g.addEdge(0, 1, 1);
    g.addEdge(1, 2, 2);
    g.addEdge(0, 2, 3);
    g.addEdge(2, 3, 5);
    g.addEdge(0, 3, 4);
    T = Kruskal.minSpanTree(g);
    assertTrue("minSpanTree failed on multiple edge graph", T.isEdge(0, 1));
    assertTrue("minSpanTree failed on multiple edge graph", T.isEdge(1, 2));
    assertTrue("minSpanTree failed on multiple edge graph", T.isEdge(0, 3));
    assertTrue("minSpanTree failed on multiple edge graph", !T.isEdge(2, 3));
    assertTrue("minSpanTree failed on multiple edge graph", !T.isEdge(0, 2));
  }

  @Test
  public void minSpanTree_connectedComponents() {
    g.addVertex(0);
    g.addVertex(1);
    g.addVertex(2);
    g.addVertex(3);
    g.addVertex(4);
    g.addVertex(5);
    g.addEdge(0, 1, 1);
    g.addEdge(1, 2, 2);
    g.addEdge(0, 2, 3);
    g.addEdge(2, 3, 5);
    g.addEdge(0, 3, 4);
    g.addEdge(4, 5, 1);
    g.addEdge(1, 1, 1);
    T = Kruskal.minSpanTree(g);
    assertTrue("minSpanTree failed on a connected component graph", T.isEdge(0, 1));
    assertTrue("minSpanTree failed on a connected component graph", T.isEdge(1, 2));
    assertTrue("minSpanTree failed on a connected component graph", T.isEdge(0, 3));
    assertTrue("minSpanTree failed on a connected component graph", T.isEdge(4, 5));
    assertTrue("minSpanTree failed on a connected component graph", !T.isEdge(2, 3));
    assertTrue("minSpanTree failed on a connected component graph", !T.isEdge(0, 2));
    assertTrue("minSpanTree failed on a connected component graph", !T.isEdge(1, 1));
  }
}