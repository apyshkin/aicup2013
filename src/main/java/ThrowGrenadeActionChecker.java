import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 7:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ThrowGrenadeActionChecker extends ActionChecker {
  public ThrowGrenadeActionChecker(Environment environment) {
    super(environment);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, TrooperModel trooper) {
    ThrowGrenadeActionParameters actionParameters = (ThrowGrenadeActionParameters) params;
    if (countActionCost(trooper) > trooper.getActionPoints())
      return false;
    if (!checkHasGrenade(trooper))
      return false;
    if (!checkCellIsWithinBoundaries(actionParameters.getX(), actionParameters.getY()))
      return false;
    if (!canThrowAtCell(actionParameters.getX(), actionParameters.getY()))
      return false;
    if (!checkCellWithinRange(trooper, actionParameters.getX(), actionParameters.getY(), environment.getGame().getGrenadeThrowRange()))
      return false;

    return true;
  }

  private boolean checkHasGrenade(TrooperModel trooper) {
    return trooper.isHoldingGrenade();
  }

  private boolean enemyIsAlive(TrooperModel enemy) {
    return enemy.getHitpoints() > 0;
  }

  private boolean canThrowAtCell(int x, int y) {
    if (!checkCellIsFree(x, y))
      return false;

    ArrayList<TrooperModel> troopers = getTroopersNearby(x, y);
    for (TrooperModel trooper1 : troopers)
      if (trooper1.isTeammate() || !enemyIsAlive(trooper1))
        return false;

    if (troopers.isEmpty())
      return false;

    if (troopers.size() == 1) {
      int trooperX = troopers.get(0).getX();
      int trooperY = troopers.get(0).getY();
      if (x != trooperX && y != trooperY)
        return false;
    }

    return true;
  }

  private boolean checkCellWithinRange(TrooperModel trooper, int x, int y, double range) {
    return cellChecker.cellIsWithinRange(trooper.getX(), trooper.getY(), x, y, range);
  }

  private ArrayList<TrooperModel> getTroopersNearby(int x, int y) {
    ArrayList<TrooperModel> troopers = environment.getEnemies();
    ArrayList<TrooperModel> answer = new ArrayList<>();
    for (TrooperModel trooper : troopers)
      if ((Math.abs(trooper.getX() - x) + Math.abs(trooper.getY() - y)) <= 1)
        answer.add(trooper);
    return answer;
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return environment.getGame().getGrenadeThrowCost();
  }

  public boolean checkActionValidity(ThrowGrenadeActionParameters actionParameters) {
    if (!checkCellIsWithinBoundaries(actionParameters.getX(), actionParameters.getY()))
      return false;
    if (!canThrowAtCell(actionParameters.getX(), actionParameters.getY()))
      return false;

    return true;
  }
}
