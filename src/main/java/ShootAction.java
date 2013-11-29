import model.ActionType;
import model.Move;
import model.TrooperType;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 5:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShootAction extends Action {
  public ShootAction(Environment environment) {
    super(ActionType.SHOOT, new ShootActionChecker(environment), environment);
  }

  @Override
  public void innerUndoActSimulating(IActionParameters params, TrooperModel trooper) {
    ShootActionParameters shootParams = (ShootActionParameters) params;
    int newHitPoints = shootParams.getEnemyTrooper().getHitpoints();
    int oldHitPoints = newHitPoints + trooper.getDamage();
    shootParams.getEnemyTrooper().setHitpoints(oldHitPoints);
  }

  @Override
  protected int innerActSimulating(IActionParameters params, TrooperModel trooper) {
    ShootActionParameters shootParams = (ShootActionParameters) params;
    final TrooperModel enemyTrooper = shootParams.getEnemyTrooper();
    int oldHitPoints = enemyTrooper.getHitpoints();
    int newHitPoints = oldHitPoints - trooper.getDamage();
    enemyTrooper.setHitpoints(newHitPoints);
    int points = Math.max(oldHitPoints - newHitPoints, 0);
    if (newHitPoints <= 0)
      points += environment.getGame().getTrooperEliminationScore();
    if (enemyTrooper.getType() == TrooperType.FIELD_MEDIC)
      points += 2;

    return points * Math.min(6, 3 + 100 / (10 + oldHitPoints));
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel trooper, Move move) {
    ShootActionParameters shootParams = (ShootActionParameters) params;
    move.setX(shootParams.getX());
    move.setY(shootParams.getY());
  }


}
