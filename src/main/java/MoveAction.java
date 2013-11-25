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
public class MoveAction extends Action {

  public MoveAction(Environment env) {
    super(ActionType.MOVE, new MoveActionChecker(env), env);
  }

  @Override
  protected int innerActSimulating(IActionParameters params, TrooperModel trooper) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    trooper.setX(moveParams.getX());
    trooper.setY(moveParams.getY());
    return 0;
  }

  @Override
  protected void innerAct(IActionParameters params, TrooperModel trooper, Move move) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    final int x = moveParams.getX();
    final int y = moveParams.getY();
    move.setX(x);
    move.setY(y);
    environment.visitCell(x, y);
  }

  @Override
  public void innerUndoActSimulating(IActionParameters params, TrooperModel trooper) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    Direction direction = moveParams.getDirection();
    trooper.setX(trooper.getX() - direction.getOffsetX());
    trooper.setY(trooper.getY() - direction.getOffsetY());
  }
}
