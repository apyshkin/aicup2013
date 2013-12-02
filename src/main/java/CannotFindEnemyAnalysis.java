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
public class CannotFindEnemyAnalysis implements IAnalysis {
  private final static Logger logger = Logger.getLogger(CannotFindEnemyAnalysis.class.getName());
  private static boolean waitingForEnemiesLocations;
  private static boolean walking;
  private final Environment environment;
  private final BattleHistory battleHistory;

  public CannotFindEnemyAnalysis(Environment environment, BattleHistory battleHistory) {
    this.environment = environment;
    this.battleHistory = battleHistory;
  }

  @Override
  public boolean condition() {
    if (nowIsCommanderMove() && hasEnoughAP()) {
      if (waitingForEnemiesLocations)
        return true;
      if (walking) {
        if (environment.getRouter().checkPointWasReached()) {
          walking = false;
          return false;
        }
      } else if (haveNotSeenEnemyForAges())
        return true;
    }
    return false;
  }

  private boolean hasEnoughAP() {
    return environment.getCurrentTrooper().getActionPoints() == 10;
  }

  private boolean nowIsCommanderMove() {
    return environment.getCurrentTrooper().getType() == TrooperType.COMMANDER;
  }

  private boolean haveNotSeenEnemyForAges() {
    int count = 0;
    for (int i = battleHistory.size() - 1; i >= 0; --i) {
      Environment env = battleHistory.get(i);
      if (env.getActualEnemies().isEmpty())
        ++count;
      else
        break;

      if (count > 15 * (env.getBattleState().getPlayersTotal() - env.getBattleState().getDeadPlayersCount()))
        return true;
    }
    return false;
  }

  @Override
  public void doAction() {
    if (waitingForEnemiesLocations) {
      Player[] players = environment.getWorld().getPlayers();
      ArrayList<MapCell> possibleDestinations = new ArrayList<>();
      for (Player player : players)
        if (player.getId() != environment.getBattleState().getMyId()) {
          int x = player.getApproximateX();
          int y = player.getApproximateY();
          if (x == -1)
            environment.getBattleState().setPlayerDead(player);
          else
            possibleDestinations.add(new MapCell(environment.getWorld().getWidth(), environment.getBattleMap().getHeight(), x, y));
        }
      assert !possibleDestinations.isEmpty();

      Router myRouter = environment.getRouter();
      myRouter.setCheckPoint(chooseClosestCellToUs(environment.getMyTroopers(), possibleDestinations));
      logger.info("CHOOSING TACTICS: LOOKING FOR THE ENEMY, COMMANDER MADE A CALL -- time to AttACK! ");
      waitingForEnemiesLocations = false;
      walking = true;
    } else
      waitingForEnemiesLocations = true;
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
    if (waitingForEnemiesLocations)
      return new SearchTactics(environment);

    return new PatrolTactics();
  }
}
