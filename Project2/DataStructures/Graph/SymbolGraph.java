/* SymbolGraph.java */

/**
 * This Graph client allows clients to define graphs with Integer[] vertex symbols instead of
 * int indices.  It maintains instance variables table (a hash table that maps Integer[]
 * to indices), keys (a hash table that maps indices to Integer[]), and G (a graph, with
 * int vertex indices).
 *
 * To build these data structures:
 *  1. Insert all the vertices using addVertex(), then
 *  2. Connect all the edges between vertices using addEdge()
 *
 * Credit to Algorithms 4th by Sedgewick & Wayne (pgs. 548-552).
 * Modified to use hash tables to map between vertex symbols and int indices.
 */

package DataStructures.Graph;

import DataStructures.dict.Entry;
import DataStructures.dict.HashTableChained;
import player.IntegerArray;

public class SymbolGraph {
  private HashTableChained table; // IntegerArray -> index
  private HashTableChained keys;  // index -> IntegerArray
  private Graph G;
  private int index;

  /**
   * SymbolGraph() constructs a new SymbolGraph.
   */
  public SymbolGraph() {
    table = new HashTableChained();
    keys = new HashTableChained();
    G = null;
  }

  /**
   * addVertex() adds a vertex to this SymbolGraph.  The index assigned to an Integer[] in the
   * underlying Graph is 0 to V-1 for V vertices added.
   *
   * @param v an int array (of a chip position) vertex.
   *
   * Performance: runs in O(1) time.
   */
  public void addVertex(IntegerArray v) {
    if (table.find(v) != null) {
      return;
    }
    table.insert(v, index); // associates IntegerArray with index
    keys.insert(index, v);  // associates index with IntegerArray
    index++;
  }

  /**
   * addEdge() connects the origin vertex v with the destination vertex w (which also contains a
   * direction from v to w).
   *
   * This should be called only after all vertices are added to this SymbolGraph.
   *
   * @param v an int array (of a chip position) vertex.
   * @param w an int array (of another chip position and direction) vertex.
   *
   * Performance: runs in O(1) time.
   */
  public void addEdge(IntegerArray v, IntegerArray w) {
    if (G == null) {
      G = new Graph(table.size());
    }
    int directionVtoW = w.component[2];
    Integer[] temp = { w.component[0], w.component[1] };
    IntegerArray wNoDirection = new IntegerArray(temp);
    // vertices aren't in graph
    if (table.find(v) == null || table.find(wNoDirection) == null) {
      return;
    }
    int u = (int) table.find(v).value(); // get index associated with IntegerArray v's key
    G.addEdge(u, (int) table.find(wNoDirection).value(), directionVtoW); // connect the vertices
  }

  /**
   * contains() returns true if the given vertex is found, false otherwise.
   *
   * @param v an int array (of a chip position) vertex.
   * @return true if the vertex v is found, false otherwise.
   *
   * Performance: runs in O(1) time.
   */
  public boolean contains(IntegerArray v) {
    if (table.find(v) != null) {
      return true;
    }
    return false;
  }

  /**
   * index() returns the index of the given vertex.
   *
   * @param v an int array (of a chip position) vertex.
   * @return the index of the vertex if found, or -1 if not found.
   *
   * Performance: runs in O(1) time.
   */
  public int index(IntegerArray v) {
    Entry entry = table.find(v);
    if (entry != null) {
      return (int) entry.value();
    }
    return -1;
  }

  /**
   * symbol() returns the Integer[] of the given index.
   *
   * @param i the index.
   * @return the key of the vertex if found, or null if not found.
   *
   * Performance: runs in O(1) time.
   */
  public IntegerArray symbol(int i) {
    Entry entry = keys.find(i);
    if (entry == null) {
      return null;
    }
    return (IntegerArray) entry.value();
  }

  /**
   * G() returns the underlying Graph.
   *
   * @return the underlying Graph.
   *
   * Performance: runs in O(1) time.
   */
  public Graph G() {
    if (G == null) {
      G = new Graph(table.size());
    }
    return G;
  }
}
