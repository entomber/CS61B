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
    Integer[][] moves = { {2,0}, {2,7} };
    for (Integer[] move : moves) {
      sg.addVertex(new IntegerArray(move));
    }
    int dirFirstToSecond = 5;
    Integer[] v1 = { moves[1][0], moves[1][1], dirFirstToSecond };
    sg.addEdge(new IntegerArray(moves[0]), new IntegerArray(v1)); // connect first and second
    Graph g = sg.G();
    DepthFirstPaths dfs = new DepthFirstPaths(
        sg,
        g,
        sg.index(new IntegerArray(moves[0])),
        sg.index(new IntegerArray(moves[1])),
        new HashTableChained(2),
        1);
    System.out.println(dfs.hasPath());
    assertTrue("Depth-first search found a getPath.", dfs.hasPath());
    int i = moves.length-1;
    for (int vertex : dfs.getPath()) {
      System.out.println(sg.symbol(vertex));
      assertEquals("Path is correct.", new IntegerArray(moves[i--]), sg.symbol(vertex));
    }
  }

}
