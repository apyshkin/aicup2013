import model.ActionType;
import model.Move;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 5:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class ThrowGrenadeAction extends AbstractAction {
  public ThrowGrenadeAction(Environment environment) {
    super(ActionType.THROW_GRENADE, new ThrowGrenadeActionChecker(environment), environment);
  }

  @Override
  public void innerUndoActSimulating(IActionParameters params, TrooperModel trooper) {
    ThrowGrenadeActionParameters actionParameters = (ThrowGrenadeActionParameters) params;
    ArrayList<TrooperModel> enemyTroopers = getEnemyTroopersNearby(actionParameters.getX(), actionParameters.getY());
    for (TrooperModel enemy : enemyTroopers) {
      int newHitPoints = enemy.getHitpoints();
      int deltaPoints = 0;
      if (enemy.getX() == actionParameters.getX() && enemy.getY() == actionParameters.getY())
        deltaPoints = environment.getGame().getGrenadeDirectDamage();
      else
        deltaPoints = environment.getGame().getGrenadeCollateralDamage();

      int oldHitPoints = newHitPoints + deltaPoints;
      enemy.setHitpoints(oldHitPoints);
    }
    trooper.obtainGrenade();
  }

  @Override
  protected int innerActSimulating(IActionParameters params, TrooperModel trooper) {
    ThrowGrenadeActionParameters actionParameters = (ThrowGrenadeActionParameters) params;
    int points = 0;
    for (TrooperModel enemy : getEnemyTroopersNearby(actionParameters.getX(), actionParameters.getY())) {
      int oldHitPoints = enemy.getHitpoints();
      int deltaPoints = 0;
      if (enemy.getX() == actionParameters.getX() && enemy.getY() == actionParameters.getY())
        deltaPoints = environment.getGame().getGrenadeDirectDamage();
      else
        deltaPoints = environment.getGame().getGrenadeCollateralDamage();
      int newHitPoints = oldHitPoints - deltaPoints;
      enemy.setHitpoints(newHitPoints);
      points += Math.max(oldHitPoints - newHitPoints, 0);
      if (newHitPoints <= 0)
        points += environment.getGame().getTrooperEliminationScore();
    }
    trooper.throwGrenade();

    return 2 * points;
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel trooper, Move move) {
    ThrowGrenadeActionParameters actionParameters = (ThrowGrenadeActionParameters) params;
    move.setX(actionParameters.getX());
    move.setY(actionParameters.getY());
  }

  private ArrayList<TrooperModel> getEnemyTroopersNearby(int x, int y) {
    ArrayList<TrooperModel> enemies = environment.getEnemies();
    ArrayList<TrooperModel> answer = new ArrayList<>();
    for (TrooperModel enemy : enemies)
      if ((Math.abs(enemy.getX() - x) + Math.abs(enemy.getY() - y)) <= 1)
        answer.add(enemy);
    return answer;
  }
}
