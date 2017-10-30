package player;

import DataStructures.List.InvalidNodeException;
import DataStructures.List.List;
import DataStructures.List.ListNode;

public class GameBoardTest {

  protected final static boolean WHITE_PLAYER = true;
  protected final static boolean BLACK_PLAYER = false;

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
     getValidMoves_partialBoard1();

    // test getConnections()
    // test hasValidNetwork()
  }

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

  private static void setChip_occupied() {
    GameBoard board = new GameBoard();
    board.setWhiteChip(new Move(GameBoard.BOARD_SIZE/2, GameBoard.BOARD_SIZE/2));
    Move move1 = new Move(GameBoard.BOARD_SIZE/2, GameBoard.BOARD_SIZE/2);

    TestHelper.verify(!board.setWhiteChip(move1), "setChip() failed for white chip " +
        "move to occupied square (" + move1.x1 + "," + move1.y1 + ")");
    TestHelper.verify(!board.setBlackChip(move1), "setChip() failed for black chip " +
        "move to occupied square (" + move1.x1 + "," + move1.y1 + ")");
  }

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
                "getValidMoves() failed for empty board for square (" + x + "," + y + ") " +
                    "of white player");
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
                "getValidMoves() failed for empty board for square (" + x + "," + y + ") " +
                    "of black player");
            node = node.next();
          } catch (InvalidNodeException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  private static void getValidMoves_partialBoard1() {
    GameBoard board = new GameBoard();

    // set up board like in "README" file "Legal moves" section
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

}


