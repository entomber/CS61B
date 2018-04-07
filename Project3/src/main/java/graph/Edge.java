package graph;

import list.ListNode;

/**
 * Edge represents a weighted edge in a WUGraph.  An edge will be stored
 * in both u and v's adjacency list (except for self-edges which are only
 * stored in the single adjacency list).  The node in the list it is stored
 * in is referenced in first and second.  For a self-edge, second is not
 * used.
 *
 */
class Edge {

  /**
   * u is one vertex
   * v is the other vertex
   * weight is the edge weight
   * first is the ListNode the edge is stored at in u's adjacency list
   * second is the ListNode the edge is stored at in v's adjacency list (for non self-edges)
   */
  protected final Object u;
  protected final Object v;
  protected int weight;
  protected ListNode first;
  protected ListNode second;


  /**
   * Constructs a new Edge object.
   * @param u the first vertex.
   * @param v the second vertex.
   * @param weight the edge weight.
   */
  protected Edge(Object u, Object v, int weight) {
    this.u = u;
    this.v = v;
    this.weight = weight;
    first = null;
    second = null;
  }
}
