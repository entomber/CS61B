package player;

import DataStructures.List.List;
import DataStructures.List.ListNode;
import DataStructures.List.DList;
import DataStructures.List.InvalidNodeException;

public class GameBoard {

  /**
   * BOARD_SIZE is the size of the game board.
   * NO_CHIP is stored in a square without any chip.
   * WHITE_CHIP is stored in a square with a white chip.
   * BLACK_CHIP is stored in a square with a black chip.
   * CORNER is stored in a square that is a corner.
   *
   * board is the internal representation of the game board
   * whiteChipCount is the amount of white chips on the board
   * blackChipCount is the amount of black chips on the board
   * whiteChipPositions references an Integer array of the white chip positions on the board
   * blackChipPositions references an Integer array of the black chip positions on the board
   */
  protected final static int BOARD_SIZE = 8;
  protected final static int NO_CHIP = 0;
  protected final static int WHITE_CHIP = 1;
  protected final static int BLACK_CHIP = 2;
  protected final static int CORNER = 3;
  private int[][] board;
  /*private */int whiteChipCount;
  /*private */int blackChipCount;
  private List<Integer[]> whiteChipPositions;
  private List<Integer[]> blackChipPositions;

  /**
   *  Creates a new game board
   */
  public GameBoard() {
    board = new int[BOARD_SIZE][BOARD_SIZE];
    board[0][0] = CORNER;
    board[BOARD_SIZE-1][BOARD_SIZE-1] = CORNER;
    board[0][BOARD_SIZE-1] = CORNER;
    board[BOARD_SIZE-1][0] = CORNER;
    whiteChipCount = 0;
    blackChipCount = 0;
    whiteChipPositions = new DList<>();
    blackChipPositions = new DList<>();
  }

  /**
   *  setWhiteChip() sets a white chip on the board.  For step moves, it removes the
   *  chip from the previous position.
   *
   *  @return true if move was valid and completed, false otherwise.
   *
   *  Performance: runs in O(1) time.
   */
  protected boolean setWhiteChip(Move m) {
    if (!isValidMove(true, m)) {
      return false;
    }

    if (whiteChipCount == 10) {
      // remove the chip in old position for step move
      ListNode<Integer[]> node = whiteChipPositions.front();
      while (node.isValidNode()) {
        try {
          Integer[] square = node.item();
          if (m.x2 == square[0] && m.y2 == square[1]) {
            node.remove();
            break;
          }
          node = node.next();
        } catch (InvalidNodeException e) {
          e.printStackTrace();
        }
      }
      board[m.x2][m.y2] = NO_CHIP;
      board[m.x1][m.y1] = WHITE_CHIP;
    } else {
      board[m.x1][m.y1] = WHITE_CHIP;
      whiteChipCount++;
    }
    Integer[] square = {m.x1, m.y1};
    whiteChipPositions.insertBack(square);

    return true;
  }

  /**
   *  setWhiteChip() sets a white chip on the board.  For step moves, it removes the
   *  chip from the previous position.
   *
   *  @return true if move was valid and completed, false otherwise.
   *
   *  Performance: runs in O(1) time.
   */
  protected boolean setBlackChip(Move m) {
    if (!isValidMove(false, m)) {
      return false;
    }

    if (blackChipCount == 10) {
      // remove the chip in old position for step move
      ListNode<Integer[]> node = blackChipPositions.front();
      while (node.isValidNode()) {
        try {
          Integer[] square = node.item();
          if (m.x2 == square[0] && m.y2 == square[1]) {
            node.remove();
            break;
          }
          node = node.next();
        } catch (InvalidNodeException e) {
          e.printStackTrace();
        }
      }
      board[m.x2][m.y2] = NO_CHIP;
      board[m.x1][m.y1] = BLACK_CHIP;
    } else {
      board[m.x1][m.y1] = BLACK_CHIP;
      blackChipCount++;
    }
    Integer[] square = {m.x1, m.y1};
    blackChipPositions.insertBack(square);

    return true;
  }

