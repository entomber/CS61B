/* DList.java */

package DataStructures.List;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  A DList is a mutable doubly-linked list ADT.  Its implementation is
 *  circularly-linked and employs a sentinel node at the head of the list.
 *
 *  This is my HW5 solution expanded to use generics.
 **/

public class DList<E> extends List<E> {

  /**
   *  (inherited)  size is the number of items in the list.
   *  head references the sentinel node.
   *  Note that the sentinel node does not store an item, and is not included
   *  in the count stored by the "size" field.
   *
   **/

  protected DListNode<E> head;

  /* DList invariants:
   *  1)  head != null.
   *  2)  For every DListNode x in a DList, x.next != null.
   *  3)  For every DListNode x in a DList, x.prev != null.
   *  4)  For every DListNode x in a DList, if x.next == y, then y.prev == x.
   *  5)  For every DListNode x in a DList, if x.prev == y, then y.next == x.
   *  6)  For every DList l, l.head.myList = null.  (Note that l.head is the
   *      sentinel.)
   *  7)  For every DListNode x in a DList l EXCEPT l.head (the sentinel),
   *      x.myList = l.
   *  8)  size is the number of DListNodes, NOT COUNTING the sentinel,
   *      that can be accessed from the sentinel (head) by a sequence of
   *      "next" references.
   **/

  /**
   *  newNode() calls the DListNode constructor.  Use this method to allocate
   *  new DListNodes rather than calling the DListNode constructor directly.
   *  That way, only this method need be overridden if a subclass of DList
   *  wants to use a different kind of node.
   *
   *  @param item the item to store in the node.
   *  @param list the list that owns this node.  (null for sentinels.)
   *  @param prev the node previous to this node.
   *  @param next the node following this node.
   **/
  protected DListNode<E> newNode(E item, DList<E> list,
                              DListNode<E> prev, DListNode<E> next) {
    return new DListNode<E>(item, list, prev, next);
  }

  /**
   *  DList() constructs for an empty DList.
   **/
  public DList() {
    head = newNode(null, null, null, null);
    head.prev = head;
    head.next = head;
    size = 0;
  }

  /**
   *  insertFront() inserts an item at the front of this DList.
   *
   *  @param item is the item to be inserted.
   *
   *  Performance:  runs in O(1) time.
   **/
  public void insertFront(E item) {
    DListNode<E> node = newNode(item, this, head, head.next);
    // in an empty list, head, head.prev and head.next all refer to sentinel
    head.next.prev = node;
    head.next = node;
    size++;
  }

  /**
   *  insertBack() inserts an item at the back of this DList.
   *
   *  @param item is the item to be inserted.
   *
   *  Performance:  runs in O(1) time.
   **/
  public void insertBack(E item) {
    DListNode<E> node = newNode(item, this, head.prev, head);
    // in an empty list, head, head.prev, and head.next all refer to sentinel
    head.prev.next = node;
    head.prev = node;
    size++;
  }

  /**
   *  front() returns the node at the front of this DList.  If the DList is
   *  empty, return an "invalid" node--a node with the property that any
   *  attempt to use it will cause an exception.  (The sentinel is "invalid".)
   *
   *  @return a ListNode at the front of this DList.
   *
   *  Performance:  runs in O(1) time.
   */
  public ListNode<E> front() {
    return head.next;
  }

  /**
   *  back() returns the node at the back of this DList.  If the DList is
   *  empty, return an "invalid" node--a node with the property that any
   *  attempt to use it will cause an exception.  (The sentinel is "invalid".)
   *
   *  @return a ListNode at the back of this DList.
   *
   *  Performance:  runs in O(1) time.
   */
  public ListNode<E> back() {
    return head.prev;
  }

  /**
   *  toString() returns a String representation of this DList.
   *
   *  @return a String representation of this DList.
   *
   *  Performance:  runs in O(n) time, where n is the length of the list.
   */
  public String toString() {
    StringBuilder sb = new StringBuilder(100);
    sb.append("[  ");
    DListNode current = head.next;
    while (current != head) {
      sb.append(current.item);
      sb.append("  ");
      current = current.next;
    }
    sb.append("]");
    return sb.toString();
  }

  @Override
  public Iterator<E> iterator() {
    return new DListIterator();
  }

  private class DListIterator implements Iterator<E> {
    private ListNode<E> currentNode = null;
    private int remaining = size;
    private boolean calledNext = false;

    @Override
    public boolean hasNext() {
      return remaining > 0;
    }

    @Override
    public E next() {
      calledNext = true;
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      if (currentNode == null) {
        currentNode = head.next;
        remaining--;
        try {
          return currentNode.item();
        } catch (InvalidNodeException e) {
          e.printStackTrace();
        }
      }
      try {
        currentNode = currentNode.next();
        remaining--;
        return currentNode.item();
      } catch (InvalidNodeException e) {
        e.printStackTrace();
      }
      throw new NoSuchElementException();
    }

