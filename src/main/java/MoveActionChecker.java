/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 4:54 AM
 * To change this template use File | Settings | File Templates.
 */
public final class MoveActionChecker extends ActionChecker {

  public MoveActionChecker(Environment env)
  {
    super(env);
  }

  public boolean checkActionValidity(IActionParameters params, TrooperModel trooper)
  {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    if (countActionCost(trooper) > trooper.getActionPoints())
      return false;
    if (!checkCellIsWithinBoundaries(moveParams.getX(), moveParams.getY()))
      return false;
    if (!checkCellsAreNeighbours(moveParams.getX(), moveParams.getY(), trooper.getX(), trooper.getY()))
      return false;
    if (!checkCellIsFree(moveParams.getX(), moveParams.getY()))
      return false;
    if (!checkCellHasNoMen(moveParams.getX(), moveParams.getY()))
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
        assert(false);
        break;
    }
    return 0;
  }


}
