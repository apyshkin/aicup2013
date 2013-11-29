/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 7:14 AM
 * To change this template use File | Settings | File Templates.
 */
public final class ThrowGrenadeActionParameters extends DestinationActionParameters {

  public ThrowGrenadeActionParameters(int x, int y) {
    super(x, y);
  }

  public String toString() {
    return "throw grenade AT " + super.toString();
  }
}
