package player;

public class GameBoard {

  protected final static int BOARD_SIZE = 8;
  protected final static int NO_CHIP = 0;
  protected final static int WHITE_CHIP = 1;
  protected final static int BLACK_CHIP = 2;
  protected final static int CORNER = 3;
  protected final static int NON_CORNER = 4;
  protected final static int BLACK_GOAL = 5;
  protected final static int WHITE_GOAL = 6;

  protected int[][] board;
  protected final Player whitePlayer;
  protected final Player blackPlayer;
  protected int whiteChipCount;
  protected int blackChipCount;

//  protected List boardList;


  /**
   *  Creates a new game board
   */
  public GameBoard(Player whitePlayer, Player blackPlayer) {
    board = new int[BOARD_SIZE][BOARD_SIZE];
    this.whitePlayer = whitePlayer;
    this.blackPlayer = blackPlayer;
    whiteChipCount = 0;
    blackChipCount = 0;
    board[0][0] = CORNER;
    board[BOARD_SIZE-1][BOARD_SIZE-1] = CORNER;
    board[0][BOARD_SIZE-1] = CORNER;
    board[BOARD_SIZE-1][0] = CORNER;
  }

  /**
   *  isValidMove() determines whether a Move "m" is valid for player "p".
   *  A full description of what constitutes a valid move appears in the
   *  project "README" file.
   *
   *  @param p is MachinePlayer.COMPUTER or MachinePlayer.OPPONENT
   *  @param m is the move player "p" wants to make
   *  @return true if Move "m" is valid for player "p", false otherwise.
   **/
  protected boolean isValidMove(Player p, Move m) {
    // No chip may be placed outside game board.
    if (m.x1 < 0 || m.x1 > BOARD_SIZE-1 || m.y1 < 0 || m.y1 > BOARD_SIZE-1) {
      return false;
    }
    // No chip may be placed in any of the four corners.
    if (inCornerArea(m.x1, m.y1)) {
      return false;
    }
    // No chip may be placed in a goal of the opposite color.
    if (p == whitePlayer && inBlackGoalArea(m.x1, m.y1) ||
        p == blackPlayer && inWhiteGoalArea(m.x1, m.y1)) {
      return false;
    }
    // No chip may be placed in a square that is already occupied.
    if (board[m.x1][m.y1] != NO_CHIP) {
      return false;
    }
    // No clusters of three or more chips of the same color.
    if (p == whitePlayer && neighboringChips(WHITE_CHIP, m.x1, m.y1) > 3 ||
        p == blackPlayer && neighboringChips(BLACK_CHIP, m.x1, m.y1) > 3) {
      return false;
    }
    // Player attempting step move when they have less than 10 chips on the board.
    if (m.moveKind == Move.STEP && ((p == whitePlayer && whiteChipCount < 10) ||
        (p == blackPlayer && blackChipCount < 10))) {
      return false;
    }
    // Player attempting add move when they have more than 10 chips on the board.
    if (m.moveKind == Move.ADD && ((p == whitePlayer && whiteChipCount >= 10) ||
        (p == blackPlayer && blackChipCount >= 10))) {
      return false;
    }
    // Player attempting to step move to the same square.
    if (m.moveKind == Move.STEP && m.x1 == m.x2 && m.y1 == m.y2) {
      return false;
    }

    return true;
  }

