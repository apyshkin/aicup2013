import model.TrooperStance;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/29/13
 * Time: 2:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class DefensePriority implements IPriority {
  private final Environment environment;
  private final TrooperModel trooper;
  private int[][] distances;

  public DefensePriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    init();
  }

  private void init() {
    final BattleMap battleMap = environment.getBattleMap();
    distances = new int[battleMap.getWidth()][battleMap.getHeight()];
    for (int i = 0; i < battleMap.getWidth(); ++i)
      for (int j = 0; j < battleMap.getHeight(); ++j)
        if (battleMap.getCellChecker().cellIsFree(i, j)) {
          for (TrooperModel enemy : environment.getActualEnemies())
            distances[i][j] += battleMap.getDistance(enemy.getX(), enemy.getY(), i, j);
        }
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    assert (trooper.getStance() == Utils.getStance(stance));
    ArrayList<TrooperModel> enemies = environment.getEnemies();
    int damageToMe = countPotentialDamage(enemies);
    int countOfIterationsISurvive = trooper.getHitpoints() / (damageToMe + 1);
    if (countOfIterationsISurvive <= 1)
      return 2 * (damageToMe + distances[x][y]);
    else if (countOfIterationsISurvive == 2)
      return damageToMe;

    return damageToMe;
  }

  private int countPotentialDamage(ArrayList<TrooperModel> enemies) {
    final BattleMap battleMap = environment.getBattleMap();
    int sum = 0;
    for (TrooperModel enemy : enemies) {
      if (battleMap.ableToShoot(enemy, trooper)) {
        sum += countAverageShootPoints(enemy, enemy.getStance());
      }
      if (battleMap.ableToThrowGrenade(enemy, trooper)) {
        sum += countMaxGrenadePoints() / 4;
      }
    }
    return sum;
  }

  private int countMaxGrenadePoints() {
    int max = 0;
    for (TrooperModel myTrooper : environment.getMyTroopers()) {
      int sum = 0;
      for (int dx = -1; dx <= 1; ++dx)
        for (int dy = -1; dy <= 1; ++dy) {
          int x = myTrooper.getX() + dx;
          int y = myTrooper.getY() + dy;
          for (TrooperModel myTrooper1 : environment.getMyTroopers()) {
            int x1 = myTrooper1.getX();
            int y1 = myTrooper1.getY();
            if (x == x1 && y == y1)
              sum += environment.getGame().getGrenadeDirectDamage();
            else if (Math.abs(x - x1) + Math.abs(y - y1) == 1)
              sum += environment.getGame().getGrenadeCollateralDamage();
          }
        }
      max = Math.max(max, sum);
    }
    return max;
  }

  private int countAverageShootPoints(TrooperModel trooper, TrooperStance stance) {
    return trooper.getDamage(stance) * (trooper.getInitialActionPoints() / trooper.getShootCost());
  }
}
