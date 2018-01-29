package DataStructures.dict;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import player.IntegerArray;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class HashTableChainedTest {
  private HashTableChained table;
  private Field field;

  @Before
  public void setup() throws NoSuchFieldException {
    table = new HashTableChained();
    field = table.getClass().getDeclaredField("N");
    field.setAccessible(true);
  }

  @After
  public void tearDown() {
    table.makeEmpty();
  }

  @Test
  public void HashTableChained_parameter() throws IllegalAccessException {
    int[][] sizes = { {2,5}, {3,7}, {4,11}, {5,11}, {6,13}, {7,17}, {8,17}, {9,19} };
    for (int size[] : sizes) {
      table = new HashTableChained(size[0]);
      int N = (int) field.get(table);
      assertTrue("Constructor with sizeEstimate entries should generate prime number " +
          "of buckets.", N == size[1]);
      assertTrue("Constructor with sizeEstimate entries should generate table with  " +
          "load factor of at most 0.5", size[0] * 2 < N);
    }
  }

  @Test
  public void HashTableChained_noParameter() throws IllegalAccessException {
      assertTrue("Constructor with sizeEstimate entries should generate prime number " +
          "of buckets and at least 100.", (int) field.get(table) == 101);
  }

  @Test
  public void compFunction() throws IllegalAccessException {
    int[] hashcodes = { Integer.MIN_VALUE, Integer.MIN_VALUE+1, Integer.MIN_VALUE/2,
        -3, -2, -1, 0, 1, 2, 3, Integer.MAX_VALUE/2, Integer.MAX_VALUE-1, Integer.MAX_VALUE};
    for (int hashcode : hashcodes) {
      assertTrue("Hash codes should hash to positive values.",
          table.compFunction(hashcode, (int) field.get(table)) >= 0);
    }
  }

  @Test
  public void insert() {
    Object key;
    Object value;

    assertTrue("Size should be 0.", table.size() == 0);

    key = "David";
    value = "Bowie";
    table.insert(key, value);
    Entry entry = table.find(key);
    assertEquals("Should find entry.", value, entry.value());
    assertTrue("Size should be 1.", table.size() == 1);

    key = null;
    value = null;
    table.insert(key, value);
    entry = table.find(key);
    assertEquals("Should not find entry.", null, entry);
    assertTrue("Size should be 1.", table.size() == 1);

    key = "Donnie";
    value = null;
    table.insert(key, value);
    entry = table.find(key);
    assertEquals("Should find entry.", null, entry.value());
    assertTrue("Size should be 2.", table.size() == 2);

    key = "david";
    value = "Bowie";
    table.insert(key, value);
    entry = table.find(key);
    assertEquals("Should find entry.", value, entry.value());
  }

  @Test
  public void find() {
    Object key;
    Object value;

    key = "David";
    value = "Bowie";
    Entry entry = table.find(key);
    assertEquals("Should not find entry.", null, entry);

    table.insert(key, value);
    entry = table.find(key);
    assertEquals("Should find entry.", value, entry.value());

    value = "Johnson";
    table.insert(key, value);
    entry = table.find(key);
    assertEquals("Should find entry.", key, entry.key());

    table.remove(key);
    entry = table.find(key);
    assertEquals("Should find entry.", key, entry.key());
  }

  @Test
  public void find_multipleArrays() {
    Integer[] a = {1, 2};
    IntegerArray key1 = new IntegerArray(a);
    int value1 = 3;

    table.insert(key1, value1);
    Entry entry = table.find(key1);
    assertEquals("Should find entry.", value1, entry.value());

    Integer[] b = {1, 2};
    IntegerArray key1New = new IntegerArray(b);
    entry = table.find(key1New);
    assertEquals("Should find entry.", key1New, entry.key());
  }

  @Test
  public void remove() {
    Object key;
    Object value;

    key = "David";
    value = "Bowie";
    Entry entry = table.remove(key);
    assertEquals("Should not remove entry.", null, entry);

    entry = table.insert(key, value);
    assertEquals("Should find entry.", value, entry.value());

    value = "Johnson";
    table.insert(key, value);
    entry = table.find(key);
    assertEquals("Should find entry.", key, entry.key());

    table.remove(key);
    entry = table.find(key);
    assertEquals("Should find entry.", key, entry.key());

    table.remove(key);
    entry = table.find(key);
    assertEquals("Should not find entry.", null, entry);

    key = "David";
    table.insert(key, value);
    entry = table.find(key);
    assertEquals("Should find entry.", key, entry.key());
  }

  @Test
  public void makeEmpty() {
    Object[][] objects = { {"David","Bowie"}, {"Don", "Johnson"}, {"David","Anderson"},
        {"Bill",null}, {"Bill","Nye"}, {"Steve","Smith"}, {"Steve",null} };

    for (Object[] object : objects) {
      Object key = object[0];
      Object value = object[1];
      table.insert(key, value);
    }

    table.makeEmpty();

    for (Object[] object : objects) {
      Object key = object[0];
      Entry entry = table.find(key);
      assertEquals("Should not find entry.", null, entry);
      assertTrue("Size should be 0.", table.size() == 0);
    }
  }

  @Test
  public void insert_expandTable() throws IllegalAccessException {
    table = new HashTableChained(2);

    int[][] items = { {1,1}, {2,2}, {3,3}, {4,4}, {5,5}, {6,6}, {7,7}, {8,8}, {9,9} };
    int[] Nvalue =  { 5, 11, 23 };
    int nIndex = 0;
    for (int i = 0; i < items.length; i++) {
      int key = items[i][0];
      int value = items[i][1];
      table.insert(key, value);
      if (i == 0 || i == 3 || i == 8) {
        assertEquals("New table should have correct N value", Nvalue[nIndex++], (int) field.get(table));
      }
    }

    for (int[] item : items) {
      Object key = item[0];
      Object value = item[1];
      assertEquals("All entries found.", value, table.find(key).value);
    }
  }

  @Test
  public void iterator_empty() {
    assertFalse("Should be empty.", table.iterator().hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void iterator_throwIfNextOnEmpty() {
    table.iterator().next();
  }

  @Test(expected = NoSuchElementException.class)
  public void iterator_throwIfIterateBeyond() {
    table.insert("John", "Cena");
    table.insert("Dave", "Chapelle");
    Iterator<Entry> iter = table.iterator();
    iter.next();
    iter.next();
    iter.next();
  }

  @Test
  public void iterator_standardIteration() {
    Object[][] objects = { {"David","Bowie"}, {"Don", "Johnson"}, {"Mr.","Anderson"},
        {"Bill","Nye"}, {"Steve","Smith"}};
    for (Object[] object : objects) {
      Object key = object[0];
      Object value = object[1];
      table.insert(key, value);
    }
    Iterator<Entry> iter = table.iterator();
    for (Object[] object : objects) {
      assertTrue("Should have next item.", iter.hasNext());
      iter.next();
    }
    assertFalse("Should not have more items.", iter.hasNext());
  }

}