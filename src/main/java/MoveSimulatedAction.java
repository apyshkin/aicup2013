import model.ActionType;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 4:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class MoveSimulatedAction extends SimulatedAction {

  public MoveSimulatedAction(Environment env) {
    super(ActionType.MOVE, new MoveActionChecker(env), env);
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel self) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    self.setX(moveParams.getX());
    self.setY(moveParams.getY());
  }

  @Override
  protected void innerActForReal(IActionParameters params, TrooperModel self, Move move) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    move.setX(moveParams.getX());
    move.setY(moveParams.getY());
  }
}
