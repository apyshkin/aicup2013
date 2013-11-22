import model.Trooper;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
class MedicStrategy extends TrooperStrategyAdapter {

  public MedicStrategy(Trooper self) {
    super(self);
  }

  @Override
  public Action setActionUnderTactics(AttackTactics attackTactics) {
    return null;
  }

  @Override
  public Action setActionUnderTactics(PatrolTactics patrolTactics) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }
}
