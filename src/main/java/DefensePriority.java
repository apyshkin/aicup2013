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

  public DefensePriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    assert (trooper.getStance() == Utils.getStance(stance));
    ArrayList<TrooperModel> enemies = environment.getEnemies();
    int damageToMe = countPotentialDamage(enemies);
    int countOfIterationsISurvive = trooper.getHitpoints() / (damageToMe + 1);
    if (countOfIterationsISurvive <= 1)
      return 4 * damageToMe;
    else if (countOfIterationsISurvive == 2)
      return 2 * damageToMe;

    return damageToMe;
  }

  private int countPotentialDamage(ArrayList<TrooperModel> enemies) {
    final BattleMap battleMap = environment.getBattleMap();
    int sum = 0;
    for (TrooperModel enemy : enemies)
      if (battleMap.isVisibleFrom(enemy.getX(), enemy.getY(), trooper)) {
        sum += countAverageShootPoints(enemy, enemy.getStance());
      }
    return sum;
  }

  private int countAverageShootPoints(TrooperModel trooper, TrooperStance stance) {
    return trooper.getDamage(stance) * trooper.getInitialActionPoints() / trooper.getShootCost();
  }
}
