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

  /**
   * toString() returns a String representation of this MoveWithPlayer.
   *
   * @return a String representation of this MoveWithPlayer.
   */
  @Override
  public String toString() {
    String playerString = "";
    if (player == MachinePlayer.BLACK_PLAYER) {
      playerString = "BLACK";
    } else if (player == MachinePlayer.WHITE_PLAYER) {
      playerString = "WHITE";
    }
    switch (moveKind) {
      case QUIT:
        return "[quit]";
      case ADD:
        return "[add to " + x1 + "" + y1 + " " + playerString + "]";
      default:
        return "[step from " + x2 + "" + y2 + " to " + x1 + "" + y1 + " for " + playerString + "]";
    }
  }

  /**
   * equals() compares the specified object with this MoveWithPlayer for equality.  Return true if the given
   * object is also a MoveWithPlayer, the two MoveWithPlayers have the same coordinates, same move type,
   * and same player.
   *
   * @param obj object to be compared for equality with this MoveWithPlayer.
   *
   * @return true if the specified object is equal to this MoveWithPlayer.
   */
  @Override
  public boolean equals(Object obj) {
    if (obj == null || !(obj instanceof MoveWithPlayer)) {
      return false;
    }
    MoveWithPlayer other = (MoveWithPlayer) obj;
    return x1 == other.x1 && y1 == other.y1 && player == other.player && moveKind == other.moveKind &&
        x2 == other.x2 && y2 == other.y2;
  }

}
