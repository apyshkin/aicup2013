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
  public boolean checkActionValidity(IActionParameters params, Trooper self) {
    ShootActionParameters shootParams = (ShootActionParameters) params;

    if (countActionCost(self) > self.getActionPoints())
      return false;
    if (!canShootAtCell(self, shootParams.getEnemyTrooper()))
      return false;

    return true;
  }

  private boolean canShootAtCell(Trooper self, Trooper enemyTrooper) {
    return checkEnemyIsVisible(self, enemyTrooper) && !enemyTrooper.isTeammate();
  }

  @Override
  public int countActionCost(Trooper self) {
    return self.getShootCost();
  }
}
