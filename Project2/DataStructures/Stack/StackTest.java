package DataStructures.Stack;

import org.junit.Test;
import player.Move;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class StackTest {

  @Test
  public void push_emptyStack() {
    Stack<Move> stack = new Stack<Move>();
    Move m1 = new Move(1, 1);

    assertEquals("Should construct empty stack.", "[  ]  size: 0",
        stack.toString());
    stack.push(m1);
    assertEquals("Should add to empty stack.", "[  [add to 11]  ]  size: 1",
        stack.toString());
  }

  @Test
  public void push_nonEmptyStack() {
    Stack<Move> stack = new Stack<Move>();
    Move m1 = new Move(1, 1);
    Move m2 = new Move(2, 4);

    stack.push(m1);
    stack.push(m2);
    assertEquals("Should push to non-empty stack.",
        "[  [add to 24]  [add to 11]  ]  size: 2", stack.toString());
  }

  @Test
  public void pop_emptyStack() {
    Stack<Move> stack = new Stack<Move>();
    Move m = stack.pop();

    assertEquals("Should pop empty stack.", "[  ]  size: 0", stack.toString());
    assertTrue("Popped item should be null.", m == null);
  }

  @Test
  public void pop_nonEmptyStack() {
    Stack<Move> stack = new Stack<Move>();
    Move m1 = new Move(1, 1);
    Move m2 = new Move(2, 4);

    stack.push(m1);
    stack.push(m2);
    assertEquals("Should push to non-empty stack.",
        "[  [add to 24]  [add to 11]  ]  size: 2", stack.toString());
    Move m3 = stack.pop();
    assertEquals("Should pop non-empty stack.", "[  [add to 11]  ]  size: 1",
        stack.toString());
    assertTrue("Popped item is correct.", m3.equals(m2));


    Move m4 = stack.pop();
    assertEquals("Should pop for non-empty stack.", "[  ]  size: 0",
        stack.toString());
    assertTrue("Popped item is correct.", m4.equals(m1));
  }

  @Test
  public void peek_emptyStack() {
    Stack<Move> stack = new Stack<Move>();
    Move m = stack.peek();

    assertEquals("Should peek empty stack.", "[  ]  size: 0", stack.toString());
    assertTrue("Peeked item should be null.", m == null);
  }

  @Test
  public void peek_nonEmptyStack() {
    Stack<Move> stack = new Stack<Move>();
    Move m1 = new Move(1, 1);
    Move m2 = new Move(2, 4);

    stack.push(m1);
    Move m3 = stack.peek();
    assertEquals("Should peek non-empty stack.", "[  [add to 11]  ]  size: 1",
        stack.toString());
    assertTrue("Peeked item should be correct.", m3.equals(m1));

    stack.push(m2);
    Move m4 = stack.peek();
    assertEquals("Should peek non-empty stack.",
        "[  [add to 24]  [add to 11]  ]  size: 2", stack.toString());
    assertTrue("Peeked item should be correct.", m4.equals(m2));
  }

  @Test
  public void iterator_empty() {
    Stack<Integer> stack = new Stack<Integer>();
    assertFalse("Should be empty.", stack.iterator().hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void iterator_throwIfNextOnEmpty() {
    Stack<Integer> stack = new Stack<Integer>();
    stack.iterator().next();
  }

  @Test(expected = NoSuchElementException.class)
  public void iterator_throwIfIterateBeyond() {
    Stack<Integer> stack = new Stack<Integer>();
    stack.push(1);
    stack.push(2);
    Iterator<Integer> iter = stack.iterator();
    iter.next();
    iter.next();
    iter.next();
  }

  @Test
  public void iterator_StandardIteration() {
    Stack<Integer> stack = new Stack<Integer>();
    Integer[] items = { 42, 0, 23 };
    for (int i : items) {
      stack.push(i);
    }
    Iterator<Integer> iter = stack.iterator();
    for (int i = items.length - 1; i >= 0; i--) {
      assertTrue("Should have next item.", iter.hasNext());
      assertEquals("Item should match.", items[i], iter.next());
    }
    assertFalse("Should not have more items.", iter.hasNext());
  }
}
