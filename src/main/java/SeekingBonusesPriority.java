import model.TrooperStance;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/29/13
 * Time: 2:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class SeekingBonusesPriority implements IPriority {
  private final Environment environment;
  private final TrooperModel trooper;

  public SeekingBonusesPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    init();
  }

  private void init() {
    //To change body of created methods use File | Settings | File Templates.
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
      return damageToMe;

    return damageToMe;
  }

  private int countPotentialDamage(ArrayList<TrooperModel> enemies) {
    final BattleMap battleMap = environment.getBattleMap();
    int sum = 0;
    for (TrooperModel enemy : enemies)
      if (battleMap.ableToShoot(enemy, trooper)) {
        sum += countAverageShootPoints(enemy, enemy.getStance());
      }
    return sum;
  }

  private int countAverageShootPoints(TrooperModel trooper, TrooperStance stance) {
    return trooper.getDamage(stance) * (trooper.getInitialActionPoints() / trooper.getShootCost());
  }
}
