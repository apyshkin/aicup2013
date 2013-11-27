import model.Move;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:25 AM
 * To change this template use File | Settings | File Templates.
 */
class MedicStrategy extends TrooperStrategyAdapter {
  private final static Logger logger = Logger.getLogger(MedicStrategy.class.getName());

  public MedicStrategy(Environment environment, TrooperModel self, Move move) {
    super(environment, self, move);
  }

  private void setAction(CellPriorities priorities) {
    MedicActionsGenerator actionsGenerator = new MedicActionsGenerator(environment);
    TrooperAlgorithmChooser algorithmChooser = new TrooperAlgorithmChooser(environment, trooper, priorities, actionsGenerator);
    Pair<Action, IActionParameters> bestActionWithParams = algorithmChooser.findBest();
    Action action = bestActionWithParams.getKey();
    IActionParameters parameters = bestActionWithParams.getValue();
    assert (trooper.getActionPoints() <= 2 || (action != null && parameters != null));
    if (action == null) {
      action = new EndTurnAction(environment);
      logger.warning("I prefer to end turn now");
    }

    try {
      action.act(parameters, trooper, move);
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }
  }
  @Override
  public void setActionUnderTactics(AttackTactics tactics) {
    PriorityCalculator priorityCalculator = new PriorityCalculator(environment, trooper, 3, 4, -2);
    setAction(priorityCalculator.getPriorities());
  }

  @Override
  public void setActionUnderTactics(PatrolTactics tactics) {
    PriorityCalculator priorityCalculator = new PriorityCalculator(environment, trooper, 4, 4, -2);
    setAction(priorityCalculator.getPriorities());
  }
}

class MedicActionsGenerator extends TrooperActionsGenerator {
  public MedicActionsGenerator(Environment environment) {
    super(environment);
  }

  protected void init(TrooperModel healer) {
    super.init(healer);

    for (TrooperModel patient : environment.getMyTroopers()) {
      HealActionParameters healActionParams = new HealActionParameters(patient);
      actionsList.add(new Pair<Action, IActionParameters>(new HealAction(environment), healActionParams));
    }
  }

}
