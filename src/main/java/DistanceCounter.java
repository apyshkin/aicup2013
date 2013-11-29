import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/29/13
 * Time: 4:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class DistanceCounter {
  private final World world;
  private CellChecker cellChecker;
  private PathFinder pathFinder;
  private int[][][][] distances;

  public DistanceCounter(World world, CellChecker cellChecker, PathFinder pathFinder) {
    this.world = world;
    this.cellChecker = cellChecker;
    this.pathFinder = pathFinder;
    countDistances();
  }

  private void countDistances() {
    final int width = world.getWidth();
    final int height = world.getHeight();
    distances = new int[width][height][width][height];
    for (int i = 0; i < width >> 1; ++i)
      for (int j = 0; j < height >> 1; ++j)
        if (cellChecker.cellIsFree(i, j)) {
          pathFinder.findDistances(i, j, distances[i][j]);
        }
  }

  public int getDistance(int x, int y, int x1, int y1) {
    int n = world.getWidth();
    int m = world.getHeight();
    if (x >= n >> 1) {
      x = n - 1 - x;
      x1 = n - 1 - x1;
    }
    if (y >= m >> 1) {
      y = m - 1 - y;
      y1 = m - 1 - y1;
    }
    assert (cellChecker.cellIsFree(x, y) && cellChecker.cellIsFree(x1, y1));
    return distances[x][y][x1][y1];
  }
}
