import model.Direction;
import model.World;

import java.util.ArrayList;

public class TrooperActionsGenerator implements IActionsGenerator {
  protected Environment environment;
  private ArrayList<MoveActionParameters> moveActionParameters = null;
  protected ArrayList<Pair<AbstractAction, IActionParameters>> actionsList = null;

  public TrooperActionsGenerator(Environment environment) {
    this.environment = environment;
  }

  protected void init(TrooperModel trooper) {
    actionsList = new ArrayList<>();
    moveActionParameters = new ArrayList<>();
    addMoveActions(trooper);
    addStanceChangeActions();
    addShootActions(trooper);
    addMedkitActions();
    addFieldRationActions(trooper);
    addThrowGrenadeActions();
  }

  private void addFieldRationActions(TrooperModel me) {
    if (environment.getBattleMap().getEnemies().isEmpty())
      return;
    EatFieldRationActionParameters actionParameters = new EatFieldRationActionParameters(me);
    actionsList.add(new Pair<AbstractAction, IActionParameters>(new EatFieldRationAction(environment), actionParameters));
  }

  private void addMedkitActions() {
    for (TrooperModel patient : environment.getMyTroopers())
      if (patient.getHitpoints() < patient.getMaximalHitpoints() - environment.getGame().getMedikitBonusHitpoints() >> 1) {
        UseMedkitActionParameters useMedkitActionParams = new UseMedkitActionParameters(patient);
        actionsList.add(new Pair<AbstractAction, IActionParameters>(new UseMedkitAction(environment), useMedkitActionParams));
      }
  }

  private void addShootActions(TrooperModel trooper) {
    for (TrooperModel enemy : environment.getVisibleTroopers())
      if (!enemy.isTeammate()) {
        ShootActionParameters shootActionParams = new ShootActionParameters(enemy);
        ShootActionChecker checker = new ShootActionChecker(environment);
//        if (checker.checkActionValidity(shootActionParams, trooper))
          actionsList.add(new Pair<AbstractAction, IActionParameters>(new ShootAction(environment), shootActionParams));
      }
  }

  private void addThrowGrenadeActions() {
    World world = environment.getWorld();
    ThrowGrenadeActionChecker checker = new ThrowGrenadeActionChecker(environment);

    int maxCount = 0;
    ThrowGrenadeActionParameters bestActionParameters = null;
    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j) {
        int count = 0;
        ArrayList<TrooperModel> enemiesNearby = getActualEnemiesNearby(i, j);
        for (TrooperModel enemy : enemiesNearby)
          if (enemy.getHitpoints() > 0)
            ++count;
        if (count > maxCount - 1) {
          ThrowGrenadeActionParameters actionParameters = new ThrowGrenadeActionParameters(i, j);
          if (checker.checkActionValidity(actionParameters)) {
            maxCount = count;
            bestActionParameters = actionParameters;
          }
        }
      }
    if (bestActionParameters != null)
      actionsList.add(new Pair<AbstractAction, IActionParameters>(new ThrowGrenadeAction(environment), bestActionParameters));
  }

  private ArrayList<TrooperModel> getActualEnemiesNearby(int x, int y) {
    ArrayList<TrooperModel> troopers = environment.getActualEnemies();
    ArrayList<TrooperModel> answer = new ArrayList<>();
    for (TrooperModel trooper : troopers)
      if ((Math.abs(trooper.getX() - x) + Math.abs(trooper.getY() - y)) <= 1)
        answer.add(trooper);
    return answer;
  }

  private void addStanceChangeActions() {
    actionsList.add(new Pair<AbstractAction, IActionParameters>(new RaiseStanceAction(environment), new StanceActionParameters()));
    actionsList.add(new Pair<AbstractAction, IActionParameters>(new LowerStanceAction(environment), new StanceActionParameters()));
  }

  private void addMoveActions(TrooperModel trooper) {
    for (Direction direction : Direction.values()) {
      if (direction == Direction.CURRENT_POINT)
        continue;

      MoveActionParameters moveActionParams = new MoveActionParameters(direction);
      actionsList.add(new Pair<AbstractAction, IActionParameters>(new MoveAction(environment), moveActionParams));
      moveActionParameters.add(moveActionParams);
    }
  }

  @Override
  public ArrayList<Pair<AbstractAction, IActionParameters>> updateActionParametersWithTrooper(TrooperModel trooper) {
    if (actionsList == null)
      init(trooper);

//    int i = 0;
//    for (Direction direction : Direction.values()) {
//      if (direction == Direction.CURRENT_POINT)
//        continue;
//
//      MoveActionParameters actionParameters = moveActionParameters.get(i++);
//      actionParameters.update(trooper, direction);
//    }
    return actionsList;
  }
}
