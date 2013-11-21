package strategy.actions;

import model.Direction;
import model.Trooper;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 7:16 AM
 * To change this template use File | Settings | File Templates.
 */
public class DestinationActionParameters implements IActionParameters {

  private int x;
  private int y;

  public DestinationActionParameters(int x, int y)
  {
    setX(x);
    setY(y);
  }

  public DestinationActionParameters(Trooper trooper, Direction direction)
  {
    setX(trooper.getX() + direction.getOffsetX());
    setY(trooper.getY() + direction.getOffsetY());
  }

  public int getX()
  {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
}
