package player;

import DataStructures.Graph.DepthFirstPaths;
import DataStructures.Graph.Graph;
import DataStructures.Graph.SymbolGraph;
import DataStructures.List.InvalidNodeException;
import DataStructures.List.List;
import DataStructures.List.DList;
import DataStructures.dict.HashTableChained;
import java.util.Iterator;

public class GameBoard {

  /**
   * BLACK_PLAYER is the internal representation of a black player.
   * WHITE_PLAYER is the internal representation of a white player.
   * MIN_CONNECTION_LENGTH is the minimum number of chips required for a network.
   * BOARD_SIZE is the size of the game board.
   * NO_CHIP is stored in a square without any chip.
   * WHITE_CHIP is stored in a square with a white chip.
   * BLACK_CHIP is stored in a square with a black chip.
   * CORNER is stored in a square that is a corner.
   * DIR_x is the direction towards another chip
   *
   * board is the internal representation of the game board.
   * whiteChipPositions is an int[][] of the white chip positions on the board.
   * blackChipPositions is an int[][] of the black chip positions on the board.
   * whiteChipCount is the index of the next empty spot in whiteChipPositions, 10 if full.
   * blackChipCount is the index of the next empty spot in blackChipPositions, 10 if full.
   * chipsLeftGoal is a List of white chips in the left white goal area.
   * chipsRightGoal is a List of white chips in the right white goal area.
   * chipsTopGoal is a List of black chips in the top black goal area.
   * chipsBottomGoal is a List of black chips in the bottom black goal area.
   *
   */
  private final static int BLACK_PLAYER = 0;
  private final static int WHITE_PLAYER = 1;
  private final static int MIN_CONNECTION_LENGTH = 6;
  private final static int BOARD_SIZE = 8;
  private final static int NO_CHIP = 0;
  private final static int WHITE_CHIP = 1;
  private final static int BLACK_CHIP = 2;
  private final static int CORNER = 3;
  final static int DIR_UP = 1;
  final static int DIR_UP_RIGHT = 2;
  final static int DIR_RIGHT = 3;
  final static int DIR_DOWN_RIGHT = 4;
  final static int DIR_DOWN = -DIR_UP;
  final static int DIR_DOWN_LEFT = -DIR_UP_RIGHT;
  final static int DIR_LEFT = -DIR_RIGHT;
  final static int DIR_UP_LEFT = -DIR_DOWN_RIGHT;

  private int[][] board;
   List<Integer[]> whiteChipPositions;
   List<Integer[]> blackChipPositions;
  private int whiteChipCount;
  private int blackChipCount;
   List<Integer[]> chipsLeftGoal;
   List<Integer[]> chipsRightGoal;
   List<Integer[]> chipsTopGoal;
   List<Integer[]> chipsBottomGoal;
  private List<MoveWithPlayer> whiteMoves;
  private List<MoveWithPlayer> blackMoves;

  /**
   *  Creates a new game board
   */
  public GameBoard() {
    board = new int[BOARD_SIZE][BOARD_SIZE];
    board[0][0] = CORNER;
    board[BOARD_SIZE-1][BOARD_SIZE-1] = CORNER;
    board[0][BOARD_SIZE-1] = CORNER;
    board[BOARD_SIZE-1][0] = CORNER;
    whiteChipPositions = new DList<Integer[]>();
    blackChipPositions = new DList<Integer[]>();
    whiteChipCount = 0;
    blackChipCount = 0;
    chipsLeftGoal = new DList<Integer[]>();
    chipsRightGoal = new DList<Integer[]>();
    chipsTopGoal = new DList<Integer[]>();
    chipsBottomGoal = new DList<Integer[]>();
    whiteMoves = new DList<MoveWithPlayer>();
    blackMoves = new DList<MoveWithPlayer>();
  }

