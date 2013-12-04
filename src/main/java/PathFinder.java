import model.World;

import java.util.HashSet;
import java.util.PriorityQueue;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 5:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class PathFinder {
  private boolean[][] was;
  private int[][] distances;
  private final CellChecker cellChecker;
  private World world;

  private final boolean[][] noObstacles;
  private static final int MAX_CAPACITY = 30 * 20;
  private static final int[] deltaX = {-1, 0, 1, 0};
  private static final int[] deltaY = {0, 1, 0, -1};
  private IntQueue xQueue;
  private IntQueue yQueue;

  public PathFinder(World world, CellChecker cellChecker) {
    this.world = world;
    this.cellChecker = cellChecker;
    noObstacles = new boolean[world.getWidth()][world.getHeight()];
    init();
  }

  private void init() {
    xQueue = new IntQueue(MAX_CAPACITY);
    yQueue = new IntQueue(MAX_CAPACITY);
  }

  public void findDistances(int xStart, int yStart, int[][] distances) {
    findDistancesWithObstacles(xStart, yStart, distances, noObstacles, Utils.INFINITY);
  }

  public int[][] findDistances(int xStart, int yStart) {
    return findDistancesWithObstacles(xStart, yStart, noObstacles, Utils.INFINITY);
  }

  public int[][] findDistancesWithObstacles(int xStart, int yStart, boolean[][] obstacles, int maxRadius) {
    int[][] distances = initDistances(xStart, yStart);
    findDistancesWithObstacles(xStart, yStart, distances, obstacles, maxRadius);
    return distances;
  }

  public void findDistancesWithObstacles(int xStart, int yStart, int[][] distances, boolean[][] obstacles, int maxRadius) {
    boolean[][] was = initWas(xStart, yStart);
    xQueue.reset();
    yQueue.reset();
    xQueue.add(xStart);
    yQueue.add(yStart);

    while (!(xQueue.isEmpty() && yQueue.isEmpty())) {
      int cellX = xQueue.poll();
      int cellY = yQueue.poll();
      if (distances[cellX][cellY] >= maxRadius)
        continue;

      if (obstacles[cellX][cellY])
        continue;

      for (int i = 0; i < 4; ++i) {
        int dx = deltaX[i];
        int dy = deltaY[i];
        int neighCellX = cellX + dx;
        int neighCellY = cellY + dy;
        if (!cellChecker.cellIsWithinBoundaries(neighCellX, neighCellY))
          continue;

        if (!was[neighCellX][neighCellY] && cellChecker.cellIsFree(neighCellX, neighCellY)) {
          was[neighCellX][neighCellY] = true;
          distances[neighCellX][neighCellY] = distances[cellX][cellY] + 1;
          xQueue.add(neighCellX);
          yQueue.add(neighCellY);
        }
      }
    }
  }

  public int countDistanceToCellWithObstacles(int xStart, int yStart, int yEnd, int xEnd, boolean[][] obstacles,
                                              int maxRadius) {
    boolean[][] was = initWas(xStart, yStart);
    int[][] distances = initDistances(xStart, yStart);
    xQueue.reset();
    yQueue.reset();
    xQueue.add(xStart);
    yQueue.add(yStart);
    was[xStart][yStart] = true;

    while (!(xQueue.isEmpty() && yQueue.isEmpty())) {
      int cellX = xQueue.poll();
      int cellY = yQueue.poll();
      if (distances[cellX][cellY] >= maxRadius)
        continue;

      if (obstacles[cellX][cellY])
        continue;

      for (int i = 0; i < 4; ++i) {
        int dx = deltaX[i];
        int dy = deltaY[i];
        int neighCellX = cellX + dx;
        int neighCellY = cellY + dy;
        if (!cellChecker.cellIsWithinBoundaries(neighCellX, neighCellY))
          continue;

        if (cellChecker.cellIsFree(neighCellX, neighCellY) && !was[neighCellX][neighCellY]) {
          was[neighCellX][neighCellY] = true;
          distances[neighCellX][neighCellY] = distances[cellX][cellY] + 1;
          if (neighCellX == xEnd && neighCellY == yEnd)
            return distances[neighCellX][neighCellY];

          xQueue.add(neighCellX);
          yQueue.add(neighCellY);
        }
      }
    }
    return maxRadius + 1;
  }

  // bfs
  public int countDistanceToMarkedCellsWithObstacles(int xStart, int yStart, boolean[][] obstacles,
                                                     boolean[][] markedCells, int maxRadius) {
    int[][] distances = initDistances(xStart, yStart);
    boolean[][] was = initWas(xStart, yStart);
    xQueue.reset();
    yQueue.reset();
    xQueue.add(xStart);
    yQueue.add(yStart);
    was[xStart][yStart] = true;
    while (!(xQueue.isEmpty() && yQueue.isEmpty())) {
      int cellX = xQueue.poll();
      int cellY = yQueue.poll();
      if (distances[cellX][cellY] >= maxRadius)
        continue;

      if (obstacles[cellX][cellY])
        continue;

      for (int i = 0; i < 4; ++i) {
        int dx = deltaX[i];
        int dy = deltaY[i];
        int neighCellX = cellX + dx;
        int neighCellY = cellY + dy;
        if (!cellChecker.cellIsWithinBoundaries(neighCellX, neighCellY))
          continue;

        if (cellChecker.cellIsFree(neighCellX, neighCellY) && !was[neighCellX][neighCellY]) {
          was[neighCellX][neighCellY] = true;
          distances[neighCellX][neighCellY] = distances[cellX][cellY] + 1;

          if (markedCells[neighCellX][neighCellY])
            return distances[neighCellX][neighCellY];

          xQueue.add(neighCellX);
          yQueue.add(neighCellY);
        }
      }
    }
    return maxRadius + 1;
  }

  public int countDistanceToCellWithObstacles(int xStart, int yStart, int xEnd, int yEnd, int obstacleX, int obstacleY, int maxRadius) {
    int[][] distances = initDistances(xStart, yStart);
    boolean[][] was = initWas(xStart, yStart);
    xQueue.reset();
    yQueue.reset();
    xQueue.add(xStart);
    yQueue.add(yStart);
    was[xStart][yStart] = true;
    while (!(xQueue.isEmpty() && yQueue.isEmpty())) {
      int cellX = xQueue.poll();
      int cellY = yQueue.poll();
      if (distances[cellX][cellY] >= maxRadius)
        continue;

      if (obstacleX == cellX && obstacleY == cellY)
        continue;

      for (int i = 0; i < 4; ++i) {
        int dx = deltaX[i];
        int dy = deltaY[i];
        int neighCellX = cellX + dx;
        int neighCellY = cellY + dy;
        if (!cellChecker.cellIsWithinBoundaries(neighCellX, neighCellY))
          continue;

        if (cellChecker.cellIsFree(neighCellX, neighCellY) && !was[neighCellX][neighCellY]) {
          was[neighCellX][neighCellY] = true;
          distances[neighCellX][neighCellY] = distances[cellX][cellY] + 1;

          if (neighCellX == xEnd && neighCellY == yEnd)
            return distances[neighCellX][neighCellY];

          xQueue.add(neighCellX);
          yQueue.add(neighCellY);
        }
      }
    }
    return maxRadius + 1;
  }

  private int[][] initDistances(int x, int y) {
    if (distances == null)
      distances = new int[world.getWidth()][world.getHeight()];

    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j)
        distances[i][j] = 100;

    distances[x][y] = 0;

    return distances;
  }

  private boolean[][] initWas(int x, int y) {
    if (was == null)
      was = new boolean[world.getWidth()][world.getHeight()];
    else
      for (int i = 0; i < world.getWidth(); ++i)
        for (int j = 0; j < world.getHeight(); ++j)
          was[i][j] = false;

    was[x][y] = true;

    return was;
  }

}

class IntQueue {
  int[] queue;
  int l;
  int r;

  public IntQueue(int capacity) {
    init(capacity);
  }

  private void init(int capacity) {
    queue = new int[capacity];
    l = r = 0;
  }

  public int poll() {
    assert !isEmpty();
    return queue[l++];
  }

  public void add(int elem) {
    assert r < queue.length;
    queue[r++] = elem;
  }

  public boolean isEmpty() {
    return l == r;
  }

  public void reset() {
    l = r = 0;
  }
}
