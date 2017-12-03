/* List.java */

package DataStructures.List;

/**
 *  A List is a mutable list ADT.  No implementation is provided.
 *
 *  This is my HW5 solution expanded to use generics and Iterable interface
 **/

public abstract class List<E> implements Iterable<E> {

  /**
   *  size is the number of items in the list.
   **/

  protected int size;

  /**
   *  isEmpty() returns true if this List is empty, false otherwise.
   *
   *  @return true if this List is empty, false otherwise. 
   *
   *  Performance:  runs in O(1) time.
   **/
  public boolean isEmpty() {
    return size == 0;
  }

  /** 
   *  length() returns the length of this List. 
   *
   *  @return the length of this List.
   *
   *  Performance:  runs in O(1) time.
   **/
  public int length() {
    return size;
  }

  /**
   *  insertFront() inserts an item at the front of this List.
   *
   *  @param item is the item to be inserted.
   **/
  public abstract void insertFront(E item);

  /**
   *  insertBack() inserts an item at the back of this List.
   *
   *  @param item is the item to be inserted.
   **/
  public abstract void insertBack(E item);

  /**
   *  front() returns the node at the front of this List.  If the List is
   *  empty, return an "invalid" node--a node with the property that any
   *  attempt to use it will cause an exception.
   *
   *  @return a ListNode at the front of this List.
   */
  public abstract ListNode<E> front();

  /**
   *  back() returns the node at the back of this List.  If the List is
   *  empty, return an "invalid" node--a node with the property that any
   *  attempt to use it will cause an exception.
   *
   *  @return a ListNode at the back of this List.
   */
  public abstract ListNode<E> back();

  /**
   *  contains() returns true if the item is in this List, false otherwise.
   *
   *  @return true if the item is in the List, false otherwise.
   *
   *  Performance: runs in O(n) time, where n is the length of this List.
   */
  public abstract boolean contains(E item);

  /**
   *  toString() returns a String representation of this List.
   *
   *  @return a String representation of this List.
   */
  public abstract String toString();

}
