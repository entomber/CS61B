package DataStructures.Graph;

import DataStructures.List.DList;
import DataStructures.List.InvalidNodeException;
import DataStructures.List.List;
import DataStructures.dict.HashTableChained;

/**
 * This Graph client uses depth-first search (DFS) to find paths of a minimum length between two given vertices.
 *
 * Credit to Algorithms 4th by Sedgewick & Wayne (pg. 536) and user 'Casey Watson' and 'amit' @
 * https://stackoverflow.com/questions/58306/graph-algorithm-to-find-all-connections-between-two-arbitrary-vertices
 * https://stackoverflow.com/questions/10881014/how-to-find-path-of-exact-length-in-graph
 * Modifications:
 *  - no two adjacent vertices along a path share the same directional element
 *  - paths must adhere to a minimum path length
 *  - certain vertices in the graph are excluded from DFS
 */

public class DepthFirstPaths {
  /**
   * MAX_VERTICES is the upper limit of the number of vertices in a path.
   * MIN_VERTICES is the lower limit of the number of vertices in a path.
   * destination is the destination vertex.
   * shortestFound is the number of vertices in a path that's the shortest found.
   * excluded is a Hash table that contains the set of vertices to ignore.
   * SG is the SymbolGraph associated with the graph.
   * G is the Graph.
   * paths is a Hash table that contains the paths from source to destination.
   * visited is a List of visited vertices.
   */

  private final static int MAX_VERTICES = 10;
  private final int MIN_VERTICES;
  private final int destination;
  private int shortestFound;
  private HashTableChained excluded;
  private SymbolGraph SG;
  private Graph G;
  private HashTableChained paths;
  private List<Integer> visited;

  /**
   * DepthFirstPaths() constructs a new DepthFirstPaths which finds the paths in G from source to destination.
   * Only the first occurrence of a path of certain length is saved.  Subsequent paths of same length are ignored.
   *
   * @param SG the SymbolGraph associated with the graph G.
   * @param G the Graph to operate on.
   * @param source the source (origin) vertex to find paths from.
   * @param destination is the destination vertex to find a getPath to.
   * @param excluded the set of vertices to exclude.
   * @param minVertices the minimum required number of vertices in the path.
   *
   */

  public DepthFirstPaths(SymbolGraph SG, Graph G, int source, int destination,
                         HashTableChained excluded, int minVertices) {
    this.destination = destination;
    MIN_VERTICES = minVertices;
    this.excluded = excluded;
    this.SG = SG;
    this.G = G;
    paths = new HashTableChained(MAX_VERTICES);
    shortestFound = Integer.MAX_VALUE;
    visited = new DList<Integer>();

    visited.insertBack(source);
    dfs(source, Integer.MIN_VALUE);
  }

  // performs DFS
  private void dfs(int v, int prevDirection) {
    int vertexCount = visited.length();
    // destination found, path length of at least MIN_LENGTH and less than shortestFound
    if (v == destination && (vertexCount >= MIN_VERTICES && vertexCount < shortestFound)) {
      shortestFound = vertexCount;
      List<Integer> path = new DList<Integer>();
      paths.insert(visited.length(), path);
      for (Integer i : visited) {
        path.insertBack(i);
      }
      return;
    }
    // check all vertices in vertex v's adjacency list
    for (Integer[] vertexAndDirection : G.adj(v)) {
      int vertex = vertexAndDirection[0];
      int currentDirection = vertexAndDirection[1];
      // perform DFS on vertex if: 1. not visited, 2. direction changed, 3. not excluded
      if (!visited.contains(vertex) && !isExcluded(vertex) && prevDirection != currentDirection) {
        visited.insertBack(vertex);
        dfs(vertex, currentDirection);
        try {
          visited.back().remove();
        } catch (InvalidNodeException e) {
          e.printStackTrace();
        }
      }
    }
  }

  // returns true if vertex is in excluded hash table, false otherwise
  private boolean isExcluded(int vertex) {
    return excluded.find(SG.symbol(vertex)) != null;
  }

  /**
   * hasPath() returns true if there is a path from source to destination, false otherwise.
   *
   * @return true if there is a path from source to destination, false otherwise.
   */
  public boolean hasPath() {
    for (int i = MIN_VERTICES; i <= MAX_VERTICES; i++) {
      if (paths.find(i) != null) {
        return true;
      }
    }
    return false;
  }

  /**
   * getPath() returns the path from source to destination, or an empty one if there is none.
   *
   * @return an Iterable of Integers which contains the path from source to destination.
   */
  public Iterable<Integer> getPath() {
    for (int i = MIN_VERTICES; i <= MAX_VERTICES; i++) {
      if (paths.find(i) != null) {
        return (List<Integer>) paths.find(i).value();
      }
    }
    return new DList<Integer>();
  }
}
