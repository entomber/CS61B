package player;


import DataStructures.List.DList;
import DataStructures.List.InvalidNodeException;
import DataStructures.List.List;
import DataStructures.List.ListNode;
import java.util.Arrays;
import java.util.Iterator;

import org.junit.Ignore;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GameBoardTest {

  private final static int BLACK_PLAYER = 0;
  private final static int WHITE_PLAYER = 1;
  private final static int BOARD_SIZE = 8;
  private final static int UP = GameBoard.DIR_UP;
  private final static int UP_RIGHT = GameBoard.DIR_UP_RIGHT;
  private final static int RIGHT = GameBoard.DIR_RIGHT;
  private final static int DOWN_RIGHT = GameBoard.DIR_DOWN_RIGHT;
  private final static int DOWN = GameBoard.DIR_DOWN;
  private final static int DOWN_LEFT = GameBoard.DIR_DOWN_LEFT;
  private final static int LEFT = GameBoard.DIR_LEFT;
  private final static int UP_LEFT = GameBoard.DIR_UP_LEFT;

  // test setChip() on a move out of the boundaries of the board.
  @Test
  public void setChip_outOfBounds() {
    GameBoard board = new GameBoard();
    MoveWithPlayer m1 = new MoveWithPlayer(BOARD_SIZE, BOARD_SIZE, WHITE_PLAYER);
    MoveWithPlayer m2 = new MoveWithPlayer(-1, -1, WHITE_PLAYER);
    MoveWithPlayer m3 = new MoveWithPlayer(BOARD_SIZE, BOARD_SIZE, BLACK_PLAYER);
    MoveWithPlayer m4 = new MoveWithPlayer(-1, -1, BLACK_PLAYER);

    assertFalse("Should not set white chip out of bounds (" + m1.x1 + "," + m1.y1 + ").", board.setChip(m1));
    assertFalse("Should not set white chip out of bounds (" + m1.x1 + "," + m1.y1 + ").", board.setChip(m2));
    assertFalse("Should not set black chip out of bounds (" + m1.x1 + "," + m1.y1 + ").", board.setChip(m3));
    assertFalse("Should not set black chip out of bounds (" + m1.x1 + "," + m1.y1 + ").", board.setChip(m4));
  }

  // test setChip() on a move to each corner.
  @Test
  public void setChip_corner() {
    GameBoard board = new GameBoard();
    MoveWithPlayer m1 = new MoveWithPlayer(0, 0, WHITE_PLAYER);
    MoveWithPlayer m2 = new MoveWithPlayer(0, BOARD_SIZE-1, WHITE_PLAYER);
    MoveWithPlayer m3 = new MoveWithPlayer(BOARD_SIZE-1, 0, WHITE_PLAYER);
    MoveWithPlayer m4 = new MoveWithPlayer(BOARD_SIZE-1, BOARD_SIZE-1, WHITE_PLAYER);
    MoveWithPlayer m5 = new MoveWithPlayer(0, 0, BLACK_PLAYER);
    MoveWithPlayer m6 = new MoveWithPlayer(0, BOARD_SIZE-1, BLACK_PLAYER);
    MoveWithPlayer m7 = new MoveWithPlayer(BOARD_SIZE-1, 0, BLACK_PLAYER);
    MoveWithPlayer m8 = new MoveWithPlayer(BOARD_SIZE-1, BOARD_SIZE-1, BLACK_PLAYER);

    assertFalse("Should not set white chip in corner (" + m1.x1 + "," + m1.y1 + ").", board.setChip(m1));
    assertFalse("Should not set white chip in corner (" + m2.x1 + "," + m2.y1 + ").", board.setChip(m2));
    assertFalse("Should not set white chip in corner (" + m3.x1 + "," + m3.y1 + ").", board.setChip( m3));
    assertFalse("Should not set white chip in corner (" + m4.x1 + "," + m4.y1 + ").", board.setChip(m4));
    assertFalse("Should not set black chip in corner (" + m1.x1 + "," + m1.y1 + ").", board.setChip(m1));
    assertFalse("Should not set black chip in corner (" + m2.x1 + "," + m2.y1 + ").", board.setChip(m2));
    assertFalse("Should not set black chip in corner (" + m3.x1 + "," + m3.y1 + ").", board.setChip(m3));
    assertFalse("Should not set black chip in corner (" + m4.x1 + "," + m4.y1 + ").", board.setChip(m4));
  }

  // test setChip() on a move to the wrong goal.
  @Test
  public void setChip_wrongGoal() {
    GameBoard board = new GameBoard();
    MoveWithPlayer m1 = new MoveWithPlayer(BOARD_SIZE/2, 0, WHITE_PLAYER); // top goal
    MoveWithPlayer m2 = new MoveWithPlayer(BOARD_SIZE/2, BOARD_SIZE-1, WHITE_PLAYER); // bottom goal
    MoveWithPlayer m3 = new MoveWithPlayer(0, BOARD_SIZE/2, BLACK_PLAYER); // left goal
    MoveWithPlayer m4 = new MoveWithPlayer(BOARD_SIZE-1, BOARD_SIZE/2, BLACK_PLAYER); // right goal

    assertFalse("Should not set white chip in top goal (" + m1.x1 + "," + m1.y1 + ").", board.setChip(m1));
    assertFalse("Should not set white chip in bottom goal (" + m2.x1 + "," + m2.y1 + ").", board.setChip(m2));
    assertFalse("Should not set black chip in left goal (" + m3.x1 + "," + m3.y1 + ").", board.setChip(m3));
    assertFalse("Should not set black chip in right goal (" + m4.x1 + "," + m4.y1 + ").", board.setChip(m4));
  }

  // test setChip() on a move to the correct goal.
  @Test
  public void setChip_correctGoal() {
    GameBoard board = new GameBoard();
    MoveWithPlayer m1 = new MoveWithPlayer(BOARD_SIZE/2, 0, BLACK_PLAYER); // top goal
    MoveWithPlayer m2 = new MoveWithPlayer(BOARD_SIZE/2, BOARD_SIZE-1, BLACK_PLAYER); // bottom goal
    MoveWithPlayer m3 = new MoveWithPlayer(0, BOARD_SIZE/2, WHITE_PLAYER); // left goal
    MoveWithPlayer m4 = new MoveWithPlayer(BOARD_SIZE-1, BOARD_SIZE/2, WHITE_PLAYER); // right goal

    assertTrue("Should set black chip in top goal (" + m1.x1 + "," + m1.y1 + ").", board.setChip(m1));
    assertTrue("Should set black chip in bottom goal (" + m2.x1 + "," + m2.y1 + ").", board.setChip(m2));
    assertTrue("Should set white chip in left goal (" + m3.x1 + "," + m3.y1 + ").", board.setChip(m3));
    assertTrue("Should set white chip in right goal (" + m4.x1 + "," + m4.y1 + ").", board.setChip(m4));
  }

  // test setChip() on a move to an already occupied square.
  @Test
  public void setChip_occupied() {
    GameBoard board = new GameBoard();
    board.setChip(new MoveWithPlayer(BOARD_SIZE/2, BOARD_SIZE/2, WHITE_PLAYER));
    MoveWithPlayer m1 = new MoveWithPlayer(BOARD_SIZE/2, BOARD_SIZE/2, WHITE_PLAYER);
    MoveWithPlayer m2 = new MoveWithPlayer(BOARD_SIZE/2, BOARD_SIZE/2, BLACK_PLAYER);

    assertFalse("Should not set white chip in occupied square (" + m1.x1 + "," + m1.y1 + ").",
        board.setChip(m1));
    assertFalse("Should not set black chip in occupied square (" + m1.x1 + "," + m1.y1 + ").",
        board.setChip(m2));
  }

  // test setChip() on moves that would form a cluster.
  @Test
  public void setChip_clusters() {
    GameBoard board = new GameBoard();
    // set up board like in "README" file "Legal moves" section
    int[][] initialMoves = { {3,0}, {2,1}, {2,4}, {1,6}, {5,2}, {5,3}, {6,5}, {4,7} };
    for (int[] move : initialMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }

    int[][] moves = { {1,0}, {2,0}, {4,0}, {1,1}, {3,1}, {4,1}, {5,1}, {6,1}, {1,2}, {2,2},
        {3,2}, {4,2}, {6,2}, {4,3}, {6,3}, {4,4}, {5,4}, {6,4}, {1,5}, {2,5}, {5,6}};
    for (int[] move : moves) {
      int x = move[0];
      int y = move[1];
      MoveWithPlayer m = new MoveWithPlayer(x, y, BLACK_PLAYER);
      assertFalse("Should not form a cluster (" + x + "," + y + ").", board.setChip(m));
    }
  }

  // test setChip() on step moves that should not form clusters. Series of step moves below moves chip 1
  // that is adjacent to chip 2 to another position while maintaining adjacency with chip 2
  // (e.g. step move 1 to x position: 1 2 x -> x 2 1)
  @Test
  public void setChip_stepMoveDoNotCluster() {
    GameBoard board = new GameBoard();
    // 10 black chips
    int[][] blackMoves = { {1,0}, {2,0}, {5,0}, {6,0}, {1,7}, {2,7}, {5,7}, {6,7}, {3,2}, {4,2} };
    for (int[] move : blackMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    // 10 white chips
    int[][] whiteMoves = { {0,1}, {0,2}, {0,5}, {0,6}, {7,1}, {7,2}, {7,5}, {7,6}, {5,3}, {5,4} };
    for (int[] move : whiteMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], WHITE_PLAYER));
    }
    System.out.println(board);
    // first step move: in goal area
    MoveWithPlayer m1 = new MoveWithPlayer(3, 0, 1, 0, BLACK_PLAYER);
    MoveWithPlayer m2 = new MoveWithPlayer(0, 3, 0, 1, WHITE_PLAYER);
    assertTrue("Should make step move that doesn't form cluster.", board.setChip(m1));
    assertTrue("Should make step move that doesn't form cluster.", board.setChip(m2));
    // second step move: other goal area
    MoveWithPlayer m3 = new MoveWithPlayer(4, 7, 6, 7, BLACK_PLAYER);
    MoveWithPlayer m4 = new MoveWithPlayer(7, 4, 7, 6, WHITE_PLAYER);
    assertTrue("Should make step move that doesn't form cluster.", board.setChip(m3));
    assertTrue("Should make step move that doesn't form cluster.", board.setChip(m4));
    // third step move: non-goal area
    MoveWithPlayer m5 = new MoveWithPlayer(4, 3, 3, 2, BLACK_PLAYER);
    MoveWithPlayer m6 = new MoveWithPlayer(4, 4, 5, 3, WHITE_PLAYER);
    assertTrue("Should make step move that doesn't form cluster.", board.setChip(m5));
    assertTrue("Should make step move that doesn't form cluster.", board.setChip(m6));

  }

  // test setChip() for a step move to the same square.
  @Test
  public void setChip_stepSameSquare() {
    GameBoard board = new GameBoard();

    int[][] blackMoves = { {1,0}, {2,0}, {4,0}, {5,0}, {1,2}, {2,2}, {4,2}, {5,2}, {1,4}, {2,4} };
    for (int[] move : blackMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }

    int[][] whiteMoves = { {1,1}, {2,1}, {4,1}, {5,1}, {1,3}, {2,3}, {4,3}, {5,3}, {1,5}, {2,5} };
    for (int[] move : whiteMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], WHITE_PLAYER));
    }

    MoveWithPlayer m1 = new MoveWithPlayer(1, 0, 1, 0, BLACK_PLAYER);
    MoveWithPlayer m2 = new MoveWithPlayer(1, 1, 1, 1, WHITE_PLAYER);

    assertFalse("Should not step move to same square. From (" + m1.x1 + "," + m1.y1 + ") to ("
        + m1.x2 + "," + m1.y2 + ").", board.setChip(m1));
    assertFalse("Should not step move to same square. From (" + m2.x1 + "," + m2.y1 + ") to ("
        + m2.x2 + "," + m2.y2 + ").", board.setChip(m2));
  }

  // test setChip() for a step move to another occupied square.
  @Test
  public void setChip_stepOccupiedSquare() {
    GameBoard board = new GameBoard();

    int[][] blackMoves = { {1,0}, {2,0}, {4,0}, {5,0}, {1,2}, {2,2}, {4,2}, {5,2}, {1,4}, {2,4} };
    for (int[] move : blackMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }

    int[][] whiteMoves = { {1,1}, {2,1}, {4,1}, {5,1}, {1,3}, {2,3}, {4,3}, {5,3}, {1,5}, {2,5} };
    for (int[] move : whiteMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], WHITE_PLAYER));
    }

    MoveWithPlayer m1 = new MoveWithPlayer(2, 0, 1, 0, BLACK_PLAYER);
    MoveWithPlayer m2 = new MoveWithPlayer(2, 1, 1, 1, WHITE_PLAYER);

    assertFalse("Should not step move to another occupied square. From (" + m1.x1 + "," + m1.y1 + ") to ("
        + m1.x2 + "," + m1.y2 + ").", board.setChip(m1));
    assertFalse("Should not step move to another occupied square. From (" + m2.x1 + "," + m2.y1 + ") to ("
        + m2.x2 + "," + m2.y2 + ").", board.setChip(m2));
  }

  // test setChip() for a step move to an unoccupied square.
  @Test
  public void setChip_goodMove() {
    GameBoard board = new GameBoard();

    int[][] blackMoves = { {1,0}, {2,0}, {4,0}, {5,0}, {1,2}, {2,2}, {4,2}, {5,2}, {1,4}, {2,4} };
    for (int[] move : blackMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }

    int[][] whiteMoves = { {1,1}, {2,1}, {4,1}, {5,1}, {1,3}, {2,3}, {4,3}, {5,3}, {1,5}, {2,5} };
    for (int[] move : whiteMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], WHITE_PLAYER));
    }

    MoveWithPlayer m1 = new MoveWithPlayer(6, 7, 1, 0, BLACK_PLAYER);
    MoveWithPlayer m2 = new MoveWithPlayer(7, 6, 1, 1, WHITE_PLAYER);

    assertTrue("Should step move to an unoccupied square. From (" + m1.x1 + "," + m1.y1 + ") to (" +
        m1.x2 + "," + m1.y2 + ").", board.setChip(m1));
    assertTrue("Should step move to an unoccupied square. From (" + m2.x1 + "," + m2.y1 + ") to (" +
        m2.x2 + "," + m2.y2 + ").", board.setChip(m2));
  }

  // test setChip() on illegal step and add moves.
  @Test
  public void setChip_illegalMove() {
    GameBoard board = new GameBoard();
    // 9 black chips
    int[][] blackMoves = { {1,0}, {2,0}, {4,0}, {5,0}, {1,2}, {2,2}, {4,2}, {5,2}, {1,4} };
    for (int[] move : blackMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    // 9 white chips
    int[][] whiteMoves = { {1,1}, {2,1}, {4,1}, {5,1}, {1,3}, {2,3}, {4,3}, {5,3}, {1,5}};
    for (int[] move : whiteMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], WHITE_PLAYER));
    }

    MoveWithPlayer m1 = new MoveWithPlayer(6, 6, 1, 0, BLACK_PLAYER);
    MoveWithPlayer m2 = new MoveWithPlayer(6, 6, 1, 0, WHITE_PLAYER);

    assertFalse("Should not step move with less than 9 chips on the board.", board.setChip(m1));
    assertFalse("Should not step move with less than 9 chips on the board.", board.setChip(m2));

    // now 10 black and white chips
    board.setChip(new MoveWithPlayer(2, 4, BLACK_PLAYER));
    board.setChip(new MoveWithPlayer(2, 5, WHITE_PLAYER));

    MoveWithPlayer m3 = new MoveWithPlayer(6, 6, BLACK_PLAYER);
    MoveWithPlayer m4 = new MoveWithPlayer(6, 6, WHITE_PLAYER);

    assertFalse("Should not add move with 10 chips on the board.", board.setChip(m3));
    assertFalse("Should not add move with 10 chips on the board.", board.setChip(m4));
  }

  // test setChip() updates internal white/blackChipPositions list correctly. Remove private access
  // of blackChipPositions field and explicitly run.
  @Ignore
  @Test
  public void setChip_stepMovesUpdateChipPositionsList() {
    GameBoard board = new GameBoard();
    // 10 black chips
    int[][] blackMoves = { {1,0}, {2,0}, {4,0}, {5,0}, {1,2}, {2,2}, {4,2}, {5,2}, {1,4}, {2,4} };
    for (int[] move : blackMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    // first step moves, remove original first and last chip
    MoveWithPlayer m1 = new MoveWithPlayer(4, 4, 1, 0, BLACK_PLAYER);
    MoveWithPlayer m2 = new MoveWithPlayer(5, 4, 2, 4, BLACK_PLAYER);
    board.setChip(m1);
    board.setChip(m2);
    String expected = "{ [2, 0] [4, 0] [5, 0] [1, 2] [2, 2] [4, 2] [5, 2] [1, 4] [4, 4] [5, 4] }";
    String result = "{ ";
    for (Integer[] square : board.blackChipPositions) {
      result += Arrays.toString(square) + " ";
    }
    result += "}";
    assertEquals("chipPosition list should be correct.", expected, result);
  }

  // test setChip() updates internal goal lists correctly for add moves. Remove private access
  // of the 4 goal chip list fields and explicitly run.
  @Ignore
  @Test
  public void setChip_addMovesUpdateGoalLists() {
    GameBoard board = new GameBoard();
    // 10 black chips
    int[][] blackMoves = {{1, 0}, {2, 0}, {5, 0}, {6, 0}, {1, 7}, {2, 7}, {5, 7}, {6, 7}, {3, 2}, {4, 2}};
    for (int[] move : blackMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    // 10 white chips
    int[][] whiteMoves = {{0, 1}, {0, 2}, {0, 5}, {0, 6}, {7, 1}, {7, 2}, {7, 5}, {7, 6}, {5, 3}, {5, 4}};
    for (int[] move : whiteMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], WHITE_PLAYER));
    }

    String expectedTop = "{ [1, 0] [2, 0] [5, 0] [6, 0] }";
    String expectedBottom = "{ [1, 7] [2, 7] [5, 7] [6, 7] }";
    String expectedLeft = "{ [0, 1] [0, 2] [0, 5] [0, 6] }";
    String expectedRight = "{ [7, 1] [7, 2] [7, 5] [7, 6] }";
    String[] expectedResults1 = {expectedTop, expectedBottom, expectedLeft, expectedRight};
    String[] actualResults = new String[4];
    Object[] goals1 = {board.chipsTopGoal, board.chipsBottomGoal, board.chipsLeftGoal, board.chipsRightGoal};
    for (int i = 0; i < actualResults.length; i++) {
      actualResults[i] = "{ ";
      for (Integer[] square : (List<Integer[]>) goals1[i]) {
        actualResults[i] += Arrays.toString(square) + " ";
      }
      actualResults[i] += "}";
      assertEquals("goal chip list should be correct.", expectedResults1[i], actualResults[i]);
    }
  }

  // test setChip() updates internal goal lists correctly for step moves. Remove private access
  // of the 4 goal chip list fields and explicitly run.
  @Ignore
  @Test
  public void setChip_stepMovesUpdateGoalLists() {
    GameBoard board = new GameBoard();
    // 10 black chips: 3 in top goal, 3 in bottom goal, 4 in non-goal
    int[][] blackMoves = { {1,0}, {2,0}, {5,0}, {1,7}, {2,7}, {5,7}, {3,2}, {4,2}, {3,5}, {4,5} };
    for (int[] move : blackMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    // 10 white chips: 3 in top goal, 3 in bottom goal, 4 in non-goal
    int[][] whiteMoves = { {0,1}, {0,2}, {0,5}, {7,1}, {7,2}, {7,5}, {2,3}, {2,4}, {5,3}, {5,4} };
    for (int[] move : whiteMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], WHITE_PLAYER));
    }
    // first step moves, moves a chip in goal to different position in same goal area
    board.setChip(new MoveWithPlayer(3, 0, 1, 0, BLACK_PLAYER));
    board.setChip(new MoveWithPlayer(0, 6, 0, 5, WHITE_PLAYER));
    board.setChip(new MoveWithPlayer(3, 7, 2, 7, BLACK_PLAYER));
    board.setChip(new MoveWithPlayer(7, 3, 7, 2, WHITE_PLAYER));

    String expectedTop = "{ [2, 0] [5, 0] [3, 0] }";
    String expectedBottom = "{ [1, 7] [5, 7] [3, 7] }";
    String expectedLeft = "{ [0, 1] [0, 2] [0, 6] }";
    String expectedRight = "{ [7, 1] [7, 5] [7, 3] }";
    String[] expectedResults1 = { expectedTop, expectedBottom, expectedLeft, expectedRight };
    String[] actualResults = new String[4];
    Object[] goals1 = { board.chipsTopGoal, board.chipsBottomGoal, board.chipsLeftGoal, board.chipsRightGoal};
    for (int i = 0; i < actualResults.length; i++) {
      actualResults[i] = "{ ";
      for (Integer[] square : (List<Integer[]>) goals1[i]) {
        actualResults[i] += Arrays.toString(square) + " ";
      }
      actualResults[i] += "}";
      assertEquals("goal chip list should be correct.", expectedResults1[i], actualResults[i]);
    }
    // second step moves: 1. move chip from goal area to non-goal area, move chip from one goal area to other,
    // 3. move chip from non-goal area to goal area, 4. move chip from non-goal area to non-goal area
    board.setChip(new MoveWithPlayer(1, 1, 2, 0, BLACK_PLAYER)); // 1
    board.setChip(new MoveWithPlayer(6, 7, 4, 2, BLACK_PLAYER)); // 2
    board.setChip(new MoveWithPlayer(7, 6, 2, 3, WHITE_PLAYER)); // 3
    board.setChip(new MoveWithPlayer(6, 1, 2, 4, WHITE_PLAYER)); // 4

    expectedTop = "{ [5, 0] [3, 0] }";
    expectedBottom = "{ [1, 7] [5, 7] [3, 7] [6, 7] }";
    expectedLeft = "{ [0, 1] [0, 2] [0, 6] }";
    expectedRight = "{ [7, 1] [7, 5] [7, 3] [7, 6] }";
    String[] expectedResults2 = { expectedTop, expectedBottom, expectedLeft, expectedRight };
    actualResults = new String[4];
    Object[] goals2 = { board.chipsTopGoal, board.chipsBottomGoal, board.chipsLeftGoal, board.chipsRightGoal};
    for (int i = 0; i < actualResults.length; i++) {
      actualResults[i] = "{ ";
      for (Integer[] square : (List<Integer[]>) goals2[i]) {
        actualResults[i] += Arrays.toString(square) + " ";
      }
      actualResults[i] += "}";
      assertEquals("goal chip list should be correct.", expectedResults2[i], actualResults[i]);
    }
    // third step moves: empty a goal list and add a chip back to it
    board.setChip(new MoveWithPlayer(1, 6, 3, 0, BLACK_PLAYER));
    board.setChip(new MoveWithPlayer(6, 5, 5, 0, BLACK_PLAYER));
    board.setChip(new MoveWithPlayer(5, 0, 3, 2, BLACK_PLAYER));

    expectedTop = "{ [5, 0] }";
    String actualResult = "{ ";
    for (Integer[] square : board.chipsTopGoal) {
      actualResult += Arrays.toString(square) + " ";
    }
    actualResult += "}";
    assertEquals("goal chip list should be correct.", expectedTop, actualResult);

    System.out.println(board);

  }



  @Test
  public void setChip_undoAddMoves() {
    GameBoard board = new GameBoard();
  }


  @Test
  public void setChip_undoStepMoves() {
    GameBoard board = new GameBoard();

  }

  @Test
  public void setChip_undoStepAndAddMoves() {
    GameBoard board = new GameBoard();

  }

  // test getValidMoves() on an empty board.
  @Test
  public void getValidMoves_emptyBoard() {
    GameBoard board = new GameBoard();

    List<MoveWithPlayer> player1ValidMoves = board.getValidMoves(WHITE_PLAYER);
    Iterator<MoveWithPlayer> iter1 = player1ValidMoves.iterator();
    for (int y = 1; y < BOARD_SIZE - 1; y++) {
      for (int x = 0; x < BOARD_SIZE; x++) {
        MoveWithPlayer expectedMove = new MoveWithPlayer(x, y, WHITE_PLAYER);
        if (iter1.hasNext()) {
          MoveWithPlayer move = iter1.next();
          assertEquals("Empty board does not return correct valid move for (" + x + "," + y + ")",
              expectedMove.toString(), move.toString());
        }
      }
    }

    List<MoveWithPlayer> player2ValidMoves = board.getValidMoves(BLACK_PLAYER);
    Iterator<MoveWithPlayer> iter2 = player2ValidMoves.iterator();
    for (int y = 0; y < BOARD_SIZE; y++) {
      for (int x = 1; x < BOARD_SIZE - 1; x++) {
        MoveWithPlayer expectedMove = new MoveWithPlayer(x, y, BLACK_PLAYER);
        if (iter2.hasNext()) {
          MoveWithPlayer move = iter2.next();
          assertEquals("Empty board does not return correct valid move for (" + x + "," + y + ")",
              expectedMove.toString(), move.toString());
        }
      }
    }
  }

  // test getValidMoves() on a board set up like in "README" file "Legal moves" section.
  @Test
  public void getValidMoves_partialBoard() {
    GameBoard board = new GameBoard();

    int[][] initialMoves = { {3,0}, {2,1}, {2,4}, {1,6}, {5,2}, {5,3}, {6,5}, {4,7} };
    for (int[] move : initialMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }

    int[][] whiteMoves = { {0,1}, {1,1}, {3,1}, {4,1}, {5,1}, {6,1}, {7,1}, {0,2}, {1,2},
        {2,2}, {3,2}, {4,2}, {6,2}, {7,2}, {0,3}, {1,3}, {2,3}, {3,3}, {4,3}, {6,3}, {7,3},
        {0,4}, {1,4}, {3,4}, {4,4}, {5,4}, {6,4}, {7,4}, {0,5}, {1,5}, {2,5}, {3,5}, {4,5},
        {5,5}, {7,5}, {0,6}, {2,6}, {3,6}, {4,6}, {5,6}, {6,6}, {7,6}};

    List<MoveWithPlayer> player1ValidMoves = board.getValidMoves(WHITE_PLAYER);
    Iterator<MoveWithPlayer> iter1 = player1ValidMoves.iterator();
    for (int[] move : whiteMoves) {
      int x = move[0];
      int y = move[1];
      MoveWithPlayer actualMove = new MoveWithPlayer(x, y, WHITE_PLAYER);
      if (iter1.hasNext()) {
        MoveWithPlayer expectedMove = iter1.next();
        assertEquals("Partial filled board does not return correct valid move for (" + x + "," + y + ")",
            actualMove.toString(), expectedMove.toString());
      }
    }

    int[][] blackMoves = { {5,0}, {6,0}, {1,3}, {2,3}, {3,3}, {1,4}, {3,4}, {3,5},
        {4,5}, {5,5}, {2,6}, {3,6}, {4,6}, {6,6}, {1,7}, {2,7}, {3,7}, {5,7}, {6,7} };

    List<MoveWithPlayer> player2ValidMoves = board.getValidMoves(BLACK_PLAYER);
    Iterator<MoveWithPlayer> iter2 = player2ValidMoves.iterator();
    for (int[] move : blackMoves) {
      int x = move[0];
      int y = move[1];
      MoveWithPlayer expectedMove = new MoveWithPlayer(x, y, BLACK_PLAYER);
      if (iter2.hasNext()) {
        MoveWithPlayer actualMove = iter2.next();
        assertEquals("Partial filled board does not return correct valid move for (" + x + "," + y + ")",
            expectedMove.toString(), actualMove.toString());
      }
    }
  }

  // test getConnections() on an empty board.
  @Test
  public void getConnections_emptyBoard() {
    GameBoard board = new GameBoard();
    for (int j = 0; j < BOARD_SIZE; j++) {
      for (int i = 0; i < BOARD_SIZE; i++) {
        List<Integer[]> connections1 = board.getConnections(WHITE_PLAYER, i, j);
        List<Integer[]> connections2 = board.getConnections(BLACK_PLAYER, i, j);
        assertTrue("Should not have any connections for empty board.",
            connections1.isEmpty());
        assertTrue("Should not have any connections for empty board.",
            connections2.isEmpty());
      }
    }
  }

  // returns a string in the format "{ [x1, y1] [x2, y2] }" for a given List of connections
  private static String connectionToString(List<Integer[]> list) {
    StringBuilder sb = new StringBuilder(50);
    sb.append("{ ");
    for (Integer[] item : list) {
      sb.append(Arrays.toString(item));
      sb.append(" ");
    }
    sb.append("}");
    return sb.toString();
  }

  // test getConnections() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void getConnections_partialBoard() {
    GameBoard board = new GameBoard();

    int[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));

    String[] expectedResult = {
        "{ [2, 5, " + DOWN + "] [4, 2, " + DOWN_RIGHT + "] }",
        "{ [6, 5, " + DOWN + "] [4, 2, " + DOWN_LEFT +  "] }",
        "{ [2, 0, " + UP_LEFT + "] [6, 0, " + UP_RIGHT + "] [3, 3, " + DOWN_LEFT + "] }",
        "{ [3, 3, " + RIGHT + "] [3, 5, " + DOWN_RIGHT + "] }",
        "{ [3, 5, " + DOWN + "] [1, 3, " + LEFT + "] [4, 2, " + UP_RIGHT + "] [5, 5, " +
            DOWN_RIGHT + "] }",
        "{ [2, 0, " + UP + "] [3, 5, " + RIGHT + "] }",
        "{ [3, 3, " + UP + "] [2, 5, " + LEFT + "] [1, 3, " + UP_LEFT + "] [5, 7, " +
            DOWN_RIGHT + "] }",
        "{ [5, 7, " + DOWN + "] [6, 5, " + RIGHT + "] [3, 3, " + UP_LEFT + "] }",
        "{ [6, 0, " + UP + "] [5, 5, " + LEFT + "] }",
        "{ [5, 5, " + UP + "] [3, 5, " + UP_LEFT + "] }"
    };

    for (int i = 0; i < moves.length; i++) {
      int x = moves[i][0];
      int y = moves[i][1];
      List<Integer[]> connections = board.getConnections(BLACK_PLAYER, moves[i][0], moves[i][1]);
      String result = connectionToString(connections);
      assertEquals("Incorrect connections for chip " + i + " at " + x + "," + y + ".",
          expectedResult[i], result);
    }
  }

  // test getConnections() on chips only in goals.
  @Test
  public void getConnections_multipleInGoal() {
    GameBoard board = new GameBoard();

    int[][] blackMoves = { {1,0}, {2,7}, {3,0}, {4,7}, {5,0}, {6,7} };
    for (int[] move : blackMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }

    int[][] whiteMoves = { {0,1}, {7,2}, {0,3}, {7,4}, {0,5}, {7,6} };
    for (int[] move : whiteMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], WHITE_PLAYER));
    }

    for (int[] move : blackMoves) {
      int x = move[0];
      int y = move[1];
      List<Integer[]> connections = board.getConnections(BLACK_PLAYER, x, y);
      String result = connectionToString(connections);
      assertEquals("Should not be connections between chips in same goal area.",
          "{ }", result);
    }

    for (int[] move : whiteMoves) {
      int x = move[0];
      int y = move[1];
      List<Integer[]> connections = board.getConnections(WHITE_PLAYER, x, y);
      String result = connectionToString(connections);
      assertEquals("Should not be connections between chips in same goal area.",
          "{ }", result);
    }
  }

  // test getConnections() on a chip added to an empty board
  @Test
  public void getConnections_singleChip() {
    GameBoard board = new GameBoard();

    board.setChip(new MoveWithPlayer(3, 3, WHITE_PLAYER));

    List<Integer[]> connections = board.getConnections(WHITE_PLAYER, 3, 3);
    String result = connectionToString(connections);
    assertEquals("Should not be connections for a single chip.", "{ }", result);
  }

  // test getConnections() on a chip with connections in all directions.
  @Test
  public void getConnections_connectionInAllDirections() {
    GameBoard board = new GameBoard();

    board.setChip(new MoveWithPlayer(3, 3, WHITE_PLAYER));

    int[][] whiteMoves = { {3, 1}, {3, 6}, {0,3}, {7,3}, {1,1}, {5,1}, {0,6}, {6,6} };
    for (int[] move : whiteMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], WHITE_PLAYER));
    }

    List<Integer[]> connections = board.getConnections(WHITE_PLAYER,3, 3);
    String result = connectionToString(connections);
    assertEquals("Should have connections in all directions.",
        "{ [3, 1, " + UP + "] [3, 6, " + DOWN + "] [0, 3, " + LEFT + "] [7, 3, " +
            RIGHT + "] [1, 1, " + UP_LEFT + "] [5, 1, " + UP_RIGHT + "] [0, 6, " +
            DOWN_LEFT + "] [6, 6, " + DOWN_RIGHT + "] }", result);
  }

  // test getConnections() on a chip that has connection in all directions blocked by chips
  // from opposing side.
  @Test
  public void getConnections_connectionInAllDirectionsBlocked() {
    GameBoard board = new GameBoard();

    // test blocking connection to upper left and upper right
    board.setChip(new MoveWithPlayer(3, 3, WHITE_PLAYER));

    int[][] whiteMoves1 = { {1,1}, {5,1} };
    for (int[] move : whiteMoves1) {
      board.setChip(new MoveWithPlayer(move[0], move[1], WHITE_PLAYER));
    }

    int[][] blackMoves1 = { {2, 2}, {4,2} };
    for (int[] move : blackMoves1) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }

    List<Integer[]> connections = board.getConnections(WHITE_PLAYER,3, 3);
    String result = connectionToString(connections);
    assertEquals("Should not have connection in all directions (blocked).", "{ }",
        result);

    // test blocking connection to remaining directions
    board = new GameBoard();
    board.setChip(new MoveWithPlayer(3, 3, WHITE_PLAYER));

    int[][] whiteMoves2 = { {3, 1}, {3, 6}, {0,3}, {7,3}, {0,6}, {6,6} };
    for (int[] move : whiteMoves2) {
      board.setChip(new MoveWithPlayer(move[0], move[1], WHITE_PLAYER));
    }

    int[][] blackMoves2 = { {3, 2}, {3, 5}, {1,3}, {5,3}, {1,5}, {5,5} };
    for (int[] move : blackMoves2) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }

    connections = board.getConnections(WHITE_PLAYER,3, 3);
    result = connectionToString(connections);
    assertEquals("Should not have connection in all directions (blocked).", "{ }",
        result);
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_noBottomGoalChip() {
    GameBoard board = new GameBoard();

    int[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}, /*{5,7}*/ };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    assertFalse("Board has a valid network.", board.hasValidNetwork(BLACK_PLAYER));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_noTopGoalChip() {
    GameBoard board = new GameBoard();

    int[][] moves = { /*{2,0}, {6,0},*/ {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    assertFalse("Board has a valid network.", board.hasValidNetwork(BLACK_PLAYER));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_noTopOrBottomGoalChips() {
    GameBoard board = new GameBoard();

    int[][] moves = { /*{2,0}, {6,0},*/ {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}/*, {5,7}*/ };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    assertFalse("Board has a valid network.", board.hasValidNetwork(BLACK_PLAYER));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_goodNetwork1() {
    GameBoard board = new GameBoard();

    int[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    // 20 - 25 - 35 - 33 - 55 - 57
    assertTrue("Board has a valid network.", board.hasValidNetwork(BLACK_PLAYER));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_goodNetwork2() {
    GameBoard board = new GameBoard();

    int[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, /*{2,5},*/ {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    // 60 - 65 - 55 - 33 - 35 - 57
    assertTrue("Board has a valid network.", board.hasValidNetwork(BLACK_PLAYER));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_goodNetwork3() {
    GameBoard board = new GameBoard();

    int[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, {2,5}, /*{3,5},*/ {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    // 60 - 65 - 55 - 33 - 13 - 57
    assertTrue("Board has a valid network.", board.hasValidNetwork(BLACK_PLAYER));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_allChipsFormNetwork() {
    GameBoard board = new GameBoard();

    int[][] moves = { /*{2,0},*/ {6,0}, /*{4,2}, {1,3},*/ {3,3}, /*{2,5},*/ {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    // 60 - 65 - 55 - 33 - 35 - 57
    assertTrue("Board has a valid network.", board.hasValidNetwork(BLACK_PLAYER));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_shortOneChipNoNetwork() {
    GameBoard board = new GameBoard();

    int[][] moves = { /*{2,0},*/ {6,0}, /*{4,2}, {1,3}, {3,3}, {2,5},*/ {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    assertFalse("Board should not have a valid network.", board.hasValidNetwork(BLACK_PLAYER));
  }
}