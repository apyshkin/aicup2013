import model.*;

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
  private CellChecker cellChecker;
  private PathFinder pathFinder;
  private ArrayList<TrooperModel> visibleTroopers;
  private ArrayList<Bonus> bonuses;
  private Cell[][] cells;
  private int[][][][] distances;
  private byte[][][][][] visibilityFrom; // a player from (i, j) in a standing stance can see a player in (x, y) in a stance z
  private int[][][] exposure;


  public BattleMap(World world, CellChecker cellChecker) {
    this.world = world;
    this.cellChecker = cellChecker;
    this.pathFinder = new PathFinder(world, cellChecker);
    init();
  }

  private void init() {
    cells = new Cell[world.getWidth()][world.getHeight()];
    CellType[][] worldCells = world.getCells();
    for (int i = 0; i < worldCells.length; ++i)
      for (int j = 0; j < worldCells[0].length; ++j)
        cells[i][j] = new Cell(worldCells[i][j], 0);

    countDistances();
    countVisibilities();
  }

  private void countVisibilities() {
    int n = world.getWidth();
    int m = world.getHeight();
    visibilityFrom = new byte[n][m][n][m][3];
    exposure = new int[n][m][3];
    for (int i = 0; i < n >> 1; ++i)
      for (int j = 0; j < m >> 1; ++j)
        if (cellChecker.cellIsFree(i, j))
          for (int x = 0; x < n; ++x)
            for (int y = 0; y < m; ++y)
              if (cellChecker.cellIsFree(x, y)) {
                visibilityFrom[i][j][x][y][0] = world.isVisible(7, i, j, TrooperStance.STANDING,
                        x, y, TrooperStance.PRONE) ? (byte) 1 : (byte) 0;
                visibilityFrom[i][j][x][y][1] = world.isVisible(7, i, j, TrooperStance.STANDING,
                        x, y, TrooperStance.KNEELING) ? (byte) 1 : (byte) 0;
                visibilityFrom[i][j][x][y][2] = world.isVisible(7, i, j, TrooperStance.STANDING,
                        x, y, TrooperStance.STANDING) ? (byte) 1 : (byte) 0;
                exposure[x][y][0] += visibilityFrom[i][j][x][y][0];
                exposure[x][y][1] += visibilityFrom[i][j][x][y][1];
                exposure[x][y][2] += visibilityFrom[i][j][x][y][2];
              }


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

  public boolean isVisibleFrom(int x, int y, TrooperModel trooper) {
    int n = world.getWidth();
    int m = world.getHeight();
    int x1 = trooper.getX();
    int y1 = trooper.getY();
    if (x >= n >> 1) {
      x = n - 1 - x;
      x1 = n - 1 - x1;
    }
    if (y >= m >> 1) {
      y = m - 1 - y;
      y1 = m - 1 - y1;
    }
    return visibilityFrom[x][y][x1][y1][trooper.getStance().ordinal()] == 1;
  }

  public int getExposure(TrooperModel trooper, int x, int y) {
    if (x >= world.getWidth() >> 1)
      x = world.getWidth() - 1 - x;
    if (y >= world.getHeight() >> 1)
      y = world.getHeight() - 1 - y;
    return exposure[x][y][trooper.getStance().ordinal()];
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

  public PathFinder getPathFinder() {
    return pathFinder;
  }

  public void update(World world) {

  }

}

class Cell {
  private CellType cellType;
  private TrooperModel trooper = null;
  private Bonus bonus = null;
  private int timeOfLastVisit;

  public Cell(CellType cellType, int timeOfLastVisit) {
    this.cellType = cellType;
    this.timeOfLastVisit = timeOfLastVisit;
  }

  int getTimeOfLastVisit() {
    return timeOfLastVisit;
  }

  public TrooperModel getTrooper() {
    return trooper;
  }

  void setTrooper(TrooperModel trooper) {
    this.trooper = trooper;
  }

  public Bonus getBonus() {
    return bonus;
  }

  void setBonus(Bonus bonus) {
    this.bonus = bonus;
  }

  public void update(int time) {
    assert (time > timeOfLastVisit);
    timeOfLastVisit = time;
  }
}
