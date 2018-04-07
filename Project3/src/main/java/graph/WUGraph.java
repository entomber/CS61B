/* WUGraph.java */

package graph;

import dict.Dictionary;
import dict.HashTableChained;
import list.DList;
import list.InvalidNodeException;
import list.List;
import list.ListNode;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {

  /**
   * vertexCount stores the number of vertices
   * edgeCount stores the number of edges
   * vertices is an adjacency list that stores lists of edges incident to a vertex
   * vertexToAdjList maps a vertex Object to its adjacency list
   * vertexPairToEdge maps an adjacency list to its vertex Object
   * key maps an index to an Object in the adjacency list
   */
  private int vertexCount;
  private int edgeCount;
  private List<List<Edge>> vertices;
  private Dictionary vertexToAdjList;
  private Dictionary adjListToVertex;
  private Dictionary vertexPairToEdge;

  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
  public WUGraph() {
    vertexCount = 0;
    edgeCount = 0;
    vertices = new DList<List<Edge>>();
    vertexToAdjList = new HashTableChained();
    adjListToVertex = new HashTableChained();
    vertexPairToEdge = new HashTableChained();
  }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
  public int vertexCount() {
    return vertexCount;
  }

  /**
   * edgeCount() returns the total number of edges in the graph.
   *
   * Running time:  O(1).
   */
  public int edgeCount() {
    return edgeCount;
  }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|vertexCount|).
   */
  public Object[] getVertices() {
    Object[] v = new Object[vertices.length()];
    ListNode<List<Edge>> node = vertices.front();
    for (int i = 0; i < vertices.length(); i++) {
      try {
        v[i] = adjListToVertex.find(node.item()).value();
        node = node.next();
      } catch (InvalidNodeException e) {
        e.printStackTrace();
      }
    }
    return v;
  }

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.
   * The vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
  public void addVertex(Object vertex) {
    if (vertexToAdjList.find(vertex) != null) {
      return;
    }
    vertices.insertBack(new DList<Edge>()); // create new adjacency list for vertex
    try {
      vertexToAdjList.insert(vertex, vertices.back().item()); // associates vertex with adjacency list
      adjListToVertex.insert(vertices.back().item(), vertex); // associates adjacency list with vertex
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
    vertexCount++;
  }

  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public void removeVertex(Object vertex) {
    if (vertexToAdjList.find(vertex) == null) {
      return;
    }
    // TODO: refactor variable names: list, adjList
    List<Edge> targetAdjList = (List<Edge>) vertexToAdjList.find(vertex).value();
    for (Edge edge : targetAdjList) {
      removeEdge(edge.u, edge.v);
    }
    // remove adjacency list for the vertex
    ListNode<List<Edge>> node = vertices.front();
    while (node != null) {
      try {
        List<Edge> currentAdjList = node.item();
        if (targetAdjList.equals(currentAdjList)) {
          node.remove();
          break;
        }
        node = node.next();
      } catch (InvalidNodeException e) {
        e.printStackTrace();
      }
    }
    // remove vertex from both vertex maps
    adjListToVertex.remove(targetAdjList);
    vertexToAdjList.remove(vertex);
    vertexCount--;
  }

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
  public boolean isVertex(Object vertex) {
    return vertexToAdjList.find(vertex) != null;
  }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
  public int degree(Object vertex);

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
  public Neighbors getNeighbors(Object vertex);

  /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the graph already contains
   * edge (u, v), the weight is updated to reflect the new value.  Self-edges
   * (where u.equals(v)) are allowed.
   *
   * Running time:  O(1).
   */
  public void addEdge(Object u, Object v, int weight) {
    if (!isVertex(u) || !isVertex(v)) {
      return;
    }
    VertexPair pair = new VertexPair(u, v);
    if (vertexPairToEdge.find(pair) == null) {
      Edge edge = new Edge(u, v, weight);
      vertexPairToEdge.insert(pair, edge);
      List<Edge> uAdjList = (List<Edge>) vertexToAdjList.find(u).value();
      uAdjList.insertBack(edge);
      edge.first = uAdjList.back();
      edgeCount++;
      // not a self-edge, add edge to other adj list and update edge's second reference
      if (!u.equals(v)) {
        List<Edge> vAdjList = (List<Edge>) vertexToAdjList.find(v).value();
        vAdjList.insertBack(edge);
        edge.second = vAdjList.back();
        edgeCount++;
      }
    } else { // update weight if graph already contains edge
      Edge edge = (Edge) vertexPairToEdge.find(pair).value();
      edge.weight = weight;
    }
  }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
  public void removeEdge(Object u, Object v) {
    // if second reference is not null (not self-edge), remove the edge from
    // other vertex's list.
    if (isEdge(u, v)) {
      VertexPair pair = new VertexPair(u, v);
      Edge edge = (Edge) vertexPairToEdge.find(pair).value();
      try {
        if (edge.second != null) {
          edge.second.remove();
          edgeCount--;
        }
        edge.first.remove();
        edgeCount--;
      } catch (InvalidNodeException e) {
        e.printStackTrace();
      }
      vertexPairToEdge.remove(pair);
    }
  }

  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
  public boolean isEdge(Object u, Object v) {
    if (!isVertex(u) || !isVertex(v)) {
      return false;
    }
    VertexPair pair = new VertexPair(u, v);
    return vertexPairToEdge.find(pair) != null;
  }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but also more
   * annoying.)
   *
   * Running time:  O(1).
   */
  public int weight(Object u, Object v);

}
