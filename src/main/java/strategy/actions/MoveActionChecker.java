package strategy.actions;

import model.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 4:54 AM
 * To change this template use File | Settings | File Templates.
 */
public final class MoveActionChecker extends ActionChecker {

  public MoveActionChecker(Game game, World world)
  {
    super(game, world);
  }

  public boolean checkActionValidity(IActionParameters params, Trooper self)
  {
    boolean result = true;
    MoveActionParameters moveParams = (MoveActionParameters) params;
    if (countActionCost(self) > self.getActionPoints())
      return false;
    if (!CheckCellIsWithinBoundaries(moveParams.getX(), moveParams.getY()))
      return false;
    if (!CheckCellsAreNeighbours(moveParams.getX(), moveParams.getY(), self.getX(), self.getY()))
      return false;
    if (!CheckCellIsFree(moveParams.getX(), moveParams.getY()))
      return false;
    if (!CheckCellHasNoMen(moveParams.getX(), moveParams.getY()))
      return false;

    return result;
  }

  @Override
  public int countActionCost(Trooper self) {
    switch (self.getStance()) {
      case STANDING:
        return game.getStandingMoveCost();
      case KNEELING:
        return game.getKneelingMoveCost();
      case PRONE:
        return game.getProneMoveCost();
      default:
        assert(false);
        break;
    }
    return 0;
  }

  private boolean CheckCellHasNoMen(int x, int y) {
    Trooper[] troopers = world.getTroopers();
    for (Trooper tr : troopers)
      if (tr.getX() == x && tr.getY() == y)
        return false;
    return true;
  }

  private boolean CheckCellIsFree(int x, int y) {
    return (world.getCells()[x][y] == CellType.FREE);
  }

  private boolean CheckCellsAreNeighbours(int x, int y, int x1, int y1) {
    return Math.abs(x-x1) + Math.abs(y-y1) == 1;
  }

  private boolean CheckCellIsWithinBoundaries(int x, int y) {
    if (x < 0 || x >= world.getWidth())
      return false;
    if (y < 0 || y >= world.getHeight())
      return false;
    return true;
  }

}
