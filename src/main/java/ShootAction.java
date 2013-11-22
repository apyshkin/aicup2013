import model.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 7:12 AM
 * To change this template use File | Settings | File Templates.
 */
public class ShootAction extends Action {
  public ShootAction(TrooperModel self, Environment environment) {
    super(ActionType.SHOOT, new ShootActionChecker(environment), self, environment);
  }

  @Override
  protected void innerAct(IActionParameters params, Move move) {
    ShootActionParameters shootParams = (ShootActionParameters) params;
    move.setX(shootParams.getX());
    move.setY(shootParams.getY());
  }
}
