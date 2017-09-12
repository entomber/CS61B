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
		head = new DListNode(null, null, null);
		head.next = head;
		head.prev = head;
		size = 0;
	}

	/**
	 *	DList() constructor for a one-node DList.
	 */
	public DList(int[] i) {
		head = new DListNode(null, null, null);
		head.next = new DListNode(i, head, head);
		head.prev = head.next;
		size = 1;
	}

	/**
	 *	length() returns the length of this DList.
	 *	@return the length of this DList.
	 */
	public long length() {
		return size;
	}

	/**
	 *	insertBack() inserts an item at the back of this DList.
	 */
	public void insertBack(int[] i) {
		DListNode node = new DListNode(i, head.prev, head);
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
	 *	doTest() checks an invariant and prints an error message if it fails.
	 *	If invariant is true, this method does nothing.  If invariant is false,
	 *	the message is printed, followed by a dump of the program call stack.
	 *	
	 *	@param invariant the condition to be verified.
	 *	@param message the error message to be printed if the invariant is false.
	 */
	private static void doTest(boolean invariant, String message) {
		if (!invariant) {
			System.err.println("*** ERROR:  " + message);
			Thread.dumpStack();
		}
	}

	public static void main(String[] args) {

		// test DList() constructor, insertBack(), node links, length()
		DList l = new DList();
		System.out.println("\nTesting DList() constructor. " +
												"Empty list is " + l);
		doTest(l.toString().equals("[  ]"),
						"DList() constructor failed");
		doTest(l.length() == 0,
						"length() failed");

		int[] a1 = {3, 7, 7, 7};
		l.insertBack(a1);
		System.out.println("Inserting 3,7,7,7 at back. " +
												"List with 3,7,7,7 is " + l);

		doTest(l.head.next.item[0] == 3, 
						"head.next.value is wrong.");
		doTest(l.head.next.item[1] == 7, 
						"head.next.number is wrong.");
		doTest(l.head.next.prev == l.head, 
						"head.next.prev is wrong.");
		doTest(l.head.prev.item[0] == 3, 
						"head.prev.value is wrong.");
		doTest(l.head.prev.item[1] == 7, 
						"head.prev.number is wrong.");
		doTest(l.head.prev.next == l.head, 
						"head.prev.next is wrong.");
		doTest(l.length() == 1, 
						"length() failed)");

		// test DList(int[]) constructor, node links, length()
		DList k = new DList(a1);
		System.out.println("\nTesting DList(int[]) constructor. " +
												"List with 3,7,7,7 is " + k);
		doTest(k.toString().equals("[  3,7,7,7  ]"),
						"DList() constructor failed");

		doTest(k.head.next.item[0] == 3, 
						"head.next.value is wrong.");
		doTest(k.head.next.item[1] == 7, 
						"head.next.number is wrong.");
		doTest(k.head.next.prev == k.head, 
						"head.next.prev is wrong.");
		doTest(k.head.prev.item[0] == 3, 
						"head.prev.value is wrong.");
		doTest(k.head.prev.item[1] == 7, 
						"head.prev.number is wrong.");
		doTest(k.head.prev.next == k.head, 
						"head.prev.next is wrong.");
		doTest(k.length() == 1, 
						"length() failed");

		// test insertBack(), node links, length() on non-empty list
		int[] a2 = {5, 88, 88, 88};
		l.insertBack(a2);
		System.out.println("\nInserting 5,88,88,88 at back. " +
												"List with 7,3 and 88,5 is " + l);
		doTest(l.toString().equals("[  3,7,7,7  5,88,88,88  ]"),
						"DList() constructor failed");

		doTest(l.head.next.item[0] == 3, 
						"head.next.value is wrong.");
		doTest(l.head.next.item[1] == 7, 
						"head.next.number is wrong.");
		doTest(l.head.next.prev == l.head, 
						"head.next.prev is wrong.");
		doTest(l.head.prev.item[0] == 5, 
						"head.prev.value is wrong.");
		doTest(l.head.prev.item[1] == 88, 
						"head.prev.number is wrong.");
		doTest(l.head.prev.next == l.head, 
						"head.prev.next is wrong.");
		doTest(l.head.next.next == l.head.prev, 
						"head.next.next != head.prev");
		doTest(l.head.prev.prev == l.head.next, 
						"head.prev.prev != head.next");
		doTest(l.length() == 2, 
						"length() failed)");

	}
}