  /**
   *  isValidMove() determines whether a Move "m" is valid for player.
   *  A full description of what constitutes a valid move appears in the
   *  project "README" file.
   *
   *  @param whitePlayer true if white player, false if black player
   *  @param m is the move player wants to make
   *  @return true if Move "m" is valid for player, false otherwise.
   *
   *  Performance: runs in O(1) time.
   **/
  private boolean isValidMove(boolean whitePlayer, Move m) {
    // No chip may be placed outside game board.
    if (m.x1 < 0 || m.x1 > BOARD_SIZE-1 || m.y1 < 0 || m.y1 > BOARD_SIZE-1) {
      return false;
    }
    // No chip may be placed in any of the four corners.
    if (inCornerArea(m.x1, m.y1)) {
      return false;
    }
    // No chip may be placed in a goal of the opposite color.
    if ((whitePlayer && inBlackGoalArea(m.x1, m.y1)) ||
        (!whitePlayer && inWhiteGoalArea(m.x1, m.y1))) {
      return false;
    }
    // No chip may be placed in a square that is already occupied.
    if (board[m.x1][m.y1] != NO_CHIP) {
      return false;
    }
    // No clusters of three or more chips of the same color.
    if (whitePlayer && neighboringChips(WHITE_CHIP, m.x1, m.y1, -1, -1) > 2 ||
        !whitePlayer && neighboringChips(BLACK_CHIP, m.x1, m.y1, -1, -1) > 2) {
      return false;
    }
    // Player attempting step move when they have less than 10 chips on the board.
    if (m.moveKind == Move.STEP && ((whitePlayer && whiteChipCount < 10) ||
        (!whitePlayer && blackChipCount < 10))) {
      return false;
    }
    // Player attempting add move when they have 10 or more chips on the board.
    if (m.moveKind == Move.ADD && ((whitePlayer && whiteChipCount == 10) ||
        (!whitePlayer && blackChipCount >= 10))) {
      return false;
    }
    // Player attempting step move to the same square.
    if (m.moveKind == Move.STEP && m.x1 == m.x2 && m.y1 == m.y2) {
      return false;
    }

    return true;
  }

