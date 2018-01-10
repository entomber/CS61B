package player;

/**
 *  A public class for holding a move and the static evaluation when move is made.
 *  This class is a container for data, not an ADT; hence, all fields are public.
 *  It implements Comparable so that moves can be reordered based upon the evaluation.
 */
public class EvaluatedMove implements Comparable<EvaluatedMove> {
  public MoveWithPlayer move;
  public double evaluation;

  public EvaluatedMove(MoveWithPlayer move, double evaluation) {
    this.move = move;
    this.evaluation = evaluation;
  }

  @Override
  public int compareTo(EvaluatedMove other) {
    double result = other.evaluation - evaluation;
    if (result > 0) {
      return 1;
    } else if (result < 0) {
      return -1;
    } else {
      return 0;
    }
  }

  @Override
  public String toString() {
    return move.toString() + " eval: " + String.format("%.3f",evaluation);
  }
}
