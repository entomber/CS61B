/* ListNode.java */

package list;

/**
 *  A ListNode is a mutable node in a list.  No implementation is provided.
 *
 *  This is my HW5 solution expanded to use generics.
 **/

public abstract class ListNode<E> {

  /**
   *  item references the item stored in the current node.
   *  myList references the List that contains this node.
   *
   */

  protected E item;
  protected List<E> myList;

  /**
   *  isValidNode returns true if this node is valid; false otherwise.
   *  By default, an invalid node is one that doesn't belong to a list (myList
   *  is null), but subclasses can override this definition.
   *
   *  @return true if this node is valid; false otherwise.
   *
   *  Performance:  runs in O(1) time.
   */
  public boolean isValidNode() {
    return myList != null;
  }

  /**
   *  item() returns this node's item.  If this node is invalid,
   *  throws an exception.
   *
   *  @return the item stored in this node.
   *
   *  Performance:  runs in O(1) time.
   */
  public E item() throws InvalidNodeException {
    if (!isValidNode()) {
      throw new InvalidNodeException();
    }
    return item;
  }

  /**
   *  setItem() sets this node's item to "item".  If this node is invalid,
   *  throws an exception.
   *
   *  Performance:  runs in O(1) time.
   */
  public void setItem(E item) throws InvalidNodeException {
    if (!isValidNode()) {
      throw new InvalidNodeException();
    }
    this.item = item;
  }

  /**
   *  next() returns the node following this node.  If this node is invalid,
   *  throws an exception.
   *
   *  @return the node following this node.
   *  @exception InvalidNodeException if this node is not valid.
   */
  public abstract ListNode<E> next() throws InvalidNodeException;

  /**
   *  prev() returns the node preceding this node.  If this node is invalid,
   *  throws an exception.
   *
   *  @return the node preceding this node.
   *  @exception InvalidNodeException if this node is not valid.
   */
  public abstract ListNode<E> prev() throws InvalidNodeException;

  /**
   *  insertAfter() inserts an item immediately following this node.  If this
   *  node is invalid, throws an exception.
   *
   *  @param item the item to be inserted.
   *  @exception InvalidNodeException if this node is not valid.
   */
  public abstract void insertAfter(E item) throws InvalidNodeException;

  /**
   *  insertBefore() inserts an item immediately preceding this node.  If this
   *  node is invalid, throws an exception.
   *
   *  @param item the item to be inserted.
   *  @exception InvalidNodeException if this node is not valid.
   */
  public abstract void insertBefore(E item) throws InvalidNodeException;

  /**
   *  remove() removes this node from its List.  If this node is invalid,
   *  throws an exception.
   *
   *  @exception InvalidNodeException if this node is not valid.
   */
  public abstract void remove() throws InvalidNodeException;

}
