import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 4:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class LowerStanceSimAction extends SimAction {
  public LowerStanceSimAction(Environment environment) {
    super(ActionType.LOWER_STANCE, new LowerStanceActionChecker(environment), environment);
  }

  @Override
  protected int innerAct(IActionParameters params, TrooperModel self) {
    self.lowStance();
    return 0;
  }

  @Override
  protected void innerActForReal(IActionParameters params, TrooperModel self, Move move) {
  }

  @Override
  public void reset(IActionParameters actionParameters, TrooperModel trooper) {
    trooper.setActionPoints(trooper.getActionPoints() + cost(trooper));
    trooper.raiseStance();
  }
}
