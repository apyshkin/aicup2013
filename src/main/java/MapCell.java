import model.World;

public class MapCell {
  private World world;
  int x;
  int y;

  private int getWidth() {
    return world.getWidth();
  }

  private int getHeight() {
    return world.getHeight();
  }

  public MapCell(World world, int x, int y) {
    this.world = world;
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
    return new MapCell(world, x, getHeight() - 1 - y);
  }

  public MapCell reflectY() {
    return new MapCell(world, getWidth() - 1 - x, y);
  }

  public MapCell reflectXY() {
    return new MapCell(world, getWidth() - 1 - x, getHeight() - 1 - y);
  }

  @Override
  public String toString() {
    return "Cell (" + getX() + "," + getY() + ")";
  }
}
