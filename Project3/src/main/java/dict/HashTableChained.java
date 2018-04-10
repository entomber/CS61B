/* HashTableChained.java */

package dict;

import list.List;
import list.ListNode;
import list.DList;
import list.InvalidNodeException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  HashTableChained implements a Dictionary as a hash table with chaining.
 *  All objects used as keys must have a valid hashCode() method, which is
 *  used to determine which bucket of the hash table an entry is stored in.
 *  Each object's hashCode() is presumed to return an int between
 *  Integer.MIN_VALUE and Integer.MAX_VALUE.  The HashTableChained class
 *  implements only the compression function, which maps the hash code to
 *  a bucket in the table's range.
 *
 *  This is my HW6 solution expanded to use Iterable interface, expand hash table
 *  when load factor >= 0.75, and shrink hash table when load factor < 0.25.
 **/

public class HashTableChained implements Dictionary, Iterable<Entry> {

  /**
   *  LOWER_THRESHOLD is the lower load factor (size/N) threshold to hit before shrinking the table.
   *  UPPER_THRESHOLD is the upper load factor (size/N) threshold to hit before expanding the table.
   *  DEFAULT_SIZE is the default size of the table when no initial size is supplied to the constructor.
   *  size is the number of entries in the dictionary.
   *  N is the number of buckets in the dictionary.
   *  table stores lists of entries.
   **/
  private final static double LOWER_THRESHOLD = 0.25;
  private final static double UPPER_THRESHOLD = 0.75;
  private final static int DEFAULT_SIZE = 50;
  private int size;
  private int N;
  private Object[] table;



  /** 
   *  Construct a new empty hash table intended to hold roughly sizeEstimate
   *  entries.  (The precise number of buckets is up to you, but we recommend
   *  you use a prime number, and shoot for a load factor between 0.5 and 1.)
   **/

  public HashTableChained(int sizeEstimate) {
    // set up hash table with initial load factor of ~0.5 and N to first prime > sizeEstimate * 2
    N = getFirstPrimeAfter(sizeEstimate * 2);
    table = new Object[N];
    size = 0;
  }

  /** 
   *  Construct a new empty hash table with a default size.  Say, a prime in
   *  the neighborhood of 100.
   **/

  public HashTableChained() {
    this(DEFAULT_SIZE);
  }

  // returns input if prime or the first prime number after input
  private int getFirstPrimeAfter(int x) {
    int prime = x;
    boolean primeFound = false;
    while (!primeFound) {
      primeFound = true;
      for (int i = 2; i <= Math.sqrt(prime); i++) {
        if (prime % i == 0) {
          prime++;
          primeFound = false;
          break;
        }
      }
    }
    return prime;
  }

  /**
   *  Converts a hash code in the range Integer.MIN_VALUE...Integer.MAX_VALUE
   *  to a value in the range 0...(size of hash table) - 1.
   *
   *  @return an int in the range [0, N-1] that maps a hash code to a bucket
   **/

  int compFunction(int code, int N) {
    int a = 3;
    int b = 7;
    int p = 16908799;
    long hash = ((a*code + b) % p) % N;
    if (hash < 0) {
      hash += N;
    }
    return (int) hash;
  }

  /** 
   *  Returns the number of entries stored in the dictionary.  Entries with
   *  the same key (or even the same key and value) each still count as
   *  a separate entry.
   *
   *  @return number of entries in the dictionary.
   **/

  public int size() {
    return size;
  }

  /** 
   *  Tests if the dictionary is empty.
   *
   *  @return true if the dictionary has no entries; false otherwise.
   **/

  public boolean isEmpty() {
    return size == 0;
  }

  /**
   *  Create a new Entry object referencing the input key and associated value,
   *  and insert the entry into the dictionary.  Return a reference to the new
   *  entry.  Multiple entries with the same key (or even the same key and
   *  value) can coexist in the dictionary.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the key by which the entry can be retrieved.
   *  @param value an arbitrary object.
   *  @return an entry containing the key and value.
   **/

  public Entry insert(Object key, Object value) {
    if (key == null) {
      return null;
    }
    if ((double) (size + 1) / N >= UPPER_THRESHOLD) {
      expandHashTable();
    }

    int index = compFunction(key.hashCode(), N);
    List<Entry> list;
    if (table[index] == null) {
      list = new DList<Entry>();
      table[index] = list;
    } else {
      list = (List<Entry>) table[index];
    }

    Entry entry = new Entry(key, value);
    list.insertBack(entry);
    size++;
    return entry;
  }

  // doubles the size of the hash table when load factor >= 0.75
  private void expandHashTable() {
    changeTableSize(2);
  }

  // new table size is updated by multiplier given
  private void changeTableSize(double multiplier) {
    int newN = getFirstPrimeAfter( (int)(N * multiplier) );
    Object[] newTable = new Object[newN];
    for (Entry entry : this) {
      int index = compFunction(entry.key.hashCode(), newN);
      List<Entry> list;

      if (newTable[index] == null) {
        list = new DList<Entry>();
        newTable[index] = list;
      } else {
        list = (List<Entry>) newTable[index];
      }
      Entry newEntry = new Entry(entry.key, entry.value);
      list.insertBack(newEntry);
    }
    N = newN;
    table = newTable;
  }

