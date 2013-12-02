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
  public boolean checkActionValidity(IActionParameters params, TrooperModel shooter) {
    ShootActionParameters shootParams = (ShootActionParameters) params;

    if (countActionCost(shooter) > shooter.getActionPoints())
      return false;
    if (!enemyIsSurelyThere(shootParams.getEnemyTrooper()))
      return false;
    if (!canShootAtCell(shooter, shootParams.getEnemyTrooper()))
      return false;
    if (!enemyIsAlive(shootParams.getEnemyTrooper()))
      return false;

    return true;
  }

  private boolean enemyIsSurelyThere(TrooperModel enemy) {
    return environment.getBattleMap().getCell(enemy.getX(), enemy.getY(), enemy.getStance().ordinal()).isActual(environment.getCurrentTime());
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
