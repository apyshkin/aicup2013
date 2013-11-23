import model.Direction;
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

  public CommanderStrategy(Environment environment, TrooperModel self, Move move) {
    super(environment, self, move);
  }

  @Override
  public void setActionUnderTactics(AttackTactics tactics) {
    CommanderActionsGenerator actionsGenerator = new CommanderActionsGenerator(environment);
    final CellPriorities priorities = tactics.generateCellPriorities();
    TrooperAlgorithmChooser algorithmChooser = new TrooperAlgorithmChooser(environment, trooper, priorities, actionsGenerator);
    Pair<SimAction, IActionParameters> pair = algorithmChooser.findBest();
    SimAction action = pair.getKey();
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
    Pair<SimAction, IActionParameters> pair = algorithmChooser.findBest();
    SimAction action = pair.getKey();
    IActionParameters parameters = pair.getValue();
    assert(trooper.getActionPoints() < 2 || (action != null && parameters != null));
    if (action == null)
      return;

    try {
      action.actReal(parameters, trooper, move);
    } catch (InvalidActionException e) {
      e.printStackTrace();
    }
  }

}

interface IActionsGenerator {
  public ArrayList<Pair<SimAction, IActionParameters>> getActionsWithParameters(TrooperModel trooper);
}

class CommanderActionsGenerator implements IActionsGenerator {
  private Environment environment;
  private ArrayList<MoveActionParameters> moveActionParameters = null;
  private ArrayList<ShootActionParameters> shootActionParameters = null;
  private ArrayList<Pair<SimAction, IActionParameters>> actionsList = null;

  public CommanderActionsGenerator(Environment environment) {
    this.environment = environment;
  }

  private void init(TrooperModel trooper) {
    actionsList = new ArrayList<>();
    moveActionParameters = new ArrayList<>();
    shootActionParameters = new ArrayList<>();
    actionsList.add(new Pair<SimAction, IActionParameters>(new RaiseStanceSimAction(environment), new StanceActionParameters()));
    actionsList.add(new Pair<SimAction, IActionParameters>(new LowerStanceSimAction(environment), new StanceActionParameters()));
    for (Direction direction : Direction.values()) {
      if (direction == Direction.CURRENT_POINT)
        continue;

      MoveActionParameters moveActionParams = new MoveActionParameters(trooper, direction);
      actionsList.add(new Pair<SimAction, IActionParameters>(new MoveSimAction(environment), moveActionParams));
      moveActionParameters.add(moveActionParams);
    }
    for (TrooperModel enemy : environment.getAllVisibleTroopers())
      if (!enemy.isTeammate()) {
        ShootActionParameters shootActionParams = new ShootActionParameters(enemy);
        actionsList.add(new Pair<SimAction, IActionParameters>(new ShootSimAction(environment), shootActionParams));
        shootActionParameters.add(shootActionParams);
      }
  }

  @Override
  public ArrayList<Pair<SimAction, IActionParameters>> getActionsWithParameters(TrooperModel trooper) {
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
    for (TrooperModel enemy : environment.getAllVisibleTroopers())
      if (!enemy.isTeammate()) {
        ShootActionParameters actionParameters = shootActionParameters.get(i++);
        actionParameters.update(enemy.getX(), enemy.getY());
      }

    return actionsList;
  }

}

class Pair<K, V> {
  K k;
  V v;

  public Pair(K k, V v) {
    this.k = k;
    this.v = v;
  }

  public K getKey() {
    return k;
  }

  public V getValue() {
    return v;
  }
}
