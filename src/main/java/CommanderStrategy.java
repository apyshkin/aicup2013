import model.Move;
import model.World;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommanderStrategy extends TrooperStrategyAdapter {
  private final static Logger logger = Logger.getLogger(CommanderStrategy.class.getName());

  public CommanderStrategy(Environment environment, TrooperModel trooper, Move move) {
    super(environment, trooper, move);
  }

  public void setAction(CellPriorities priorities) {
    CommanderActionsGenerator actionsGenerator = new CommanderActionsGenerator(environment);
    TrooperAlgorithmChooser algorithmChooser = new TrooperAlgorithmChooser(environment, trooper, priorities, actionsGenerator);

    Pair<Action, IActionParameters> bestActionWithParams = algorithmChooser.findBest();

    Action action = bestActionWithParams.getKey();
    IActionParameters parameters = bestActionWithParams.getValue();
    assert (trooper.getActionPoints() <= 2 || (action != null && parameters != null));
    if (action == null) {
      logger.warning("I prefer to end turn now");
      action = new EndTurnAction(environment);
    }

    try {
      action.act(parameters, trooper, move);
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }

  }

  @Override
  public void setActionUnderTactics(AttackTactics tactics) {
    PriorityCalculator priorityCalculator = new PriorityCalculator(environment, trooper, 3, 3, -1);
    setAction(priorityCalculator.getPriorities());
  }

  @Override
  public void setActionUnderTactics(PatrolTactics tactics) {
    PriorityCalculator priorityCalculator = new PriorityCalculator(environment, trooper, 5, 3, -1);
    setAction(priorityCalculator.getPriorities());
  }
}

class CommanderActionsGenerator extends TrooperActionsGenerator {
  public CommanderActionsGenerator(Environment environment) {
    super(environment);
  }
}
