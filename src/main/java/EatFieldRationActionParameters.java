import model.Direction;

public class EatFieldRationActionParameters extends DestinationActionParameters {

  private TrooperModel trooper;

  public EatFieldRationActionParameters(TrooperModel trooper) {
    super(trooper, Direction.CURRENT_POINT);
    this.trooper = trooper;
  }

  @Override
  public int getX() {
    return trooper.getX();
  }

  @Override
  public int getY() {
    return trooper.getY();
  }

  @Override
  public String toString() {
    return this.trooper + " eating field ration at " + getX() + " " + getY();
  }
}
