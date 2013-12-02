import model.ActionType;
import model.Move;
import model.Trooper;
import model.TrooperType;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 5:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShootAction extends AbstractAction {
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
    int points = Math.max(oldHitPoints - newHitPoints, 0) + environment.getBattleMap().getActuality(enemyTrooper, environment.getCurrentTime());
    if (newHitPoints <= 0) {
      if (enemyTrooper.getType() != TrooperType.FIELD_MEDIC)
        points += environment.getGame().getTrooperEliminationScore();
      else if (environment.getActualEnemies().size() <= 1) {
        if (newHitPoints > 30 && newHitPoints < 60)
          points = 5;
        else
          points = 0;
      }
    }
    if (enemyTrooper.getType() == TrooperType.FIELD_MEDIC)
      points += 2;

    return points * Math.min(6, 3 + 100 / (10 + oldHitPoints));
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel trooper, Move move) {
    ShootActionParameters shootParams = (ShootActionParameters) params;
    move.setX(shootParams.getX());
    move.setY(shootParams.getY());
    final TrooperModel enemyTrooper = shootParams.getEnemyTrooper();
    int oldHitPoints = enemyTrooper.getHitpoints();
    int newHitPoints = oldHitPoints - trooper.getDamage();
    enemyTrooper.setHitpoints(newHitPoints);
    assert (oldHitPoints > 0);

    Trooper[] troopers = environment.getWorld().getTroopers();
    if (environment.getBattleMap().checkVisibility(environment.getMyTroopers(), shootParams.getEnemyTrooper())) {
      boolean found = false;
      for (Trooper tr : troopers) {
        if (!tr.isTeammate() && tr.getX() == shootParams.getX() && tr.getY() == shootParams.getY())
          found = true;
      }

      assert found;
    }
  }


}
