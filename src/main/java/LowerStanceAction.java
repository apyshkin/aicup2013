import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 4:58 AM
 * To change this template use File | Settings | File Templates.
 */
public class LowerStanceAction extends Action {
  public LowerStanceAction(Environment environment) {
    super(ActionType.LOWER_STANCE, new LowerStanceActionChecker(environment), environment);
  }

  @Override
  protected int innerActSimulating(IActionParameters params, TrooperModel trooper) {
    trooper.lowStance();
    return 0;
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel trooper, Move move) {
  }

  @Override
  public void innerUndoActSimulating(IActionParameters actionParameters, TrooperModel trooper) {
    trooper.raiseStance();
  }
}
