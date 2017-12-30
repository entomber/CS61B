package player;

import DataStructures.List.List;
import DataStructures.List.InvalidNodeException;
import DataStructures.dict.HashTableChained;

import java.util.Iterator;

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
   * BLACK_PLAYER is the internal representation of a black player.
   * WHITE_PLAYER is the internal representation of a white player.
   * ALPHA is the initial alpha value passed in to alpha-beta search.
   * BETA is the initial beta value passed in to alpha-beta search.
   * MAX_SCORE is the maximum possible score.
   * COMPUTER is the current player, either BLACK_PLAYER or WHITE_PLAYER.
   * OPPONENT is the opponent player, either BLACK_PLAYER or WHITE_PLAYER.
   * board is the current game board state.
   * table is a transposition table, keys: board configuration, values: Best (move and score).
   * score contains the score when the game is over (there's a network or search depth reached).
   */
  private final static int BLACK_PLAYER = MachinePlayer.BLACK_PLAYER;
  private final static int WHITE_PLAYER = MachinePlayer.WHITE_PLAYER;
  private final static int ALPHA = Integer.MIN_VALUE;
  private final static int BETA = Integer.MAX_VALUE;
  private final static double MAX_SCORE = 1000.0;

  private final int COMPUTER;
  private final int OPPONENT;
  private GameBoard board;
  private HashTableChained table;
  private double score;

  public GameTree(GameBoard board, MachinePlayer player) {
    COMPUTER = player.getColor();
    if (COMPUTER == BLACK_PLAYER) {
      OPPONENT = WHITE_PLAYER;
    } else {
      OPPONENT = BLACK_PLAYER;
    }
    this.board = board;
    table = new HashTableChained(500000);
  }

  protected MoveWithPlayer fixedDepthSearch(int depth) {
    Best best = chooseMove(COMPUTER, 0, depth, ALPHA, BETA);
    table.makeEmpty(); // clean up transposition table
    return best.move;
  }

  /**
   * chooseMove() searches the game tree for the best move utilizing minimax with iterative deepening given a maximum depth and time constraint.
   *
   * @param maxDepth the maximum depth to perform iterative deepening depth-first search.
   * @return
   */
  protected MoveWithPlayer iterativeDeepeningSearch(int maxDepth) {
    double bestScore = -1.0;
    Best best = null;
    // Execute minimax at each depth, if score at next depth is greater than score at current depth, continue
    // deeper.  If score is not greater at current depth, return prior depth's best move.
    for (int searchDepth = 1; searchDepth <= maxDepth; searchDepth++) {
      Best bestAtCurrentDepth = chooseMove(COMPUTER, 0, searchDepth, ALPHA, BETA);
      if (bestAtCurrentDepth.score > bestScore) {
        best = bestAtCurrentDepth;
        bestScore = bestAtCurrentDepth.score;
      }
      // TODO: break if score at multiple depths down isn't better?
/*      else {
        break;
      }*/
    }
    table.makeEmpty(); // clean up transposition table
    // return either the prior depth's best move (if next depth didn't evaluate higher) or maxDepth best move

    // temp for testing
    if (best == null) {
      return null;
    }

    return best.move;
  }

  // minimax with alpha-beta pruning and transposition table
  private Best chooseMove(int side, int currentDepth, int searchDepth, double alpha, double beta) {
    Best myBest = new Best(); // my best move
    Best reply; // opponent's best reply
    // if board is in transposition table, return the stored Best
    if (currentDepth >= 2 && table.find(board) != null) {
      return (Best) table.find(board).value();
    }
    // check if either player won or search depth reached
    if (isGameOver(currentDepth, searchDepth)) {
      myBest.score = score;
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

      // randomize first move
      if (board.getBlackChips().length() == 0 && COMPUTER == BLACK_PLAYER ||
          board.getWhiteChips().length() == 0 && COMPUTER == WHITE_PLAYER) {
        move = getRandomMove(moves);
      }

      board.setChip(move); // perform move
      reply = chooseMove(opponentSide, currentDepth + 1, searchDepth, alpha, beta);
      // store in hash table
      if (currentDepth >= 2 && table.find(board) == null) {
        table.insert(board, new Best(move, reply.score));
      }
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
      if (alpha >= beta) {
        return myBest;
      }
    }
    return myBest;
  }

  // returns a randomized move out of all possible moves (used for initial move by computer)
  private MoveWithPlayer getRandomMove(List<MoveWithPlayer> moves) {
    MoveWithPlayer move = null;
    int targetPosition = (int) (Math.random() * moves.length());
    int currentPosition = 0;
    for (MoveWithPlayer m : moves) {
      if (currentPosition == targetPosition) {
        move = m;
        break;
      }
      currentPosition++;
    }
    return move;
  }

  // returns true if there is a winner and false otherwise
  private boolean isGameOver(int currentDepth, int searchDepth) {
    // valid network detected for a player, update score
    if (board.hasValidNetwork(COMPUTER)) {
      score = MAX_SCORE - currentDepth;
      return true;
    } else if (board.hasValidNetwork(OPPONENT)) {
      score = -MAX_SCORE + currentDepth;
      return true;
    }
    // search depth reached, update evaluation function score
    if (currentDepth >= searchDepth) {
      score = evaluateBoard();
      return true;
    }
    return false;
  }

  // returns the first move in move list
  private MoveWithPlayer getFirstMove(List<MoveWithPlayer> moves) {
    MoveWithPlayer move = null;
    try {
      move = moves.front().item();
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

  private double evaluateBoard() {
    return evaluate_basic();
  }

  // board evaluation function to score indeterminate boards
  private double evaluate_basic() {
    int sign;
    if (COMPUTER == BLACK_PLAYER) {
      sign = 1;
    } else {
      sign = -1;
    }
    double mobilityWeight = 0.1;
    double goalWeight = 0.25;
    return sign * (evaluateMobility(mobilityWeight) + evaluateGoals(goalWeight));
  }

  // return a weighted score of computer's moves minus opponent's moves
  private double evaluateMobility(double weight) {
    // compute pairs of chips that can see each other - opponent's pairs
    List<Integer[]> blackChips = board.getBlackChips();
    List<Integer[]> whiteChips = board.getWhiteChips();
    int blackChipConnections = 0;
    for (Integer[] chip : blackChips) {
      int numConnections = board.getConnections(BLACK_PLAYER, chip[0], chip[1]).length();
      blackChipConnections += numConnections;
    }
    int whiteChipConnections = 0;
    for (Integer[] chip : whiteChips) {
      int numConnections = board.getConnections(WHITE_PLAYER, chip[0], chip[1]).length();
      whiteChipConnections += numConnections;
    }
    return weight * (blackChipConnections - whiteChipConnections);
  }

  private double evaluateGoals(double weight) {
    // establish at least one chip in each goal early in game
    int numChipsTopGoal = board.getTopGoalChips().length();
    int numChipsBottomGoal = board.getBottomGoalChips().length();
    int numChipsLeftGoal = board.getLeftGoalChips().length();
    int numChipsRightGoal = board.getRightGoalChips().length();
    int blackChipsGoal = 0;
    int whiteChipsGoal = 0;
    int threshold = 4;
    int blackChipCount = board.getBlackChips().length();
    int whiteChipCount = board.getWhiteChips().length();
    if (numChipsTopGoal > 0 && blackChipCount < threshold) {
      blackChipsGoal += 1;
    }
    if (numChipsBottomGoal > 0 && blackChipCount < threshold) {
      blackChipsGoal += 1;
    }
    if (numChipsLeftGoal > 0 && whiteChipCount < threshold) {
      whiteChipsGoal += 1;
    }
    if (numChipsRightGoal > 0 && whiteChipCount < threshold) {
      whiteChipsGoal += 1;
    }
    return weight * (blackChipsGoal - whiteChipsGoal);

  }
}
