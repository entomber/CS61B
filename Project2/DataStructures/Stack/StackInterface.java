package DataStructures.Stack;

public abstract class StackInterface<E> implements Iterable<E> {

  /**
   * isEmpty() returns true if this Stack is empty, false otherwise.
   *
   * @return true if this Stack is empty, false otherwise.
   *
   * Performance: runs in O(1) time.
   */
  public abstract boolean isEmpty();

  /**
   * length() returns the length of this Stack.
   *
   * @return the length of this Stack.
   *
   * Performance: runs in O(1) time.
   */
  public abstract int length();

  /**
   * push() adds an item onto top of this Stack.
   *
   * @param item is the item to be added.
   *
   * Performance: runs in O(1) time.
   */
  public abstract void push(E item);

  /**
   * pop() removes and returns the item at the top of this Stack.
   *
   * @return the item at the top of this Stack or null if empty.
   *
   * Performance: runs in O(1) time.
   */
  public abstract E pop();

  /**
   * peek() returns the item at the top of this Stack.  Does not modify the stack.
   *
   * @return the item at the top of this Stack or null if empty.
   *
   * Performance: runs in O(1) time.
   */
  public abstract E peek();

  /**
   * toString() returns a String representation of this Stack.
   *
   * @return a String representation of this Stack.
   */
  public abstract String toString();
}
