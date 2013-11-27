import model.World;

import java.util.ArrayList;
import java.util.Arrays;
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
  static boolean[][] was;
  private final CellChecker cellChecker;
  private World world;

  public PathFinder(World world, CellChecker cellChecker) {
    this.world = world;
    this.cellChecker = cellChecker;
  }

  public int[][] findShortestDistances(int x, int y) {
    return findShortestDistances(x, y, new ArrayList<MapCell>(), Utils.INFINITY);
  }

  // bfs
  public int[][] findShortestDistances(int x, int y, ArrayList<MapCell> trooperCells, int maxRadius) {
    boolean[][] was = initWas();

    int[][] distances = new int[world.getWidth()][world.getHeight()];
    Queue<Integer> xQueue = new LinkedList<>();
    Queue<Integer> yQueue = new LinkedList<>();
    xQueue.add(x);
    yQueue.add(y);
    was[x][y] = true;
    final int[] deltaX = {-1, 0, 1, 0};
    final int[] deltaY = {0, 1, 0, -1};

    while (!(xQueue.isEmpty() && yQueue.isEmpty())) {
      int cellX = xQueue.poll();
      int cellY = yQueue.poll();
      if (distances[cellX][cellY] > maxRadius)
        continue;

      if (checkCellHasTrooper(cellX, cellY, trooperCells))
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
    return distances;
  }

  private boolean checkCellHasTrooper(int cellX, int cellY, ArrayList<MapCell> trooperCells) {
    for (MapCell cell : trooperCells)
      if (cellX == cell.getX() && cellY == cell.getY())
        return true;

    return false;
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
