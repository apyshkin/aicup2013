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

  public abstract boolean checkActionValidity(IActionParameters params, TrooperModel trooper);

  public abstract int countActionCost(TrooperModel self);

  protected boolean checkCellHasNoMen(int x, int y) {
    TrooperModel[] troopers = environment.getAllVisibleTroopers();
    for (TrooperModel trooper : troopers)
      if (trooper.getX() == x && trooper.getY() == y)
        return false;
    return true;
  }

  protected boolean checkCellIsFree(int x, int y) {
    return environment.cellIsFree(x, y);
  }

  protected boolean checkCellsAreNeighbours(int x, int y, int x1, int y1) {
    return environment.cellsAreNeighbours(x, y, x1, y1);
  }

  protected boolean checkCellsAreTheSame(int x, int y, int x1, int y1) {
    return environment.cellsAreTheSame(x, y, x1, y1);
  }

  protected boolean checkCellIsWithinBoundaries(int x, int y) {
    return environment.cellIsWithinBoundaries(x, y);
  }

  protected boolean checkEnemyIsVisible(TrooperModel self, TrooperModel enemyTrooper) {
    return environment.enemyIsVisible(self, enemyTrooper);
  }
}
