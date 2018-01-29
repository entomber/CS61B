package DataStructures.List;

import org.junit.Test;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DListTest {

  @Test
  public void iterator_empty() {
    List<Integer> list = new DList<Integer>();
    assertFalse("Should be empty.", list.iterator().hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void iterator_throwIfNextOnEmpty() {
    List<Integer> list = new DList<Integer>();
    list.iterator().next();
  }

  @Test(expected = NoSuchElementException.class)
  public void iterator_throwIfIterateBeyond() {
    List<Integer> list = new DList<Integer>();
    list.insertBack(1);
    list.insertBack(2);
    Iterator<Integer> iter = list.iterator();
    iter.next();
    iter.next();
    iter.next();
  }

  @Test
  public void iterator_standardIteration() {
    List<Integer> list = new DList<Integer>();
    Integer[] items = { 12, 3, 4 };
    for (int i : items) {
      list.insertBack(i);
    }
    Iterator<Integer> iter = list.iterator();
    for (Integer item : items) {
      assertTrue("Should have next item.", iter.hasNext());
      assertEquals("Should be correct item.", item, iter.next());
    }
    assertFalse("Should not have more items.", iter.hasNext());
  }

  @Test(expected = IllegalStateException.class)
  public void iterator_throwIfRemoveWithoutNext() {
    List<Integer> list = new DList<Integer>();
    Integer[] items = { 12, 3, 4 };
    for (int i : items) {
      list.insertBack(i);
    }
    Iterator<Integer> iter = list.iterator();
    iter.remove();
  }

  @Test(expected = IllegalStateException.class)
  public void iterator_throwIfRemoveTwice() {
    List<Integer> list = new DList<Integer>();
    Integer[] items = { 12, 3, 4 };
    for (int i : items) {
      list.insertBack(i);
    }
    Iterator<Integer> iter = list.iterator();
    iter.next();
    iter.remove();
    iter.remove();
  }

  @Test
  public void iterator_removeAll() {
    List<Integer> list = new DList<Integer>();
    Integer[] items = { 12, 3, 4 };
    String expectedResult[] = { "[  3  4  ]", "[  4  ]", "[  ]" };
    for (int i : items) {
      list.insertBack(i);
    }
    Iterator<Integer> iter = list.iterator();
    int i = 0;
    for (Integer item : items) {
      assertEquals("Should be correct item.", item, iter.next());
      iter.remove();
      assertEquals("Remove should mutate list correctly.", expectedResult[i++],
          list.toString());
    }
    assertFalse("Should not have more items.", iter.hasNext());
  }
}
