import model.Player;
import model.TrooperStance;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 9:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class Analyzer {
  private final static Logger logger = Logger.getLogger(Analyzer.class.getName());

  private final Environment environment;

  public Analyzer(Environment environment) {
    this.environment = environment;
  }

  public ITactics chooseTactics(BattleHistory battleHistory) {
    if (canSeeTheEnemy()) {
      logger.log(Level.INFO, "Choosing tactics: the enemy is visible");
      return new AttackTactics(environment);
    } else if (wasAttackedSinceLastMove(battleHistory)) {
      logger.log(Level.INFO, "Choosing tactics: we have been attacked!");
      determineEnemy(battleHistory);
      return new AttackTactics(environment);
    } else if (hasMaximalPoints()) { //todo
      logger.log(Level.INFO, "Choosing tactics: we have maximal points, so lets hide");
      determineEnemy(battleHistory);
      return new PatrolTactics(environment);
    } else {
      logger.log(Level.INFO, "Choosing tactics: we have nothing to do, so lets go sightseeing");
      return new PatrolTactics(environment);
    }
  }

  private boolean hasMaximalPoints() {
    long myPlayerId = environment.getMyTroopers().get(0).getPlayerId();
    Player[] players = environment.getWorld().getPlayers();
    Player me = null;

    for (Player player : players)
      if (player.getId() == myPlayerId)
        me = player;

    assert me != null;

    for (Player player : players)
      if (player != me && player.getScore() >= me.getScore())
        return false;

    return true;
  }

  private void determineEnemy(BattleHistory battleHistory) {
    assert (battleHistory.size() > 1);
    assert (environment.getEnemies().isEmpty());

    Environment last = battleHistory.getLast();
    Environment oneBeforeLast = battleHistory.get(battleHistory.size() - 2);

    TrooperModel suffered = null;
    for (TrooperModel oldTrooper : oneBeforeLast.getMyTroopers()) {
      boolean found = false;
      for (TrooperModel trooper : last.getMyTroopers())
        if (trooper.getType() == oldTrooper.getType()) {
          found = true;
          if (trooper.getHitpoints() < oldTrooper.getHitpoints())
            suffered = oldTrooper;
        }

      if (!found)
        suffered = oldTrooper;
    }

    if (suffered == null)
      return;

    MapCell closestCell = calcEnemyLocation(suffered);

    environment.putEnemy(createEnemy(closestCell.getX(), closestCell.getY()));
  }

  private MapCell calcEnemyLocation(TrooperModel suffered) {
    MapCell closestCell = null;
    int minDist = Utils.INFINITY;
    int n = environment.getWorld().getWidth();
    int m = environment.getWorld().getHeight();
    for (int i = 0; i < n; ++i)
      for (int j = 0; j < m; ++j)
        if (environment.getCellChecker().cellIsFree(i, j) && environment.getBattleMap().getDistance(suffered.getX(), suffered.getY(), i, j) < minDist
                && !environment.getWorld().isVisible(suffered.getVisionRange(),
                      suffered.getX(), suffered.getY(), suffered.getStance(), i, j, TrooperStance.STANDING)) {
          minDist = environment.getBattleMap().getDistance(suffered.getX(), suffered.getY(), i, j);
          closestCell = new MapCell(environment.getWorld(), i, j);
        }
    return closestCell;
  }

  private TrooperModel createEnemy(int x, int y) {
    TrooperModel enemy = environment.getMyTroopers().get(0).clone(); // todo better
    enemy.setActionPoints(10);
    enemy.setHitpoints(100);
    enemy.setStance(TrooperStance.STANDING);
    enemy.setX(x);
    enemy.setY(y);
    enemy.turnToEnemy();
    return enemy;
  }

  private boolean canSeeTheEnemy() {
    return !environment.getEnemies().isEmpty();
  }

  private boolean wasAttackedSinceLastMove(BattleHistory battleHistory) {
    if (battleHistory.size() < 2)
      return false;

    Environment last = battleHistory.getLast();
    Environment oneBeforeLast = battleHistory.get(battleHistory.size() - 2);
    int summaryHP = 0;
    for (TrooperModel myTrooper : last.getMyTroopers())
      summaryHP += myTrooper.getHitpoints();

    int summaryHPBeforeLastMove = 0;
    for (TrooperModel myTrooper : oneBeforeLast.getMyTroopers())
      summaryHPBeforeLastMove += myTrooper.getHitpoints();

    if (summaryHPBeforeLastMove > summaryHP)
      return true;
    else
      return false;
  }

}
