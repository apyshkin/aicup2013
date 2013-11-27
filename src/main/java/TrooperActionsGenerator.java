import model.Direction;
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
    addStanceChangeActions();
    addMoveActions(trooper);
    addShootActions();
    addMedkitActions();
  }

  private void addMedkitActions() {
    for (TrooperModel patient : environment.getMyTroopers()) {
      UseMedkitActionParameters useMedkitActionParams = new UseMedkitActionParameters(patient);
      actionsList.add(new Pair<Action, IActionParameters>(new UseMedkitAction(environment), useMedkitActionParams));
    }
  }

  private void addShootActions() {
    for (TrooperModel enemy : environment.getAllVisibleTroopers())
      if (!enemy.isTeammate()) {
        ShootActionParameters shootActionParams = new ShootActionParameters(enemy);
        actionsList.add(new Pair<Action, IActionParameters>(new ShootAction(environment), shootActionParams));
        shootActionParameters.add(shootActionParams);
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
