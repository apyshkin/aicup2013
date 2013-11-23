import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 4:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class RaiseStanceSimAction extends SimAction {
  public RaiseStanceSimAction(Environment env) {
    super(ActionType.RAISE_STANCE, new RaiseStanceActionChecker(env), env);
  }

  @Override
  protected int innerAct(IActionParameters params, TrooperModel self) {
    self.raiseStance();
    return 0;
  }

  @Override
  protected void innerActForReal(IActionParameters params, TrooperModel self, Move move) {
  }

  @Override
  public void reset(IActionParameters actionParameters, TrooperModel trooper) {
    trooper.setActionPoints(trooper.getActionPoints() + cost(trooper));
    trooper.lowStance();
  }
}
