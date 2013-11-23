import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 5:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShootSimAction extends SimAction {
  public ShootSimAction(Environment environment) {
    super(ActionType.SHOOT, new ShootActionChecker(environment), environment);
  }

  @Override
  public void reset(IActionParameters params, TrooperModel trooper) {
    trooper.setActionPoints(trooper.getActionPoints() + cost(trooper));
    ShootActionParameters shootParams = (ShootActionParameters) params;
    int newhp = shootParams.getEnemyTrooper().getHitpoints();
    int oldhp = newhp + trooper.getDamage();
    shootParams.getEnemyTrooper().setHitpoints(oldhp);
  }


  @Override
  protected int innerAct(IActionParameters params, TrooperModel self) {
    ShootActionParameters shootParams = (ShootActionParameters) params;
    int oldhp = shootParams.getEnemyTrooper().getHitpoints();
    int newhp = oldhp - self.getDamage();
    shootParams.getEnemyTrooper().setHitpoints(newhp);
    int points;
    if (newhp <= 0)
      points = oldhp + 25;
    else
      points = self.getDamage();

    return 2 * points;
  }

  @Override
  protected void innerActForReal(IActionParameters params, TrooperModel self, Move move) {
    ShootActionParameters shootParams = (ShootActionParameters) params;
    move.setX(shootParams.getX());
    move.setY(shootParams.getY());
  }


}
