import model.*;

class RaiseStanceActionChecker extends ActionChecker {
  public RaiseStanceActionChecker(Environment environment) {
    super(environment);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, TrooperModel self) {
    if (countActionCost(self) > self.getActionPoints())
      return false;
    if (self.getStance() == TrooperStance.STANDING)
      return false;

    return true;
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return environment.getGame().getStanceChangeCost();
  }
}

