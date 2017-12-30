package player;

import DataStructures.Graph.DepthFirstPaths;
import DataStructures.Graph.Graph;
import DataStructures.Graph.SymbolGraph;
import DataStructures.List.List;
import DataStructures.List.DList;
import DataStructures.Stack.Stack;
import DataStructures.dict.HashTableChained;
import java.util.Arrays;
import java.util.Iterator;

/**
 * GameBoard is a representation of a board in a Network game.  Keeps track of the chips on the board,
 * and has methods for setting chips, undoing last set chip, validating moves, getting all connections
 * for a given chip position, and detecting a network (win condition).
 */
public class GameBoard {

  /**
   * BLACK_PLAYER is the internal representation of a black player.
   * WHITE_PLAYER is the internal representation of a white player.
   * MINIMUM_CHIPS_FOR_NETWORK is the minimum number of chips required for a network.
   * MAX_NEIGHBORING_CHIPS is the maximum number of neighboring chips (including itself) for a chip at x, y.
   * STEP_MOVE_THRESHOLD is the number of chips required to play step moves.
   * BOARD_SIZE is the size of the game board.
   * NO_CHIP is stored in a square without any chip.
   * WHITE_CHIP is stored in a square with a white chip.
   * BLACK_CHIP is stored in a square with a black chip.
   * DIR_x is the direction towards another chip
   *
   * board is the internal representation of the game board.
   * whiteChips is an int[][] of the white chip positions on the board.
   * blackChips is an int[][] of the black chip positions on the board.
   * whiteChipCount is the number of white chips on the board.
   * blackChipCount is the number of black chips on the board.
   * leftGoalChips is a List of white chips in the left white goal area.
   * rightGoalChips is a List of white chips in the right white goal area.
   * topGoalChips is a List of black chips in the top black goal area.
   * bottomGoalChips is a List of black chips in the bottom black goal area.
   *
   */
  private final static int BLACK_PLAYER = MachinePlayer.BLACK_PLAYER;
  private final static int WHITE_PLAYER = MachinePlayer.WHITE_PLAYER;
  private final static int MINIMUM_CHIPS_FOR_NETWORK = 6;
  private final static int MAX_NEIGHBORING_CHIPS = 2;
  private final static int STEP_MOVE_THRESHOLD = 10;
  private final static int BOARD_SIZE = 8;
  private final static int NO_CHIP = 0;
  private final static int WHITE_CHIP = 1;
  private final static int BLACK_CHIP = 2;
  final static int DIRECTION_UP = 1;
  final static int DIRECTION_UP_RIGHT = 2;
  final static int DIRECTION_RIGHT = 3;
  final static int DIRECTION_DOWN_RIGHT = 4;
  final static int DIRECTION_DOWN = -DIRECTION_UP;
  final static int DIRECTION_DOWN_LEFT = -DIRECTION_UP_RIGHT;
  final static int DIRECTION_LEFT = -DIRECTION_RIGHT;
  final static int DIRECTION_UP_LEFT = -DIRECTION_DOWN_RIGHT;

  private int[][] grid;
  private int whiteChipCount;
  private int blackChipCount;
  private List<Integer[]> whiteChips;
  private List<Integer[]> blackChips;
  private List<Integer[]> leftGoalChips;
  private List<Integer[]> rightGoalChips;
  private List<Integer[]> topGoalChips;
  private List<Integer[]> bottomGoalChips;
  private Stack<MoveWithPlayer> whiteMoves;
  private Stack<MoveWithPlayer> blackMoves;

