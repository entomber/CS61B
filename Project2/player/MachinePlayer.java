/* MachinePlayer.java */

package player;

import DataStructures.List.List;
import DataStructures.List.ListNode;
import DataStructures.List.InvalidNodeException;

/**
 * An implementation of an automatic Network player.  Keeps track of moves
 * made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

  private final static int BLACK_PLAYER = 0;
  private final static int WHITE_PLAYER = 1;

  private int color;
  private int searchDepth;
  private GameBoard board;

  // Creates a machine player with the given color.  Color is either 0 (black)
  // or 1 (white).  (White has the first move.)
  public MachinePlayer(int color) {
    if (color == 0) {
      this.color = BLACK_PLAYER;
    } else if (color == 1) {
      this.color = WHITE_PLAYER;
    } else {
      throw new IllegalArgumentException("color: " + color + " is invalid.");
    }
    board = new GameBoard();
    searchDepth = 1; // update later
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    if (color == 0) {
      this.color = BLACK_PLAYER;
    } else if (color == 1) {
      this.color = WHITE_PLAYER;
    } else {
      throw new IllegalArgumentException("color: " + color + " is invalid.");
    }
    board = new GameBoard();
    this.searchDepth = searchDepth;

  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    // make simple move
    List<Move> moves = board.getValidMoves(color);
    ListNode<Move> node = moves.front();
    Move move = null;
    try {
      move = node.item();
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
    if (color == BLACK_PLAYER) {
      board.setChip(BLACK_PLAYER, move);
    } else {
      board.setChip(WHITE_PLAYER, move);
    }
    return move;
  }

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    return (color == BLACK_PLAYER && board.setChip(BLACK_PLAYER, m)) ||
        (color == WHITE_PLAYER && board.setChip(WHITE_PLAYER, m));
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    return (color == BLACK_PLAYER && board.setChip(BLACK_PLAYER, m)) ||
        (color == WHITE_PLAYER && board.setChip(WHITE_PLAYER, m));
  }

  protected int getColor() {
    return color;
  }
}