    @Override
    public void remove() {
      if (!calledNext) {
        throw new IllegalStateException("next() has not been called yet.");
      }
      try {
        // set current node to a temp node after remove() called to preserve next reference
        DListNode<E> tempNode = newNode(null, DList.this, null,
            (DListNode<E>) currentNode.next());
        currentNode.remove();
        currentNode = tempNode;
        calledNext = false;
      } catch (InvalidNodeException e) {
        e.printStackTrace();
      }
    }


  }

  private static void testInvalidNode(ListNode<Integer> p) {
    System.out.println("p.isValidNode() should be false: " + p.isValidNode());
    try {
      p.item();
      System.out.println("p.item() should throw an exception, but didn't.");
    } catch (InvalidNodeException lbe) {
      System.out.println("p.item() should throw an exception, and did.");
    }
    try {
      p.setItem(0);
      System.out.println("p.setItem() should throw an exception, but didn't.");
    } catch (InvalidNodeException lbe) {
      System.out.println("p.setItem() should throw an exception, and did.");
    }
    try {
      p.next();
      System.out.println("p.next() should throw an exception, but didn't.");
    } catch (InvalidNodeException lbe) {
      System.out.println("p.next() should throw an exception, and did.");
    }
    try {
      p.prev();
      System.out.println("p.prev() should throw an exception, but didn't.");
    } catch (InvalidNodeException lbe) {
      System.out.println("p.prev() should throw an exception, and did.");
    }
    try {
      p.insertBefore(1);
      System.out.println("p.insertBefore() should throw an exception, but " +
                         "didn't.");
    } catch (InvalidNodeException lbe) {
      System.out.println("p.insertBefore() should throw an exception, and did."
                         );
    }
    try {
      p.insertAfter(1);
      System.out.println("p.insertAfter() should throw an exception, but " +
                         "didn't.");
    } catch (InvalidNodeException lbe) {
      System.out.println("p.insertAfter() should throw an exception, and did."
                         );
    }
    try {
      p.remove();
      System.out.println("p.remove() should throw an exception, but didn't.");
    } catch (InvalidNodeException lbe) {
      System.out.println("p.remove() should throw an exception, and did.");
    }
  }

  private static void testEmpty() {
    List<Integer> l = new DList<Integer>();
    System.out.println("An empty list should be [  ]: " + l);
    System.out.println("l.isEmpty() should be true: " + l.isEmpty());
    System.out.println("l.length() should be 0: " + l.length());
    System.out.println("Finding front node p of l.");
    ListNode<Integer> p = l.front();
    testInvalidNode(p);
    System.out.println("Finding back node p of l.");
    p = l.back();
    testInvalidNode(p);
    l.insertFront(10);
    System.out.println("l after insertFront(10) should be [  10  ]: " + l);
  }

  public static void main(String[] argv) {
    testEmpty();
    List<Integer> l = new DList<Integer>();
    l.insertFront(3);
    l.insertFront(2);
    l.insertFront(1);
    System.out.println("l is a list of 3 elements: " + l);
    try {
      ListNode<Integer> n;
      int i = 1;
      for (n = l.front(); n.isValidNode(); n = n.next()) {
        System.out.println("n.item() should be " + i + ": " + n.item());
        n.setItem(n.item() * 2);
        System.out.println("n.item() should be " + 2 * i + ": " + n.item());
        i++;
      }
      System.out.println("After doubling all elements of l: " + l);
      testInvalidNode(n);

      i = 6;
      for (n = l.back(); n.isValidNode(); n = n.prev()) {
        System.out.println("n.item() should be " + i + ": " + n.item());
        n.setItem(n.item() * 2);
        System.out.println("n.item() should be " + 2 * i + ": " + n.item());
        i = i - 2;
      }
      System.out.println("After doubling all elements of l again: " + l);
      testInvalidNode(n);

      n = l.front().next();
      System.out.println("Removing middle element (8) of l: " + n.item());
      n.remove();
      System.out.println("l is now: " + l);
      testInvalidNode(n);    
      n = l.back();
      System.out.println("Removing end element (12) of l: " + n.item());
      n.remove();
      System.out.println("l is now: " + l);
      testInvalidNode(n);    

      n = l.front();
      System.out.println("Removing first element (4) of l: " + n.item());
      n.remove();
      System.out.println("l is now: " + l);
      testInvalidNode(n);    
    } catch (InvalidNodeException lbe) {
      System.err.println ("Caught InvalidNodeException that should not happen."
                          );
      System.err.println ("Aborting the testing code.");
    }
  }
}
