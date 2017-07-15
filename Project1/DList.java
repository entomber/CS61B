/* DList.java */

/**
 *	A DList is a mutable doubly-linked list.  Its implementation is
 *	circularly-linked and employs a sentintel (dummy) node at the head
 *	of the list.
 */

public class DList {
	/**
	 *	head refences the sentinel node.
	 */

	protected DListNode head;
	protected long size;

	/* DList invariants:
	 *	1)	head != null.
	 *	2)	For any DListNode x in a DList, x.next != null.
	 *	3)	For any DListNode x in a DList, x.prev != null.
	 *	4)	For any DListNode x in a DList, if x.next == y, then y.prev == x.
	 *	5)	For any DListNode x in a DList, if x.prev == y, then y.next == x.
	 *	6)	size is the number of DListNodes, NOT COUNTING the sentinel
	 *			(denoted by "head"), that can be accessed from the sentinel by
	 *			a sequence of "next" references.
	 */

	/**
	 *	DList() constructor for an empty DList.
	 */
	public DList() {
		head = new DListNode(null);
		head.next = head;
		head.prev = head;
		size = 0;
	}

	/**
	 *	DList() constructor for a one-node DList.
	 */
	public DList(int[] i) {
		head = new DListNode(null);
		head.next = new DListNode(i);
		head.prev = head.next;
		head.next.prev = head;
		head.prev.next = head;
		size = 1;
	}

	/**
	 *	DList() constructor for a two-node DList.
	 */
/*	public DList(int val1, int num1, int val2, int num2) {
		head = new DListNode(Integer.MIN_VALUE);
		head.next = new DListNode(val1, num1);
		head.prev = new DListNode(val2, num2);
		head.next.prev = head;
		head.next.next = head.prev;
		head.prev.next = head;
		head.prev.prev = head.next;
		size = 2;
	}*/

	/**
	 *	insertFront() inserts an item at the front of a DList.
	 */
	public void insertFront(int i) {
	}

	/**
	 *	removeFront() removes the first item (and first non-sentinel node) from
	 *	a DList.  If the list is empty, do nothing.
	 */
	public void removeFront() {
	}

	/**
	 *	insertBack() inserts an item at the back of a DList.
	 */
	public void insertBack(int[] i) {
		DListNode node = new DListNode(i);
		node.prev = head.prev;
		node.next = head;
		head.prev.next = node;
		head.prev = node;
		size++;
	}

	/**
	 *	toString() returns a String representation of this DList.
	 *	@return a String representation of this DList.
	 */
	public String toString() {
		String result = "[  ";
		DListNode current = head.next;
		while (current != head) {
			result = result + current.item[0] + "," + current.item[1] + "," +
								current.item[2] + "," + current.item[3] + "  ";
			current = current.next;
		}
		return result + "]";
	}

	/**
	 *	doTest() checks whether the condition is true and prints the given error
	 *	message if it is not.
	 *	
	 *	@param b the condition to check.
	 *	@param msg the error message to print if the condition is false.
	 */
	private static void doTest(boolean b, String msg) {
		if (!b) {
			System.err.println(msg);
		}
	}

	public static void main(String[] args) {

		/*	testing insertBack:
				1. insert item to an empty list
					- head.next.value correct
					- head.next.number correct
					- head.next.prev == head
					- head.prev.value correct
					- head.prev.number correct
					- head.prev.next == head
					- size correct
				2. insert item to 1 item list
					- (in addition to adding to empty list...)
					- head.next.next == head.prev
					- head.
		*/
		DList l = new DList();
		System.out.println("### TESTING insertBack ###\nEmpty list is " + l);
		int[] a1 = {3, 7, 7, 7};
		l.insertBack(a1);
		System.out.println("\nInserting 3,7,7,7 at back.\nList with 3,7,7,7 is " + l);
		doTest(l.head.next.item[0] == 3, "head.next.value is wrong.");
		doTest(l.head.next.item[1] == 7, "head.next.number is wrong.");
		doTest(l.head.next.prev == l.head, "head.next.prev is wrong.");
		doTest(l.head.prev.item[0] == 3, "head.prev.value is wrong.");
		doTest(l.head.prev.item[1] == 7, "head.prev.number is wrong.");
		doTest(l.head.prev.next == l.head, "head.prev.next is wrong.");
		doTest(l.size == 1, "size is wrong");

		int[] a2 = {5, 88, 88, 88};
		l.insertBack(a2);
		System.out.println("\nInserting 88,5 at back.\nList with 7,3 and 88,5 is " + l);
		doTest(l.head.next.item[0] == 3, "head.next.value is wrong.");
		doTest(l.head.next.item[1] == 7, "head.next.number is wrong.");
		doTest(l.head.next.prev == l.head, "head.next.prev is wrong.");
		doTest(l.head.prev.item[0] == 5, "head.prev.value is wrong.");
		doTest(l.head.prev.item[1] == 88, "head.prev.number is wrong.");
		doTest(l.head.prev.next == l.head, "head.prev.next is wrong.");
		doTest(l.head.next.next == l.head.prev, "head.next.next != head.prev");
		doTest(l.head.prev.prev == l.head.next, "head.prev.prev != head.next");
		doTest(l.size == 2, "size is wrong");

	}
}