  /**
   *  setChip() sets a chip for a given player on the board.  For step moves, it removes the
   *  chip from the previous position.
   *
   *  @param move is the move player wants to make.
   *  @return true if move was valid and completed, false otherwise.
   *
   *  Performance: runs in O(1) time.
   */
  protected boolean setChip(MoveWithPlayer move) {
    if (!isValidMove(move)) {
      return false;
    }
    int chipCount;
    List<Integer[]> chipPositions;
    int chipColor;
    if (move.player == WHITE_PLAYER) {
      chipCount = whiteChipCount;
      chipPositions = whiteChipPositions;
      chipColor = WHITE_CHIP;
    } else {
      chipCount = blackChipCount;
      chipPositions = blackChipPositions;
      chipColor = BLACK_CHIP;
    }
    // step move
    if (chipCount == 10) {
      // remove chip in old position
      Iterator<Integer[]> iter = chipPositions.iterator();
      while (iter.hasNext()) {
        Integer[] square = iter.next();
        if (move.x2 == square[0] && move.y2 == square[1]) {
          iter.remove();
          break;
        }
      }
      // if old chip position is in goal list, remove it from respective goal list
      removeChipIfInGoal(move.x2, move.y2);
      board[move.x2][move.y2] = NO_CHIP;
      board[move.x1][move.y1] = chipColor;
    }
    // add move
    else {
      board[move.x1][move.y1] = chipColor;
      if (move.player == WHITE_PLAYER) {
        whiteChipCount++;
      } else {
        blackChipCount++;
      }
    }
    Integer[] square = { move.x1, move.y1 };
    // add to goal chip list
    if (inLeftGoal(move.x1, move.y1)) {
      chipsLeftGoal.insertBack(square);
    } else if (inRightGoal(move.x1, move.y1)) {
      chipsRightGoal.insertBack(square);
    } else if (inTopGoal(move.x1, move.y1)) {
      chipsTopGoal.insertBack(square);
    } else if (inBottomGoal(move.x1, move.y1)) {
      chipsBottomGoal.insertBack(square);
    }
    // add chip to chipPositions
    chipPositions.insertBack(square);
    // save move data for undo
    if (move.player == WHITE_PLAYER) {
      whiteMoves.insertBack(move);
    } else {
      blackMoves.insertBack(move);
    }
    return true;
  }

