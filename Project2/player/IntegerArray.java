package player;

/**
 * A public wrapper class for Integer arrays.  This class overrides the default hashCode()
 * and equals() method for Integer arrays so they can be used as keys in hash tables.
 */

public class IntegerArray {
  public Integer[] component;

  /**
   * IntegerArray() constructs a new wrapped Integer array.
   * @param component the Integer array to wrap.
   */
  public IntegerArray(Integer[] component) {
    this.component = component;
  }

  /**
   *  hashCode() returns the hash code value for this IntegerArray.  It is similar to that
   *  defined in Interface List<E>, which returns a hash code as the result of the following
   *  calculation:
   *
   *  int hashCode = 1;
   *  for (int item : component)
   *    hashCode = 31 * hashCode + item;
   *
   *  @return hash code for this IntegerArray.
   *
   */
  public int hashCode() {
    int hashCode = 1;
    for (int item : component) {
      hashCode = 31 * hashCode + item;
    }
    return hashCode;
  }

  public boolean equals(Object array) {
    if (array == null || !(array instanceof IntegerArray)) {
      return false;
    }
    IntegerArray other = (IntegerArray) array;
    if (other.component.length != component.length) {
      return false;
    }
    for (int i = 0; i < component.length; i++) {
      if (!component[i].equals(other.component[i])) {
        return false;
      }
    }
    return true;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder(20);
    sb.append("[");
    for (int item : component) {
      sb.append(item);
      sb.append(", ");
    }
    sb.delete(sb.length() - 2, sb.length());
    sb.append("]");
    return sb.toString();
  }

}
