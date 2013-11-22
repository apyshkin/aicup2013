import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 1:26 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class TrooperStrategyAdapter implements ITrooperStrategy {
  protected Environment environment;
  protected TrooperModel trooper;
  protected final Move move;

  protected TrooperStrategyAdapter(Environment environment, TrooperModel trooper, Move move) {
    this.environment = environment;
    this.trooper = trooper;
    this.move = move;
  }

}
