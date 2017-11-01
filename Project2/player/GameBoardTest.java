package player;

import DataStructures.List.InvalidNodeException;
import DataStructures.List.List;
import DataStructures.List.ListNode;
import java.util.Arrays;

public class GameBoardTest {

  private final static boolean WHITE_PLAYER = true;
  private final static boolean BLACK_PLAYER = false;

  public static void main(String[] args) {
    // test setChip()
    setChip_outOfBounds();
    setChip_corner();
    setChip_wrongGoal();
    setChip_correctGoal();
    setChip_occupied();
    setChip_clusters();
    setChip_sameSquare();
    setChip_badMoveType();

    // test getValidMoves()
     getValidMoves_emptyBoard();
     getValidMoves_partialBoard();

    // test getConnections()
    getConnections_emptyBoard();
    getConnections_multipleInGoal();
    getConnections_singleChip();
    getConnections_connectionInAllDirections();
    getConnections_connectionInAllDirectionsBlocked();
    getConnections_partialBoard();

    // test hasValidNetwork()
  }


  // test setChip() on a move out of the boundaries of the board.
  private static void setChip_outOfBounds() {
    GameBoard board = new GameBoard();
    Move move1 = new Move(GameBoard.BOARD_SIZE, GameBoard.BOARD_SIZE);
    Move move2 = new Move(-1, -1);

    TestHelper.verify(!board.setWhiteChip(move1), "setChip() failed for out of " +
        "bounds move (" + move1.x1 + "," + move1.y1 + ")");
    TestHelper.verify(!board.setWhiteChip(move2), "setChip() failed for out of " +
        "bounds move (" + move2.x1 + "," + move2.y1 + ")");

    TestHelper.verify(!board.setBlackChip(move1), "setChip() failed for out of " +
        "bounds move (" + move1.x1 + "," + move1.y1 + ")");
    TestHelper.verify(!board.setBlackChip(move2), "setChip() failed for out of " +
        "bounds move (" + move2.x1 + "," + move2.y1 + ")");
  }

  // test setChip() on a move to each corner.
  private static void setChip_corner() {
    GameBoard board = new GameBoard();
    Move move1 = new Move(0, 0);
    Move move2 = new Move(0, GameBoard.BOARD_SIZE-1);
    Move move3 = new Move(GameBoard.BOARD_SIZE-1, 0);
    Move move4 = new Move(GameBoard.BOARD_SIZE-1, GameBoard.BOARD_SIZE-1);

    TestHelper.verify(!board.setWhiteChip(move1), "setChip() failed for move to " +
        "corner (" + move1.x1 + "," + move1.y1 + ")");
    TestHelper.verify(!board.setWhiteChip(move2), "setChip() failed for move to " +
        "corner (" + move2.x1 + "," + move2.y1 + ")");
    TestHelper.verify(!board.setWhiteChip(move3), "setChip() failed for move to " +
        "corner (" + move3.x1 + "," + move3.y1 + ")");
    TestHelper.verify(!board.setWhiteChip(move4), "setChip() failed for move to " +
        "corner (" + move4.x1 + "," + move4.y1 + ")");

    TestHelper.verify(!board.setBlackChip(move1), "setChip() failed for move to " +
        "corner (" + move1.x1 + "," + move1.y1 + ")");
    TestHelper.verify(!board.setBlackChip(move2), "setChip() failed for move to " +
        "corner (" + move2.x1 + "," + move2.y1 + ")");
    TestHelper.verify(!board.setBlackChip(move3), "setChip() failed for move to " +
        "corner (" + move3.x1 + "," + move3.y1 + ")");
    TestHelper.verify(!board.setBlackChip(move4), "setChip() failed for move to " +
        "corner (" + move4.x1 + "," + move4.y1 + ")");
  }

