import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 4:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class RaiseStanceAction extends Action {
  public RaiseStanceAction(Environment env) {
    super(ActionType.RAISE_STANCE, new RaiseStanceActionChecker(env), env);
  }

  @Override
  protected int innerActSimulating(IActionParameters params, TrooperModel trooper) {
    trooper.raiseStance();
    return 0;
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel trooper, Move move) {
  }

  @Override
  public void innerUndoActSimulating(IActionParameters actionParameters, TrooperModel trooper) {
    trooper.lowStance();
  }
}