  /**
   *  getValidMoves() generates a List of all valid moves for the player.
   *  A full description of what constitutes a valid move appears in the
   *  project "README" file.
   *
   *  @param whitePlayer true if white player, false if black player
   *  @return a List of all valid moves for the player.
   *
   *  Performance: runs in O(n^2) time, where n is the size of the board, BOARD_SIZE
   **/
  protected List<Move> getValidMoves(boolean whitePlayer) {
    List<Move> moves = new DList<>();

    // add move
    if ((whitePlayer && whiteChipCount < 10) || (!whitePlayer && blackChipCount < 10)) {
      for (int y = 0; y < BOARD_SIZE; y++) {
        for (int x = 0; x < BOARD_SIZE; x++) {
          Move m = new Move(x, y);
          if (isValidMove(whitePlayer, m)) {
            moves.insertBack(m);
          }
        }
      }
    }
    // step move for white player
    else if (whitePlayer) {
      ListNode<Integer[]> node = whiteChipPositions.front();
      while (node.isValidNode()) {
        try {
          Integer[] square = node.item();
          for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
              Move m = new Move(x, y, square[0], square[1]);
              if (isValidMove(true, m)) {
                moves.insertBack(m);
              }
            }
          }
          node = node.next();
        } catch (InvalidNodeException e) {
          e.printStackTrace();
        }
      }
    }
    // step move for black player
    else {
      ListNode<Integer[]> node = blackChipPositions.front();
      while (node.isValidNode()) {
        try {
          Integer[] square = node.item();
          for (int y = 0; y < BOARD_SIZE; y++) {
            for (int x = 0; x < BOARD_SIZE; x++) {
              Move m = new Move(x, y, square[0], square[1]);
              if (isValidMove(false, m)) {
                moves.insertBack(m);
              }
            }
          }
          node = node.next();
        } catch (InvalidNodeException e) {
          e.printStackTrace();
        }
      }
    }

    return moves;
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

  /**
   *  toString() returns a String representation of this GameBoard.
   *
   *  @return a String representation of this GameBoard.
   *
   *  Performance: runs in O(n^2) time, where n is the size of the board, BOARD_SIZE.
   */
  public String toString() {
    int length = (5*BOARD_SIZE + 1)*(BOARD_SIZE+1) +
        (5*BOARD_SIZE + 5)*BOARD_SIZE + 5*BOARD_SIZE + 1;
    StringBuilder sb = new StringBuilder(length);
    for (int i = 0; i < BOARD_SIZE; i++) {
      sb.append("-----");
    }
    for (int y = 0; y < BOARD_SIZE; y++) {
      sb.append("\n");
      for (int x = 0; x < BOARD_SIZE; x++) {
        sb.append("| " );
        if (board[x][y] == WHITE_CHIP) {
          sb.append("WW ");
        } else if (board[x][y] == BLACK_CHIP) {
          sb.append("BB ");
        } else if (board[x][y] == CORNER) {
          sb.append("~~ ");
        } else {
          sb.append("   ");
        }
      }
      sb.append("| _");
      sb.append(y);
      sb.append("\n");
      for (int i = 0; i < BOARD_SIZE; i++) {
        sb.append("-----");
      }
    }
    sb.append("\n");
    for (int i = 0; i < BOARD_SIZE; i++) {
      sb.append("  ");
      sb.append(i);
      sb.append("_ ");
    }
    sb.append("\n");
    return sb.toString();
  }

  // returns true if given x, y is a corner; false, otherwise.
  private boolean inCornerArea(int x, int y) {
    return (x == 0 && y == 0) || (x == BOARD_SIZE - 1 && y == BOARD_SIZE - 1) ||
        (x == 0 && y == BOARD_SIZE - 1) || (x == BOARD_SIZE - 1 && y == 0);
  }

  // returns true if given x, y is a black goal area; false, otherwise.
  private boolean inBlackGoalArea(int x, int y) {
    return (y == 0 || y == BOARD_SIZE - 1) && (x > 0 && x < BOARD_SIZE - 1);
  }

  // returns true if given x, y is a white goal area; false, otherwise.
  private boolean inWhiteGoalArea(int x, int y) {
    return (x == 0 || x == BOARD_SIZE - 1) && (y > 0 && y < BOARD_SIZE - 1);
  }

  // returns true if given x, y is a non-border area; false, otherwise.
  private boolean inNonBorderArea(int x, int y) {
    return (x > 0 && x < BOARD_SIZE - 1) && (y > 0 && y < BOARD_SIZE - 1);
  }

  // return the count of all chips connected to this chip (including this chip)
  private int neighboringChips(int chipColor, int x, int y, int xPrev, int yPrev) {
    int count = 1; // 1 for chip at x, y

    // non-border area
    if (inNonBorderArea(x, y)) {
      for (int j = y-1; j <= y+1; j++) {
        for (int i = x-1; i <= x+1; i++) {
          if (board[i][j] == chipColor && !(i == x && j == y) &&
              !(i == xPrev && j == yPrev)) {
            count += neighboringChips(chipColor, i, j, x, y);
          }
        }
      }
    }
    // top goal
    else if (y == 0 && (x > 0 && x < BOARD_SIZE-1)) {
      for (int j = y; j <= y+1; j++) {
        for (int i = x-1; i <= x+1; i++) {
          if (board[i][j] == chipColor && !(i == x && j == y) &&
              !(i == xPrev && j == yPrev)) {
            count += neighboringChips(chipColor, i, j, x, y);
          }
        }
      }
    }
    // bottom goal
    else if (y == BOARD_SIZE-1 && (x > 0 && x < BOARD_SIZE-1)) {
      for (int j = y-1; j <= y; j++) {
        for (int i = x-1; i <= x+1; i++) {
          if (board[i][j] == chipColor && !(i == x && j == y) &&
              !(i == xPrev && j == yPrev)) {
            count += neighboringChips(chipColor, i, j, x, y);
          }
        }
      }
    }
    // left goal
    else if (x == 0 && (y > 0 && y < BOARD_SIZE-1)) {
      for (int j = y-1; j <= y+1; j++) {
        for (int i = x; i <= x+1; i++) {
          if (board[i][j] == chipColor && !(i == x && j == y) &&
              !(i == xPrev && j == yPrev)) {
            count += neighboringChips(chipColor, i, j, x, y);
          }
        }
      }
    }
    // right goal
    else if (x == BOARD_SIZE-1 && (y > 0 && y < BOARD_SIZE-1)) {
      for (int j = y-1; j <= y+1; j++) {
        for (int i = x-1; i <= x; i++) {
          if (board[i][j] == chipColor && !(i == x && j == y) &&
              !(i == xPrev && j == yPrev)) {
            count += neighboringChips(chipColor, i, j, xPrev, yPrev);
          }
        }
      }
    }

    return count;
  }

}
