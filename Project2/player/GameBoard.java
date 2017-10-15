package player;

public class GameBoard {

  /**
   *  Creates a new game board
   */
  public GameBoard() {

  }

  /**
   *  isValidMove() determines whether a Move "m" is valid for player "side".
   *  A full description of what constitutes a valid move appears in the
   *  project "README" file.
   *
   *  @param side is MachinePlayer.COMPUTER or MachinePlayer.OPPONENT
   *  @param m is the move player "side" wants to make
   *  @return true if Move "m" is valid for player "side", false otherwise.
   **/
  protected boolean isValidMove(int side, Move m) {
    return false;
  }

  /**
   *  getValidMoves() generates a List of all valid moves for player "side".
   *  A full description of what constitutes a valid move appears in the
   *  project "README" file.
   *
   *  @param side is MachinePlayer.COMPUTER or MachinePlayer.OPPONENT
   *  @return a List of all valid moves for player "side".
   **/
  public List getValidMoves(int side) {
    return null;
  }

  /**
   *  getConnections() generates a List of chips (prior Moves) that form a
   *  connection with the current chip (current Move).
   *
   *  @param side is MachinePlayer.COMPUTER or MachinePlayer.OPPONENT
   *  @param m is the move player "side" wants to make
   *  @return a List of all prior moves that form a connection with Move "m".
   **/
  protected List getConnections(int side, Move m) {
    return null;
  }

  /**
   *  hasValidNetwork() determines whether "this" GameBoard has a valid network
   *  for player "side".  (Does not check whether the opponent has a network.)
   *  A full description of what constitutes a valid network appears in the
   *  project "README" file.
   *
   *  Unusual conditions:
   *    If side is neither MachinePlayer.COMPUTER nor MachinePlayer.OPPONENT,
   *      returns false.
   *    If GameBoard squares contain illegal values, the behavior of this
   *      method is undefined (i.e., don't expect any reasonable behavior).
   *
   *  @param side is MachinePlayer.COMPUTER or MachinePlayer.OPPONENT
   *  @return true if player "side" has a winning network in "this" GameBoard,
   *    false otherwise.
   **/
  protected boolean hasValidNetwork(int side) {
    return false;
  }
}
