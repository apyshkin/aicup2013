import javafx.util.Pair;
import model.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 12:34 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommanderStrategy extends TrooperStrategyAdapter {

  public CommanderStrategy(Environment environment, TrooperModel self, Move move) {
    super(environment, self, move);
  }

  @Override
  public void setActionUnderTactics(AttackTactics tactics) {
    CommanderActionsGenerator actionsGenerator = new CommanderActionsGenerator(environment);
    final CellPriorities priorities = tactics.generateCellPriorities();
    TrooperAlgorithmChooser algorithmChooser = new TrooperAlgorithmChooser(environment, trooper, priorities, actionsGenerator);
    Pair<SimulatedAction, IActionParameters> pair = algorithmChooser.findBest();
    SimulatedAction action = pair.getKey();
    IActionParameters parameters = pair.getValue();

    try {
      action.actReal(parameters, trooper, move);
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setActionUnderTactics(PatrolTactics tactics) {
    CommanderActionsGenerator actionsGenerator = new CommanderActionsGenerator(environment);
    final CellPriorities priorities = tactics.generateCellPriorities();
    TrooperAlgorithmChooser algorithmChooser = new TrooperAlgorithmChooser(environment, trooper, priorities, actionsGenerator);
    Pair<SimulatedAction, IActionParameters> pair = algorithmChooser.findBest();
    SimulatedAction action = pair.getKey();
    IActionParameters parameters = pair.getValue();
    assert(action != null && parameters != null);

    try {
      action.actReal(parameters, trooper, move);
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }
  }

}

interface IActionsGenerator {
  public ArrayList<Pair<SimulatedAction, IActionParameters>> getActionsWithParameters(TrooperModel trooper);
}

class CommanderActionsGenerator implements IActionsGenerator {
  private Environment environment;
  private ArrayList<MoveActionParameters> moveActionParameters = null;
  private ArrayList<ShootActionParameters> shootActionParameters = null;
  private ArrayList<Pair<SimulatedAction, IActionParameters>> actionsList = null;

  public CommanderActionsGenerator(Environment environment) {
    this.environment = environment;
  }

  private TrooperModel[] getEnemies() {
    return environment.getAllVisibleTroopers();
  }

  private void init(TrooperModel trooper) {
    actionsList = new ArrayList<>();
    moveActionParameters = new ArrayList<>();
    shootActionParameters = new ArrayList<>();
//    actionsList.add(new Pair<SimulatedAction, IActionParameters>(new RaiseStanceSimulatedAction(trooper, environment), new StanceActionParameters()));
//    actionsList.add(new Pair<SimulatedAction, IActionParameters>(new LowerStanceSimulatedAction(trooper, environment), new StanceActionParameters()));
    for (Direction direction : Direction.values()) {
      if (direction == Direction.CURRENT_POINT)
        continue;

      MoveActionParameters moveActionParams = new MoveActionParameters(trooper, direction);
      actionsList.add(new Pair<SimulatedAction, IActionParameters>(new MoveSimulatedAction(environment), moveActionParams));
      moveActionParameters.add(moveActionParams);
    }
    for (TrooperModel enemy : getEnemies()) {
      ShootActionParameters shootActionParams = new ShootActionParameters(enemy);
      actionsList.add(new Pair<SimulatedAction, IActionParameters>(new ShootSimulatedAction(environment), shootActionParams));
      shootActionParameters.add(shootActionParams);
    }
  }

  @Override
  public ArrayList<Pair<SimulatedAction, IActionParameters>> getActionsWithParameters(TrooperModel trooper) {
    if (actionsList == null)
      init(trooper);

    int i = 0;
    for (Direction direction : Direction.values()) {
      if (direction == Direction.CURRENT_POINT)
        continue;

      MoveActionParameters actionParameters = moveActionParameters.get(i++);
      actionParameters.update(trooper, direction);
    }
    i = 0;
    for (TrooperModel enemy : getEnemies()) {
      ShootActionParameters actionParameters = shootActionParameters.get(i++);
      actionParameters.update(enemy, Direction.CURRENT_POINT);
    }

    return actionsList;
  }

}
