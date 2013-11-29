import model.Direction;
import model.World;

import java.util.ArrayList;

public class TrooperActionsGenerator implements IActionsGenerator {
  protected Environment environment;
  private ArrayList<MoveActionParameters> moveActionParameters = null;
  private ArrayList<ShootActionParameters> shootActionParameters = null;
  protected ArrayList<Pair<Action, IActionParameters>> actionsList = null;

  public TrooperActionsGenerator(Environment environment) {
    this.environment = environment;
  }

  protected void init(TrooperModel trooper) {
    actionsList = new ArrayList<>();
    moveActionParameters = new ArrayList<>();
    shootActionParameters = new ArrayList<>();
    addMoveActions(trooper);
    addStanceChangeActions();
    addShootActions();
    addMedkitActions();
    addFieldRationActions(trooper);
    addThrowGrenadeActions();
  }

  private void addFieldRationActions(TrooperModel me) {
    EatFieldRationActionParameters actionParameters = new EatFieldRationActionParameters(me);
    actionsList.add(new Pair<Action, IActionParameters>(new EatFieldRationAction(environment), actionParameters));
  }

  private void addMedkitActions() {
    for (TrooperModel patient : environment.getMyTroopers()) {
      UseMedkitActionParameters useMedkitActionParams = new UseMedkitActionParameters(patient);
      actionsList.add(new Pair<Action, IActionParameters>(new UseMedkitAction(environment), useMedkitActionParams));
    }
  }

  private void addShootActions() {
    for (TrooperModel enemy : environment.getVisibleTroopers())
      if (!enemy.isTeammate()) {
        ShootActionParameters shootActionParams = new ShootActionParameters(enemy);
        actionsList.add(new Pair<Action, IActionParameters>(new ShootAction(environment), shootActionParams));
        shootActionParameters.add(shootActionParams);
      }
  }

  private void addThrowGrenadeActions() {
    World world = environment.getWorld();
    ThrowGrenadeActionChecker checker = new ThrowGrenadeActionChecker(environment);

    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j) {
        ThrowGrenadeActionParameters actionParameters = new ThrowGrenadeActionParameters(i, j);
        if (checker.checkActionValidity(actionParameters)) {
          actionsList.add(new Pair<Action, IActionParameters>(new ThrowGrenadeAction(environment), actionParameters));
        }
      }
  }

  private void addStanceChangeActions() {
    actionsList.add(new Pair<Action, IActionParameters>(new RaiseStanceAction(environment), new StanceActionParameters()));
    actionsList.add(new Pair<Action, IActionParameters>(new LowerStanceAction(environment), new StanceActionParameters()));
  }

  private void addMoveActions(TrooperModel trooper) {
    for (Direction direction : Direction.values()) {
      if (direction == Direction.CURRENT_POINT)
        continue;

      MoveActionParameters moveActionParams = new MoveActionParameters(trooper, direction);
      actionsList.add(new Pair<Action, IActionParameters>(new MoveAction(environment), moveActionParams));
      moveActionParameters.add(moveActionParams);
    }
  }

  @Override
  public ArrayList<Pair<Action, IActionParameters>> updateActionParametersWithTrooper(TrooperModel trooper) {
    if (actionsList == null)
      init(trooper);

    int i = 0;
    for (Direction direction : Direction.values()) {
      if (direction == Direction.CURRENT_POINT)
        continue;

      MoveActionParameters actionParameters = moveActionParameters.get(i++);
      actionParameters.update(trooper, direction);
    }
    return actionsList;
  }
}