  /**
   *  getValidMoves() generates a List of all valid moves for player "p".
   *  A full description of what constitutes a valid move appears in the
   *  project "README" file.
   *
   *  @param p is MachinePlayer.COMPUTER or MachinePlayer.OPPONENT
   *  @return a List of all valid moves for player "p".
   **/
  public Object /*List*/ getValidMoves(Player p) {
    for (int y = 0; y < BOARD_SIZE; y++) {
      for (int x = 0; x < BOARD_SIZE; x++) {
        if (isValidMove(p, new Move(x, y))) {
          // add to list of valid moves
        }
      }
    }

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
  protected Object /*List*/ getConnections(int side, Move m) {
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

  // return the chip above the given x, y square
  private int aboveSquare(int x, int y) {
    if (y == 0) {
      throw new IndexOutOfBoundsException("Index y-1: " + (y - 1) + " is out of bounds");
    }
    return board[x][y-1];
  }

  // return the chip below the given x, y square
  private int belowSquare(int x, int y) {
    if (y == BOARD_SIZE - 1) {
      throw new IndexOutOfBoundsException("Index y+1: " + (y + 1) + " is out of bounds");
    }
    return board[x][y+1];
  }

  // return the chip to the left of the given x, y square
  private int leftSquare(int x, int y) {
    if (x == 0) {
      throw new IndexOutOfBoundsException("Index x-1: " + (x - 1) + " is out of bounds");
    }
    return board[x-1][y];
  }

  // return the chip to the right of the given x, y square
  private int rightSquare(int x, int y) {
    if (x == BOARD_SIZE - 1) {
      throw new IndexOutOfBoundsException("Index x+1: " + (x + 1) + " is out of bounds");
    }
    return board[x+1][y];
  }

  // return the chip to the above left of the given x, y square
  private int aboveLeftSquare(int x, int y) {
    if (x == 0 && y == 0) {
      throw new IndexOutOfBoundsException("Index x-1: " + (x - 1) +
          ", Index y-1: " + (y - 1) + " is out of bounds");
    }
    if (x == 0) {
      throw new IndexOutOfBoundsException("Index x-1: " + (x - 1) + " is out of bounds");
    }
    if (y == 0) {
      throw new IndexOutOfBoundsException("Index y-1: " + (y - 1) + " is out of bounds");
    }
    return board[x-1][y-1];
  }

  // return the chip to the above right of the given x, y square
  private int aboveRightSquare(int x, int y) {
    if (x == BOARD_SIZE - 1 && y == 0) {
      throw new IndexOutOfBoundsException("Index x+1: " + (x + 1) +
          ", Index y-1: " + (y - 1) + " is out of bounds");
    }
    if (x == BOARD_SIZE - 1) {
      throw new IndexOutOfBoundsException("Index x+1: " + (x + 1) + " is out of bounds");
    }
    if (y == 0) {
      throw new IndexOutOfBoundsException("Index y-1: " + (y - 1) + " is out of bounds");
    }
    return board[x+1][y-1];
  }

  // return the chip to the below left of the given x, y square
  private int belowLeftSquare(int x, int y) {
    if (x == 0 && y == BOARD_SIZE - 1) {
      throw new IndexOutOfBoundsException("Index x-1: " + (x - 1) +
          ", Index y+1: " + (y + 1) + " is out of bounds");
    }
    if (x == 0) {
      throw new IndexOutOfBoundsException("Index x-1: " + (x - 1) + " is out of bounds");
    }
    if (y == BOARD_SIZE - 1) {
      throw new IndexOutOfBoundsException("Index y+1: " + (y + 1) + " is out of bounds");
    }
    return board[x-1][y+1];
  }

  // return the chip to the below right of the given x, y square
  private int belowRightSquare(int x, int y) {
    if (x == BOARD_SIZE - 1 && y == BOARD_SIZE - 1) {
      throw new IndexOutOfBoundsException("Index x+1: " + (x + 1) +
          ", Index y+1: " + (y + 1) + " is out of bounds");
    }
    if (x == BOARD_SIZE - 1) {
      throw new IndexOutOfBoundsException("Index x+1: " + (x + 1) + " is out of bounds");
    }
    if (y == BOARD_SIZE - 1) {
      throw new IndexOutOfBoundsException("Index y+1: " + (y + 1) + " is out of bounds");
    }
    return board[x+1][y+1];
  }

  // return the count of neighboring chips including the center
  private int neighboringChips(int chipColor, int x, int y) {
    int chips = 1; // 1 for chip at x, y

    // non-border area
    if (inNonBorderArea(x, y)) {
      for (int i = x-1; i <= x+1; i++) {
        for (int j = y-1; j <= y+1; j++) {
          if (board[i][j] == chipColor) {
            chips++;
          }
        }
      }
    }
    // top goal
    else if (y == 0 && (x > 0 && x < BOARD_SIZE-1)) {
      for (int i = x-1; i <= x+1; i++) {
        for (int j = y; j <= y+1; j++) {
          if (board[i][j] == chipColor) {
            chips++;
          }
        }
      }
    }
    // bottom goal
    else if (y == BOARD_SIZE-1 && (x > 0 && x < BOARD_SIZE-1)) {
      for (int i = x-1; i <= x+1; i++) {
        for (int j = y-1; j <= y; j++) {
          if (board[i][j] == chipColor) {
            chips++;
          }
        }
      }
    }
    // left goal
    else if (x == 0 && (y > 0 && y < BOARD_SIZE-1)) {
      for (int i = x; i <= x+1; i++) {
        for (int j = y-1; j <= y+1; j++) {
          if (board[i][j] == chipColor) {
            chips++;
          }
        }
      }
    }
    // right goal
    else if (x == BOARD_SIZE-1 && (y > 0 && y < BOARD_SIZE-1)) {
      for (int i = x-1; i <= x; i++) {
        for (int j = y-1; j <= y+1; j++) {
          if (board[i][j] == chipColor) {
            chips++;
          }
        }
      }
    }

    return chips;
  }

}
