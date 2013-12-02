import model.Trooper;
import model.TrooperStance;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 12/2/13
 * Time: 2:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class IsUnderAttackAnalysis implements IAnalysis {
  private final static Logger logger = Logger.getLogger(IsUnderAttackAnalysis.class.getName());
  private final Environment environment;
  private final BattleHistory battleHistory;

  public IsUnderAttackAnalysis(Environment environment, BattleHistory battleHistory) {
    this.environment = environment;
    this.battleHistory = battleHistory;
  }

  @Override
  public boolean condition() {
    return  wasAttackedSinceLastMove();
  }

  private boolean wasAttackedSinceLastMove() {
    int size = battleHistory.size();
    if (size < 2)
      return false;

    int teamSize = environment.getMyTeam().count();
    for (int i = 0; i < teamSize && i + 1 < size; ++i) {
      Environment environment1 = battleHistory.get(size - 1 - i);
      Environment environment2 = battleHistory.get(size - 1 - i - 1);
      int summaryHP = countSummaryHP(environment1.getWorld().getTroopers());
      int summaryHPBefore = countSummaryHP(environment2.getWorld().getTroopers());
      if (summaryHPBefore > summaryHP)
        return true;
    }

    return false;
  }

  private int countSummaryHP(Trooper[] troopers) {
    int sum = 0;
    for (Trooper trooper : troopers)
      sum += trooper.getHitpoints();
    return sum;
  }

  private int countSummaryHP(ArrayList<TrooperModel> troopers) {
    int sum = 0;
    for (TrooperModel trooper : troopers)
      sum += trooper.getHitpoints();
    return sum;
  }

  private void tryToFindAndKill() {
    assert (environment.getActualEnemies().isEmpty());
    int width = environment.getWorld().getWidth();
    int height = environment.getWorld().getHeight();
    ArrayList <TrooperModel> enemies = environment.getEnemies();
    ArrayList<TrooperModel> myTroopers = environment.getMyTroopers();
    if (environment.getCurrentTime() > 6 && (enemies.size() < myTroopers.size())
            || countSummaryHP(enemies) < 0.7 * countSummaryHP(myTroopers)) {
      final TrooperModel enemyClosestToUs = getEnemyClosestToUs(myTroopers, enemies);
      environment.getRouter().setCheckPoint(new MapCell(width, height, enemyClosestToUs.getX(), enemyClosestToUs.getY()));
    }
  }

  private TrooperModel getEnemyClosestToUs(ArrayList<TrooperModel> myTroopers, ArrayList<TrooperModel> enemies) {
    int minDist = Utils.INFINITY;
    TrooperModel closestEnemy = null;
    for (TrooperModel enemy : enemies) {
      int sumDist = 0;
      for (TrooperModel trooper : myTroopers) {
        sumDist += environment.getBattleMap().getDistance(enemy.getX(), enemy.getY(), trooper.getX(), trooper.getY());
      }
      if (sumDist < minDist) {
        closestEnemy = enemy;
        minDist = sumDist;
      }
    }
    return closestEnemy;
  }

  @Override
  public void doAction() {
    addEnemyToBattleMap(battleHistory);
    tryToFindAndKill();
  }

  private void addEnemyToBattleMap(BattleHistory battleHistory) {
    assert (battleHistory.size() > 1);
    assert (environment.getActualEnemies().isEmpty());

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

    MapCell closestCell = findEnemyLocation(battleHistory, suffered);
    environment.putEnemy(createEnemy(closestCell.getX(), closestCell.getY()));
  }

  private MapCell findEnemyLocation(BattleHistory history, TrooperModel suffered) {
    TrooperModel lastEnemyWeHaveSeen = getPotentialEnemy(history, suffered);
    if (lastEnemyWeHaveSeen == null)
      return getCellClosestToTrooper(suffered);
    else
      return getCellClosestToTrooper(lastEnemyWeHaveSeen);
  }

  private TrooperModel getPotentialEnemy(BattleHistory history, TrooperModel suffered) {
    ArrayList<TrooperModel> enemies = history.getEnemyLocator().getEnemies();
    TrooperModel best = null;
    double minDist = Utils.INFINITY;
    for (TrooperModel enemy : enemies) {
      if (minDist > suffered.getDistanceTo(enemy.getX(), enemy.getY())) {
        minDist = suffered.getDistanceTo(enemy.getX(), enemy.getY());
        best = enemy;
      }
    }
    return best;
  }

  private MapCell getCellClosestToTrooper(TrooperModel suffered) {
    MapCell closestCell = null;
    double minDist = Utils.INFINITY;
    ArrayList<TrooperModel> myTroopers = environment.getMyTroopers();
    for (int i = 0; i < environment.getWorld().getWidth(); ++i)
      for (int j = 0; j < environment.getWorld().getHeight(); ++j)
        if (environment.getCellChecker().cellIsFree(i, j)
                && suffered.getDistanceTo(i, j) < minDist - 0.001
                && environment.getBattleMap().isVisibleFrom(i, j, suffered)
                && !environment.getBattleMap().checkVisibility(myTroopers, i, j, TrooperStance.STANDING)) {
          minDist = suffered.getDistanceTo(i, j);
          closestCell = new MapCell(environment.getBattleMap().getWidth(), environment.getBattleMap().getHeight(), i, j);
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

  @Override
  public ITactics getTactics() {
    logger.info("CHOOSING TACTICS: WE HAVE BEEN ATTACKED!");
    return new AttackTactics();
  }
}
