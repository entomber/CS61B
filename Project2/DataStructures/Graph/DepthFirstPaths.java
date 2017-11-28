package DataStructures.Graph;

import DataStructures.Stack.Stack;
import DataStructures.dict.HashTableChained;

/**
 * This Graph client uses depth-first search to find paths to all the vertices in a graph
 * that are connected to a given start vertex s with additional requirements noted below.
 *
 * Credit to Algorithms 4th by Sedgewick & Wayne (pg. 536) and user amit @
 * https://stackoverflow.com/questions/10881014/how-to-find-path-of-exact-length-in-graph
 * Modifications:
 *  - no two adjacent vertices along a path share the same directional element
 *  - paths must adhere to a minimum path length
 *  - certain vertices in the graph are excluded from dfs
 */

public class DepthFirstPaths {
  /**
   * marked is a boolean array that stores whether the vertex has been visited during dfs.
   * d is the destination vertex.
   * path is a Stack of vertices that leads from d to s if pathExists is true.
   * pathExists is true if path exists, and false otherwise.
   * MIN_LENGTH is the minimum required path length.
   * exclusion is a Hash table that contains the set of vertices to ignore.
   * SG is the SymbolGraph associated with the graph.
   */
  private boolean[] marked;
  private final int d;
  private Stack<Integer> path;
  private boolean pathExists;
  private final int MIN_LENGTH;
  private HashTableChained exclusion;
  private SymbolGraph SG;

  /**
   * DepthFirstPaths() constructs a new DepthFirstPaths which finds the paths in G from source s.
   *
   * @param SG the SymbolGraph associated with the graph G.
   * @param G the Graph to operate on.
   * @param s the source (origin) vertex to find paths from.
   * @param d is the destination vertex to find a getPath to.
   * @param exclusion the set of vertices to exclude.
   * @param minimumLength the minimum required path length.
   */
  public DepthFirstPaths(SymbolGraph SG, Graph G, int s, int d,
                         HashTableChained exclusion, int minimumLength) {
    marked = new boolean[G.V()];
    this.d = d;
    path = new Stack<Integer>();
    pathExists = false;
    MIN_LENGTH = minimumLength;
    this.exclusion = exclusion;
    this.SG = SG;
    dfs(G, 1, s, Integer.MIN_VALUE);
  }

  // performs DFS
  private void dfs(Graph G, int length, int v, int prevDirection) {
    marked[v] = true; // mark v as visited
    path.push(v); // add current vertex to the getPath
    for (Integer[] w : G.adj(v)) {
      // check all paths that match condition
      if (!marked[ w[0] ] && prevDirection != w[1] && exclusion.find(SG.symbol(w[0])) == null) {
        dfs(G, length++, w[0], w[1]);
      }
      // successful getPath found
      if (length >= MIN_LENGTH && marked[d]) {
        pathExists = true;
        return;
      }
      // getPath was found, return
      if (pathExists) {
        return;
      }
    }
    // clean up
    marked[v] = false;
    path.pop();
    return;
  }

  /**
   * hasPathTo() returns true if there is a getPath from vertex s to vertex d, false otherwise.
   *
   * @return true if there is a getPath from vertex s to vertex d, false otherwise.
   */
  public boolean hasPath() {
    return pathExists;
  }

  /**
   * pathTo() returns the getPath from vertex s to vertex d.
   *
   * @return an Iterable of Integers which contains the getPath from s to v.
   */
  public Iterable<Integer> getPath() {
    return path;
  }
}
