/* WUGraph.java */

package graph;

import dict.Dictionary;
import dict.HashTableChained;
import list.DList;
import list.DListNode;
import list.ListNode;
import list.InvalidNodeException;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {

  /**
   * vertexCount stores the number of vertices
   * edgeCount stores the number of edges
   * vertices is an adjacency list that stores lists of edges incident to a vertex
   * vertexToDegree maps a vertex Object to its degree
   * vertexToAdjList maps a vertex Object to a DListNode that stores its adjacency list
   * adjListToVertex maps a DListNode that stores an adjacency list to its vertex Object
   * vertexPairToEdge maps a VertexPair to an Edge
   */
  private int vertexCount;
  private int edgeCount;
  private DList<DList<Edge>> vertices;
  private Dictionary vertexToDegree;    // Object(vertex)  -->  Integer(vertex's degree)
  private Dictionary vertexToAdjList;   // Object(vertex)  -->  DListNode<DList<Edge>>
  private Dictionary adjListToVertex;   // DListNode<DList<Edge>>  -->  Object(vertex)
  private Dictionary vertexPairToEdge;  // VertexPair  -->  Edge

  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
  public WUGraph() {
    vertexCount = 0;
    edgeCount = 0;
    vertices = new DList<DList<Edge>>();
    vertexToDegree = new HashTableChained();
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
    ListNode<DList<Edge>> node = vertices.front();
    for (int i = 0; i < vertices.length(); i++) {
      try {
        v[i] = adjListToVertex.find(node).value();
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
    if (isVertex(vertex)) {
      return;
    }
    vertices.insertBack(new DList<Edge>()); // create new adjacency list for vertex
    vertexToAdjList.insert(vertex, vertices.back());
    adjListToVertex.insert(vertices.back(), vertex);
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
    if (!isVertex(vertex)) {
      return;
    }
    // create shallow copy of list, and iterate over it to remove edges
    DList<Edge> targetAdjList = getAdjList(vertex);
    DList<Edge> copyAdjList = createCopy(targetAdjList);
    for (Edge edge : copyAdjList) {
      removeEdge(edge.u, edge.v);
    }
    // remove adjacency list for the vertex
    DListNode<DList<Edge>> node = (DListNode<DList<Edge>>) vertexToAdjList.find(vertex).value();
    try {
      node.remove();
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
    // vertex removed, update mappings
    adjListToVertex.remove(targetAdjList);
    vertexToAdjList.remove(vertex);
    vertexCount--;
  }

  // returns a shallow copy of the given list
  private static DList<Edge> createCopy(DList<Edge> targetAdjList) {
    DList<Edge> copy = new DList<Edge>();
    for (Edge edge : targetAdjList) {
      copy.insertBack(edge);
    }
    return copy;
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
  public int degree(Object vertex) {
    if (!isVertex(vertex)) {
      return 0;
    }
    if (vertexToDegree.find(vertex) == null) {
      return 0;
    }
    return (Integer) vertexToDegree.find(vertex).value();
  }

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
  public Neighbors getNeighbors(Object vertex) {
    if (degree(vertex) == 0) { // degree returns 0 if vertex not in graph or has degree zero
      return null;
    }
    Neighbors neighbors = new Neighbors();
    DList<Edge> adjList = getAdjList(vertex);
    neighbors.neighborList = new Object[adjList.length()];
    neighbors.weightList = new int[adjList.length()];
    int i = 0;
    for (Edge edge : adjList) {
      // not a self-edge, and vertex is edge.u, thus must be other vertex
      if (vertex.equals(edge.u) && edge.u != edge.v) {
        neighbors.neighborList[i] = edge.v;
      } else {
        neighbors.neighborList[i] = edge.u;
      }
      neighbors.weightList[i] = edge.weight;
      i++;
    }
    return neighbors;
  }

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
    if (!isEdge(u, v)) {
      Edge edge = new Edge(u, v, weight);
      vertexPairToEdge.insert(pair, edge);
      DList<Edge> uAdjList = getAdjList(u);
      uAdjList.insertBack(edge);
      edge.first = uAdjList.back();
      edgeCount++;
      incrementDegree(u);
      // not a self-edge, add edge to other adj list and update edge's second reference
      if (!u.equals(v)) {
        DList<Edge> vAdjList = getAdjList(v);
        vAdjList.insertBack(edge);
        edge.second = vAdjList.back();
        incrementDegree(v);
      }
    } else { // edge exists, update the weight
      Edge edge = (Edge) vertexPairToEdge.find(pair).value();
      edge.weight = weight;
    }
  }

  // increments the degree of the vertex by 1
  private void incrementDegree(Object vertex) {
    updateDegree(vertex, 1);
  }

  // returns the adjacency list of the given vertex
  private DList<Edge> getAdjList(Object vertex) {
    DListNode<DList<Edge>> node = (DListNode<DList<Edge>>) vertexToAdjList.find(vertex).value();
    DList<Edge> adjList = null;
    try {
      adjList = node.item();
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
    return adjList;
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
    if (!isEdge(u, v)) {
      return;
    }
    VertexPair pair = new VertexPair(u, v);
    Edge edge = (Edge) vertexPairToEdge.find(pair).value();
    try {
      // not a self-edge, remove edge from other vertex's list
      if (edge.second != null) {
        edge.second.remove();
        decrementDegree(v);
      }
      edge.first.remove();
      edgeCount--;
      decrementDegree(u);
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
    vertexPairToEdge.remove(pair);
  }

  // decrements the degree of the vertex by 1
  private void decrementDegree(Object vertex) {
    updateDegree(vertex, -1);
  }

  // updates the degree in vertexToDegree map by adding given value
  private void updateDegree(Object vertex, int addToDegree) {
    int degree;
    if (vertexToDegree.find(vertex) == null) {
      degree = 0;
    } else {
      degree = (Integer) vertexToDegree.find(vertex).value();
    }
    degree += addToDegree;
    vertexToDegree.remove(vertex);
    vertexToDegree.insert(vertex, degree);
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
  public int weight(Object u, Object v) {
    if (!isEdge(u, v)) {
      return 0;
    }
    VertexPair pair = new VertexPair(u, v);
    Edge edge = (Edge) vertexPairToEdge.find(pair).value();
    return edge.weight;
  }
}
