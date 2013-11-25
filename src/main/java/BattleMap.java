import model.CellType;
import model.World;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class BattleMap {
  private World world;
  private Cell[][] cells;
  private int[][][][] distances;


  public BattleMap(World world) {
    this.world = world;
    init();
  }

  private void init() {
    cells = new Cell[world.getWidth()][world.getHeight()];
    CellType[][] worldCells = world.getCells();
    for (int i = 0; i < worldCells.length; ++i)
      for (int j = 0; j < worldCells[0].length; ++j)
        cells[i][j] = new Cell(worldCells[i][j], 0);

    countDistances();
  }

  private void countDistances() {
    distances = new int[world.getWidth()][world.getHeight()][world.getWidth()][world.getHeight()];
    for (int i = 0; i < world.getWidth() >> 1; ++i)
      for (int j = 0; j < world.getHeight() >> 1; ++j)
        if (cellIsFree(i, j)) {
          findShortestDistances(i, j);
        }
  }

  // bfs
  private void findShortestDistances(int x, int y) {
    boolean was[][] = new boolean[world.getWidth()][world.getHeight()];
    Queue<Integer> xQueue = new LinkedList<>();
    Queue<Integer> yQueue = new LinkedList<>();
    xQueue.add(x);
    yQueue.add(y);
    was[x][y] = true;
    final int[] deltaX = {-1, 0, 1,  0};
    final int[] deltaY = { 0, 1, 0, -1};

    while (!(xQueue.isEmpty() && yQueue.isEmpty())) {
      int cellX = xQueue.poll();
      int cellY = yQueue.poll();
      for (int i = 0; i < 4; ++i) {
        int dx = deltaX[i];
        int dy = deltaY[i];
        int neighCellX = cellX + dx;
        int neighCellY = cellY + dy;
        if (!cellIsWithinBoundaries(neighCellX, neighCellY))
          continue;

        if (cellIsFree(neighCellX, neighCellY) && !was[neighCellX][neighCellY]) {
          was[neighCellX][neighCellY] = true;
          updateDistance(x, y, neighCellX, neighCellY, distances[x][y][cellX][cellY] + 1);
          xQueue.add(neighCellX);
          yQueue.add(neighCellY);
        }
      }
    }
  }

  private void updateDistance(int x, int y, int x1, int y1, int distance) {
    int width = world.getWidth();
    int height = world.getHeight();
    distances[x][height - 1 - y][x1][height - 1 - y1] = distance;
    distances[width - 1 - x][y][width - 1 - x1][y1] = distance;
    distances[width - 1 - x][height - 1 - y][width - 1 - x1][height - 1 -y1] = distance;
  }

  public boolean cellIsFree(int x, int y) {
    return (world.getCells()[x][y] == CellType.FREE);
  }

  public boolean cellIsWithinBoundaries(int x, int y) {
    if (x < 0 || x >= world.getWidth())
      return false;
    if (y < 0 || y >= world.getHeight())
      return false;
    return true;
  }

  public void visitCell(int x, int y, int time) {
    cells[x][y].update(time);
  }

  public Cell getCell(int x, int y) {
    return cells[x][y];
  }

  public int getDistance(TrooperModel trooper, TrooperModel trooper1) {
    return getDistance(trooper.getX(), trooper.getY(), trooper1.getX(), trooper1.getY());
  }

  public int getDistance(int x, int y, int x1, int y1) {
    return distances[x][y][x1][y1];
  }
}

class Cell {
  CellType cellType;
  int timeOfLastVisit;

  public Cell(CellType cellType, int timeOfLastVisit) {
    this.cellType = cellType;
    this.timeOfLastVisit = timeOfLastVisit;
  }

  public void update(int time) {
    assert(time > timeOfLastVisit);
    timeOfLastVisit = time;
  }
}
