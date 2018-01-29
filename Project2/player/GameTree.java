package player;

import DataStructures.List.List;
import DataStructures.dict.Entry;
import DataStructures.dict.HashTableChained;

import java.util.Arrays;
import java.util.Collections;

/**
 * GameTree performs a search given a board, player color and search depths for add and step moves.
 *
 * Call search() to get the best move up to max depth.  It utilizes:
 *  - minimax search with alpha-beta pruning,
 *  - iterative deepening: allows us to find the best best move at a given depth
 *  - transposition table: speeds up iterative deepening by letting us look up prior depth's evaluated board states,
 *      and speeds up search by storing/retrieving transpositions
 *  - move reordering: by static evaluating all single depth moves we can enhance cutoff in alpha-beta pruning
 *
 * Credit to Jeroen W.T. Carolus' 'Alpha-Beta with Sibling Prediction Pruning in Chess'
 * (https://homepages.cwi.nl/~paulk/theses/Carolus.pdf (pg. 14) for explanation and pseudo code for transpostion tables.
 */
public class GameTree {
  /**
   * BLACK_PLAYER is the internal representation of a black player.
   * WHITE_PLAYER is the internal representation of a white player.
   * ALPHA is the initial alpha value passed in to alpha-beta search.
   * BETA is the initial beta value passed in to alpha-beta search.
   * MAX_SCORE is the maximum possible score.
   * ESTIMATED_TABLE_ENTRIES is the estimated number of entries in the transposition table.
   * computerPlayer is the current player, either BLACK_PLAYER or WHITE_PLAYER.
   * opponentPlayer is the opponent player, either BLACK_PLAYER or WHITE_PLAYER.
   * searchDepthAdd is the maximum search depth for add moves.
   * searchDepthStep is the maximum search depth for step moves.
   * board is the current game board state.
   * table is a transposition table, keys: zobrist hash of board, values: Best (move, score and cutoff values).
   * winner holds the winner if there is one.
   * isWin is true if there's a winner, false otherwise.
   */
  private final static int BLACK_PLAYER = MachinePlayer.BLACK_PLAYER;
  private final static int WHITE_PLAYER = MachinePlayer.WHITE_PLAYER;
  private final static int ALPHA = Integer.MIN_VALUE;
  private final static int BETA = Integer.MAX_VALUE;
  private final static double MAX_SCORE = 1000.0;
  private final static int ESTIMATED_TABLE_ENTRIES = 140000; // for depth 5

  private final int computerPlayer;
  private final int opponentPlayer;
  private final int searchDepthAdd;
  private final int searchDepthStep;
  private GameBoard board;
  private HashTableChained table;
  private int winner;
  private boolean isWin;

  public GameTree(GameBoard board, int playerColor, int searchDepthAdd, int searchDepthStep) {
    computerPlayer = playerColor;
    if (computerPlayer == BLACK_PLAYER) {
      opponentPlayer = WHITE_PLAYER;
    } else {
      opponentPlayer = BLACK_PLAYER;
    }
    this.board = board;
    this.searchDepthAdd = searchDepthAdd;
    this.searchDepthStep = searchDepthStep;
    // create a table of bucket size ~4/3 * ESTIMATED_TABLE_ENTRIES
    table = new HashTableChained((int) (ESTIMATED_TABLE_ENTRIES/2 * 4/3.0));
  }

  /**
   * search() returns the best move for the player.
   *
   * @return the best move encountered up to the search depth
   */
  protected MoveWithPlayer search() {
    double bestScore = -1.0;
    Best best = null;

    int blackChipCount = board.getBlackChips().length();
    int searchDepth = searchDepthAdd;
    if (blackChipCount == 10) {
      searchDepth = searchDepthStep;
    }
    for (int depth = 1; depth <= searchDepth; depth++) {
      Best bestAtCurrentDepth = alphaBeta(computerPlayer, depth, ALPHA, BETA);
      if (bestAtCurrentDepth.score > bestScore) {
        best = bestAtCurrentDepth;
        bestScore = bestAtCurrentDepth.score;
      }

      // stop searching if we find ANY winning move at depth 0
      if (bestScore == MAX_SCORE) {
        break;
      }
    }
    table.makeEmpty(); // clean up transposition table

    // temp for testing
    if (best == null) {
      return null;
    }

    return best.move;
  }

