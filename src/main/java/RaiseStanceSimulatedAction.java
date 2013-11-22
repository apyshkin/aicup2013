import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 4:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class RaiseStanceSimulatedAction extends SimulatedAction {
  public RaiseStanceSimulatedAction(TrooperModel self, Environment env) {
    super(ActionType.RAISE_STANCE, new RaiseStanceActionChecker(env), self, env);
  }

  @Override
  protected void innerAct(IActionParameters params) {
    self.raiseStance();
  }

  @Override
  protected void innerActForReal(IActionParameters params, Move move) {
  }
}
