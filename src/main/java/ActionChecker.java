import model.Trooper;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 5:41 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ActionChecker {
  protected final Environment environment;

  public ActionChecker(Environment environment) {
    this.environment = environment;
  }

  public abstract boolean checkActionValidity(IActionParameters params, Trooper self);

  public abstract int countActionCost(Trooper self);

  protected boolean checkCellHasNoMen(int x, int y) {
    Trooper[] troopers = environment.getAllVisibleTroopers();
    for (Trooper tr : troopers)
      if (tr.getX() == x && tr.getY() == y)
        return false;
    return true;
  }

  protected boolean checkCellIsFree(int x, int y) {
    return environment.cellIsFree(x, y);
  }

  protected boolean checkCellsAreNeighbours(int x, int y, int x1, int y1) {
    return environment.cellsAreNeighbours(x, y, x1, y1);
  }

  protected boolean checkCellIsWithinBoundaries(int x, int y) {
    return environment.cellIsWithinBoundaries(x, y);
  }

  protected boolean checkEnemyIsVisible(Trooper self, Trooper enemyTrooper) {
    return environment.enemyIsVisible(self, enemyTrooper);
  }
}