  /**
   *  undoSetChip() undoes the last setChip() for the given player.  It returns false if there was no chip
   *  successfully set for the player prior.
   *
   *  Unusual conditions:
   *   - if player is neither WHITE_PLAYER or BLACK_PLAYER, throws IllegalArgumentException.
   *
   *  @param player is WHITE_PLAYER or BLACK_PLAYER.
   *  @return true if move was valid and completed, false otherwise.
   *
   *  Performance: runs in O(1) time.
   */
  protected boolean undoSetChip(int player) {
    List<MoveWithPlayer> moves;
    int chipColor;
    List<Integer[]> chipPositions;
    if (player == WHITE_PLAYER) {
      moves = whiteMoves;
      chipColor = WHITE_CHIP;
      chipPositions = whiteChipPositions;
    } else {
      moves = blackMoves;
      chipColor = BLACK_CHIP;
      chipPositions = blackChipPositions;
    }
    // no moves made yet
    if (moves.length() == 0) {
      return false;
    }
    // save reference to last move and remove it from moves list
    // also remove last chip from chipPositions which was the last added chip
    MoveWithPlayer lastMove = null;
    try {
      lastMove = moves.back().item();
      moves.back().remove();
      chipPositions.back().remove();
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
    // step moves
    if (lastMove.moveKind == Move.STEP) {
      // add old chip position of step move
      Integer[] square = { lastMove.x2, lastMove.y2 };
      chipPositions.insertBack(square);
      board[lastMove.x1][lastMove.y1] = NO_CHIP;
      board[lastMove.x2][lastMove.y2] = chipColor;
    }
    // add moves
    else if (lastMove.moveKind == Move.ADD) {
      if (lastMove.player == WHITE_PLAYER) {
        whiteChipCount--;
      } else {
        blackChipCount--;
      }
      board[lastMove.x1][lastMove.y1] = NO_CHIP;
    }
    // remove if in goal list
    removeChipIfInGoal(lastMove.x1, lastMove.y1);
    return true;
  }

  // removes the chip at given x, y position if it is in the goal area
  private void removeChipIfInGoal(int x, int y) {
    // if in left goal area, remove from left goal chip list
    if (inLeftGoal(x, y)) {
      Iterator<Integer[]> iter = chipsLeftGoal.iterator();
      while (iter.hasNext()) {
        Integer[] goalSquare = iter.next();
        if (x == goalSquare[0] && y == goalSquare[1]) {
          iter.remove();
          break;
        }
      }
    }
    // if in right goal area, remove from right goal chip list
    else if (inRightGoal(x, y)) {
      Iterator<Integer[]> iter = chipsRightGoal.iterator();
      while (iter.hasNext()) {
        Integer[] goalSquare = iter.next();
        if (x == goalSquare[0] && y == goalSquare[1]) {
          iter.remove();
          break;
        }
      }
    }
    // if in top goal area, remove from top goal chip list
    else if (inTopGoal(x, y)) {
      Iterator<Integer[]> iter = chipsTopGoal.iterator();
      while (iter.hasNext()) {
        Integer[] goalSquare = iter.next();
        if (x == goalSquare[0] && y == goalSquare[1]) {
          iter.remove();
          break;
        }
      }
    }
    // if in bottom goal area, remove from bottom goal chip list
    else if (inBottomGoal(x, y)) {
      Iterator<Integer[]> iter = chipsBottomGoal.iterator();
      while (iter.hasNext()) {
        Integer[] goalSquare = iter.next();
        if (x == goalSquare[0] && y == goalSquare[1]) {
          iter.remove();
          break;
        }
      }
    }
  }

  /**
   *  isValidMove() determines whether a Move "move" is valid for player.
   *  A full description of what constitutes a valid move appears in the
   *  project "README" file.
   *
   *  @param move is the move player wants to make.
   *  @return true if Move "move" is valid for player, false otherwise.
   *
   *  Performance: runs in O(1) time.
   **/
  private boolean isValidMove(MoveWithPlayer move) {
    // move.player should be WHITE_PLAYER or BLACK_PLAYER.
    if (move.player != WHITE_PLAYER && move.player != BLACK_PLAYER) {
      return false;
    }
    // No chip may be placed outside game board.
    if (move.x1 < 0 || move.x1 > BOARD_SIZE-1 || move.y1 < 0 || move.y1 > BOARD_SIZE-1) {
      return false;
    }
    // No chip may be placed in any of the four corners.
    if (inCorner(move.x1, move.y1)) {
      return false;
    }
    // No chip may be placed in a goal of the opposite color.
    if ((move.player == WHITE_PLAYER && (inTopGoal(move.x1, move.y1) || inBottomGoal(move.x1, move.y1))) ||
        (move.player == BLACK_PLAYER && (inLeftGoal(move.x1, move.y1) || inRightGoal(move.x1, move.y1)))) {
      return false;
    }
    // No chip may be placed in a square that is already occupied.
    if (board[move.x1][move.y1] != NO_CHIP) {
      return false;
    }
    // No clusters of three or more chips of the same color.
    if (move.player == WHITE_PLAYER && neighboringChips(WHITE_CHIP, move.x1, move.y1, -1, -1, move.x2, move.y2) > 2 ||
        move.player == BLACK_PLAYER && neighboringChips(BLACK_CHIP, move.x1, move.y1, -1, -1, move.x2, move.y2) > 2) {
      return false;
    }
    // Player attempting step move when they have less than 10 chips on the board.
    if (move.moveKind == Move.STEP && ((move.player == WHITE_PLAYER && whiteChipCount < 10) ||
        (move.player == BLACK_PLAYER && blackChipCount < 10))) {
      return false;
    }
    // Player attempting add move when they have 10 or more chips on the board.
    if (move.moveKind == Move.ADD && ((move.player == WHITE_PLAYER && whiteChipCount == 10) ||
        (move.player == BLACK_PLAYER && blackChipCount >= 10))) {
      return false;
    }
    // Player attempting step move to the same square.
    if (move.moveKind == Move.STEP && move.x1 == move.x2 && move.y1 == move.y2) {
      return false;
    }

    return true;
  }

  /**
   *  getValidMoves() generates a List of all valid moves for the player.
   *  A full description of what constitutes a valid move appears in the
   *  project "README" file.
   *
   *  Unusual conditions:
   *   - if player is neither WHITE_PLAYER nor BLACK_PLAYER, throws IllegalArgumentException.
   *
   *  @param player is WHITE_PLAYER or BLACK_PLAYER.
   *  @return a List of all valid moves for the player.
   *
   *  Performance: runs in O(n^2) time, where n is the size of the board, BOARD_SIZE
   **/
  protected List<MoveWithPlayer> getValidMoves(int player) {
    if (player != WHITE_PLAYER && player != BLACK_PLAYER) {
      throw new IllegalArgumentException("Invalid player specified.");
    }
    int chipCount;
    List<Integer[]> chipPositions;
    if (player == WHITE_PLAYER) {
      chipCount = whiteChipCount;
      chipPositions = whiteChipPositions;
    } else {
      chipCount = blackChipCount;
      chipPositions = blackChipPositions;
    }
    List<MoveWithPlayer> validMoves = new DList<MoveWithPlayer>();
    // add move
    if (chipCount < 10) {
      for (int y = 0; y < BOARD_SIZE; y++) {
        for (int x = 0; x < BOARD_SIZE; x++) {
          MoveWithPlayer m = new MoveWithPlayer(x, y, player);
          if (isValidMove(m)) {
            validMoves.insertBack(m);
          }
        }
      }
    }
    // step move
    else {
      for (Integer[] square : chipPositions) {
        for (int y = 0; y < BOARD_SIZE; y++) {
          for (int x = 0; x < BOARD_SIZE; x++) {
            MoveWithPlayer m = new MoveWithPlayer(x, y, square[0], square[1], player);
            if (isValidMove(m)) {
              validMoves.insertBack(m);
            }
          }
        }
      }
    }

    return validMoves;
  }

  /**
   *  getConnections() generates a List of chip positions (Integer arrays coordinates
   *  and direction) that form a connection with chips already on the board and the
   *  given x, y position.
   *
   *  Unusual conditions:
   *   - if player is neither WHITE_PLAYER or BLACK_PLAYER, throws IllegalArgumentException.
   *
   *  @param player is WHITE_PLAYER or BLACK_PLAYER.
   *  @param x is x coordinate of the square the chip is in.
   *  @param y is the y coordinate of the square the chip is in.
   *  @return a List of chip positions that form a connection with chip at given x,y.
   *
   *  Performance: runs in O(1) time.
   **/
  protected List<Integer[]> getConnections(int player, int x, int y) {
    if (player != WHITE_PLAYER && player != BLACK_PLAYER) {
      throw new IllegalArgumentException("Invalid player specified.");
    }
    List<Integer[]> connections = new DList<Integer[]>();
    int playerChipColor;
    int opponentChipColor;

    if (player == WHITE_PLAYER) {
      playerChipColor = WHITE_CHIP;
      opponentChipColor = BLACK_CHIP;
    } else {
      playerChipColor = BLACK_CHIP;
      opponentChipColor = WHITE_CHIP;
    }
    // check all directions to see if there's a connecting chip
    // for up, down, left, and right, first check if in goal area (no connection between chips in
    // same goal)

    // up
    if (x > 0 && x < BOARD_SIZE-1) {
      for (int j = y - 1; j >= 0; j--) {
        if (board[x][j] == opponentChipColor) {
          break;
        } else if (board[x][j] == playerChipColor) {
          Integer[] square = {x, j, DIR_UP};
          connections.insertBack(square);
          break;
        }
      }
    }
    // down
    if (x > 0 && x < BOARD_SIZE-1) {
      for (int j = y + 1; j < BOARD_SIZE; j++) {
        if (board[x][j] == opponentChipColor) {
          break;
        } else if (board[x][j] == playerChipColor) {
          Integer[] square = {x, j, DIR_DOWN};
          connections.insertBack(square);
          break;
        }
      }
    }
    // left
    if (y > 0 && y < BOARD_SIZE-1) {
      for (int i = x - 1; i >= 0; i--) {
        if (board[i][y] == opponentChipColor) {
          break;
        } else if (board[i][y] == playerChipColor) {
          Integer[] square = {i, y, DIR_LEFT};
          connections.insertBack(square);
          break;
        }
      }
    }
    // right
    if (y > 0 && y < BOARD_SIZE-1) {
      for (int i = x + 1; i < BOARD_SIZE; i++) {
        if (board[i][y] == opponentChipColor) {
          break;
        } else if (board[i][y] == playerChipColor) {
          Integer[] square = {i, y, DIR_RIGHT};
          connections.insertBack(square);
          break;
        }
      }
    }
    // up left
    for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
      if (board[i][j] == opponentChipColor) {
        break;
      } else if (board[i][j] == playerChipColor) {
        Integer[] square = {i, j, DIR_UP_LEFT};
        connections.insertBack(square);
        break;
      }
    }
    // up right
    for (int i = x + 1, j = y - 1; i < BOARD_SIZE && j >= 0; i++, j--) {
      if (board[i][j] == opponentChipColor) {
        break;
      } else if (board[i][j] == playerChipColor) {
        Integer[] square = {i, j, DIR_UP_RIGHT};
        connections.insertBack(square);
        break;
      }
    }
    // down left
    for (int i = x - 1, j = y + 1; i >= 0 && j < BOARD_SIZE; i--, j++) {
      if (board[i][j] == opponentChipColor) {
        break;
      } else if (board[i][j] == playerChipColor) {
        Integer[] square = {i, j, DIR_DOWN_LEFT};
        connections.insertBack(square);
        break;
      }
    }
    // down right
    for (int i = x + 1, j = y + 1; i < BOARD_SIZE && j < BOARD_SIZE; i++, j++) {
      if (board[i][j] == opponentChipColor) {
        break;
      } else if (board[i][j] == playerChipColor) {
        Integer[] square = {i, j, DIR_DOWN_RIGHT};
        connections.insertBack(square);
        break;
      }
    }
    return connections;
  }