  /**
   *  Creates a new game board
   */
  public GameBoard() {
    grid = new int[BOARD_SIZE][BOARD_SIZE];
    whiteChips = new DList<Integer[]>();
    blackChips = new DList<Integer[]>();
    whiteChipCount = 0;
    blackChipCount = 0;
    leftGoalChips = new DList<Integer[]>();
    rightGoalChips = new DList<Integer[]>();
    topGoalChips = new DList<Integer[]>();
    bottomGoalChips = new DList<Integer[]>();
    whiteMoves = new Stack<MoveWithPlayer>();
    blackMoves = new Stack<MoveWithPlayer>();
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
    int chipColor;
    int chipCount;
    List<Integer[]> chipPositions;
    if (move.player == WHITE_PLAYER) {
      chipColor = WHITE_CHIP;
      chipCount = whiteChipCount;
      chipPositions = whiteChips;
    } else {
      chipColor = BLACK_CHIP;
      chipCount = blackChipCount;
      chipPositions = blackChips;
    }
    // step move
    if (chipCount == STEP_MOVE_THRESHOLD) {
      // remove chip in old position
      Iterator<Integer[]> iterator = chipPositions.iterator();
      while (iterator.hasNext()) {
        Integer[] square = iterator.next();
        if (move.x2 == square[0] && move.y2 == square[1]) {
          iterator.remove();
          break;
        }
      }
      // if old chip position is in goal list, remove it from respective goal list
      removeChipIfInGoal(move.x2, move.y2);
      grid[move.x2][move.y2] = NO_CHIP;
      grid[move.x1][move.y1] = chipColor;
    }
    // add move
    else {
      grid[move.x1][move.y1] = chipColor;
      if (move.player == WHITE_PLAYER) {
        whiteChipCount++;
      } else {
        blackChipCount++;
      }
    }
    Integer[] square = { move.x1, move.y1 };
    // add to goal chip list
    if (isInLeftGoal(move.x1, move.y1)) {
      leftGoalChips.insertBack(square);
    } else if (isInRightGoal(move.x1, move.y1)) {
      rightGoalChips.insertBack(square);
    } else if (isInTopGoal(move.x1, move.y1)) {
      topGoalChips.insertBack(square);
    } else if (isInBottomGoal(move.x1, move.y1)) {
      bottomGoalChips.insertBack(square);
    }
    // add chip to chipPositions
    chipPositions.insertBack(square);
    // save move data for undo
    if (move.player == WHITE_PLAYER) {
      whiteMoves.push(move);
    } else {
      blackMoves.push(move);
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
    int chipColor;
    Stack<MoveWithPlayer> moves;
    List<Integer[]> chipPositions;
    if (player == WHITE_PLAYER) {
      chipColor = WHITE_CHIP;
      moves = whiteMoves;
      chipPositions = whiteChips;
    } else {
      chipColor = BLACK_CHIP;
      moves = blackMoves;
      chipPositions = blackChips;
    }
    // no moves made yet
    if (moves.length() == 0) {
      return false;
    }
    // save reference to last move and remove it from moves list and remove chip from chipPositions
    MoveWithPlayer lastMove = moves.pop();
    Iterator<Integer[]> iterator = chipPositions.iterator();
    while (iterator.hasNext()) {
      Integer[] square = iterator.next();
      if (lastMove.x1 == square[0] && lastMove.y1 == square[1]) {
        iterator.remove();
        break;
      }
    }
    // step moves
    if (lastMove.moveKind == Move.STEP) {
      // add old chip position of step move
      Integer[] square = { lastMove.x2, lastMove.y2 };
      chipPositions.insertBack(square);
      grid[lastMove.x1][lastMove.y1] = NO_CHIP;
      grid[lastMove.x2][lastMove.y2] = chipColor;
      // add to goal chip list
      if (isInLeftGoal(lastMove.x2, lastMove.y2)) {
        leftGoalChips.insertBack(square);
      } else if (isInRightGoal(lastMove.x2, lastMove.y2)) {
        rightGoalChips.insertBack(square);
      } else if (isInTopGoal(lastMove.x2, lastMove.y2)) {
        topGoalChips.insertBack(square);
      } else if (isInBottomGoal(lastMove.x2, lastMove.y2)) {
        bottomGoalChips.insertBack(square);
      }
    }
    // add moves
    else {
      if (lastMove.player == WHITE_PLAYER) {
        whiteChipCount--;
      } else {
        blackChipCount--;
      }
      grid[lastMove.x1][lastMove.y1] = NO_CHIP;
    }
    // remove if in goal list
    removeChipIfInGoal(lastMove.x1, lastMove.y1);
    return true;
  }

  // removes the chip at given x, y position if it is in the goal area
  private void removeChipIfInGoal(int x, int y) {
    if (isInLeftGoal(x, y)) {
      Iterator<Integer[]> iterator = leftGoalChips.iterator();
      while (iterator.hasNext()) {
        Integer[] goalSquare = iterator.next();
        if (x == goalSquare[0] && y == goalSquare[1]) {
          iterator.remove();
          break;
        }
      }
    }
    else if (isInRightGoal(x, y)) {
      Iterator<Integer[]> iterator = rightGoalChips.iterator();
      while (iterator.hasNext()) {
        Integer[] goalSquare = iterator.next();
        if (x == goalSquare[0] && y == goalSquare[1]) {
          iterator.remove();
          break;
        }
      }
    }
    else if (isInTopGoal(x, y)) {
      Iterator<Integer[]> iterator = topGoalChips.iterator();
      while (iterator.hasNext()) {
        Integer[] goalSquare = iterator.next();
        if (x == goalSquare[0] && y == goalSquare[1]) {
          iterator.remove();
          break;
        }
      }
    }
    else if (isInBottomGoal(x, y)) {
      Iterator<Integer[]> iterator = bottomGoalChips.iterator();
      while (iterator.hasNext()) {
        Integer[] goalSquare = iterator.next();
        if (x == goalSquare[0] && y == goalSquare[1]) {
          iterator.remove();
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
    int chipCount;
    int chipColor;
    if (move.player == WHITE_PLAYER) {
      chipCount = whiteChipCount;
      chipColor = WHITE_CHIP;
    } else {
      chipCount = blackChipCount;
      chipColor = BLACK_CHIP;
    }
    // move.player should be WHITE_PLAYER or BLACK_PLAYER.
    if (move.player != WHITE_PLAYER && move.player != BLACK_PLAYER) {
      return false;
    }
    // move.moveKind should be add or step move.
    if (move.moveKind != Move.ADD && move.moveKind != Move.STEP) {
      return false;
    }
    // No chip may be placed outside game board.
    if (move.x1 < 0 || move.x1 > BOARD_SIZE-1 || move.y1 < 0 || move.y1 > BOARD_SIZE-1) {
      return false;
    }
    // No chip may be placed in any of the four corners.
    if (isInCorner(move.x1, move.y1)) {
      return false;
    }
    // No chip may be placed in a goal of the opposite color.
    if ((move.player == WHITE_PLAYER && (isInTopGoal(move.x1, move.y1) || isInBottomGoal(move.x1, move.y1))) ||
        (move.player == BLACK_PLAYER && (isInLeftGoal(move.x1, move.y1) || isInRightGoal(move.x1, move.y1)))) {
      return false;
    }
    // No chip may be placed in a square that is already occupied.
    if (grid[move.x1][move.y1] != NO_CHIP) {
      return false;
    }
    // No clusters of three or more chips of the same color.
    if (getNeighboringChipCount(chipColor, move.x1, move.y1, -1, -1, move.x2, move.y2) >
        MAX_NEIGHBORING_CHIPS) {
      return false;
    }
    // Player attempting step move when they have less than 10 chips on the board.
    if (move.moveKind == Move.STEP && chipCount < STEP_MOVE_THRESHOLD) {
      return false;
    }
    // Player attempting add move when they have 10 or more chips on the board.
    if (move.moveKind == Move.ADD && chipCount == STEP_MOVE_THRESHOLD) {
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
      chipPositions = whiteChips;
    } else {
      chipCount = blackChipCount;
      chipPositions = blackChips;
    }
    List<MoveWithPlayer> validMoves = new DList<MoveWithPlayer>();
    // add move
    if (chipCount < STEP_MOVE_THRESHOLD) {
      for (int y = 0; y < BOARD_SIZE; y++) {
        for (int x = 0; x < BOARD_SIZE; x++) {
          MoveWithPlayer move = new MoveWithPlayer(x, y, player);
          if (isValidMove(move)) {
            validMoves.insertBack(move);
          }
        }
      }
    }
    // step move
    else {
      for (Integer[] square : chipPositions) {
        for (int y = 0; y < BOARD_SIZE; y++) {
          for (int x = 0; x < BOARD_SIZE; x++) {
            MoveWithPlayer move = new MoveWithPlayer(x, y, square[0], square[1], player);
            if (isValidMove(move)) {
              validMoves.insertBack(move);
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
    int playerChipColor;
    int opponentChipColor;
    if (player == WHITE_PLAYER) {
      playerChipColor = WHITE_CHIP;
      opponentChipColor = BLACK_CHIP;
    } else {
      playerChipColor = BLACK_CHIP;
      opponentChipColor = WHITE_CHIP;
    }
    List<Integer[]> connections = new DList<Integer[]>();
    // check all directions to see if there's a connecting chip

    // skip left and right goal
    if (x > 0 && x < BOARD_SIZE-1) {
      // up
      for (int j = y - 1; j >= 0; j--) {
        if (grid[x][j] == opponentChipColor) {
          break;
        } else if (grid[x][j] == playerChipColor) {
          Integer[] square = {x, j, DIRECTION_UP};
          connections.insertBack(square);
          break;
        }
      }
      // down
      for (int j = y + 1; j < BOARD_SIZE; j++) {
        if (grid[x][j] == opponentChipColor) {
          break;
        } else if (grid[x][j] == playerChipColor) {
          Integer[] square = {x, j, DIRECTION_DOWN};
          connections.insertBack(square);
          break;
        }
      }
    }
    // skip top and bottom goal
    if (y > 0 && y < BOARD_SIZE-1) {
      // left
      for (int i = x - 1; i >= 0; i--) {
        if (grid[i][y] == opponentChipColor) {
          break;
        } else if (grid[i][y] == playerChipColor) {
          Integer[] square = {i, y, DIRECTION_LEFT};
          connections.insertBack(square);
          break;
        }
      }
      // right
      for (int i = x + 1; i < BOARD_SIZE; i++) {
        if (grid[i][y] == opponentChipColor) {
          break;
        } else if (grid[i][y] == playerChipColor) {
          Integer[] square = {i, y, DIRECTION_RIGHT};
          connections.insertBack(square);
          break;
        }
      }
    }
    // up left
    for (int i = x - 1, j = y - 1; i >= 0 && j >= 0; i--, j--) {
      if (grid[i][j] == opponentChipColor) {
        break;
      } else if (grid[i][j] == playerChipColor) {
        Integer[] square = {i, j, DIRECTION_UP_LEFT};
        connections.insertBack(square);
        break;
      }
    }
    // up right
    for (int i = x + 1, j = y - 1; i < BOARD_SIZE && j >= 0; i++, j--) {
      if (grid[i][j] == opponentChipColor) {
        break;
      } else if (grid[i][j] == playerChipColor) {
        Integer[] square = {i, j, DIRECTION_UP_RIGHT};
        connections.insertBack(square);
        break;
      }
    }
    // down left
    for (int i = x - 1, j = y + 1; i >= 0 && j < BOARD_SIZE; i--, j++) {
      if (grid[i][j] == opponentChipColor) {
        break;
      } else if (grid[i][j] == playerChipColor) {
        Integer[] square = {i, j, DIRECTION_DOWN_LEFT};
        connections.insertBack(square);
        break;
      }
    }
    // down right
    for (int i = x + 1, j = y + 1; i < BOARD_SIZE && j < BOARD_SIZE; i++, j++) {
      if (grid[i][j] == opponentChipColor) {
        break;
      } else if (grid[i][j] == playerChipColor) {
        Integer[] square = {i, j, DIRECTION_DOWN_RIGHT};
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
      chipPositions = whiteChips;
      chipsInFirstGoal = leftGoalChips;
      chipsInSecondGoal = rightGoalChips;
    } else {
      chipPositions = blackChips;
      chipsInFirstGoal = topGoalChips;
      chipsInSecondGoal = bottomGoalChips;
    }
    // network consists of 6 or more chips
    if ((player == WHITE_PLAYER && whiteChipCount < MINIMUM_CHIPS_FOR_NETWORK) ||
        (player == BLACK_PLAYER && blackChipCount < MINIMUM_CHIPS_FOR_NETWORK)) {
      return false;
    }
    // there is at least 1 chip in each goal
    if (!isAtLeastOneChipInEachGoal(player, chipPositions)) {
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
    // set up goal chip excluded set
    HashTableChained excluded = new HashTableChained(6);
    for (Integer[] square : chipsInFirstGoal) {
      excluded.insert(new IntegerArray(square), null);
    }
    for (Integer[] square : chipsInSecondGoal) {
      excluded.insert(new IntegerArray(square), null);
    }
    // perform depth-first search on the graph
    for (Integer[] firstGoalSquare : chipsInFirstGoal) {
      for (Integer[] secondGoalSquare : chipsInSecondGoal) {
        excluded.remove(new IntegerArray(firstGoalSquare));
        excluded.remove(new IntegerArray(secondGoalSquare));
        DepthFirstPaths dfs = new DepthFirstPaths(sg, g, sg.index(new IntegerArray(firstGoalSquare)),
            sg.index(new IntegerArray(secondGoalSquare)), excluded, MINIMUM_CHIPS_FOR_NETWORK);
        excluded.insert(new IntegerArray(firstGoalSquare), null);
        excluded.insert(new IntegerArray(secondGoalSquare), null);
        if (dfs.hasPath()) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * getWhiteChips() returns an Integer array List of white chips on the board.
   *
   * @return the white chip Integer array list.
   */
  public List<Integer[]> getWhiteChips() {
    return whiteChips;
  }

  /**
   * getBlackChips() returns an Integer array List of black chips on the board.
   *
   * @return the black chip Integer array list.
   */
  public List<Integer[]> getBlackChips() {
    return blackChips;
  }

  /**
   * getTopGoalChips() returns an Integer array List of (black) chips in the top goal on the board.
   *
   * @return the (black) chips in top goal Integer array list.
   */
  public List<Integer[]> getTopGoalChips() {
    return topGoalChips;
  }

  /**
   * getBottomGoalChips() returns an Integer array List of (black) chips in the bottom goal on the board.
   *
   * @return the (black) chips in bottom goal Integer array list.
   */
  public List<Integer[]> getBottomGoalChips() {
    return bottomGoalChips;
  }

  /**
   * getLeftGoalChips() returns an Integer array List of (white) chips in the left goal on the board.
   *
   * @return the (white) chips in left goal Integer array list.
   */
  public List<Integer[]> getLeftGoalChips() {
    return leftGoalChips;
  }

  /**
   * getRightGoalChips() returns an Integer array List of (black) chips in the right goal on the board.
   *
   * @return the (black) chips in right goal Integer array list.
   */
  public List<Integer[]> getRightGoalChips() {
    return rightGoalChips;
  }

  // returns true if there is at least 1 chip in each goal, false otherwise
  private boolean isAtLeastOneChipInEachGoal(int player, List<Integer[]> chipPositions) {
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
   *  getChipListsAsString() returns a String representation of the internal chip lists.
   *
   *  @return a String representation of chip lists: white, black, top goal, bottom goal, left goal, and right goal.
   *
   *  Performance: runs in O(1) time.
   */
  public String getChipListsAsString() {
    StringBuilder sb = new StringBuilder(100);
    Object[] chipLists = {whiteChips, blackChips, topGoalChips, bottomGoalChips,
        leftGoalChips, rightGoalChips};
    String[] listNames = { "White", "Black", "Top", "Bottom", "Left", "Right" };
    for (int i = 0; i < chipLists.length; i++) {
      List<Integer[]> list  = ((List<Integer[]>) chipLists[i]);
      sb.append(listNames[i]);
      sb.append(": { ");
      for (Integer[] chip : list) {
        sb.append(Arrays.toString(chip));
        sb.append(" ");
      }
      sb.append("}");
      if (i != chipLists.length - 1) {
        sb.append("\n");
      }
    }
    return sb.toString();
  }

  /**
   *  toString() returns a String representation of this GameBoard.
   *
   *  @return a String representation of this GameBoard.
   *
   *  Performance: runs in O(n^2) time, where n is the size of the board, BOARD_SIZE.
   */
  @Override
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
        if (grid[x][y] == WHITE_CHIP) {
          sb.append("WW ");
        } else if (grid[x][y] == BLACK_CHIP) {
          sb.append("BB ");
        } else if (isInCorner(x, y)) {
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

  /**
   *  equals() returns true if "this" GameBoard "board" have identical values in every cell.
   *
   *  @param board is the second GameBoard.
   *  @return true if the boards are equal, false otherwise.
   */
  @Override
  public boolean equals(Object board) {
    if (board == null) {
      return false;
    }
    if (!(board instanceof GameBoard)) {
      return false;
    }
    GameBoard other = (GameBoard) board;
    for (int y = 0; y < BOARD_SIZE; y++) {
      for (int x = 0; x < BOARD_SIZE; x++) {
        if (grid[x][y] != other.grid[x][y]) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   *  hashCode() returns a hash code for this GameBoard.
   *
   *  See "dmeister" @ https://stackoverflow.com/questions/113511/best-implementation-for-hashcode-method
   *  for details.
   *
   *  @return a hash code for this GameBoard.
   */
  @Override
  public int hashCode() {
    int result = 13;
    for (int y = 0; y < grid.length; y++) {
      for (int x = 0; x < grid.length; x++) {
        result = 37 * result + grid[x][y];
      }
    }
    return result;
  }

  // returns true if given x, y is a corner; false, otherwise.
  private boolean isInCorner(int x, int y) {
    return (x == 0 && y == 0) || (x == BOARD_SIZE - 1 && y == BOARD_SIZE - 1) ||
        (x == 0 && y == BOARD_SIZE - 1) || (x == BOARD_SIZE - 1 && y == 0);
  }

  // returns true if given x, y is a top black goal area; false, otherwise.
  private boolean isInTopGoal(int x, int y) {
    return y == 0 && (x > 0 && x < BOARD_SIZE - 1);
  }

  // returns true if given x, y is a bottom black goal area; false, otherwise.
  private boolean isInBottomGoal(int x, int y) {
    return y == BOARD_SIZE - 1 && (x > 0 && x < BOARD_SIZE - 1);
  }

  // returns true if given x, y is a left white goal area; false, otherwise.
  private boolean isInLeftGoal(int x, int y) {
    return x == 0 && (y > 0 && y < BOARD_SIZE - 1);
  }

  // returns true if given x, y is a right white goal area; false, otherwise.
  private boolean isInRightGoal(int x, int y) {
    return x == BOARD_SIZE - 1 && (y > 0 && y < BOARD_SIZE - 1);
  }

  // returns true if given x, y is a non-border area; false, otherwise.
  private boolean inNonBorder(int x, int y) {
    return (x > 0 && x < BOARD_SIZE - 1) && (y > 0 && y < BOARD_SIZE - 1);
  }

  // return the count of all chips connected to this chip at currentX, currentY (excluding chip at excludeX, excludeY).
  // previousX, previousY is the chip checked before the current call.
  private int getNeighboringChipCount(int chipColor, int currentX, int currentY, int previousX, int previousY,
                                      int excludeX, int excludeY) {
    int count = 1; // 1 for chip at currentX, currentY

    // non-border area
    if (inNonBorder(currentX, currentY)) {
      for (int j = currentY-1; j <= currentY+1; j++) {
        for (int i = currentX-1; i <= currentX+1; i++) {
          if (grid[i][j] == chipColor && !(i == currentX && j == currentY) && !(i == previousX && j == previousY) &&
              !(i == excludeX && j == excludeY)) {
            count += getNeighboringChipCount(chipColor, i, j, currentX, currentY, excludeX, excludeY);
          }
        }
      }
    }
    // top goal
    else if (isInTopGoal(currentX, currentY)) {
      for (int j = currentY; j <= currentY+1; j++) {
        for (int i = currentX-1; i <= currentX+1; i++) {
          if (grid[i][j] == chipColor && !(i == currentX && j == currentY) && !(i == previousX && j == previousY) &&
              !(i == excludeX && j == excludeY)) {
            count += getNeighboringChipCount(chipColor, i, j, currentX, currentY, excludeX, excludeY);
          }
        }
      }
    }
    // bottom goal
    else if (isInBottomGoal(currentX, currentY)) {
      for (int j = currentY-1; j <= currentY; j++) {
        for (int i = currentX-1; i <= currentX+1; i++) {
          if (grid[i][j] == chipColor && !(i == currentX && j == currentY) && !(i == previousX && j == previousY) &&
              !(i == excludeX && j == excludeY)) {
            count += getNeighboringChipCount(chipColor, i, j, currentX, currentY, excludeX, excludeY);
          }
        }
      }
    }
    // left goal
    else if (isInLeftGoal(currentX, currentY)) {
      for (int j = currentY-1; j <= currentY+1; j++) {
        for (int i = currentX; i <= currentX+1; i++) {
          if (grid[i][j] == chipColor && !(i == currentX && j == currentY) && !(i == previousX && j == previousY) &&
              !(i == excludeX && j == excludeY)) {
            count += getNeighboringChipCount(chipColor, i, j, currentX, currentY, excludeX, excludeY);
          }
        }
      }
    }
    // right goal
    else if (isInRightGoal(currentX, currentY)) {
      for (int j = currentY-1; j <= currentY+1; j++) {
        for (int i = currentX-1; i <= currentX; i++) {
          if (grid[i][j] == chipColor && !(i == currentX && j == currentY) && !(i == previousX && j == previousY) &&
              !(i == excludeX && j == excludeY)) {
            count += getNeighboringChipCount(chipColor, i, j, currentX, currentY, excludeX, excludeY);
          }
        }
      }
    }

    return count;
  }

}
