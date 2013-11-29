import model.World;

import java.util.LinkedList;
import java.util.Queue;

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

  public PathFinder(World world, CellChecker cellChecker) {
    this.world = world;
    this.cellChecker = cellChecker;
    noObstacles = new boolean[world.getWidth()][world.getHeight()];
  }

  private final boolean[][] noObstacles;

  public void findDistances(int xStart, int yStart, int[][] distances) {
    findDistancesWithObstacles(xStart, yStart, Utils.INFINITY, noObstacles, distances);
  }

  public int[][] findDistances(int xStart, int yStart) {
    return findDistancesWithObstacles(xStart, yStart, Utils.INFINITY, noObstacles);
  }

  public void findDistancesWithObstacles(int xStart, int yStart, int maxRadius, boolean[][] obstacles, int[][] distances) {
    boolean[][] was = initWas(xStart, yStart);
    Queue<Integer> xQueue = new LinkedList<>();
    Queue<Integer> yQueue = new LinkedList<>();
    xQueue.add(xStart);
    yQueue.add(yStart);
    was[xStart][yStart] = true;
    final int[] deltaX = {-1, 0, 1, 0};
    final int[] deltaY = {0, 1, 0, -1};

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
          xQueue.add(neighCellX);
          yQueue.add(neighCellY);
        }
      }
    }
  }

  public int[][] findDistancesWithObstacles(int xStart, int yStart, int maxRadius, boolean[][] obstacles) {
    int[][] distances = initDistances(xStart, yStart);
    findDistancesWithObstacles(xStart, yStart, maxRadius, obstacles, distances);
    return distances;
  }

  public int countDistanceToCellWithObstacles(int xStart, int yStart, boolean[][] obstacles,
                                              int xEnd, int yEnd, int maxRadius) {
    boolean[][] was = initWas(xStart, yStart);
    int[][] distances = initDistances(xStart, yStart);
    Queue<Integer> xQueue = new LinkedList<>();
    Queue<Integer> yQueue = new LinkedList<>();
    xQueue.add(xStart);
    yQueue.add(yStart);
    was[xStart][yStart] = true;
    final int[] deltaX = {-1, 0, 1, 0};
    final int[] deltaY = {0, 1, 0, -1};

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
    Queue<Integer> xQueue = new LinkedList<>();
    Queue<Integer> yQueue = new LinkedList<>();
    xQueue.add(xStart);
    yQueue.add(yStart);
    was[xStart][yStart] = true;
    final int[] deltaX = {-1, 0, 1, 0};
    final int[] deltaY = {0, 1, 0, -1};

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

  public int countDistanceToCellWithObstacles(int xStart, int yStart, int xEnd, int yEnd, int obstacleX, int obstacleY, int maxRadius) {
    int[][] distances = initDistances(xStart, yStart);
    boolean[][] was = initWas(xStart, yStart);
    Queue<Integer> xQueue = new LinkedList<>();
    Queue<Integer> yQueue = new LinkedList<>();
    xQueue.add(xStart);
    yQueue.add(yStart);
    was[xStart][yStart] = true;
    final int[] deltaX = {-1, 0, 1, 0};
    final int[] deltaY = {0, 1, 0, -1};

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
}
