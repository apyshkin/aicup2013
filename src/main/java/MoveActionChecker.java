import model.Direction;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 4:54 AM
 * To change this template use File | Settings | File Templates.
 */
public final class MoveActionChecker extends ActionChecker {

  public MoveActionChecker(Environment env) {
    super(env);
  }

  public boolean checkActionValidity(IActionParameters params, TrooperModel trooper) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    Direction direction = moveParams.getDirection();
    int x = trooper.getX() + direction.getOffsetX();
    int y = trooper.getY() + direction.getOffsetY();
    if (countActionCost(trooper) > trooper.getActionPoints())
      return false;
    if (!checkCellIsWithinBoundaries(x, y))
      return false;
    if (!checkCellsAreNeighbours(x, y, trooper.getX(), trooper.getY()))
      return false;
    if (!checkCellIsFree(x, y))
      return false;
    if (!checkCellHasNoMen(x, y))
      return false;

    return true;
  }

  @Override
  public int countActionCost(TrooperModel self) {
    switch (self.getStance()) {
      case STANDING:
        return environment.getGame().getStandingMoveCost();
      case KNEELING:
        return environment.getGame().getKneelingMoveCost();
      case PRONE:
        return environment.getGame().getProneMoveCost();
      default:
        assert (false);
        break;
    }
    return 0;
  }


}