  // test setChip() on a move to the wrong goal.
  private static void setChip_wrongGoal() {
    GameBoard board = new GameBoard();
    Move move1 = new Move(GameBoard.BOARD_SIZE/2, 0); // top goal
    Move move2 = new Move(GameBoard.BOARD_SIZE/2, GameBoard.BOARD_SIZE-1); // bottom goal
    Move move3 = new Move(0, GameBoard.BOARD_SIZE/2); // left goal
    Move move4 = new Move(GameBoard.BOARD_SIZE-1, GameBoard.BOARD_SIZE/2); // right goal

    TestHelper.verify(!board.setWhiteChip(move1), "setChip() failed for white chip " +
        "move to top goal (" + move1.x1 + "," + move1.y1 + ")");
    TestHelper.verify(!board.setWhiteChip(move2), "setChip() failed for white chip " +
        "move to bottom goal (" + move2.x1 + "," + move2.y1 + ")");
    TestHelper.verify(!board.setBlackChip(move3), "setChip() failed for black chip " +
        "move to left goal (" + move3.x1 + "," + move3.y1 + ")");
    TestHelper.verify(!board.setBlackChip(move4), "setChip() failed for black chip " +
        "move to right goal (" + move4.x1 + "," + move4.y1 + ")");
  }

  // test setChip() on a move to the correct goal.
  private static void setChip_correctGoal() {
    GameBoard board = new GameBoard();
    Move move1 = new Move(GameBoard.BOARD_SIZE/2, 0); // top goal
    Move move2 = new Move(GameBoard.BOARD_SIZE/2, GameBoard.BOARD_SIZE-1); // bottom goal
    Move move3 = new Move(0, GameBoard.BOARD_SIZE/2); // left goal
    Move move4 = new Move(GameBoard.BOARD_SIZE-1, GameBoard.BOARD_SIZE/2); // right goal

    TestHelper.verify(board.setBlackChip(move1), "setChip() failed for black chip " +
        "move to top goal (" + move1.x1 + "," + move1.y1 + ")");
    TestHelper.verify(board.setBlackChip(move2), "setChip() failed for black chip " +
        "move to bottom goal (" + move2.x1 + "," + move2.y1 + ")");
    TestHelper.verify(board.setWhiteChip(move3), "setChip() failed for white chip " +
        "move to left goal (" + move3.x1 + "," + move3.y1 + ")");
    TestHelper.verify(board.setWhiteChip(move4), "setChip() failed for white chip " +
        "move to right goal (" + move4.x1 + "," + move4.y1 + ")");
  }

  // test setChip() on a move to an already occupied square.
  private static void setChip_occupied() {
    GameBoard board = new GameBoard();
    board.setWhiteChip(new Move(GameBoard.BOARD_SIZE/2, GameBoard.BOARD_SIZE/2));
    Move move1 = new Move(GameBoard.BOARD_SIZE/2, GameBoard.BOARD_SIZE/2);

    TestHelper.verify(!board.setWhiteChip(move1), "setChip() failed for white chip " +
        "move to occupied square (" + move1.x1 + "," + move1.y1 + ")");
    TestHelper.verify(!board.setBlackChip(move1), "setChip() failed for black chip " +
        "move to occupied square (" + move1.x1 + "," + move1.y1 + ")");
  }

  // test setChip() on moves that would form a cluster.
  private static void setChip_clusters() {
    GameBoard board = new GameBoard();
    // set up board like in "README" file "Legal moves" section
    int[][] initialMoves = { {3,0}, {2,1}, {2,4}, {1,6}, {5,2}, {5,3}, {6,5}, {4,7} };
    for (int[] move : initialMoves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }

    int[][] moves = { {1,0}, {2,0}, {4,0}, {1,1}, {3,1}, {4,1}, {5,1}, {6,1},
        {1,2}, {2,2}, {3,2}, {4,2}, {6,2}, {4,3}, {6,3}, {4,4}, {5,4}, {6,4},
        {1,5}, {2,5}, {5,6}};
    for (int[] move : moves) {
      Move m = new Move(move[0], move[1]);
      TestHelper.verify(!board.setBlackChip(m), "setChip() failed for black chip " +
          "move (" + move[0] + "," + move[1] + ") forms a cluster");
    }
  }

