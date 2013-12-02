import model.Direction;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 4:44 AM
 * To change this template use File | Settings | File Templates.
 */
public final class MoveActionParameters implements IActionParameters {
  private Direction direction;

  public MoveActionParameters(Direction direction) {
    assert(direction != Direction.CURRENT_POINT);
    this.direction = direction;
  }

  Direction getDirection() {
    return direction;
  }

  @Override
  public String toString() {
    return "dest " + direction;
  }
}
