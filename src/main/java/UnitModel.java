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

  public static double getDistance(int x1, int y1, int x2, int y2) {
    return hypot(x2 - x1, y2 - y1);
  }

  public double getDistanceTo(UnitModel unit) {
    return getDistanceTo(unit.x, unit.y);
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }
}
