package DataStructures.Graph;

import DataStructures.List.List;
import DataStructures.List.DList;

/**
 * This Graph implementation maintains a vertex-indexed array of lists of integer arrays.
 * The index of the list represents the origin vertex, and each integer array represents a
 * destination vertex with a directional identifier from the origin to the destination vertex.
 * The graph is undirected so every edge appears twice: if an edge connects v and w, then w
 * appears in v's list and v appears in w's list.
 *
 * Credit to Algorithms 4th by Sedgewick & Wayne (pgs. 526).
 * Modifications:
 * - use a List ADT instead of Bag ADT
 * - store edges with a direction from the origin vertex to destination vertex.
 */

public class Graph {
  /**
   * V is the number of vertices
   * E is the number of edges
   * adj is the adjacency lists
   */
  private final int V;
  private int E;
  private List<Integer[]>[] adj;

  /**
   * Graph() constructs a new Graph of V vertices.
   *
   * @param V the number of vertices of this Graph.
   */
  public Graph(int V) {
    this.V = V;
    this.E = 0;
    adj = (DList<Integer[]>[])new DList[V];
    for (int v = 0; v < V; v++) {
      adj[v] = new DList<Integer[]>();
    }
  }

  /**
   * V() returns the number of vertices V.
   *
   * @return the number of vertices.
   *
   * Performance: runs in O(1) time.
   */
  public int V() {
    return V;
  }

  /**
   * E() returns the number of edges E.
   *
   * @return the number of edges.
   *
   * Performance: runs in O(1) time.
   */
  public int E() {
    return E;
  }

  /**
   * addEdge() adds an edge to this Graph.
   *
   * @param v the origin vertex.
   * @param w the destination vertex.
   * @param dir the direction from v to w.
   *
   * Performance: runs in O(1) time.
   */
  public void addEdge(int v, int w, int dir) {
    Integer[] vToW = {w, dir};
    Integer[] wToV = {v, -dir};
    adj[v].insertBack(vToW); // add w to v's list
    adj[w].insertBack(wToV); // add v to w's list
    E++;
  }

  /**
   * adj() returns the adjacency list for a given vertex.
   *
   * @param v the vertex
   * @return an Iterable adjacency list
   *
   * Performance: runs in O(1) time.
   */
  public Iterable<Integer[]> adj(int v) {
    return adj[v];
  }
}
