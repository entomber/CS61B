package DataStructures.Graph;

import DataStructures.dict.HashTableChained;
import player.IntegerArray;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
    // 2 vertex minimum path length
    DepthFirstPaths dfs = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(moves[0])),
        sg.index(new IntegerArray(moves[1])), new HashTableChained(2), 2);
    assertTrue("Depth-first search found a path.", dfs.hasPath());
    int[] expectedVertices = { 0, 1 };
    int i = 0;
    for (int vertex : dfs.getPath()) {
//      System.out.println(sg.symbol(vertex));
      assertEquals("Path is correct.", new IntegerArray(moves[expectedVertices[i++]]),
          sg.symbol(vertex));
    }
  }

  // 3-vertex connected graph with minimum vertex lengths 2 and 3
  @Test
  public void DepthFirstPaths_TwoEdgePathAdjacencySetup1() {
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
    // 2 vertex minimum path length
    DepthFirstPaths dfs1 = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(moves[0])),
        sg.index(new IntegerArray(moves[1])), new HashTableChained(2), 2);
    assertTrue("Depth-first search found a path.", dfs1.hasPath());
    int[] expectedVertices1 = { 0, 1 };
    int i = 0;
    for (int vertex : dfs1.getPath()) {
      assertEquals("Path is correct.", new IntegerArray(moves[expectedVertices1[i++]]),
          sg.symbol(vertex));
    }

    // 3 vertex minimum path length
    DepthFirstPaths dfs = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(moves[0])),
        sg.index(new IntegerArray(moves[1])), new HashTableChained(2), 3);
    assertTrue("Depth-first search found a path.", dfs.hasPath());
    int[] expectedVertices2 = { 0, 2, 1 };
    i = 0;
    for (int vertex : dfs.getPath()) {
      assertEquals("Path is correct.", new IntegerArray(moves[expectedVertices2[i++]]),
          sg.symbol(vertex));
    }
  }

  // 3-vertex connected graph with minimum vertex length 3 and adjacency list set up different
  // than DepthFirstPaths_TwoEdgePathAdjacencySetup1()
  @Test
  public void DepthFirstPaths_TwoEdgePathAdjacencySetup2() {
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

    // 3 vertex minimum path length
    DepthFirstPaths dfs = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(moves[0])),
        sg.index(new IntegerArray(moves[1])), new HashTableChained(2), 3);
    assertTrue("Depth-first search found a path.", dfs.hasPath());
    int[] expectedVertices2 = { 0, 2, 1 };
    int i = 0;
    for (int vertex : dfs.getPath()) {
//      System.out.println(sg.symbol(vertex));
      assertEquals("Path is correct.", new IntegerArray(moves[expectedVertices2[i++]]),
          sg.symbol(vertex));
    }
  }


  // 3-vertex connected graph with minimum vertex length 4
  @Test
  public void DepthFirstPaths_TwoEdgePathVertexLength1And2() {
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

    // 4 vertex minimum path length
    DepthFirstPaths dfs = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(moves[0])),
        sg.index(new IntegerArray(moves[1])), new HashTableChained(2), 4);
    assertFalse("Depth-first search should not find a path.", dfs.hasPath());
    assertFalse("Path has no vertices.", dfs.getPath().iterator().hasNext());
  }
}
