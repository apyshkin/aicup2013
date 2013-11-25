import model.Move;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommanderStrategy extends TrooperStrategyAdapter {

  public CommanderStrategy(Environment environment, TrooperModel trooper, Move move) {
    super(environment, trooper, move);
  }

  private void setAction(ITactics tactics) {
    CommanderActionsGenerator actionsGenerator = new CommanderActionsGenerator(environment);
    final CellPriorities priorities = tactics.generateCellPriorities(trooper);
    TrooperAlgorithmChooser algorithmChooser = new TrooperAlgorithmChooser(environment, trooper,
            priorities, actionsGenerator);

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

class CommanderActionsGenerator extends TrooperActionsGenerator {
  public CommanderActionsGenerator(Environment environment) {
    super(environment);
  }
}
