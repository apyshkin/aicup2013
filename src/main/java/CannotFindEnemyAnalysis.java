import model.Player;
import model.TrooperType;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 12/2/13
 * Time: 3:23 PM
 * To change this template use File | Settings | File Templates.
 */
enum CallStatus {
  DEFAULT,
  WAITING_FOR_RESPONSE,
  RESPONSE,
  WALKING_TO_ENEMY
}
public class CannotFindEnemyAnalysis implements IAnalysis {
  private final static Logger logger = Logger.getLogger(CannotFindEnemyAnalysis.class.getName());
  private final Environment environment;
  private static CallStatus status = CallStatus.DEFAULT;
  private static int timeOfPatrolling;
  private static MapCell currentCP;

  public CannotFindEnemyAnalysis(Environment environment, int timeOfPatrolling) {
    this.environment = environment;
    this.timeOfPatrolling = timeOfPatrolling;
    constructorTrick();
  }

  private void constructorTrick() {
    if (nowIsCommanderMove() && hasEnoughAP()) {
      if (status == CallStatus.WAITING_FOR_RESPONSE)
        status = CallStatus.RESPONSE;
      else if (status == CallStatus.RESPONSE)
        status = CallStatus.DEFAULT;
    }
  }

  @Override
  public boolean condition() {
    if (nowIsCommanderMove() && hasEnoughAP()) {
      if (status == CallStatus.RESPONSE)
        return true;
      if (status == CallStatus.WALKING_TO_ENEMY) {
        if (environment.getRouter().checkPointWasPassed(currentCP)) {
          status = CallStatus.DEFAULT;
          return false;
        }
        if (timeOfPatrolling > 2 * (55 - environment.getMoveIndex())) {
          status = CallStatus.DEFAULT;
          timeOfPatrolling = 0;
          return true;
        }
      } else if (haveNotSeenEnemyForAges())
        return true;
    }
    return false;
  }

  private boolean hasEnoughAP() {
    return environment.getCurrentTrooper().getActionPoints() >= 10;
  }

  private boolean nowIsCommanderMove() {
    return environment.getCurrentTrooper().getType() == TrooperType.COMMANDER;
  }

  private boolean haveNotSeenEnemyForAges() {
    if (timeOfPatrolling > 2 * (6 * environment.getMyTeam().count()))
      return true;

    return false;
  }

  @Override
  public void doAction() {
    if (status == CallStatus.RESPONSE) {
      Player[] players = environment.getWorld().getPlayers();
      ArrayList<MapCell> possibleDestinations = new ArrayList<>();
      for (Player player : players)
        if (player.getId() != environment.getBattleState().getMyId()) {
          int x = player.getApproximateX();
          int y = player.getApproximateY();
          if (x == -1)
            environment.getBattleState().setPlayerDead(player);
          else
            possibleDestinations.add(BattleMap.getMapCellFactory().createMapCell(x, y));
        }
      assert !possibleDestinations.isEmpty();

      Router myRouter = environment.getRouter();
      currentCP = chooseClosestCellToUs(environment.getMyTroopers(), possibleDestinations);
      myRouter.addCheckPoint(currentCP);
      logger.info("CHOOSING TACTICS: LOOKING FOR THE ENEMY, COMMANDER MADE A CALL -- time to AttACK! ");
      status = CallStatus.WALKING_TO_ENEMY;
    } else
      status = CallStatus.WAITING_FOR_RESPONSE;
  }

  private MapCell chooseClosestCellToUs(ArrayList<TrooperModel> myTroopers, ArrayList<MapCell> possibleDestinations) {
    int minDist = Utils.INFINITY;
    MapCell closestCell = null;
    for (MapCell cell : possibleDestinations) {
      int sumDist = 0;
      for (TrooperModel trooper : myTroopers) {
        sumDist += trooper.getDistanceTo(cell.getX(), cell.getY());
      }
      if (sumDist < minDist) {
        closestCell = cell;
        minDist = sumDist;
      }
    }
    assert closestCell != null;
    return closestCell;
  }

  @Override
  public ITactics getTactics() {
    logger.info("CHOOSING TACTICS: LOOKING FOR THE ENEMY, COMMANDER WILL MAKE A CALL ");
    if (status == CallStatus.WAITING_FOR_RESPONSE)
      return new SearchTactics(environment);

    return new PatrolTactics();
  }
}
