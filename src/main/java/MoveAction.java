import model.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 4:00 AM
 * To change this template use File | Settings | File Templates.
 */
public class MoveAction extends Action {

  public MoveAction(Trooper self, Environment env)  {
    super(ActionType.MOVE, new MoveActionChecker(env), self, env);
  }

  @Override
  protected void innerAct(IActionParameters params, Move move) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    move.setX(moveParams.getX());
    move.setY(moveParams.getY());
  }
}
