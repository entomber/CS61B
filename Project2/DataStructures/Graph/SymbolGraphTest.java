package DataStructures.Graph;

import org.junit.Test;
import player.IntegerArray;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SymbolGraphTest {

  @Test
  public void contains_noVertices() {
    SymbolGraph sg = new SymbolGraph();
    Integer[][] moves = { {2,0}, {6,0} };
    assertFalse("Should not find the vertex.", sg.contains(new IntegerArray(moves[0])));
    for (Integer[] move : moves) {
      sg.addVertex(new IntegerArray(move));
    }
    Integer[] m1 = {0,0};
    assertFalse("Should not find the vertex.", sg.contains(new IntegerArray(m1)));
  }

  @Test
  public void contains_verticesAdded() {
    SymbolGraph sg = new SymbolGraph();
    Integer[][] moves = { {2,0}, {6,0} };
    for (Integer[] move : moves) {
      sg.addVertex(new IntegerArray(move));
    }
    assertTrue("Should find the vertex.", sg.contains(new IntegerArray(moves[0])));
    assertTrue("Should find the vertex.", sg.contains(new IntegerArray(moves[1])));
  }

  @Test
  public void addEdge_noEdges() {
    SymbolGraph sg = new SymbolGraph();
    Integer[][] moves = { {2,0}, {6,0} };
    for (Integer[] move : moves) {
      sg.addVertex(new IntegerArray(move));
    }
    Graph g = sg.G();
    for (Integer[] move : moves) {
      assertEquals("Adjacency list should be empty.", "[  ]",
          g.adj(sg.index(new IntegerArray(moves[0]))).toString());
    }
  }


  @Test
  public void addEdge_edgesAdded() {
    SymbolGraph sg = new SymbolGraph();
    Integer[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3} };
    for (Integer[] move : moves) {
      sg.addVertex(new IntegerArray(move));
    }
    int direction0To1 = 1;
    int direction1To2 = -2;
    int direction1To4 = 5;
    Integer[] v0To1 = { moves[1][0], moves[1][1], direction0To1 };
    Integer[] v1To2 = { moves[2][0], moves[2][1], direction1To2 };
    Integer[] v1To4 = { moves[4][0], moves[4][1], direction1To4 };
    Integer[] v1To0 = { moves[0][0], moves[0][1], -direction0To1 };
    Integer[] v2To1 = { moves[1][0], moves[1][1], -direction1To2 };
    Integer[] v4To1 = { moves[1][0], moves[1][1], -direction1To4 };
    sg.addEdge(new IntegerArray(moves[0]), new IntegerArray(v0To1)); // connect 0 to 1
    sg.addEdge(new IntegerArray(moves[1]), new IntegerArray(v1To2)); // connect 1 to 2
    sg.addEdge(new IntegerArray(moves[1]), new IntegerArray(v1To4)); // connect 1 to 4
    sg.addEdge(new IntegerArray(moves[1]), new IntegerArray(v1To0)); // connect 1 to 0
    sg.addEdge(new IntegerArray(moves[2]), new IntegerArray(v2To1)); // connect 2 to 1
    sg.addEdge(new IntegerArray(moves[4]), new IntegerArray(v4To1)); // connect 4 to 1
    Integer[][][] expectedAdj = {
        { moves[1] },
        { moves[2], moves[4], moves[0] },
        { moves[1] },
        { },
        { moves[1] } };
    Integer[][] expectedDir = {
        { direction0To1 },
        { direction1To2, direction1To4, -direction0To1 },
        { -direction1To2 },
        { },
        { -direction1To4 } };
    Graph g = sg.G();
    for (int i = 0; i < moves.length; i++) {
      Iterable<Integer[]> adj = g.adj(sg.index(new IntegerArray(moves[i])));
      int j = 0;
      for (Integer[] edge : adj) {
//        System.out.println(Arrays.toString(expectedAdj[i][j]) + "," + expectedDir[i][j]);
        assertEquals("Adjacency list should match.",
            Arrays.toString(expectedAdj[i][j]) + "," + expectedDir[i][j],
            sg.symbol(edge[0]) + "," + edge[1]);
        j++;
      }
    }
  }

  @Test
  public void index_noVertices() {
    SymbolGraph sg = new SymbolGraph();
    assertEquals("Should not find index for vertex not in graph.", -1,
        sg.index(null));
    Integer[] m1 = {0,0};
    assertEquals("Should not find index for vertex not in graph.", -1,
        sg.index(new IntegerArray(m1)));
  }

  @Test
  public void index_verticesAdded() {
    SymbolGraph sg = new SymbolGraph();
    Integer[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (Integer[] move : moves) {
      sg.addVertex(new IntegerArray(move));
    }
    for (int i = 0; i < moves.length; i++) {
      assertEquals("Should find index for vertex in graph.", i,
          sg.index(new IntegerArray(moves[i])));
    }
  }

  @Test
  public void symbol_noVertices() {
    SymbolGraph sg = new SymbolGraph();
    assertEquals("Should not find symbol for vertex not in graph.", null,
        sg.symbol(0));
  }

  @Test
  public void symbol_verticesAdded() {
    SymbolGraph sg = new SymbolGraph();
    Integer[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (Integer[] move : moves) {
      sg.addVertex(new IntegerArray(move));
    }
    for (int i = 0; i < moves.length; i++) {
      assertEquals("Should find symbol for vertex in graph.", new IntegerArray(moves[i]),
          sg.symbol(i));
    }
  }

}