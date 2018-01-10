package player;

public class Best {

  /**
   * EXACT_VALUE used if score is exact.
   * LOWERBOUND used if score is a lower bound.
   * UPPERBOUND used if score is an upper bound.
   * move the move evaluated.
   * score the score.
   * type is one of EXACT_VALUE, LOWERBOUND, or UPPERBOUND.
   * depth is the depth.
   */
  public final static int EXACT_VALUE = 0;
  public final static int LOWER_BOUND = 1;
  public final static int UPPER_BOUND = 2;

  public MoveWithPlayer move;
  public double score;
  public int valueType;
  public int depth;

  /**
   * Constructs an empty Best.
   */
  public Best() { }

  /**
   * Constructs a Best with given move and score.
   *
   * @param move the move associated with this best.
   * @param score the score associated with this Best.
   * @param valueType the type of value stored.
   * @param depth the depth that this move is scored at.
   */
  public Best(MoveWithPlayer move, double score, int valueType, int depth) {
    this.move = move;
    this.score = score;
    this.valueType = valueType;
    this.depth = depth;
  }
}