  // minimax with alpha-beta pruning and transposition table
  private Best alphaBeta(int side, int depth, double alpha, double beta) {
    Best myBest = new Best(); // my best move
    Best reply; // opponent's best reply

    Best entryTT = getTTEntry(); // entry from the transposition table
    if (entryTT != null && entryTT.depth >= depth) {
      if (entryTT.valueType == Best.EXACT_VALUE) {
        return entryTT;
      }
      if (entryTT.valueType == Best.LOWER_BOUND && entryTT.score > alpha) {
        alpha = entryTT.score;
      } else if (entryTT.valueType == Best.UPPER_BOUND && entryTT.score < beta) {
        beta = entryTT.score;
      }
      if (alpha >= beta) {
        return entryTT;
      }
    }
    // if depth reached or player has a Network, store in transposition table
    if (isGameOver() || depth == 0) {
      myBest.score = evaluateBoard(depth);
      if (myBest.score <= alpha) {
        storeTTEntry(myBest.move, myBest.score, Best.LOWER_BOUND, depth);
      } else if (myBest.score >= beta) {
        storeTTEntry(myBest.move, myBest.score, Best.UPPER_BOUND, depth);
      } else {
        storeTTEntry(myBest.move, myBest.score, Best.EXACT_VALUE, depth);
      }
      return myBest;
    }

    // set score to most "pessimistic"
    if (side == computerPlayer) {
      myBest.score = alpha;
    } else {
      myBest.score = beta;
    }
    EvaluatedMove[] evaluatedMoves = getOrderedMoves(side); // order moves based based on evaluation
    myBest.move = getFirstMove(evaluatedMoves); // choose any legal move
    int opponentSide = getOpponent(side); // set opponent side
    for (EvaluatedMove evaluatedMove : evaluatedMoves) {
      MoveWithPlayer move = evaluatedMove.move;

      // randomize first move
      if (board.getBlackChips().length() == 0 && computerPlayer == BLACK_PLAYER ||
          board.getWhiteChips().length() == 0 && computerPlayer == WHITE_PLAYER) {
        move = getRandomMove(evaluatedMoves);
      }

      board.setChip(move); // perform move
      reply = alphaBeta(opponentSide, depth-1, alpha, beta);
      board.undoSetChip(); // undo move
      if (side == computerPlayer && reply.score > myBest.score) {
        myBest.move = move;
        myBest.score = reply.score;
        alpha = reply.score;
      } else if (side == opponentPlayer && reply.score < myBest.score) {
        myBest.move = move;
        myBest.score = reply.score;
        beta = reply.score;
      }
      if (alpha >= beta) {
        return myBest;
      }
    }
    if (myBest.score <= alpha) {
      storeTTEntry(myBest.move, myBest.score, Best.LOWER_BOUND, depth);
    } else if (myBest.score >= beta) {
      storeTTEntry(myBest.move, myBest.score, Best.UPPER_BOUND, depth);
    } else {
      storeTTEntry(myBest.move, myBest.score, Best.EXACT_VALUE, depth);
    }
    return myBest;
  }

  private EvaluatedMove[] getOrderedMoves(int side) {
    List<MoveWithPlayer> moves = board.getValidMoves(side);
    EvaluatedMove[] evaluatedMoves = new EvaluatedMove[moves.length()];
    // evaluate each move and store in EvaluatedMove array
    int i = 0;
    for (MoveWithPlayer move: moves) {
      board.setChip(move);
      double evaluation = evaluateMoveOrdering(move.player);
      board.undoSetChip();
      evaluatedMoves[i++] = new EvaluatedMove(move, evaluation);
    }
    // sort the moves based upon player (computer: descending, opponent: ascending)
    if (side == computerPlayer) {
      Arrays.sort(evaluatedMoves);
    } else {
      Arrays.sort(evaluatedMoves, Collections.reverseOrder());
    }
    return evaluatedMoves;
  }

  // returns the first move in move list
  private MoveWithPlayer getFirstMove(EvaluatedMove[] moves) {
    if (moves.length != 0) {
      return moves[0].move;
    }
    return null;
  }

  // returns entry from transposition table if found, otherwise return null
  private Best getTTEntry() {
    Entry entry = table.find(board.getZobristKey());
    if (entry == null) {
      return null;
    }
    return (Best) entry.value();
  }

  // stores an entry in the transposition table
  private void storeTTEntry(MoveWithPlayer move, double score, int bound, int depth) {
    long zobristKey = board.getZobristKey();
    if (table.find(zobristKey) == null) {
      table.insert(zobristKey, new Best(move, score, bound, depth));
    }
  }

  // returns a randomized move out of all possible moves (used for initial move by computer)
  private MoveWithPlayer getRandomMove(EvaluatedMove[] moves) {
    MoveWithPlayer move = null;
    int targetPosition = (int) (Math.random() * moves.length);
    int currentPosition = 0;
    for (EvaluatedMove m : moves) {
      if (currentPosition == targetPosition) {
        move = m.move;
        break;
      }
      currentPosition++;
    }
    return move;
  }

  // returns true if there is a valid network for a player and false otherwise
  private boolean isGameOver() {
    // valid network detected for a player
    if (board.hasValidNetwork(computerPlayer)) {
      winner = computerPlayer;
      isWin = true;
      return true;
    } else if (board.hasValidNetwork(opponentPlayer)) {
      winner = opponentPlayer;
      isWin = true;
      return true;
    }
    winner = Integer.MIN_VALUE;
    isWin = false;
    return false;
  }

  // returns the opponent side
  private int getOpponent(int side) {
    if (side == computerPlayer) {
      return opponentPlayer;
    } else {
      return computerPlayer;
    }
  }

  // for move ordering evaluation
  private double evaluateMoveOrdering(int depth) {
    // valid network detected for a player
    if (board.hasValidNetwork(computerPlayer)) {
      return MAX_SCORE - depth;
    } else if (board.hasValidNetwork(opponentPlayer)) {
      return -MAX_SCORE + depth;
    }
    int sign;
    if (computerPlayer == BLACK_PLAYER) {
      sign = 1;
    } else {
      sign = -1;
    }
    double mobilityWeight = 0.1;
    double goalWeight = 0.25;
    return sign * (evaluateMobility(mobilityWeight) + evaluateGoals(goalWeight));
  }

  // board evaluation function to score indeterminate boards
  private double evaluateBoard(int depth) {
    if (isWin) {
      if (winner == computerPlayer) {
        return MAX_SCORE - depth;
      } else if (winner == opponentPlayer) {
        return -MAX_SCORE + depth;
      }
    }
    int sign;
    if (computerPlayer == BLACK_PLAYER) {
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
