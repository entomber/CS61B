/* MachinePlayer.java */

package player;

/**
 * An implementation of an automatic Network player.  Keeps track of moves
 * made by both players.  Can select a move for itself.
 */
public class MachinePlayer extends Player {

  public final static int BLACK_PLAYER = 0;
  public final static int WHITE_PLAYER = 1;

  private final int color;
  private int searchDepthAdd;
  private int searchDepthStep;
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
    searchDepthAdd = 5;
    searchDepthStep = 4;
  }

  // Creates a machine player with the given color and search depth.  Color is
  // either 0 (black) or 1 (white).  (White has the first move.)
  public MachinePlayer(int color, int searchDepth) {
    this(color);
    searchDepthAdd = searchDepth;
    searchDepthStep = searchDepth;
  }

  // Returns a new move by "this" player.  Internally records the move (updates
  // the internal game board) as a move by "this" player.
  public Move chooseMove() {
    // make simple move
    GameTree gameTree = new GameTree(board, color, searchDepthAdd, searchDepthStep);
    MoveWithPlayer move = gameTree.search();
    board.setChip(move);
    return move;
  }

  // If the Move m is legal, records the move as a move by the opponent
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method allows your opponents to inform you of their moves.
  public boolean opponentMove(Move m) {
    // TODO: should player color be reversed?
    MoveWithPlayer move = null;
    int colorOpponent;
    if (color == BLACK_PLAYER) {
      colorOpponent = WHITE_PLAYER;
    } else {
      colorOpponent = BLACK_PLAYER;
    }
    if (m.moveKind == Move.ADD) {
      move = new MoveWithPlayer(m.x1, m.y1, colorOpponent);
    } else if (m.moveKind == Move.STEP) {
      move = new MoveWithPlayer(m.x1, m.y1, m.x2, m.y2, colorOpponent);
    }
    return board.setChip(move);
  }

  // If the Move m is legal, records the move as a move by "this" player
  // (updates the internal game board) and returns true.  If the move is
  // illegal, returns false without modifying the internal state of "this"
  // player.  This method is used to help set up "Network problems" for your
  // player to solve.
  public boolean forceMove(Move m) {
    MoveWithPlayer move = (MoveWithPlayer) m;
    if (color == BLACK_PLAYER) {
      move.player = BLACK_PLAYER;
    } else {
      move.player = WHITE_PLAYER;
    }
    return board.setChip(move);
  }

  protected int getColor() {
    return color;
  }
}
