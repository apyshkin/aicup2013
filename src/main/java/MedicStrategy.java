import model.Move;
import model.Trooper;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
class MedicStrategy extends TrooperStrategyAdapter {

  protected MedicStrategy(Environment environment, TrooperModel self, Move move) {
    super(environment, self, move);
  }

  @Override
  public void setActionUnderTactics(AttackTactics attackTactics) {
  }

  @Override
  public void setActionUnderTactics(PatrolTactics patrolTactics) {
  }
}
