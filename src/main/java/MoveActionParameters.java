import model.Direction;
import model.Trooper;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 4:44 AM
 * To change this template use File | Settings | File Templates.
 */
public final class MoveActionParameters extends DestinationActionParameters {
  private Direction direction;
  public MoveActionParameters(TrooperModel trooper, Direction direction) {
    super(trooper, direction);
    this.direction = direction;
  }

  Direction getDirection() {
    return direction;
  }

}