  // test setChip() for a step move to the same square.
  private static void setChip_sameSquare() {
    GameBoard board = new GameBoard();
    board.whiteChipCount = 10;
    board.blackChipCount = 10;
    Move move1 = new Move(1, 1, 1, 1);
    Move move2 = new Move(4, 4, 4, 4);

    TestHelper.verify(!board.setWhiteChip(move1), "setChip() failed for white " +
        "chip step move to same square. From (" + move1.x1 + "," + move1.y1 + ") to (" +
        move1.x2 + "," + move1.y2 + ")");
    TestHelper.verify(!board.setBlackChip(move2), "setChip() failed for black " +
        "chip step move to same square. From (" + move1.x1 + "," + move1.y1 + ") to (" +
        move1.x2 +"," + move1.y2 + ")");
  }

  // test setChip() on illegal step and add moves.
  private static void setChip_badMoveType() {
    GameBoard board = new GameBoard();
    board.whiteChipCount = 10;
    board.blackChipCount = 9;
    Move move1 = new Move(1, 1);
    Move move2 = new Move(2, 2, 3, 3);

    TestHelper.verify(!board.setWhiteChip(move1), "setChip() failed for illegal " +
        "step move. Player has " + board.whiteChipCount);
    TestHelper.verify(!board.setBlackChip(move2), "setChip() failed for illegal " +
        "add move. Player has " + board.blackChipCount);

    board.whiteChipCount = 9;
    board.blackChipCount = 10;
    TestHelper.verify(!board.setBlackChip(move1), "setChip() failed for illegal " +
        "step move. Player has " + board.whiteChipCount);
    TestHelper.verify(!board.setWhiteChip(move2), "setChip() failed for illegal " +
        "add move. Player has " + board.blackChipCount);
  }

