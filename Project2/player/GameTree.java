package player;

import DataStructures.List.List;
import DataStructures.List.InvalidNodeException;
import DataStructures.dict.HashTableChained;

/**
 * GameTree is a
 *
 * chooseMove() finds the best move for a player.  It uses a minimax algorithm with alpha-beta pruning and evaluation
 * function to aggressively prune branches.  The move and score for a board configuration are stored in a
 * transposition table for quick lookup so same board configurations do not need to be evaluated twice. It is only
 * useful if searching at least 3 levels deep where repeat moves board configurations for a player may occur.
 */
public class GameTree {
  /**
   * COMPUTER is either BLACK_PLAYER or WHITE_PLAYER.
   * OPPONENT is either BLACK_PLAYER or WHITE_PLAYER.
   * WHITE_PLAYER is the representation of a white player.
   * BLACK_PLAYER is the representation of a black player.
   * GameBoard is the game board.
   * depth is the search depth.
   * table is a transposition table, key: board configuration, value: Best.
   * winMoves maps the depth of a move to the associated move.
   */
  private static final int NONE = -1;
  private final int COMPUTER;
  private final int OPPONENT;
  private final static int BLACK_PLAYER = MachinePlayer.BLACK_PLAYER;
  private final static int WHITE_PLAYER = MachinePlayer.WHITE_PLAYER;
  private GameBoard board;
  private int maxDepth;
  private HashTableChained table;
  private double score;
  private int winner;

  int lookups = 0;
  int inserts = 0;

  public GameTree(GameBoard board, MachinePlayer player) {
    COMPUTER = player.getColor();
    if (COMPUTER == BLACK_PLAYER) {
      OPPONENT = WHITE_PLAYER;
    } else {
      OPPONENT = BLACK_PLAYER;
    }
    this.board = board;
    table = new HashTableChained(200);
  }

  protected MoveWithPlayer chooseMoveFixedDepth(int depth) {
    maxDepth = depth;
    Best best = chooseMove(COMPUTER, 0, depth, -1, 1);
    System.out.println(best.score);
    return best.move;
  }

  /**
   * chooseMove() searches the game tree for the best move utilizing  minimax with iterative deepening given a maximum depth and time constraint.
   *
   * @param maxDepth the maximum depth to perform iterative deepening depth-first search.
   * @return
   */
  protected MoveWithPlayer chooseMove(int maxDepth) {
    this.maxDepth = maxDepth;
    double bestScore = -1.0;
    Best best = null;
    // Execute minimax at each depth, if score at next depth is greater than score at current depth, continue
    // deeper.  If score is not greater at current depth, return prior depth's best move.
    for (int searchDepth = 1; searchDepth <= maxDepth; searchDepth++) {
      Best bestMoveAtCurrentDepth = chooseMove(COMPUTER, 0, searchDepth,  -1, 1);
      if (bestMoveAtCurrentDepth.score > bestScore) {
        best = bestMoveAtCurrentDepth;
        bestScore = bestMoveAtCurrentDepth.score;
        System.out.println("best at depth: " + searchDepth + ", move: " + bestMoveAtCurrentDepth.move +
            ", score: " + bestMoveAtCurrentDepth.score + ", bestScore: " + bestScore);
      }
//      else {
//        break;
//      }
    }
    table.makeEmpty(); // clean up transposition table
//    System.out.println("inserts: " + inserts + ", lookups: " + lookups);
    System.out.println("overall best move: " + best.move + ", score: " + best.score);
    // return either the prior depth's best move (if next depth didn't evaluate higher) or maxDepth best move
    return best.move;
  }