  /**
   *  hasValidNetwork() determines whether "this" GameBoard has a valid network
   *  for player.  (Does not check whether the opponent has a network.)
   *  A full description of what constitutes a valid network appears in the
   *  project "README" file.
   *
   *  Unusual conditions:
   *   - If player is neither WHITE_PLAYER nor BLACK_PLAYER, throws IllegalArgumentException.
   *   - If GameBoard squares contain illegal values, the behavior of this
   *     method is undefined (i.e., don't expect any reasonable behavior).
   *
   *  @param player is WHITE_PLAYER or BLACK_PLAYER.
   *  @return true if player has a winning network in "this" GameBoard, false otherwise.
   **/
  protected boolean hasValidNetwork(int player) {
    /* To determine if there is a network for a player:
     *  1. check if they have at least 6 chips on the board
     *  2. check if they have at least 1 chip in each of their goals
     *  3. add chip positions as vertices in the graph
     *  4. add connections between chips as edges of the graph
     *  5. set up goal chip exclusion set which will contain goal chips which should be ignored
     *     (the goal chips which are neither the current source or destination vertex examined by
     *     depth-first search)
     *  6. perform depth-first search on each unique set of opposite goal pairs (depth-first search
     *     doesn't care about the order of vertices given)
     */
    List<Integer[]> chipPositions;
    List<Integer[]> chipsInFirstGoal;
    List<Integer[]> chipsInSecondGoal;
    if (player == WHITE_PLAYER) {
      chipPositions = whiteChipPositions;
      chipsInFirstGoal = chipsLeftGoal;
      chipsInSecondGoal = chipsRightGoal;
    } else {
      chipPositions = blackChipPositions;
      chipsInFirstGoal = chipsTopGoal;
      chipsInSecondGoal = chipsBottomGoal;
    }
    // network consists of 6 or more chips
    if ((player == WHITE_PLAYER && whiteChipCount < 6) || (player == BLACK_PLAYER && blackChipCount < 6)) {
      return false;
    }
    // there is at least 1 chip in each goal
    if (!chipInGoals(player, chipPositions)) {
      return false;
    }
    // add chips in chipPositions as vertices of SymbolGraph sg
    SymbolGraph sg = new SymbolGraph();
    for (Integer[] square : chipPositions) {
      sg.addVertex(new IntegerArray(square));
    }
    // add connections between chips in chipPositions as edges of SymbolGraph sg
    for (Integer[] origin : chipPositions) {
      List<Integer[]> destinations = getConnections(player, origin[0], origin[1]);
      // add each connection from origin to destination as an edge
      for (Integer[] destination : destinations) {
        sg.addEdge(new IntegerArray(origin), new IntegerArray(destination));
      }
    }
    // generate Graph g after all edges added
    Graph g = sg.G();
    // set up goal chip exclusion set
    HashTableChained exclusion = new HashTableChained(6);
    for (Integer[] square : chipsInFirstGoal) {
      exclusion.insert(new IntegerArray(square), null);
    }
    for (Integer[] square : chipsInSecondGoal) {
      exclusion.insert(new IntegerArray(square), null);
    }
    // perform depth-first search on the graph
    for (Integer[] firstGoalSquare : chipsInFirstGoal) {
      for (Integer[] secondGoalSquare : chipsInSecondGoal) {
        exclusion.remove(new IntegerArray(firstGoalSquare));
        exclusion.remove(new IntegerArray(secondGoalSquare));
        DepthFirstPaths dfs = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(firstGoalSquare)),
            sg.index(new IntegerArray(secondGoalSquare)), exclusion, MIN_CONNECTION_LENGTH);
        exclusion.insert(new IntegerArray(firstGoalSquare), null);
        exclusion.insert(new IntegerArray(secondGoalSquare), null);
        // for debugging
//        System.out.println("s: " + Arrays.toString(firstGoalSquare) + ", d: " +
//            Arrays.toString(secondGoalSquare));
//        for (int vertex : dfs.getPath()) {
//          System.out.println(sg.symbol(vertex));
//        }
        if (dfs.hasPath()) {
          return true;
        }
      }
    }
    return false;
  }

  // returns true if there is at least 1 chip in each goal, false otherwise
  private boolean chipInGoals(int player, List<Integer[]> chipPositions) {
    boolean firstGoal = false;
    boolean secondGoal = false;
    for (Integer[] square : chipPositions) {
      if (player == WHITE_PLAYER) {
        if (square[0] == 0) {
          firstGoal = true;
        } else if (square[0] == BOARD_SIZE-1) {
          secondGoal = true;
        }
      } else {
        if (square[1] == 0) {
          firstGoal = true;
        } else if (square[1] == BOARD_SIZE-1) {
          secondGoal = true;
        }
      }
    }
    return firstGoal && secondGoal;
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
  private boolean inCorner(int x, int y) {
    return (x == 0 && y == 0) || (x == BOARD_SIZE - 1 && y == BOARD_SIZE - 1) ||
        (x == 0 && y == BOARD_SIZE - 1) || (x == BOARD_SIZE - 1 && y == 0);
  }

  // returns true if given x, y is a top black goal area; false, otherwise.
  private boolean inTopGoal(int x, int y) {
    return y == 0 && (x > 0 && x < BOARD_SIZE - 1);
  }

  // returns true if given x, y is a bottom black goal area; false, otherwise.
  private boolean inBottomGoal(int x, int y) {
    return y == BOARD_SIZE - 1 && (x > 0 && x < BOARD_SIZE - 1);
  }

  // returns true if given x, y is a left white goal area; false, otherwise.
  private boolean inLeftGoal(int x, int y) {
    return x == 0 && (y > 0 && y < BOARD_SIZE - 1);
  }

  // returns true if given x, y is a right white goal area; false, otherwise.
  private boolean inRightGoal(int x, int y) {
    return x == BOARD_SIZE - 1 && (y > 0 && y < BOARD_SIZE - 1);
  }

  // returns true if given x, y is a non-border area; false, otherwise.
  private boolean inNonBorder(int x, int y) {
    return (x > 0 && x < BOARD_SIZE - 1) && (y > 0 && y < BOARD_SIZE - 1);
  }

  // return the count of all chips connected to this chip at x,y (excluding chip at excludeX, excludeY).
  // prevX, prevY is the chip checked before the current call.
  private int neighboringChips(int chipColor, int x, int y, int prevX, int prevY, int excludeX, int excludeY) {
    int count = 1; // 1 for chip at x, y

    // non-border area
    if (inNonBorder(x, y)) {
      for (int j = y-1; j <= y+1; j++) {
        for (int i = x-1; i <= x+1; i++) {
          if (board[i][j] == chipColor && !(i == x && j == y) && !(i == prevX && j == prevY) &&
              !(i == excludeX && j == excludeY)) {
            count += neighboringChips(chipColor, i, j, x, y, excludeX, excludeY);
          }
        }
      }
    }
    // top goal
    else if (inTopGoal(x, y)) {
      for (int j = y; j <= y+1; j++) {
        for (int i = x-1; i <= x+1; i++) {
          if (board[i][j] == chipColor && !(i == x && j == y) && !(i == prevX && j == prevY) &&
              !(i == excludeX && j == excludeY)) {
            count += neighboringChips(chipColor, i, j, x, y, excludeX, excludeY);
          }
        }
      }
    }
    // bottom goal
    else if (inBottomGoal(x, y)) {
      for (int j = y-1; j <= y; j++) {
        for (int i = x-1; i <= x+1; i++) {
          if (board[i][j] == chipColor && !(i == x && j == y) && !(i == prevX && j == prevY) &&
              !(i == excludeX && j == excludeY)) {
            count += neighboringChips(chipColor, i, j, x, y, excludeX, excludeY);
          }
        }
      }
    }
    // left goal
    else if (inLeftGoal(x, y)) {
      for (int j = y-1; j <= y+1; j++) {
        for (int i = x; i <= x+1; i++) {
          if (board[i][j] == chipColor && !(i == x && j == y) && !(i == prevX && j == prevY) &&
              !(i == excludeX && j == excludeY)) {
            count += neighboringChips(chipColor, i, j, x, y, excludeX, excludeY);
          }
        }
      }
    }
    // right goal
    else if (inRightGoal(x, y)) {
      for (int j = y-1; j <= y+1; j++) {
        for (int i = x-1; i <= x; i++) {
          if (board[i][j] == chipColor && !(i == x && j == y) && !(i == prevX && j == prevY) &&
              !(i == excludeX && j == excludeY)) {
            count += neighboringChips(chipColor, i, j, x, y, excludeX, excludeY);
          }
        }
      }
    }

    return count;
  }

}
