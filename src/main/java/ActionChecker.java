import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 5:41 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ActionChecker {
  protected final Environment environment;
  final CellChecker cellChecker;

  public ActionChecker(Environment environment) {
    this.environment = environment;
    cellChecker = environment.getCellChecker();
  }

  public abstract boolean checkActionValidity(IActionParameters params, TrooperModel trooper);

  public abstract int countActionCost(TrooperModel self);

  protected boolean checkCellHasNoMen(int x, int y) {
    ArrayList<TrooperModel> troopers = environment.getVisibleTroopers();
    for (TrooperModel trooper : troopers)
      if (trooper.getX() == x && trooper.getY() == y)
        return false;
    return true;
  }

  protected boolean checkCellIsFree(int x, int y) {
    return cellChecker.cellIsFree(x, y);
  }

  protected boolean checkCellsAreNeighbours(int x, int y, int x1, int y1) {
    return cellChecker.cellsAreNeighbours(x, y, x1, y1);
  }

  protected boolean checkCellsAreTheSame(int x, int y, int x1, int y1) {
    return cellChecker.cellsAreTheSame(x, y, x1, y1);
  }

  protected boolean checkCellIsWithinBoundaries(int x, int y) {
    return cellChecker.cellIsWithinBoundaries(x, y);
  }

  protected boolean checkEnemyIsVisible(TrooperModel trooper, TrooperModel enemyTrooper) {
    return environment.enemyIsVisible(trooper, enemyTrooper);
  }

  protected boolean checkHasBonus(TrooperModel trooper) {
    return trooper.isHoldingFieldRation() || trooper.isHoldingMedkit() || trooper.isHoldingGrenade();
  }
}
