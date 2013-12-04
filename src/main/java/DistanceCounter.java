import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/29/13
 * Time: 4:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class DistanceCounter {
  private final TimeCounter timeCounter = new TimeCounter();
  private final int width;
  private final int height;
  private CellChecker cellChecker;
  private PathFinder pathFinder;
  private int[][][][] distances;
  private boolean[][] isCounted;

  public DistanceCounter(World world, CellChecker cellChecker, PathFinder pathFinder) {
    this.cellChecker = cellChecker;
    this.pathFinder = pathFinder;
    width = world.getWidth();
    height = world.getHeight();
    init();
  }

  private void init() {
    timeCounter.reset();
    distances = new int[width][height][width][height];
    isCounted = new boolean[width][height];
//    countDistances();
    timeCounter.status("path counting time ");
  }

  public void fillGradually(int k, int l, int n) {
    assert k < n;
    assert l < n;

    for (int i = k * (width >> 1) / n; i < (k + 1) * (width >> 1) / n; ++i)
      for (int j = l * (height >> 1) / n; j < (l + 1) * (height >> 1) / n; ++j)
        if (cellChecker.cellIsFree(i, j)) {
          assert !isCounted[i][j];
          countPaths(i, j);
          isCounted[i][j] = true;
        }

  }

  public void countDistances() {
    for (int i = 0; i < width >> 1; ++i)
      for (int j = 0; j < height >> 1; ++j)
        if (cellChecker.cellIsFree(i, j)) {
          countPaths(i, j);
        }
  }

  public int getDistance(int x, int y, int x1, int y1) {
    if (x >= width >> 1) {
      x = width - 1 - x;
      x1 = width - 1 - x1;
    }
    if (y >= height >> 1) {
      y = height - 1 - y;
      y1 = height - 1 - y1;
    }
    assert (cellChecker.cellIsFree(x, y) && cellChecker.cellIsFree(x1, y1));
    if (!isCounted[x][y] && !isCounted[x1][y1])
      return Math.abs(x1 - x) + Math.abs(y1 - y);
    else if (isCounted[x][y])
      return distances[x][y][x1][y1];
    else
      return distances[x1][y1][x][y];
  }

  private void countPaths(int x, int y) {
    pathFinder.findDistances(x, y, distances[x][y]);
  }
}
