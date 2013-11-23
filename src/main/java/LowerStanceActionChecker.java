import model.*;

public class LowerStanceActionChecker extends ActionChecker {
  public LowerStanceActionChecker(Environment environment) {
    super(environment);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, TrooperModel self) {
    if (countActionCost(self) > self.getActionPoints())
      return false;
    if (self.getStance() == TrooperStance.PRONE)
      return false;

    return true;
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return environment.getGame().getStanceChangeCost();
  }
}
