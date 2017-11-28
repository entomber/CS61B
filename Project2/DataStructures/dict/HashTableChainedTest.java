package DataStructures.dict;

import org.junit.Test;
import player.IntegerArray;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class HashTableChainedTest {

  // change access of N before test
  @Test
  public void HashTableChained_parameter() {
    int[][] sizes = { {2,5}, {3,7}, {4,11}, {5,11}, {6,13}, {7,17}, {8,17}, {9,19} };
    for (int size[] : sizes) {
      HashTableChained t = new HashTableChained(size[0]);
      assertTrue("Constructor with sizeEstimate entries should generate prime number " +
          "of buckets.", t.N == size[1]);
      assertTrue("Constructor with sizeEstimate entries should generate table with  " +
          "load factor of at most 0.5", size[0] * 2 < t.N);
    }
  }

  // change access of N before test
  @Test
  public void HashTableChained_noParameter() {
    HashTableChained t = new HashTableChained();
      assertTrue("Constructor with sizeEstimate entries should generate prime number " +
          "of buckets and at least 100.", t.N == 101);
  }

  @Test
  public void compFunction() {
    HashTableChained t = new HashTableChained();
    int[] hashcodes = { Integer.MIN_VALUE, Integer.MIN_VALUE+1, Integer.MIN_VALUE/2,
        -3, -2, -1, 0, 1, 2, 3, Integer.MAX_VALUE/2, Integer.MAX_VALUE-1, Integer.MAX_VALUE};
    for (int hashcode : hashcodes) {
      assertTrue("Hash codes should hash to positive values.",
          t.compFunction(hashcode) >= 0);
    }
  }

  @Test
  public void insert() {
    HashTableChained t = new HashTableChained();
    Object key;
    Object value;

    assertTrue("Size should be 0.", t.size() == 0);

    key = "David";
    value = "Bowie";
    t.insert(key, value);
    Entry entry = t.find(key);
    assertEquals("Should find entry.", value, entry.value());
    assertTrue("Size should be 1.", t.size() == 1);

    key = null;
    value = null;
    t.insert(key, value);
    entry = t.find(key);
    assertEquals("Should not find entry.", null, entry);
    assertTrue("Size should be 1.", t.size() == 1);

    key = "Donnie";
    value = null;
    t.insert(key, value);
    entry = t.find(key);
    assertEquals("Should find entry.", null, entry.value());
    assertTrue("Size should be 2.", t.size() == 2);

    key = "david";
    value = "Bowie";
    t.insert(key, value);
    entry = t.find(key);
    assertEquals("Should find entry.", value, entry.value());
  }

  @Test
  public void find() {
    HashTableChained t = new HashTableChained();
    Object key;
    Object value;

    key = "David";
    value = "Bowie";
    Entry entry = t.find(key);
    assertEquals("Should not find entry.", null, entry);

    t.insert(key, value);
    entry = t.find(key);
    assertEquals("Should find entry.", value, entry.value());

    value = "Johnson";
    t.insert(key, value);
    entry = t.find(key);
    assertEquals("Should find entry.", key, entry.key());

    t.remove(key);
    entry = t.find(key);
    assertEquals("Should find entry.", key, entry.key());
  }

  @Test
  public void find_multipleArrays() {
    HashTableChained t = new HashTableChained();
    Integer[] a = {1, 2};
    IntegerArray key1 = new IntegerArray(a);
    int value1 = 3;

    t.insert(key1, value1);
    Entry entry = t.find(key1);
    System.out.println(key1.hashCode());
    assertEquals("Should find entry.", value1, entry.value());

    Integer[] b = {1, 2};
    IntegerArray key1New = new IntegerArray(b);
    entry = t.find(key1New);
    System.out.println(key1New.hashCode());
    assertEquals("Should find entry.", key1New, entry.key());
  }

  @Test
  public void remove() {
    HashTableChained t = new HashTableChained();
    Object key;
    Object value;

    key = "David";
    value = "Bowie";
    Entry entry = t.remove(key);
    assertEquals("Should not remove entry.", null, entry);

    entry = t.insert(key, value);
    assertEquals("Should find entry.", value, entry.value());

    value = "Johnson";
    t.insert(key, value);
    entry = t.find(key);
    assertEquals("Should find entry.", key, entry.key());

    t.remove(key);
    entry = t.find(key);
    assertEquals("Should find entry.", key, entry.key());

    t.remove(key);
    entry = t.find(key);
    assertEquals("Should not find entry.", null, entry);

    key = "David";
    t.insert(key, value);
    entry = t.find(key);
    assertEquals("Should find entry.", key, entry.key());
  }

  @Test
  public void makeEmpty() {
    HashTableChained t = new HashTableChained();
    Object[][] objects = { {"David","Bowie"}, {"Don", "Johnson"}, {"David","Anderson"},
        {"Bill",null}, {"Bill","Nye"}, {"Steve","Smith"}, {"Steve",null} };

    for (Object[] object : objects) {
      Object key = object[0];
      Object value = object[1];
      t.insert(key, value);
    }

    t.makeEmpty();

    for (Object[] object : objects) {
      Object key = object[0];
      Entry entry = t.find(key);
      assertEquals("Should not find entry.", null, entry);
      assertTrue("Size should be 0.", t.size() == 0);
    }
  }

  @Test
  public void iterator_empty() {
    HashTableChained table = new HashTableChained();
    assertFalse("Should be empty.", table.iterator().hasNext());
  }

  @Test(expected = NoSuchElementException.class)
  public void iterator_throwIfNextOnEmpty() {
    HashTableChained table = new HashTableChained();
    table.iterator().next();
  }

  @Test(expected = NoSuchElementException.class)
  public void iterator_throwIfIterateBeyond() {
    HashTableChained table = new HashTableChained();
    table.insert("John", "Cena");
    table.insert("Dave", "Chapelle");
    Iterator<Entry> iter = table.iterator();
    iter.next();
    iter.next();
    iter.next();
  }

  @Test
  public void iterator_standardIteration() {
    HashTableChained table = new HashTableChained();
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