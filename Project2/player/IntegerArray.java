package player;

/**
 * A public wrapper class for Integer arrays.  This class overrides the default hashCode()
 * and equals() method for Integer arrays so they can be used as keys in hash tables.
 */

public class IntegerArray {
  public Integer[] component;

  /**
   * IntegerArray() constructs a new wrapped Integer array.
   *
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
   */
  @Override
  public int hashCode() {
    int hashCode = 1;
    for (int item : component) {
      hashCode = 31 * hashCode + item;
    }
    return hashCode;
  }


  /**
   * equals() compares the specified object with this IntegerArray for equality.  Return true if the given
   * object is also a IntegerArray, the two IntegerArray contain the same number of Integers, and every
   * component of the given IntegerArray is contained in this IntegerArray.
   * and same player.
   *
   * @param obj object to be compared for equality with this IntegerArray.
   *
   * @return true if the specified object is equal to this IntegerArray.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof IntegerArray)) {
      return false;
    }
    IntegerArray other = (IntegerArray) obj;
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

  /**
   * toString() returns a String representation of this IntegerArray.
   *
   * @return a String representation of this IntegerArray.
   */
  @Override
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
