import model.World;

public class MapCell {
  private final int width;
  private final int height;
  private final int x;
  private final int y;

  public MapCell(int width, int height, int x, int y) {
    this.width = width;
    this.height = height;
    this.x = x;
    this.y = y;
  }

  int getY() {
    return y;
  }

  int getX() {
    return x;
  }

  boolean isAt(int x, int y) {
    return this.x == x && this.y == y;
  }

  boolean containsATrooper(TrooperModel trooper) {
    return isAt(trooper.getX(), trooper.getY());
  }

  public MapCell reflectX() {
    return new MapCell(width, height, x, height - 1 - y);
  }

  public MapCell reflectY() {
    return new MapCell(width, height, width - 1 - x, y);
  }

  public MapCell reflectXY() {
    return new MapCell(width, height, width - 1 - x, height - 1 - y);
  }

  @Override
  public String toString() {
    return "Cell (" + getX() + "," + getY() + ")";
  }
}
