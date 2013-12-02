import model.ActionType;
import model.Move;
import model.TrooperType;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 12/2/13
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommanderAction extends AbstractAction {

  protected CommanderAction(Environment environment) {
    super(ActionType.REQUEST_ENEMY_DISPOSITION, new ActionChecker(environment) {
      @Override
      public boolean checkActionValidity(IActionParameters params, TrooperModel trooper) {
        if (trooper.getType() != TrooperType.COMMANDER)
          return false;
        if (countActionCost(trooper) > trooper.getActionPoints())
          return false;

        return true;
      }

      @Override
      public int countActionCost(TrooperModel self) {
        return 10;
      }
    }, environment);
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
    move.setAction(ActionType.REQUEST_ENEMY_DISPOSITION);
  }
}
