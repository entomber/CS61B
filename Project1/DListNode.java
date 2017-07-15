/* DListNode.java */

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
	public DListNode prev;
	public DListNode next;

	/**
	 *	DListNode constructor.
	 */
	DListNode() {
		item = new int[4];	// zero initialized
		prev = null;
		next = null;
	}

	DListNode(int[] i) {
		this();
		item = i;
	}
}