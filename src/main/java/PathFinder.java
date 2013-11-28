import model.World;

import java.util.ArrayList;
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

  private static boolean[][] noObstacles;

  public void findDistances(int xStart, int yStart, int[][] tableToFill) {
    findDistancesWithObstacles(xStart, yStart, tableToFill, noObstacles, Utils.INFINITY);
  }

  public void findDistancesWithObstacles(int xStart, int yStart, int[][] tableToFill,
                                                      boolean[][] obstacles, int maxRadius) {
    boolean[][] was = initWas();
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
      if (tableToFill[cellX][cellY] >= maxRadius)
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
          tableToFill[neighCellX][neighCellY] = tableToFill[cellX][cellY] + 1;
          xQueue.add(neighCellX);
          yQueue.add(neighCellY);
        }
      }
    }
  }

  public int countDistanceToCellWithObstacles(int xStart, int yStart, boolean[][] obstacles,
                                              int xEnd, int yEnd, int maxRadius) {
    boolean[][] was = initWas();
    int[][] distances = initDistances();
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
    int[][] distances = initDistances();
    boolean[][] was = initWas();
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

  private int[][] initDistances() {
    if (distances == null)
      distances = new int[world.getWidth()][world.getHeight()];
    else
      for (int i = 0; i < world.getWidth(); ++i)
        for (int j = 0; j < world.getHeight(); ++j)
          distances[i][j] = 0;

    return distances;
  }

  private boolean[][] initWas() {
    if (was == null)
      was = new boolean[world.getWidth()][world.getHeight()];
    else
      for (int i = 0; i < world.getWidth(); ++i)
        for (int j = 0; j < world.getHeight(); ++j)
          was[i][j] = false;

    return was;
  }
}
