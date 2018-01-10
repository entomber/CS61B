/* Entry.java */

package DataStructures.dict;

/**
 *  A class for dictionary entries.  It is part of the interface of the
 *  Dictionary ADT.
 **/

public class Entry {

  protected Object key;
  protected Object value;

  public Entry() { }

  public Entry(Object key, Object value) {
    this.key = key;
    this.value = value;
  }

  public Object key() {
    return key;
  }

  public Object value() {
    return value;
  }

}
