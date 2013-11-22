import static java.lang.StrictMath.hypot;

public abstract class UnitModel {
  private long id;
  private int x;
  private int y;

  protected UnitModel(long id, int x, int y) {
    this.id = id;
    this.x = x;
    this.y = y;
  }

  public final long getId() {
    return id;
  }

  public final int getX() {
    return x;
  }

  public final int getY() {
    return y;
  }

  public double getDistanceTo(int x, int y) {
    return hypot(x - this.x, y - this.y);
  }

  public double getDistanceTo(UnitModel unit) {
    return getDistanceTo(unit.x, unit.y);
  }
}
