import model.CellType;
import model.Unit;
import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 5:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class CellChecker {
  private final World world;

  public CellChecker(World world) {
    this.world = world;
  }

  public boolean cellIsWithinBoundaries(int x, int y) {
    if (x < 0 || x >= world.getWidth())
      return false;
    if (y < 0 || y >= world.getHeight())
      return false;
    return true;
  }

  public boolean cellIsFree(int x, int y) {
    return (world.getCells()[x][y] == CellType.FREE);
  }

  public boolean cellsAreTheSame(int x, int y, int x1, int y1) {
    return (x == x1) && (y == y1);
  }

  public boolean cellsAreNeighbours(int x, int y, int x1, int y1) {
    return Math.abs(x - x1) + Math.abs(y - y1) == 1;
  }

  protected boolean cellsAreNeighboursOrTheSame(int x, int y, int x1, int y1) {
    return cellsAreNeighbours(x, y, x1, y1) || cellsAreTheSame(x, y, x1, y1);
  }

  public boolean cellIsWithinRange(int x1, int y1, int x2, int y2, double range) {
    return UnitModel.getDistance(x1, y1, x2, y2) < range + 0.001;
  }
}
