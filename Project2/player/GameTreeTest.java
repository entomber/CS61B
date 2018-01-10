package player;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GameTreeTest {
  private final static int BLACK_PLAYER = MachinePlayer.BLACK_PLAYER;
  private final static int WHITE_PLAYER = MachinePlayer.WHITE_PLAYER;
  private GameBoard board;
  private GameTree gameTreeBlack;
  private GameTree gameTreeWhite;
  private MoveWithPlayer actualResultIterative;
  private MoveWithPlayer expectedResult;

  @Before
  public void setup() {
    board = new GameBoard();
    gameTreeBlack = new GameTree(board, BLACK_PLAYER, 5, 2);
    gameTreeWhite = new GameTree(board, WHITE_PLAYER, 5, 2);
  }

  @After
  public void tearDown() {
    board = null;
    gameTreeBlack = null;
    gameTreeWhite = null;
    actualResultIterative = null;
    expectedResult = null;
  }

  // test chooseMove() when there is network on the board for multiple depths.
  @Test
  public void chooseMove_winAlreadyExists() {
    int[][] blackMoves = { {6,0}, {3,3}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : blackMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    // 60 - 65 - 55 - 33 - 35 - 57
    assertTrue("Board has a valid network.", board.hasValidNetwork(BLACK_PLAYER));

    expectedResult = new MoveWithPlayer(3, 3, BLACK_PLAYER);
      actualResultIterative = gameTreeBlack.iterativeDeepeningSearch();
    assertNull("Iterative search should return null.", actualResultIterative);
  }

  // test chooseMove() with first move in move list as win for multiple depths.
  @Test
  public void chooseMove_oneMoveToWinFirstMoveInList() {
    int[][] moves = { {1,2}, {4,2}, {2,4}, {6,4}, {3,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    assertFalse("Board should not have a valid network.", board.hasValidNetwork(BLACK_PLAYER));

    expectedResult = new MoveWithPlayer(1, 0, BLACK_PLAYER);
    actualResultIterative = gameTreeBlack.iterativeDeepeningSearch();
    assertEquals("Move should be correct.", expectedResult, actualResultIterative);
  }

  // test chooseMove() with 1 move from win for multiple depths.
  @Test
  public void chooseMove_oneAddMoveToWin() {
    int[][] moves = { {6,0}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    assertFalse("Board should not have a valid network.", board.hasValidNetwork(BLACK_PLAYER));
    expectedResult = new MoveWithPlayer(3, 3, BLACK_PLAYER);
    actualResultIterative = gameTreeBlack.iterativeDeepeningSearch();
    assertEquals("Move should be correct.", expectedResult, actualResultIterative);
  }

  // test chooseMove() with 2 moves to win
  @Test
  public void chooseMove_twoAddMovesToWin() {
    int[][] blackMoves = {{3, 5}, {5, 5}, {6, 5}, {5, 7}};
    for (int[] move : blackMoves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    assertFalse("Board should not have a valid network.", board.hasValidNetwork(BLACK_PLAYER));
    for (int i = 0; i < 3; i++) {
      GameTree gameTree;
      if (i % 2 == 0) {
        gameTree = gameTreeBlack;
      } else {
        gameTree = gameTreeWhite;
      }
      board.setChip(gameTree.iterativeDeepeningSearch());
    }
    System.out.println(board);
    assertTrue("Board has a valid network.", board.hasValidNetwork(BLACK_PLAYER));
  }

  @Test
  public void chooseMove_playFiveTurns() {
    int turns = 5;
    int depth = 5;
    for (int i = 0; i < turns; i++) {
      board.setChip(gameTreeWhite.iterativeDeepeningSearch());
      board.setChip(gameTreeBlack.iterativeDeepeningSearch());
    }
    System.out.println(board);
  }

  @Test
  public void chooseMove_playTenTurns() {
    int turns = 10;
    for (int i = 0; i < turns; i++) {
      MoveWithPlayer whiteMove = gameTreeWhite.iterativeDeepeningSearch();
      if (whiteMove == null) {
        break;
      }
      board.setChip(whiteMove);
      MoveWithPlayer blackMove = gameTreeBlack.iterativeDeepeningSearch();
      if (blackMove == null) {
        break;
      }
      board.setChip(blackMove);
    }
    assertTrue("A player made a valid network.", board.hasValidNetwork(BLACK_PLAYER) ||
        board.hasValidNetwork(WHITE_PLAYER));
    System.out.println(board);
  }
}

