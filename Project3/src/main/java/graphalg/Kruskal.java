/* Kruskal.java */

package graphalg;

import dict.Dictionary;
import dict.HashTableChained;
import graph.*;
import queue.LinkedQueue;
import queue.QueueEmptyException;
import set.DisjointSets;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   *
   * @param g The weighted, undirected graph whose MST we want to compute.
   * @return A newly constructed WUGraph representing the MST of g.
   */
  public static WUGraph minSpanTree(WUGraph g) {
    // add vertices of g to T
    WUGraph T = new WUGraph(); // MST of g
    Dictionary vertexToInt = new HashTableChained(); // maps vertices <--> int indexes used in disjoint sets
    Object[] vertices = g.getVertices();
    for (int i = 0; i < vertices.length; i++) {
      T.addVertex(vertices[i]);
      vertexToInt.insert(vertices[i], i);
    }
    // make list of all edges in g. there will be duplicate edges
    LinkedQueue edges = new LinkedQueue();
    for (Object vertex : vertices) {
      Neighbors neighbors = g.getNeighbors(vertex);
      if (neighbors != null) {
        for (int i = 0; i < neighbors.neighborList.length; i++) {
          Edge edge = new Edge(vertex, neighbors.neighborList[i], neighbors.weightList[i]);
          edges.enqueue(edge);
        }
      }
    }
    ListSorts.quickSort(edges);
    // find edges of T using disjoint sets: if vertices in edge belong to different sets,
    // call union on them and add the edge to T.
    DisjointSets sets = new DisjointSets(vertices.length);
    while (!edges.isEmpty()) {
      try {
        Edge edge = (Edge) edges.dequeue();
        int disjointSetsIndex1 = (Integer) vertexToInt.find(edge.u).value();
        int disjointSetsIndex2 = (Integer) vertexToInt.find(edge.v).value();
        int root1 = sets.find(disjointSetsIndex1);
        int root2 = sets.find(disjointSetsIndex2);
        if (root1 != root2) {
          T.addEdge(edge.u, edge.v, edge.weight);
          sets.union(root1, root2);
        }
      } catch (QueueEmptyException e) {
        e.printStackTrace();
      }
    }
    return T;
  }

}