  private Best chooseMove(int side, int currentDepth, int searchDepth, double alpha, double beta) {
    Best myBest = new Best(); // my best move
    Best reply; // opponent's best reply

    // if board is in transposition table, return the stored Best
    if (maxDepth >= 3 && table.find(board) != null) {
      lookups++;
      return (Best) table.find(board).value();
    }

    // valid network detected for a player
    if (board.hasValidNetwork(COMPUTER)) {
      myBest.score = 100.0 - currentDepth;
      return myBest;
    } else if (board.hasValidNetwork(OPPONENT)) {
      myBest.score = -100.0 + currentDepth;
      return myBest;
    }
    // check if either player won or search depth reached
    if (checkWin(currentDepth, searchDepth)) {
      myBest.score = score;
      return myBest;
    }
    // search depth reached, return evaluation function score
    if (currentDepth >= searchDepth) {
      myBest.score = evaluate(currentDepth);
//      System.out.println(evaluate());
      return myBest;
    }
    // set score to most "pessimistic"
    if (side == COMPUTER) {
      myBest.score = alpha;
    } else {
      myBest.score = beta;
    }
    List<MoveWithPlayer> moves = board.getValidMoves(side);
    myBest.move = getFirstMove(moves); // choose any legal move
    int opponentSide = getOpponent(side); // set opponent side
    for (MoveWithPlayer move : moves) {
      board.setChip(move); // perform move
      reply = chooseMove(opponentSide, currentDepth + 1, searchDepth, alpha, beta);
      board.undoSetChip(side); // undo move
      if (side == COMPUTER && reply.score > myBest.score) {
        myBest.move = move;
        myBest.score = reply.score;
        alpha = reply.score;
      } else if (side == OPPONENT && reply.score < myBest.score) {
        myBest.move = move;
        myBest.score = reply.score;
        beta = reply.score;
      }
      // store in hash table
      if (maxDepth >= 3 && table.find(board) == null) {
        inserts++;
        table.insert(board, reply);
      }
      if (alpha >= beta) {
        return myBest;
      }
    }
    return myBest;
  }

  // returns true if there is a winner and false otherwise
  private boolean checkWin(int currentDepth, int searchDepth) {
    // valid network detected for a player
    if (board.hasValidNetwork(COMPUTER)) {
      score = 100.0 - currentDepth;
      winner = COMPUTER;
      return true;
    } else if (board.hasValidNetwork(OPPONENT)) {
      score = -100 + currentDepth;
      winner = OPPONENT;
      return true;
    }
    // search depth reached, return evaluation function score
    if (currentDepth >= searchDepth) {
      score = evaluate(currentDepth);
      winner = NONE;
      return true;
    }
    return false;
  }

  private MoveWithPlayer getFirstMove(List<MoveWithPlayer> moves) {
    MoveWithPlayer move = null;
    try {
      move = moves.front().item(); // what happens when there's no move?
    } catch (InvalidNodeException e) {
      e.printStackTrace();
    }
    return move;
  }

  // returns the opponent side
  private int getOpponent(int side) {
    if (side == COMPUTER) {
      return OPPONENT;
    } else {
      return COMPUTER;
    }
  }

  private double evaluate(int depth) {
    return evaluate1(depth);
  }

  // board evaluation function to score indeterminate boards
  private double evaluate1(int depth) {
    double result = 0.0;
    int sign = 1;
    if (COMPUTER == WHITE_PLAYER) {
      sign = -1;
    }
    // compute pairs of chips that can see each other - opponent's pairs
    List<Integer[]> whiteChips = board.getWhiteChipPositions();
    List<Integer[]> blackChips = board.getBlackChipPositions();
    double weight = 0.025;
    for (Integer[] chip : blackChips) {
      int numConnections = board.getConnections(BLACK_PLAYER, chip[0], chip[1]).length();
      result += sign * weight * numConnections;
    }
    for (Integer[] chip : whiteChips) {
      int numConnections = board.getConnections(WHITE_PLAYER, chip[0], chip[1]).length();
      result -= sign * weight * numConnections;
    }
    // establish at least one chip in each goal early in game
    int numChipsTopGoal = board.getTopGoalChipPositions().length();
    int numChipsBottomGoal = board.getBottomGoalChipPositions().length();
    int numChipsLeftGoal = board.getLeftGoalChipPositions().length();
    int numChipsRightGoal = board.getRightGoalChipPositions().length();
    weight = 0.05;
    if (numChipsTopGoal >= 1 && numChipsTopGoal <= 2) {
      result += sign * weight;
    }
    if (numChipsBottomGoal >= 1 && numChipsBottomGoal <= 2) {
      result += sign * weight;
    }
    if (numChipsLeftGoal >= 1 && numChipsLeftGoal <= 2) {
      result -= sign * weight;
    }
    if (numChipsRightGoal >= 1 && numChipsRightGoal <= 2) {
      result -= sign * weight;
    }

    return result;
  }
}
