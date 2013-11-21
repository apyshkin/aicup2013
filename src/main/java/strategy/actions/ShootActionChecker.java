package strategy.actions;

import model.Game;
import model.Trooper;
import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 7:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShootActionChecker extends ActionChecker {
  public ShootActionChecker(Game game, World world) {
    super(game, world);
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
    return CellIsVisible(self, enemyTrooper) && !enemyTrooper.isTeammate();
  }

  private boolean CellIsVisible(Trooper self, Trooper enemyTrooper) {
    return world.isVisible(self.getShootingRange(), self.getX(), self.getY(), self.getStance(),
            enemyTrooper.getX(), enemyTrooper.getY(), enemyTrooper.getStance());
  }

  @Override
  public int countActionCost(Trooper self) {
    return self.getShootCost();
  }
}
