/* Move.java  */

package player;

/**
 *  A public class for holding all the fields in a move.  This class is a
 *  container for data, not an ADT; hence, all fields are public.  It has
 *  been extended to save which player made a move.
 *
 */

public class MoveWithPlayer extends Move {

  public int player;

  // Construct a step move.
  public MoveWithPlayer(int xx1, int yy1, int xx2, int yy2, int player) {
    super(xx1, yy1, xx2, yy2);
    this.player = player;
  }

  // Construct an add move.
  public MoveWithPlayer(int x, int y, int player) {
    super(x, y);
    this.player = player;
  }

  // toString() converts the move to a String.
  public String toString() {
    switch (moveKind) {
      case QUIT:
        return "[quit]";
      case ADD:
        return "[add to " + x1 + "" + y1 + "]";
      default:
        return "[step from " + x2 + "" + y2 + " to " + x1 + "" + y1 + "]";
    }
  }

}
