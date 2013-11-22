import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 4:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class LowerStanceSimulatedAction extends SimulatedAction {
  public LowerStanceSimulatedAction(TrooperModel self, Environment environment) {
    super(ActionType.LOWER_STANCE, new LowerStanceActionChecker(environment), self, environment);
  }

  @Override
  protected void innerAct(IActionParameters params) {
    self.lowStance();
  }

  @Override
  protected void innerActForReal(IActionParameters params, Move move) {
  }
}
