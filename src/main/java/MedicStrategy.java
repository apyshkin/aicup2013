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

  public MedicStrategy(Environment environment, TrooperModel self, Move move) {
    super(environment, self, move);
  }

  private void setAction(ITactics tactics) {
    HealerActionsGenerator actionsGenerator = new HealerActionsGenerator(environment);
    final CellPriorities priorities = tactics.generateCellPriorities(trooper);
    TrooperAlgorithmChooser algorithmChooser = new TrooperAlgorithmChooser(environment, trooper, priorities, actionsGenerator);
    Pair<Action, IActionParameters> pair = algorithmChooser.findBest();
    Action action = pair.getKey();
    IActionParameters parameters = pair.getValue();
    assert(trooper.getActionPoints() < 2 || (action != null && parameters != null));
    if (action == null)
      return;

    try {
      action.act(parameters, trooper, move);
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setActionUnderTactics(AttackTactics tactics) {
    setAction(tactics);
  }

  @Override
  public void setActionUnderTactics(PatrolTactics tactics) {
    setAction(tactics);
  }
}

class HealerActionsGenerator extends TrooperActionsGenerator {
  public HealerActionsGenerator(Environment environment) {
    super(environment);
  }

  protected void init(TrooperModel healer) {
    super.init(healer);

    for (TrooperModel patient : environment.getAllVisibleTroopers())
      if (patient.isTeammate()) {
        HealActionParameters healActionParams = new HealActionParameters(patient);
        actionsList.add(new Pair<Action, IActionParameters>(new HealAction(environment), healActionParams));
      }
  }

}