  /** 
   *  Search for an entry with the specified key.  If such an entry is found,
   *  return it; otherwise return null.  If several entries have the specified
   *  key, choose one arbitrarily and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   **/

  public Entry find(Object key) {
    if (key == null) {
      return null;
    }
    int index = compFunction(key.hashCode(), N);
    // No list for key mapped to bucket, return null
    if (table[index] == null) {
      return null;
    }
    // List exists for key mapped to bucket, check for an entry in the list with same key
    List<Entry> list = (List<Entry>) table[index];
    for (Entry entry : list) {
      if (entry.key().equals(key)) {
        return entry;
      }
    }
    // entry with matching key not found, return null
    return null;
  }

  /** 
   *  Remove an entry with the specified key.  If such an entry is found,
   *  remove it from the table and return it; otherwise return null.
   *  If several entries have the specified key, choose one arbitrarily, then
   *  remove and return it.
   *
   *  This method should run in O(1) time if the number of collisions is small.
   *
   *  @param key the search key.
   *  @return an entry containing the key and an associated value, or null if
   *          no entry contains the specified key.
   */

  public Entry remove(Object key) {
    if (key == null) {
      return null;
    }
    if (((double) (size - 1) / N) < LOWER_THRESHOLD) {
      shrinkHashTable();
    }

    int index = compFunction(key.hashCode(), N);
    // No list for key mapped to bucket, return null
    if (table[index] == null) {
      return null;
    }

    // List exists for key mapped to bucket, check for an entry in the list with same key
    List<Entry> list = (List<Entry>) table[index];
    Iterator<Entry> iter = list.iterator();
    while (iter.hasNext()) {
      Entry entry = iter.next();
      if (entry.key().equals(key)) {
        iter.remove();
        size--;
        if (list.isEmpty()) {
          table[index] = null;
        }
        return entry;
      }
    }
    // entry with matching key not found, return null
    return null;
  }

  // reduces the size of the hash table by half when load factor < 0.25
  private void shrinkHashTable() {
    changeTableSize(0.5);
  }

  /**
   *  makeEmpty() removes all entries from the dictionary while keeping its size.  Use it when you
   *  want to re-use the hash table for the same purpose.
   */
  public void makeEmpty() {
    for (int i = 0; i < table.length; i++) {
      table[i] = null;
    }
    size = 0;
  }


  /**
   *  iterator() returns a newly created Iterator that can iterate through
   *  this HashTableChained.
   *
   *  @return a newly created Iterator object set to the first item stored
   *  in this HashTableChained.
   */
  public Iterator<Entry> iterator() {
    return new HashTableIterator();
  }

  private class HashTableIterator implements Iterator<Entry> {
    private ListNode<Entry> currentNode = null;
    private int currentBucket = 0;
    private int entriesRemaining = size;

    public boolean hasNext() {
      return entriesRemaining > 0;
    }

    public Entry next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }
      // first call to next
      if (currentNode == null) {
        for (int i = 0; i < table.length; i++) {
          Object list = table[i];
          if (list != null) {
            currentNode = ((List<Entry>) list).front();
            if (currentNode.isValidNode()) {
              entriesRemaining--;
              currentBucket = i;
              try {
                return currentNode.item();
              } catch (InvalidNodeException e) {
                e.printStackTrace();
              }
            }
          }
        }
      } else {
        try {
          currentNode = currentNode.next();
        } catch (InvalidNodeException e) {
          e.printStackTrace();
        }
        // if there's other nodes in bucket
        if (currentNode.isValidNode()) {
          entriesRemaining--;
          try {
            return currentNode.item();
          } catch (InvalidNodeException e) {
            e.printStackTrace();
          }
        }
        // no more nodes in bucket, look in other buckets
        else {
          for (int i = currentBucket + 1; i < table.length; i++) {
            Object list = table[i];
            if (list != null) {
              currentNode = ((List<Entry>) list).front();
              if (currentNode.isValidNode()) {
                entriesRemaining--;
                currentBucket = i;
                try {
                  return currentNode.item();
                } catch (InvalidNodeException e) {
                  e.printStackTrace();
                }
              }
            }
          }
        }
      }
      throw new NoSuchElementException();
    }

    public void remove() {
      throw new UnsupportedOperationException();
    }

  }

  /**
   * toString() returns a String representation of this hash table.
   *
   * @return a string representation of this hash table.
   */
  public String toString() {
    int collisions = 0;
    int bucketEntries = 0;
    int largestBucket = 0;
    StringBuilder sb = new StringBuilder(200);

    for (int i = 0; i < table.length; i++) {
      Object list = table[i];
      if (list != null) {
        sb.append(i);
        sb.append(": ");
        bucketEntries = ((List<Entry>) list).length();
        collisions += bucketEntries - 1;
        if (bucketEntries > largestBucket) {
          largestBucket = bucketEntries;
        }
      }
      if (bucketEntries > 0) {
        sb.append(bucketEntries);
        sb.append("\n");
        bucketEntries = 0;
      }
    }

    sb.append("\nTotal collisions: ");
    sb.append(collisions);
    sb.append("\nLargest bucket: ");
    sb.append(largestBucket);

    return sb.toString();
  }

}
