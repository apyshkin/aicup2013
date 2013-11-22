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
  public MoveActionParameters(int x, int y) {
    super(x, y);
  }

  public MoveActionParameters(Trooper trooper, Direction direction) {
    super(trooper, direction);
  }
}