  // test getValidMoves() on an empty board.
  private static void getValidMoves_emptyBoard() {
    GameBoard board = new GameBoard();
    List<Move> player1ValidMoves = board.getValidMoves(WHITE_PLAYER);
    List<Move> player2ValidMoves = board.getValidMoves(BLACK_PLAYER);

    ListNode<Move> node = player1ValidMoves.front();
    for (int y = 1; y < GameBoard.BOARD_SIZE - 1; y++) {
      for (int x = 0; x < GameBoard.BOARD_SIZE; x++) {
        Move m = new Move(x, y);
        if (node.isValidNode()) {
          try {
            TestHelper.verify(node.item().toString().equals(m.toString()),
                "getValidMoves() failed for empty board for square (" + x + "," + y +
                    ") of white player");
            node = node.next();
          } catch (InvalidNodeException e) {
            e.printStackTrace();
          }
        }
      }
    }

    node = player2ValidMoves.front();
    for (int y = 0; y < GameBoard.BOARD_SIZE; y++) {
      for (int x = 1; x < GameBoard.BOARD_SIZE - 1; x++) {
        Move m = new Move(x, y);
        if (node.isValidNode()) {
          try {
            TestHelper.verify(node.item().toString().equals(m.toString()),
                "getValidMoves() failed for empty board for square (" + x + "," + y +
                    ") of black player");
            node = node.next();
          } catch (InvalidNodeException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  // test getValidMoves() on a board set up like in "README" file "Legal moves" section.
  private static void getValidMoves_partialBoard() {
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
          TestHelper.verify(node.item().toString().equals(m.toString()),
              "getValidMoves() failed for white chip move (" + x + "," + y + ")");
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
          TestHelper.verify(node.item().toString().equals(m.toString()),
              "getValidMoves() failed for black chip move (" + x + "," + y + ")");
          node = node.next();
        } catch (InvalidNodeException e) {
          e.printStackTrace();
        }
      }
    }
  }

  // test getConnections() on an empty board.
  private static void getConnections_emptyBoard() {
    GameBoard board = new GameBoard();
    for (int j = 0; j < GameBoard.BOARD_SIZE; j++) {
      for (int i = 0; i < GameBoard.BOARD_SIZE; i++) {
        List<Integer[]> connections1 = board.getConnections(WHITE_PLAYER, i, j);
        List<Integer[]> connections2 = board.getConnections(BLACK_PLAYER, i, j);
        TestHelper.verify(connections1.isEmpty(),
            "getConnections() failed for empty board.");
        TestHelper.verify(connections2.isEmpty(),
            "getConnections() failed for empty board.");
      }
    }
  }

  // returns a string in the format "{ [x1, y1] [x2, y2] }" for a given List of connections
  private static String connectionToString(List<Integer[]> list) throws InvalidNodeException {
    ListNode<Integer[]> node = list.front();
    StringBuilder sb = new StringBuilder(50);
    sb.append("{ ");
    while (node.isValidNode()) {
      sb.append(Arrays.toString(node.item()));
      sb.append(" ");
      node = node.next();
    }
    sb.append("}");
    return sb.toString();
  }

  // test getConnections() on a board set up like in "README" file "Object of Play" section.
  private static void getConnections_partialBoard() {
    GameBoard board = new GameBoard();

    int[][] moves = { {2,0}, {6,0}, {4,2}, {1,3}, {3,3}, {2,5}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }
    board.setWhiteChip(new Move(4, 5));

    List<Integer[]> connections;
    String result;
    try {
      // chip 1 at 2,0
      connections = board.getConnections(BLACK_PLAYER, moves[0][0], moves[0][1]);
      result = connectionToString(connections);
      TestHelper.verify(result.equals("{ [2, 5] [4, 2] }"),
          "getConnections() failed for chip 1 of partial board.");

      // chip 2 at 6,0
      connections = board.getConnections(BLACK_PLAYER, moves[1][0], moves[1][1]);
      result = connectionToString(connections);
      TestHelper.verify(result.equals("{ [6, 5] [4, 2] }"),
          "getConnections() failed for chip 2 of partial board.");

      // chip 3 at 4,2
      connections = board.getConnections(BLACK_PLAYER, moves[2][0], moves[2][1]);
      result = connectionToString(connections);
      TestHelper.verify(result.equals("{ [2, 0] [6, 0] [3, 3] }"),
          "getConnections() failed for chip 3 of partial board");

      // chip 4 at 1,3
      connections = board.getConnections(BLACK_PLAYER, moves[3][0], moves[3][1]);
      result = connectionToString(connections);
      TestHelper.verify(result.equals("{ [3, 3] [3, 5] }"),
          "getConnections() failed for chip 4 of partial board");

      // chip 5 at 3,3
      connections = board.getConnections(BLACK_PLAYER, moves[4][0], moves[4][1]);
      result = connectionToString(connections);
      TestHelper.verify(result.equals("{ [3, 5] [1, 3] [4, 2] [5, 5] }"),
          "getConnections() failed for chip 5 of partial board");

      // chip 6 at 2,5
      connections = board.getConnections(BLACK_PLAYER, moves[5][0], moves[5][1]);
      result = connectionToString(connections);
      TestHelper.verify(result.equals("{ [2, 0] [3, 5] }"),
          "getConnections() failed for chip 6 of partial board");

      // chip 7 at 3,5 (white chip at 4,5)
      connections = board.getConnections(BLACK_PLAYER, moves[6][0], moves[6][1]);
      result = connectionToString(connections);
      TestHelper.verify(result.equals("{ [3, 3] [2, 5] [1, 3] [5, 7] }"),
          "getConnections() failed for chip 7 of partial board");

      // chip 8 at 5,5 (white chip at 4,5)
      connections = board.getConnections(BLACK_PLAYER, moves[7][0], moves[7][1]);
      result = connectionToString(connections);
      TestHelper.verify(result.equals("{ [5, 7] [6, 5] [3, 3] }"),
          "getConnections() failed for chip 8 of partial board");

      // chip 9 at 5,5 (white chip at 4,5)
      connections = board.getConnections(BLACK_PLAYER, moves[8][0], moves[8][1]);
      result = connectionToString(connections);
      TestHelper.verify(result.equals("{ [6, 0] [5, 5] }"),
          "getConnections() failed for chip 9 of partial board");

      // chip 10 at 5,5 (white chip at 4,5)
      connections = board.getConnections(BLACK_PLAYER, moves[9][0], moves[9][1]);
      result = connectionToString(connections);
      TestHelper.verify(result.equals("{ [5, 5] [3, 5] }"),
          "getConnections() failed for chip 10 of partial board");

    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
  }

  // test getConnections() on chips only in goals.
  private static void getConnections_multipleInGoal() {
    GameBoard board = new GameBoard();

    int[][] blackMoves = { {1,0}, {2,7}, {3,0}, {4,7}, {5,0}, {6,7} };
    for (int[] move : blackMoves) {
      board.setBlackChip(new Move(move[0], move[1]));
    }

    int[][] whiteMoves = { {0,1}, {7,2}, {0,3}, {7,4}, {0,5}, {7,6} };
    for (int[] move : whiteMoves) {
      board.setWhiteChip(new Move(move[0], move[1]));
    }

    List<Integer[]> connections;
    String result;

    for (int i = 0; i < blackMoves.length; i++) {
      try {
        connections = board.getConnections(BLACK_PLAYER, blackMoves[i][0], blackMoves[i][1]);
        result = connectionToString(connections);
        TestHelper.verify(result.equals("{ }"),
            "getConnections() failed for chip " + i + " in black goal.");
      } catch (InvalidNodeException e) {
        e.printStackTrace();
      }
    }

    for (int i = 0; i < whiteMoves.length; i++) {
      try {
        connections = board.getConnections(WHITE_PLAYER, whiteMoves[i][0], whiteMoves[i][1]);
        result = connectionToString(connections);
        TestHelper.verify(result.equals("{ }"),
            "getConnections() failed for chip " + i + " in white goal.");
      } catch (InvalidNodeException e) {
        e.printStackTrace();
      }
    }
  }

  // test getConnections() on a chip added to an empty board
  private static void getConnections_singleChip() {
    GameBoard board = new GameBoard();
    List<Integer[]> connections;

    board.setWhiteChip(new Move(3, 3));
    connections = board.getConnections(WHITE_PLAYER, 3, 3);
    try {
      String result = connectionToString(connections);
      TestHelper.verify(result.equals("{ }"), "getConnections() failed for single chip.");
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
  }

  // test getConnections() on a chip with connections in all directions
  private static void getConnections_connectionInAllDirections() {
    GameBoard board = new GameBoard();
    List<Integer[]> connections;

    board.setWhiteChip(new Move(3, 3));

    int[][] whiteMoves = { {3, 1}, {3, 6}, {0,3}, {7,3}, {1,1}, {5,1}, {0,6}, {6,6} };
    for (int[] move : whiteMoves) {
      board.setWhiteChip(new Move(move[0], move[1]));
    }

    connections = board.getConnections(WHITE_PLAYER,3, 3);
    try {
      String result = connectionToString(connections);
      TestHelper.verify(result.equals("{ [3, 1] [3, 6] [0, 3] [7, 3] [1, 1] [5, 1] [0, 6] " +
              "[6, 6] }"), "getConnections() failed for chip with connections in all " +
          "directions.");
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
  }

  // test getConnections() on a chip that has connection in all directions blocked by chips
  // from opposing side.
  private static void getConnections_connectionInAllDirectionsBlocked() {
    GameBoard board = new GameBoard();
    List<Integer[]> connections;

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

    connections = board.getConnections(WHITE_PLAYER,3, 3);
    try {
      String result = connectionToString(connections);
      TestHelper.verify(result.equals("{ }"), "getConnections() failed for chip with " +
          "connection blocked in all directions.");
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }

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
    try {
      String result = connectionToString(connections);
      TestHelper.verify(result.equals("{ }"), "getConnections() failed for chip with " +
          "connection blocked in all directions.");
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
  }

}


