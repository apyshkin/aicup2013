import model.Trooper;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 7:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShootActionChecker extends ActionChecker {
  public ShootActionChecker(Environment environment) {
    super(environment);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, TrooperModel trooper) {
    ShootActionParameters shootParams = (ShootActionParameters) params;

    if (countActionCost(trooper) > trooper.getActionPoints())
      return false;
    if (!canShootAtCell(trooper, shootParams.getEnemyTrooper()))
      return false;
    if (!enemyIsAlive(shootParams.getEnemyTrooper()))
      return false;

    return true;
  }

  private boolean enemyIsAlive(TrooperModel enemy) {
    return enemy.getHitpoints() > 0;
  }

  private boolean canShootAtCell(TrooperModel self, TrooperModel enemyTrooper) {
    return checkEnemyIsVisible(self, enemyTrooper) && !enemyTrooper.isTeammate();
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return self.getShootCost();
  }
}
