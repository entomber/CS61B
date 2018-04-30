import graphalg.ListSorts;
import queue.LinkedQueue;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ListSortsTest {
  LinkedQueue q, q1, q2; // used in mergesort
  LinkedQueue qIn, qSmall, qEquals, qLarge; // used in quicksort
  Comparable pivot;

  @Before
  public void setUp() throws Exception {
    q = new LinkedQueue();
    q1 = new LinkedQueue();
    q2 = new LinkedQueue();
    qIn = new LinkedQueue();
    qSmall = new LinkedQueue();
    qEquals = new LinkedQueue();
    qLarge = new LinkedQueue();
  }

  @Test
  public void makeQueueOfQueues_empty() {
    assertEquals("queue is not made properly.", "[ ]", q.toString());
    LinkedQueue newQ = ListSorts.makeQueueOfQueues(q);
    assertEquals("makeQueueOfQueues failed to create a queue of queues.",
        "[ ]", newQ.toString());
  }

  @Test
  public void makeQueueOfQueues_singleItem() {
    q.enqueue(3);
    assertEquals("queue is not made properly.", "[ 3 ]", q.toString());
    LinkedQueue newQ = ListSorts.makeQueueOfQueues(q);
    assertEquals("makeQueueOfQueues failed to create a queue of queues.",
        "[ [ 3 ] ]", newQ.toString());
    assertTrue("makeQueueOfQueues failed to empty original input queue.", q.isEmpty());
  }

  @Test
  public void makeQueueOfQueues_multipleItems() {
    q.enqueue(3);
    q.enqueue(5);
    q.enqueue(2);
    assertEquals("queue is not made properly.", "[ 3 5 2 ]", q.toString());
    LinkedQueue newQ = ListSorts.makeQueueOfQueues(q);
    assertEquals("makeQueueOfQueues failed to create a queue of queues.",
        "[ [ 3 ] [ 5 ] [ 2 ] ]", newQ.toString());
    assertTrue("makeQueueOfQueues failed to empty original input queue.", q.isEmpty());
  }

  @Test
  public void mergeSortedQueues_bothEmpty() {
    LinkedQueue newQ = ListSorts.mergeSortedQueues(q1, q2);
    assertEquals("mergeSortedQueues failed to merge a sorted queue of queues.",
        "[ ]", newQ.toString());
  }

  @Test
  public void mergeSortedQueues_oneEmpty() {
    q1.enqueue(3);
    LinkedQueue newQ = ListSorts.mergeSortedQueues(q1, q2);
    assertEquals("mergeSortedQueues failed to merge a sorted queue of queues.",
        "[ 3 ]", newQ.toString());
    assertTrue("mergeSortedQueues failed to empty original input queues.",
        q1.isEmpty() && q2.isEmpty());

    q2.enqueue(5);
    newQ = ListSorts.mergeSortedQueues(q1, q2);
    assertEquals("mergeSortedQueues failed to merge a sorted queue of queues.",
        "[ 5 ]", newQ.toString());
    assertTrue("mergeSortedQueues failed to empty original input queues.",
        q1.isEmpty() && q2.isEmpty());
  }

  @Test
  public void mergeSortedQueues_normalOperation() {
    String items1[] = {"E", "E", "G", "M", "R"};
    String items2[] = {"A", "C", "E", "R", "T"};
    for (String item : items1) {
      q1.enqueue(item);
    }
    for (String item : items2) {
      q2.enqueue(item);
    }
    assertEquals("q1 is not made properly", "[ E E G M R ]", q1.toString());
    assertEquals("q2 is not made properly", "[ A C E R T ]", q2.toString());
    LinkedQueue newQ = ListSorts.mergeSortedQueues(q1, q2);
    assertEquals("mergeSortedQueues failed to merge a sorted queue of queues.",
        "[ A C E E E G M R R T ]", newQ.toString());
    assertTrue("mergeSortedQueues failed to empty original input queues.",
        q1.isEmpty() && q2.isEmpty());
  }

  @Test
  public void mergeSortedQueues_reverseParameters() {
    String items1[] = {"E", "E", "G", "M", "R"};
    String items2[] = {"A", "C", "E", "R", "T"};
    for (String item : items1) {
      q1.enqueue(item);
    }
    for (String item : items2) {
      q2.enqueue(item);
    }
    LinkedQueue newQ = ListSorts.mergeSortedQueues(q2, q1);
    assertEquals("mergeSortedQueues failed to merge a sorted queue of queues.",
        "[ A C E E E G M R R T ]", newQ.toString());
    assertTrue("mergeSortedQueues failed to empty original input queues.",
        q1.isEmpty() && q2.isEmpty());
  }

  @Test
  public void mergeSort_empty() {
    ListSorts.mergeSort(q);
    assertEquals("mergeSort failed to sort queue.", "[ ]", q.toString());
  }

  @Test
  public void mergeSort_oneItem() {
    q.enqueue(3);
    ListSorts.mergeSort(q);
    assertEquals("mergeSort failed to sort queue.", "[ 3 ]", q.toString());
  }

  @Test
  public void mergeSort_multipleItems() {
    String item = "MERGESORTEXAMPLE";
    for (int i = 0; i < item.length(); i++) {
      q.enqueue(item.charAt(i));
    }
    assertEquals("q is not made properly", "[ M E R G E S O R T E X A M P L E ]", q.toString());
    ListSorts.mergeSort(q);
    assertEquals("mergeSort failed to sort queue.",
        "[ A E E E E G L M M O P R R S T X ]", q.toString());
  }

  @Test
  public void mergeSort_allSameItems() {
    String item = "33333";
    for (int i = 0; i < item.length(); i++) {
      q.enqueue(item.charAt(i));
    }
    assertEquals("q is not made properly", "[ 3 3 3 3 3 ]", q.toString());
    ListSorts.mergeSort(q);
    assertEquals("mergeSort failed to sort queue.", "[ 3 3 3 3 3 ]", q.toString());
  }

  @Test
  public void partition_empty() {
    assertTrue("queue is not made properly.", qIn.toString().equals("[ ]") &&
        qSmall.toString().equals("[ ]") && qEquals.toString().equals("[ ]") && qLarge.toString().equals("[ ]"));
    ListSorts.partition(qIn, 3, qSmall, qEquals, qLarge);
    assertTrue("partition failed on empty queue.", qIn.toString().equals("[ ]") &&
        qSmall.toString().equals("[ ]") && qEquals.toString().equals("[ ]") && qLarge.toString().equals("[ ]"));
  }

  @Test
  public void partition_oneItemLargerThanPivot() {
    pivot = 3;
    qIn.enqueue(5);
    ListSorts.partition(qIn, pivot, qSmall, qEquals, qLarge);
    assertTrue("partition failed on single item queue with item larger than pivot.",
        qIn.toString().equals("[ ]") && qSmall.toString().equals("[ ]") &&
            qEquals.toString().equals("[ ]") && qLarge.toString().equals("[ 5 ]"));
  }

  @Test
  public void partition_oneItemSmallerThanPivot() {
    pivot = 7;
    qIn.enqueue(5);
    ListSorts.partition(qIn, pivot, qSmall, qEquals, qLarge);
    assertTrue("partition failed on single item queue with item smaller than pivot.",
        qIn.toString().equals("[ ]") && qSmall.toString().equals("[ 5 ]") &&
            qEquals.toString().equals("[ ]") && qLarge.toString().equals("[ ]"));
  }

  @Test
  public void partition_oneItemEqualToPivot() {
    pivot = 5;
    qIn.enqueue(5);
    ListSorts.partition(qIn, pivot, qSmall, qEquals, qLarge);
    assertTrue("partition failed on single item queue with item equal to pivot.",
        qIn.toString().equals("[ ]") && qSmall.toString().equals("[ ]") &&
            qEquals.toString().equals("[ 5 ]") && qLarge.toString().equals("[ ]"));
  }

  @Test
  public void partition_multipleItems() {
    pivot = 5;
    int[] items = { 5, 7, 5, 0, 6, 5, 5 };
    for (int item : items) {
      qIn.enqueue(item);
    }
    assertEquals("qIn is not made properly", "[ 5 7 5 0 6 5 5 ]", qIn.toString());
    ListSorts.partition(qIn, pivot, qSmall, qEquals, qLarge);
    assertTrue("partition failed on multiple item queue.",
        qIn.toString().equals("[ ]") && qSmall.toString().equals("[ 0 ]") &&
            qEquals.toString().equals("[ 5 5 5 5 ]") && qLarge.toString().equals("[ 7 6 ]"));
  }

  @Test
  public void quickSort_empty() {
    ListSorts.quickSort(q);
    assertEquals("quickSort failed to sort queue.", "[ ]", q.toString());
  }

  @Test
  public void quickSort_oneItem() {
    q.enqueue(3);
    ListSorts.quickSort(q);
    assertEquals("quickSort failed to sort queue.", "[ 3 ]", q.toString());
  }

  @Test
  public void quickSort_multipleItems() {
    String item = "QUICKSORTEXAMPLE";
    for (int i = 0; i < item.length(); i++) {
      q.enqueue(item.charAt(i));
    }
    assertEquals("q is not made properly", "[ Q U I C K S O R T E X A M P L E ]", q.toString());
    ListSorts.quickSort(q);
    assertEquals("quickSort failed to sort queue.",
        "[ A C E E I K L M O P Q R S T U X ]", q.toString());
  }

  @Test
  public void quickSort_allSameItems() {
    String item = "33333";
    for (int i = 0; i < item.length(); i++) {
      q.enqueue(item.charAt(i));
    }
    assertEquals("q is not made properly", "[ 3 3 3 3 3 ]", q.toString());
    ListSorts.quickSort(q);
    assertEquals("quickSort failed to sort queue.", "[ 3 3 3 3 3 ]", q.toString());
  }

}