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

  @Before
  public void setup() {
    board = new GameBoard();
    gameTreeBlack = new GameTree(board, new MachinePlayer(BLACK_PLAYER));
    gameTreeWhite = new GameTree(board, new MachinePlayer(WHITE_PLAYER));
  }

  @After
  public void tearDown() {
    board = null;
    gameTreeBlack = null;
    gameTreeWhite = null;
  }

  // test chooseMove() when there is network on the board.
  @Test
  public void chooseMove_winAlreadyExists() {
    int[][] moves = { {6,0}, {3,3}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    // 60 - 65 - 55 - 33 - 35 - 57
    assertTrue("Board has a valid network.", board.hasValidNetwork(BLACK_PLAYER));
    GameTree gameTree = new GameTree(board, new MachinePlayer(BLACK_PLAYER));
    MoveWithPlayer move = gameTree.chooseMove(1);
    assertNull("best.move is null when there is already a network", move);
    System.out.println(gameTree.inserts + ", " + gameTree.lookups);
  }

  // test chooseMove() with first move in move list as win.
  @Test
  public void chooseMove_oneMoveToWinFirstMoveInList() {
    int[][] moves = { {1,2}, {4,2}, {2,4}, {6,4}, {3,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    assertFalse("Board should not have a valid network.", board.hasValidNetwork(BLACK_PLAYER));
    GameTree gameTree = new GameTree(board, new MachinePlayer(BLACK_PLAYER));
    MoveWithPlayer move = gameTree.chooseMove(1);
    MoveWithPlayer expected = new MoveWithPlayer(1, 0, BLACK_PLAYER);
    assertTrue("Move should be correct.", move.equals(expected));
    System.out.println(gameTree.inserts + ", " + gameTree.lookups);
  }

  // test chooseMove() with 1 move from win at depths [1,10].
  @Test
  public void chooseMove_oneAddMoveToWinMultipleDepth() {
    int[][] moves = { {6,0}, {3,5}, {5,5}, {6,5}, {5,7} };
    for (int[] move : moves) {
      board.setChip(new MoveWithPlayer(move[0], move[1], BLACK_PLAYER));
    }
    board.setChip(new MoveWithPlayer(4, 5, WHITE_PLAYER));
    assertFalse("Board should not have a valid network.", board.hasValidNetwork(BLACK_PLAYER));

    MoveWithPlayer actualResult;
    MoveWithPlayer expectedResult;

    // test depths from 1 to 20
    for (int depth = 1; depth < 10; depth++) {

      actualResult = gameTreeBlack.chooseMove(depth);
      expectedResult = new MoveWithPlayer(3, 3, BLACK_PLAYER);
      assertEquals("Move should be correct.", expectedResult, actualResult);
    }
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
    int depth = 1;
    GameTree gameTreeBlack = new GameTree(board, new MachinePlayer(BLACK_PLAYER));
    GameTree gameTreeWhite = new GameTree(board, new MachinePlayer(WHITE_PLAYER));
    System.out.println(gameTreeBlack.chooseMove(depth));
    System.out.println(gameTreeWhite.chooseMove(depth));
    for (int i = 0; i < 5; i++) {
      GameTree gameTree;
      if (i % 2 == 0) {
        gameTree = gameTreeBlack;
      } else {
        gameTree = gameTreeWhite;
      }
      board.setChip(gameTree.chooseMove(depth));
      System.out.println(board);
      System.out.println(i);
    }
    assertTrue("Board should have a valid network.", board.hasValidNetwork(BLACK_PLAYER));
  }
}

