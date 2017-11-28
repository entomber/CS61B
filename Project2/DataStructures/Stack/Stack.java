package DataStructures.Stack;

import DataStructures.List.List;
import DataStructures.List.ListNode;
import DataStructures.List.DList;
import DataStructures.List.InvalidNodeException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A Stack is a mutable list ADT that only allows adding items and removing items
 * from the top of the Stack.
 *
 */

public class Stack<E> extends StackInterface<E> {

  /**
   * list references the list which holds items in the Stack.
   */
  private List<E> list;

  /**
   * Stack() constructs for an empty Stack.
   */
  public Stack() {
    list = new DList<E>();
  }

  /**
   * isEmpty() returns true if this Stack is empty, false otherwise.
   *
   * @return true if this Stack is empty, false otherwise.
   *
   * Performance: runs in O(1) time.
   */
  public boolean isEmpty() {
    return list.isEmpty();
  }

  /**
   * length() returns the length of this Stack.
   *
   * @return the length of this Stack.
   *
   * Performance: runs in O(1) time.
   */
  public int length() {
    return list.length();
  }

  /**
   * push() adds an item onto top of this Stack.
   *
   * @param item is the item to be added.
   *
   * Performance: runs in O(1) time.
   */
  @Override
  public void push(E item) {
    list.insertFront(item);
  }

  /**
   * pop() removes and returns the item at the top of this Stack.
   *
   * @return the item at the top of this Stack or null if empty.
   *
   * Performance: runs in O(1) time.
   */
  @Override
  public E pop() {
    ListNode<E> node = list.front();
    if (!node.isValidNode()) {
      return null;
    }
    try {
      E item = node.item();
      node.remove();
      return item;
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * peek() returns the item at the top of this Stack.  Does not modify the stack.
   *
   * @return the item at the top of this Stack or null if empty.
   *
   * Performance: runs in O(1) time.
   */
  @Override
  public E peek() {
    ListNode<E> node = list.front();
    if (!node.isValidNode()) {
      return null;
    }
    try {
      return node.item();
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * toString() returns a String representation of this Stack.
   *
   * @return a String representation of this Stack.
   */
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder(100);
    sb.append("[  ");
    Iterator<E> iter = iterator();
    while (iter.hasNext()) {
      sb.append(iter.next());
      sb.append("  ");
    }
    sb.append("]  size: ");
    sb.append(length());
    return sb.toString();
  }

  @Override
  public Iterator<E> iterator() {
    return new StackIterator();
  }

  private class StackIterator implements Iterator<E> {
    private ListNode<E> currentNode = null;
    private int remaining = length();

    public boolean hasNext() {
      return remaining > 0;
    }

    public E next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      if (currentNode == null) {
        currentNode = list.front();
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
  }

}
