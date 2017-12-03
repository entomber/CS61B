package DataStructures.Graph;

import DataStructures.dict.HashTableChained;
import player.IntegerArray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DepthFirstPathsTest {

  @Test
  public void DepthFirstPaths_OneEdgePath() {
    SymbolGraph sg = new SymbolGraph();
    Integer[][] moves = { {0,0}, {1,1} };
    for (Integer[] move : moves) {
      sg.addVertex(new IntegerArray(move));
    }
    int direction0To1 = 5;
    Integer[] v0To1 = { moves[1][0], moves[1][1], direction0To1 };
    sg.addEdge(new IntegerArray(moves[0]), new IntegerArray(v0To1)); // connect 0 to 1
    Graph g = sg.G();
    DepthFirstPaths dfs = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(moves[0])),
        sg.index(new IntegerArray(moves[1])), new HashTableChained(2), 2);
//    System.out.println(dfs.hasPath());
    assertTrue("Depth-first search found a getPath.", dfs.hasPath());
    int[] expectedVertices = { 0, 1 };
    int i = 0;
    for (int vertex : dfs.getPath()) {
//      System.out.println(sg.symbol(vertex));
      assertEquals("Path is correct.", new IntegerArray(moves[expectedVertices[i++]]),
          sg.symbol(vertex));
    }
  }

  @Test
  public void DepthFirstPaths_TwoEdgePathAdjSetup1() {
    SymbolGraph sg = new SymbolGraph();
    Integer[][] moves = { {0,0}, {1,1}, {2,2} };
    for (Integer[] move : moves) {
      sg.addVertex(new IntegerArray(move));
    }
    int direction0To1 = 5;
    int direction0To2 = 3;
    int direction1To2 = -2;
    Integer[] v0To1 = { moves[1][0], moves[1][1], direction0To1 };
    Integer[] v0To2 = { moves[2][0], moves[2][1], direction0To2 };
    Integer[] v1To2 = { moves[2][0], moves[2][1], direction1To2 };
    Integer[] v1To0 = { moves[0][0], moves[0][1], -direction0To1 };
    Integer[] v2To0 = { moves[0][0], moves[0][1], -direction0To2 };
    Integer[] v2To1 = { moves[1][0], moves[1][1], -direction1To2 };
    sg.addEdge(new IntegerArray(moves[0]), new IntegerArray(v0To1)); // connect 0 to 1
    sg.addEdge(new IntegerArray(moves[0]), new IntegerArray(v0To2)); // connect 0 to 2
    sg.addEdge(new IntegerArray(moves[1]), new IntegerArray(v1To2)); // connect 1 to 2
    sg.addEdge(new IntegerArray(moves[1]), new IntegerArray(v1To0)); // connect 1 to 0
    sg.addEdge(new IntegerArray(moves[2]), new IntegerArray(v2To0)); // connect 2 to 0
    sg.addEdge(new IntegerArray(moves[2]), new IntegerArray(v2To1)); // connect 2 to 1
    Graph g = sg.G();
    // minimum path length 1
    DepthFirstPaths dfs1 = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(moves[0])),
        sg.index(new IntegerArray(moves[1])), new HashTableChained(2), 2);
//    System.out.println(dfs1.hasPath());
    assertTrue("Depth-first search found a getPath.", dfs1.hasPath());
    int[] expectedVertices1 = { 0, 1 };
    int i = 0;
    for (int vertex : dfs1.getPath()) {
//      System.out.println(sg.symbol(vertex));
      assertEquals("Path is correct.", new IntegerArray(moves[expectedVertices1[i++]]),
          sg.symbol(vertex));
    }

    // minimum path length 2
    DepthFirstPaths dfs = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(moves[0])),
        sg.index(new IntegerArray(moves[1])), new HashTableChained(2), 3);
//    System.out.println(dfs.hasPath());
    assertTrue("Depth-first search found a getPath.", dfs.hasPath());
    int[] expectedVertices2 = { 0, 2, 1 };
    i = 0;
    for (int vertex : dfs.getPath()) {
//      System.out.println(sg.symbol(vertex));
      assertEquals("Path is correct.", new IntegerArray(moves[expectedVertices2[i++]]),
          sg.symbol(vertex));
    }
  }

  @Test
  public void DepthFirstPaths_TwoEdgePathAdjSetup2() {
    SymbolGraph sg = new SymbolGraph();
    Integer[][] moves = { {0,0}, {1,1}, {2,2} };
    for (Integer[] move : moves) {
      sg.addVertex(new IntegerArray(move));
    }
    int direction0To1 = 5;
    int direction0To2 = 3;
    int direction1To2 = -2;
    Integer[] v0To1 = { moves[1][0], moves[1][1], direction0To1 };
    Integer[] v0To2 = { moves[2][0], moves[2][1], direction0To2 };
    Integer[] v1To2 = { moves[2][0], moves[2][1], direction1To2 };
    Integer[] v1To0 = { moves[0][0], moves[0][1], -direction0To1 };
    Integer[] v2To0 = { moves[0][0], moves[0][1], -direction0To2 };
    Integer[] v2To1 = { moves[1][0], moves[1][1], -direction1To2 };
    sg.addEdge(new IntegerArray(moves[0]), new IntegerArray(v0To2)); // connect 0 to 2
    sg.addEdge(new IntegerArray(moves[1]), new IntegerArray(v1To2)); // connect 1 to 2
    sg.addEdge(new IntegerArray(moves[0]), new IntegerArray(v0To1)); // connect 0 to 1
    sg.addEdge(new IntegerArray(moves[2]), new IntegerArray(v2To0)); // connect 2 to 0
    sg.addEdge(new IntegerArray(moves[2]), new IntegerArray(v2To1)); // connect 2 to 1
    sg.addEdge(new IntegerArray(moves[1]), new IntegerArray(v1To0)); // connect 1 to 0
    Graph g = sg.G();

    // minimum path length 1
    DepthFirstPaths dfs1 = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(moves[0])),
        sg.index(new IntegerArray(moves[1])), new HashTableChained(2), 2);
//    System.out.println(dfs1.hasPath());
    assertTrue("Depth-first search found a getPath.", dfs1.hasPath());
    int[] expectedVertices1 = { 0, 1 };
    int i = 0;
    for (int vertex : dfs1.getPath()) {
//      System.out.println(sg.symbol(vertex));
      assertEquals("Path is correct.", new IntegerArray(moves[expectedVertices1[i++]]),
          sg.symbol(vertex));
    }

    // minimum path length 2
    DepthFirstPaths dfs = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(moves[0])),
        sg.index(new IntegerArray(moves[1])), new HashTableChained(2), 3);
//    System.out.println(dfs.hasPath());
    assertTrue("Depth-first search found a getPath.", dfs.hasPath());
    int[] expectedVertices2 = { 0, 2, 1 };
    i = 0;
    for (int vertex : dfs.getPath()) {
//      System.out.println(sg.symbol(vertex));
      assertEquals("Path is correct.", new IntegerArray(moves[expectedVertices2[i++]]),
          sg.symbol(vertex));
    }
  }
}
