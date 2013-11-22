import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 5:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShootSimulatedAction extends SimulatedAction {
  public ShootSimulatedAction(Environment environment) {
    super(ActionType.SHOOT, new ShootActionChecker(environment), environment);
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel self) {
    ShootActionParameters shootParams = (ShootActionParameters) params;
    int oldhp = shootParams.getEnemyTrooper().getHitpoints();
    int newhp = oldhp - self.getDamage();
    shootParams.getEnemyTrooper().setHitpoints(newhp);
  }

  @Override
  protected void innerActForReal(IActionParameters params, TrooperModel self, Move move) {
    ShootActionParameters shootParams = (ShootActionParameters) params;
    move.setX(shootParams.getX());
    move.setY(shootParams.getY());
  }

}
