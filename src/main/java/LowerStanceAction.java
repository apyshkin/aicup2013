import model.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 5:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class LowerStanceAction extends Action {

  public LowerStanceAction(TrooperModel self, Environment environment) {
    super(ActionType.LOWER_STANCE, new LowerStanceActionChecker(environment), self, environment);
  }

  @Override
  protected void innerAct(IActionParameters params, Move move) {
  }
}

class LowerStanceActionChecker extends ActionChecker {
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
