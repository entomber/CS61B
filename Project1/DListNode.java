/* DListNode.java */

// package list;

/**
 *	A DListNode is a node in a DList (doubly-linked list).
 */

public class DListNode {
	
	/**
	 *	item references item stored in the current node.
	 *	prev references the previous node in the DList.
	 *	next references the next node in the DList.
	 */
	public int[] item;
	protected DListNode prev;
	protected DListNode next;

	/**
	 *	DListNode constructor.
	 */
	DListNode() {
		item = new int[4];	// zero initialized
		prev = null;
		next = null;
	}

	/**
	 *	DListNode() constructor
	 *	@param i the item to store in the node.
	 *	@param p the node previous to this node.
	 *	@param n the node following this node.
	 */
	DListNode(int[] i, DListNode p, DListNode n) {
		item = i;
		prev = p;
		next = n;
	}
}