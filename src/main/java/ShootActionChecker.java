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
  public boolean checkActionValidity(IActionParameters params, TrooperModel self) {
    ShootActionParameters shootParams = (ShootActionParameters) params;

    if (countActionCost(self) > self.getActionPoints())
      return false;
    if (!canShootAtCell(self, shootParams.getEnemyTrooper()))
      return false;

    return true;
  }

  private boolean canShootAtCell(TrooperModel self, TrooperModel enemyTrooper) {
    return checkEnemyIsVisible(self, enemyTrooper) && !enemyTrooper.isTeammate();
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return self.getShootCost();
  }
}
