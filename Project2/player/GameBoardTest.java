package player;


import DataStructures.List.InvalidNodeException;
import DataStructures.List.List;
import DataStructures.List.ListNode;
import java.util.Arrays;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class GameBoardTest {

  private final static boolean WHITE_PLAYER = true;
  private final static boolean BLACK_PLAYER = false;
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
    Move m1 = new Move(BOARD_SIZE, BOARD_SIZE);
    Move m2 = new Move(-1, -1);

    assertFalse("Should not set white chip out of bounds (" + m1.x1 + "," + m1.y1 + ").",
        board.setWhiteChip(m1));
    assertFalse("Should not set white chip out of bounds (" + m1.x1 + "," + m1.y1 + ").",
        board.setWhiteChip(m2));

    assertFalse("Should not set black chip out of bounds (" + m1.x1 + "," + m1.y1 + ").",
        board.setBlackChip(m1));
    assertFalse("Should not set black chip out of bounds (" + m1.x1 + "," + m1.y1 + ").",
        board.setBlackChip(m2));
  }

  // test setChip() on a move to each corner.
  @Test
  public void setChip_corner() {
    GameBoard board = new GameBoard();
    Move m1 = new Move(0, 0);
    Move m2 = new Move(0, BOARD_SIZE-1);
    Move m3 = new Move(BOARD_SIZE-1, 0);
    Move m4 = new Move(BOARD_SIZE-1, BOARD_SIZE-1);

    assertFalse("Should not set white chip in corner (" + m1.x1 + "," + m1.y1 + ").",
        board.setWhiteChip(m1));
    assertFalse("Should not set white chip in corner (" + m2.x1 + "," + m2.y1 + ").",
        board.setWhiteChip(m2));
    assertFalse("Should not set white chip in corner (" + m3.x1 + "," + m3.y1 + ").",
        board.setWhiteChip(m3));
    assertFalse("Should not set white chip in corner (" + m4.x1 + "," + m4.y1 + ").",
        board.setWhiteChip(m4));

    assertFalse("Should not set black chip in corner (" + m1.x1 + "," + m1.y1 + ").",
        board.setBlackChip(m1));
    assertFalse("Should not set black chip in corner (" + m2.x1 + "," + m2.y1 + ").",
        board.setBlackChip(m2));
    assertFalse("Should not set black chip in corner (" + m3.x1 + "," + m3.y1 + ").",
        board.setBlackChip(m3));
    assertFalse("Should not set black chip in corner (" + m4.x1 + "," + m4.y1 + ").",
        board.setBlackChip(m4));
  }

  // test setChip() on a move to the wrong goal.
  @Test
  public void setChip_wrongGoal() {
    GameBoard board = new GameBoard();
    Move m1 = new Move(BOARD_SIZE/2, 0); // top goal
    Move m2 = new Move(BOARD_SIZE/2, BOARD_SIZE-1); // bottom goal
    Move m3 = new Move(0, BOARD_SIZE/2); // left goal
    Move m4 = new Move(BOARD_SIZE-1, BOARD_SIZE/2); // right goal

    assertFalse("Should not set white chip in top goal (" + m1.x1 + "," + m1.y1 + ").",
        board.setWhiteChip(m1));
    assertFalse("Should not set white chip in bottom goal (" + m2.x1 + "," + m2.y1 + ").",
        board.setWhiteChip(m2));

    assertFalse("Should not set black chip in left goal (" + m3.x1 + "," + m3.y1 + ").",
        board.setBlackChip(m3));
    assertFalse("Should not set black chip in right goal (" + m4.x1 + "," + m4.y1 + ").",
        board.setBlackChip(m4));
  }

  // test setChip() on a move to the correct goal.
  @Test
  public void setChip_correctGoal() {
    GameBoard board = new GameBoard();
    Move m1 = new Move(BOARD_SIZE/2, 0); // top goal
    Move m2 = new Move(BOARD_SIZE/2, BOARD_SIZE-1); // bottom goal
    Move m3 = new Move(0, BOARD_SIZE/2); // left goal
    Move m4 = new Move(BOARD_SIZE-1, BOARD_SIZE/2); // right goal

    assertTrue("Should set black chip in top goal (" + m1.x1 + "," + m1.y1 + ").",
        board.setBlackChip(m1));
    assertTrue("Should set black chip in bottom goal (" + m2.x1 + "," + m2.y1 + ").",
        board.setBlackChip(m2));

    assertTrue("Should set white chip in left goal (" + m3.x1 + "," + m3.y1 + ").",
        board.setWhiteChip(m3));
    assertTrue("Should set white chip in right goal (" + m4.x1 + "," + m4.y1 + ").",
        board.setWhiteChip(m4));
  }

  // test setChip() on a move to an already occupied square.
  @Test
  public void setChip_occupied() {
    GameBoard board = new GameBoard();
    board.setWhiteChip(new Move(BOARD_SIZE/2, BOARD_SIZE/2));
    Move m1 = new Move(BOARD_SIZE/2, BOARD_SIZE/2);

    assertFalse("Should not set white chip in occupied square (" + m1.x1 + "," + m1.y1 +
        ").", board.setWhiteChip(m1));

    assertFalse("Should not set black chip in occupied square (" + m1.x1 + "," + m1.y1 +
        ").", board.setBlackChip(m1));
  }

  // test setChip() on moves that would form a cluster.
  @Test
  public void setChip_clusters() {
    GameBoard board = new GameBoard();
    // set up board like in "README" file "Legal moves" section
    int[][] initialMoves = { {3,0}, {2,1}, {2,4}, {1,6}, {5,2}, {5,3}, {6,5}, {4,7} };
    for (int[] move : initialMoves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }

    int[][] moves = { {1,0}, {2,0}, {4,0}, {1,1}, {3,1}, {4,1}, {5,1}, {6,1}, {1,2}, {2,2},
        {3,2}, {4,2}, {6,2}, {4,3}, {6,3}, {4,4}, {5,4}, {6,4}, {1,5}, {2,5}, {5,6}};
    for (int[] move : moves) {
      int x = move[0];
      int y = move[1];
      Move m = new Move(x, y);
      assertFalse("Should not form a cluster (" + x + "," + y + ").",
          board.setBlackChip(m));
    }
  }

  // test setChip() for a step move to the same square.
  @Test
  public void setChip_stepSameSquare() {
    GameBoard board = new GameBoard();

    int[][] blackMoves = { {1,0}, {2,0}, {4,0}, {5,0}, {1,2}, {2,2}, {4,2}, {5,2}, {1,4}, {2,4} };
    for (int[] move : blackMoves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }

    int[][] whiteMoves = { {1,1}, {2,1}, {4,1}, {5,1}, {1,3}, {2,3}, {4,3}, {5,3}, {1,5}, {2,5} };
    for (int[] move : whiteMoves) {
      board.setWhiteChip(new Move(move[0], move[1]));
    }

    Move m1 = new Move(1, 0, 1, 0);
    Move m2 = new Move(1, 1, 1, 1);

    assertFalse("Should not step move to same square. From (" + m1.x1 + "," + m1.y1 +
        ") to (" + m1.x2 + "," + m1.y2 + ").", board.setBlackChip(m1));

    assertFalse("Should not step move to same square. From (" + m2.x1 + "," + m2.y1 +
        ") to (" + m2.x2 + "," + m2.y2 + ").", board.setWhiteChip(m2));
  }

  // test setChip() for a step move to another occupied square.
  @Test
  public void setChip_stepOccupiedSquare() {
    GameBoard board = new GameBoard();

    int[][] blackMoves = { {1,0}, {2,0}, {4,0}, {5,0}, {1,2}, {2,2}, {4,2}, {5,2}, {1,4}, {2,4} };
    for (int[] move : blackMoves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }

    int[][] whiteMoves = { {1,1}, {2,1}, {4,1}, {5,1}, {1,3}, {2,3}, {4,3}, {5,3}, {1,5}, {2,5} };
    for (int[] move : whiteMoves) {
      board.setWhiteChip(new Move(move[0], move[1]));
    }

    Move m1 = new Move(2, 0, 1, 0);
    Move m2 = new Move(2, 1, 1, 1);

    assertFalse("Should not step move to another occupied square. From (" +
        m1.x1 + "," + m1.y1 + ") to (" + m1.x2 + "," + m1.y2 + ").", board.setBlackChip(m1));

    assertFalse("Should not step move to another occupied square. From (" +
        m2.x1 + "," + m2.y1 + ") to (" + m2.x2 + "," + m2.y2 + ").", board.setWhiteChip(m2));
  }

  // test setChip() for a step move to an unoccupied square.
  @Test
  public void setChip_goodMove() {
    GameBoard board = new GameBoard();

    int[][] blackMoves = { {1,0}, {2,0}, {4,0}, {5,0}, {1,2}, {2,2}, {4,2}, {5,2}, {1,4}, {2,4} };
    for (int[] move : blackMoves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }

    int[][] whiteMoves = { {1,1}, {2,1}, {4,1}, {5,1}, {1,3}, {2,3}, {4,3}, {5,3}, {1,5}, {2,5} };
    for (int[] move : whiteMoves) {
      board.setWhiteChip(new Move(move[0], move[1]));
    }

    Move m1 = new Move(6, 7, 1, 0);
    Move m2 = new Move(7, 6, 1, 1);

    assertTrue("Should step move to an unoccupied square. From (" +
        m1.x1 + "," + m1.y1 + ") to (" + m1.x2 + "," + m1.y2 + ").", board.setBlackChip(m1));

    assertTrue("Should step move to an unoccupied square. From (" +
        m2.x1 + "," + m2.y1 + ") to (" + m2.x2 + "," + m2.y2 + ").", board.setWhiteChip(m2));
  }

  // test setChip() on illegal step and add moves.
  @Test
  public void setChip_badMoveType() {
    GameBoard board = new GameBoard();
    // 9 black chips
    int[][] blackMoves = { {1,0}, {2,0}, {4,0}, {5,0}, {1,2}, {2,2}, {4,2}, {5,2}, {1,4} };
    for (int[] move : blackMoves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }
    // 9 white chips
    int[][] whiteMoves = { {1,1}, {2,1}, {4,1}, {5,1}, {1,3}, {2,3}, {4,3}, {5,3}, {1,5}};
    for (int[] move : whiteMoves) {
      board.setWhiteChip(new Move(move[0], move[1]));
    }

    Move m1 = new Move(6, 6, 1, 0);
    Move m2 = new Move(6, 6);

    assertFalse("Should not step move with less than 9 chips on the board.",
        board.setBlackChip(m1));
    assertFalse("Should not step move with less than 9 chips on the board.",
        board.setWhiteChip(m1));

    // now 10 black and white chips
    board.setBlackChip(new Move(2, 4));
    board.setWhiteChip(new Move(2, 5));

    assertFalse("Should not add move with 10 chips on the board.",
        board.setBlackChip(m2));
    assertFalse("Should not add move with 10 chips on the board.",
        board.setWhiteChip(m2));

  }

  // test getValidMoves() on an empty board.
  @Test
  public void getValidMoves_emptyBoard() {
    GameBoard board = new GameBoard();

    List<Move> player1ValidMoves = board.getValidMoves(WHITE_PLAYER);
    ListNode<Move> node = player1ValidMoves.front();
    for (int y = 1; y < BOARD_SIZE - 1; y++) {
      for (int x = 0; x < BOARD_SIZE; x++) {
        Move m = new Move(x, y);
        if (node.isValidNode()) {
          try {
            assertEquals("Empty board does not return correct valid move for ( " + x +
                    "," + y, m.toString(), node.item().toString());
            node = node.next();
          } catch (InvalidNodeException e) {
            e.printStackTrace();
          }
        }
      }
    }

    List<Move> player2ValidMoves = board.getValidMoves(BLACK_PLAYER);
    node = player2ValidMoves.front();
    for (int y = 0; y < BOARD_SIZE; y++) {
      for (int x = 1; x < BOARD_SIZE - 1; x++) {
        Move m = new Move(x, y);
        if (node.isValidNode()) {
          try {
            assertEquals("Empty board does not return correct valid move for ( " + x +
                "," + y, m.toString(), node.item().toString());
            node = node.next();
          } catch (InvalidNodeException e) {
            e.printStackTrace();
          }
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
      board.setBlackChip(new Move(move[0], move[1]));
    }

    int[][] whiteMoves = { {0,1}, {1,1}, {3,1}, {4,1}, {5,1}, {6,1}, {7,1}, {0,2}, {1,2},
        {2,2}, {3,2}, {4,2}, {6,2}, {7,2}, {0,3}, {1,3}, {2,3}, {3,3}, {4,3}, {6,3}, {7,3},
        {0,4}, {1,4}, {3,4}, {4,4}, {5,4}, {6,4}, {7,4}, {0,5}, {1,5}, {2,5}, {3,5}, {4,5},
        {5,5}, {7,5}, {0,6}, {2,6}, {3,6}, {4,6}, {5,6}, {6,6}, {7,6}};

    List<Move> player1ValidMoves = board.getValidMoves(WHITE_PLAYER);
    ListNode<Move> node = player1ValidMoves.front();
    for (int[] move : whiteMoves) {
      int x = move[0];
      int y = move[1];
      Move m = new Move(x, y);
      if (node.isValidNode()) {
        try {
          assertEquals("Partial filled board does not return correct valid move for (" +
              x + "," + y + ")", m.toString(), node.item().toString());
          node = node.next();
        } catch (InvalidNodeException e) {
          e.printStackTrace();
        }
      }
    }

    int[][] blackMoves = { {5,0}, {6,0}, {1,3}, {2,3}, {3,3}, {1,4}, {3,4}, {3,5},
        {4,5}, {5,5}, {2,6}, {3,6}, {4,6}, {6,6}, {1,7}, {2,7}, {3,7}, {5,7}, {6,7} };

    List<Move> player2ValidMoves = board.getValidMoves(BLACK_PLAYER);
    node = player2ValidMoves.front();
    for (int[] move : blackMoves) {
      int x = move[0];
      int y = move[1];
      Move m = new Move(x, y);
      if (node.isValidNode()) {
        try {
          assertEquals("Partial filled board does not return correct valid move for (" +
              x + "," + y + ")", m.toString(), node.item().toString());
          node = node.next();
        } catch (InvalidNodeException e) {
          e.printStackTrace();
        }
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
    ListNode<Integer[]> node = list.front();
    StringBuilder sb = new StringBuilder(50);
    sb.append("{ ");
    while (node.isValidNode()) {
      try {
        sb.append(Arrays.toString(node.item()));
        sb.append(" ");
        node = node.next();
      } catch (InvalidNodeException e) {
        e.printStackTrace();
      }
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
      board.setBlackChip(new Move(move[0], move[1]));
    }
    board.setWhiteChip(new Move(4, 5));

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
      board.setBlackChip(new Move(move[0], move[1]));
    }

    int[][] whiteMoves = { {0,1}, {7,2}, {0,3}, {7,4}, {0,5}, {7,6} };
    for (int[] move : whiteMoves) {
      board.setWhiteChip(new Move(move[0], move[1]));
    }

    for (int i = 0; i < blackMoves.length; i++) {
      int x = blackMoves[i][0];
      int y = blackMoves[i][1];
      List<Integer[]> connections = board.getConnections(BLACK_PLAYER, x, y);
      String result = connectionToString(connections);
      assertEquals("Should not be connections between chips in same goal area.",
          "{ }", result);
    }

    for (int i = 0; i < whiteMoves.length; i++) {
      int x = whiteMoves[i][0];
      int y = whiteMoves[i][1];
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

    board.setWhiteChip(new Move(3, 3));

    List<Integer[]> connections = board.getConnections(WHITE_PLAYER, 3, 3);
    String result = connectionToString(connections);
    assertEquals("Should not be connections for a single chip.", "{ }", result);
  }

  // test getConnections() on a chip with connections in all directions
  @Test
  public void getConnections_connectionInAllDirections() {
    GameBoard board = new GameBoard();

    board.setWhiteChip(new Move(3, 3));

    int[][] whiteMoves = { {3, 1}, {3, 6}, {0,3}, {7,3}, {1,1}, {5,1}, {0,6}, {6,6} };
    for (int[] move : whiteMoves) {
      board.setWhiteChip(new Move(move[0], move[1]));
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
    board.setWhiteChip(new Move(3, 3));

    int[][] whiteMoves1 = { {1,1}, {5,1} };
    for (int[] move : whiteMoves1) {
      board.setWhiteChip(new Move(move[0], move[1]));
    }

    int[][] blackMoves1 = { {2, 2}, {4,2} };
    for (int[] move : blackMoves1) {
      board.setBlackChip(new Move(move[0], move[1]));
    }

    List<Integer[]> connections = board.getConnections(WHITE_PLAYER,3, 3);
    String result = connectionToString(connections);
    assertEquals("Should not have connection in all directions (blocked).", "{ }",
        result);

    // test blocking connection to remaining directions
    board = new GameBoard();
    board.setWhiteChip(new Move(3, 3));

    int[][] whiteMoves2 = { {3, 1}, {3, 6}, {0,3}, {7,3}, {0,6}, {6,6} };
    for (int[] move : whiteMoves2) {
      board.setWhiteChip(new Move(move[0], move[1]));
    }

    int[][] blackMoves2 = { {3, 2}, {3, 5}, {1,3}, {5,3}, {1,5}, {5,5} };
    for (int[] move : blackMoves2) {
      board.setBlackChip(new Move(move[0], move[1]));
    }

    connections = board.getConnections(WHITE_PLAYER,3, 3);
    result = connectionToString(connections);
    assertEquals("Should not have connection in all directions (blocked).", "{ }",
        result);
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_partialBoardNoBottomGoalChip() {
    GameBoard board = new GameBoard();

    int[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}, /*{5,7}*/ };
    for (int[] move : moves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }
    board.setWhiteChip(new Move(4, 5));
    assertFalse("Board has a valid network.", board.hasValidNetwork(false));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_partialBoardNoTopGoalChip() {
    GameBoard board = new GameBoard();

    int[][] moves = { /*{2,0}, {6,0},*/ {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }
    board.setWhiteChip(new Move(4, 5));
    assertFalse("Board has a valid network.", board.hasValidNetwork(false));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_partialBoardNoTopOrBottomGoalChips() {
    GameBoard board = new GameBoard();

    int[][] moves = { /*{2,0}, {6,0},*/ {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}/*, {5,7}*/ };
    for (int[] move : moves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }
    board.setWhiteChip(new Move(4, 5));
    assertFalse("Board has a valid network.", board.hasValidNetwork(false));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_partialBoardNetwork1() {
    GameBoard board = new GameBoard();

    int[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }
    board.setWhiteChip(new Move(4, 5));
    // 20 - 25 - 35 - 33 - 55 - 57
    assertTrue("Board has a valid network.", board.hasValidNetwork(false));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_partialBoardNetwork2() {
    GameBoard board = new GameBoard();

    int[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, /*{2,5},*/ {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }
    board.setWhiteChip(new Move(4, 5));
    // 60 - 65 - 55 - 33 - 35 - 57
    assertTrue("Board has a valid network.", board.hasValidNetwork(false));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_partialBoardNetwork3() {
    GameBoard board = new GameBoard();

    int[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, {2,5}, /*{3,5},*/ {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }
    board.setWhiteChip(new Move(4, 5));
    // 60 - 65 - 55 - 33 - 13 - 57
    assertTrue("Board has a valid network.", board.hasValidNetwork(false));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_partialBoardAllChipsFormNetwork() {
    GameBoard board = new GameBoard();

    int[][] moves = { /*{2,0},*/ {6,0}, /*{4,2}, {1,3},*/ {3,3}, /*{2,5},*/ {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }
    board.setWhiteChip(new Move(4, 5));
    // 60 - 65 - 55 - 33 - 35 - 57
    assertTrue("Board has a valid network.", board.hasValidNetwork(false));
  }

  // test hasValidNetwork() on a board set up like in "README" file "Object of Play" section.
  @Test
  public void hasValidNetwork_partialBoardShortOneChip() {
    GameBoard board = new GameBoard();

    int[][] moves = { /*{2,0},*/ {6,0}, /*{4,2}, {1,3}, {3,3}, {2,5},*/ {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }
    board.setWhiteChip(new Move(4, 5));
    // 60 - 65 - 55 - 33 - 35 - 57
    assertFalse("Board has a valid network.", board.hasValidNetwork(false));
  }
}