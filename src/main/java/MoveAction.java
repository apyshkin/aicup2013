import model.ActionType;
import model.Direction;
import model.Move;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 4:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class MoveAction extends AbstractAction {

  public MoveAction(Environment env) {
    super(ActionType.MOVE, new MoveActionChecker(env), env);
  }

  @Override
  protected int innerActSimulating(IActionParameters params, TrooperModel trooper) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    trooper.setX(trooper.getX() + moveParams.getDirection().getOffsetX());
    trooper.setY(trooper.getY() + moveParams.getDirection().getOffsetY());
    return 0;
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel trooper, Move move) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    final int x = moveParams.getDirection().getOffsetX() + trooper.getX();
    final int y = moveParams.getDirection().getOffsetY() + trooper.getY();
    move.setX(x);
    move.setY(y);
  }

  @Override
  public void innerUndoActSimulating(IActionParameters params, TrooperModel trooper) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    Direction direction = moveParams.getDirection();
    trooper.setX(trooper.getX() - direction.getOffsetX());
    trooper.setY(trooper.getY() - direction.getOffsetY());
  }
}
