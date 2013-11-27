import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/26/13
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class EndTurnAction extends Action {
  protected EndTurnAction(Environment environment) {
    super(ActionType.END_TURN, new EndTurnActionChecker(environment), environment);
  }

  @Override
  protected int innerActSimulating(IActionParameters params, TrooperModel trooper) {
    assert false;
    return 0;
  }

  @Override
  protected void innerUndoActSimulating(IActionParameters actionParameters, TrooperModel trooper) {
    assert false;
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel trooper, Move move) {
  }
}

class EndTurnActionChecker extends ActionChecker {

  public EndTurnActionChecker(Environment environment) {
    super(environment);
  }

  @Override
  public boolean checkActionValidity(IActionParameters params, TrooperModel trooper) {
    return trooper.getActionPoints() <= 2;
  }

  @Override
  public int countActionCost(TrooperModel self) {
    return 0;
  }
}
