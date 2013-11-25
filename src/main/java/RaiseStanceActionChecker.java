import model.*;

public class RaiseStanceActionChecker extends ActionChecker {
  public RaiseStanceActionChecker(Environment environment) {
    super(environment);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, TrooperModel trooper) {
    if (countActionCost(trooper) > trooper.getActionPoints())
      return false;
    if (trooper.getStance() == TrooperStance.STANDING)
      return false;

    return true;
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return environment.getGame().getStanceChangeCost();
  }
}

