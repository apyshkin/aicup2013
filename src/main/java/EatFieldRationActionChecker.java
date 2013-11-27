/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/26/13
 * Time: 6:42 AM
 * To change this template use File | Settings | File Templates.
 */
public class EatFieldRationActionChecker extends ActionChecker {
  public EatFieldRationActionChecker(Environment environment) {
    super(environment);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, TrooperModel trooper) {
    if (countActionCost(trooper) > trooper.getActionPoints())
      return false;
    if (!checkHasFieldRation(trooper))
      return false;
    if (checkActionPointsAreMax(trooper))
      return false;

    return true;
  }

  private boolean checkHasFieldRation(TrooperModel trooper) {
    return trooper.isHoldingFieldRation();
  }

  private boolean checkActionPointsAreMax(TrooperModel trooper) {
    return (trooper.getActionPoints() == trooper.getInitialActionPoints());
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return environment.getGame().getFieldRationEatCost();
  }
}
