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
public class MoveSimAction extends SimAction {

  public MoveSimAction(Environment env) {
    super(ActionType.MOVE, new MoveActionChecker(env), env);
  }

  @Override
  protected int innerAct(IActionParameters params, TrooperModel self) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    self.setX(moveParams.getX());
    self.setY(moveParams.getY());
    return 0;
  }

  @Override
  protected void innerActForReal(IActionParameters params, TrooperModel self, Move move) {
    MoveActionParameters moveParams = (MoveActionParameters) params;
    final int x = moveParams.getX();
    final int y = moveParams.getY();
    move.setX(x);
    move.setY(y);
    environment.visitCell(x, y);
  }

  @Override
  public void reset(IActionParameters params, TrooperModel trooper) {
    trooper.setActionPoints(trooper.getActionPoints() + cost(trooper));
    MoveActionParameters moveParams = (MoveActionParameters) params;
    Direction direction = moveParams.getDirection();
    trooper.setX(moveParams.getX() - direction.getOffsetX());
    trooper.setY(moveParams.getY() - direction.getOffsetY());
  }
}
