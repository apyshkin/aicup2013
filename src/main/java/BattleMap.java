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
  private final DistanceCounter distanceCounter;
  private final VisibilityManager visibilityManager;
  private Cell[][] cells;

  private ArrayList<TrooperModel> visibleTroopers;
  private ArrayList<TrooperModel> enemies;


  public BattleMap(World world, CellChecker cellChecker) {
    this.world = world;
    this.cellChecker = cellChecker;
    this.pathFinder = new PathFinder(world, cellChecker);
    this.distanceCounter = new DistanceCounter(world, cellChecker, pathFinder);
    this.visibilityManager = new VisibilityManager(world, cellChecker);
    init();
  }

  private void init() {
    initCellArray();
    visibleTroopers = new ArrayList<>();
    enemies = new ArrayList<>();
  }

  private void initCellArray() {
    cells = new Cell[world.getWidth()][world.getHeight()];
    CellType[][] worldCells = world.getCells();
    for (int i = 0; i < worldCells.length; ++i)
      for (int j = 0; j < worldCells[0].length; ++j)
        cells[i][j] = new Cell(worldCells[i][j], 0);
  }


  public boolean isVisibleFrom(int x, int y, TrooperModel trooper) {
    return visibilityManager.isVisibleFrom(x, y, trooper);
  }

  public int getExposure(TrooperModel trooper, int x, int y) {
    return visibilityManager.getExposure(trooper, x, y);
  }

  public void visitCell(int x, int y, int time) {
    cells[x][y].update(time);
  }

  public Cell getCell(int x, int y) {
    return cells[x][y];
  }

  public int getDistance(int x, int y, int x1, int y1) {
    return distanceCounter.getDistance(x, y, x1, y1);
  }

  public void update(World world) {
    resetVisibleArea(world);
    updateBonuses(world);
    updateVisibleTroopers(world);
  }

  private void updateVisibleTroopers(World world) {
    ArrayList<TrooperModel> trooperToRemove = new ArrayList<>();
    ArrayList<TrooperModel> trooperToInsert = new ArrayList<>();
    for (Trooper trooper : world.getTroopers()) {
      TrooperModel newTrooper = new TrooperModel(trooper);
      for (TrooperModel oldTrooper : visibleTroopers)
        if (oldTrooper.getPlayerId() == newTrooper.getPlayerId() && oldTrooper.getType() == newTrooper.getType()) {
          trooperToRemove.add(oldTrooper);
        }
      trooperToInsert.add(newTrooper);
    }
    for (TrooperModel trooper : trooperToRemove)
      visibleTroopers.remove(trooper);

    for (TrooperModel trooper : trooperToInsert)
      visibleTroopers.add(trooper);
    ArrayList<TrooperModel> myTroopers = new ArrayList<>();
    for (TrooperModel trooper : visibleTroopers)
      if (trooper.isTeammate())
        myTroopers.add(trooper);

    trooperToRemove.clear();

    for (TrooperModel oldTrooper : visibleTroopers) {
        boolean canSeeItIndeed = false;
        boolean mustBeVisibleNow = checkVisibility(myTroopers, oldTrooper);
        if (mustBeVisibleNow) {
          for (Trooper trooper : world.getTroopers()) {
            if (trooper.getPlayerId() == oldTrooper.getPlayerId() && trooper.getType() == oldTrooper.getType()) {
              canSeeItIndeed = true;
            }
          }
          if (!canSeeItIndeed)
            trooperToRemove.add(oldTrooper);
        }
    }
      for (TrooperModel trooper : trooperToRemove)
        visibleTroopers.remove(trooper);

      enemies = null;
  }

  private boolean checkVisibility(ArrayList<TrooperModel> myTroopers, TrooperModel oldTrooper) {
    if (oldTrooper.isTeammate())
      return true;
    for (TrooperModel myTrooper : myTroopers)
      if (world.isVisible(myTrooper.getVisionRange(), myTrooper.getX(), myTrooper.getY(), myTrooper.getStance(),
              oldTrooper.getX(), oldTrooper.getY(), oldTrooper.getStance()))
        return true;

    return false;
  }

  private void updateBonuses(World world) {
    for (Bonus bonus : world.getBonuses()) {
      int x = bonus.getX();
      int y = bonus.getY();
      cells[x][y].setBonus(bonus);
    }
  }

  private void resetVisibleArea(World world) {
    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j)
        if (world.getCells()[i][j] == CellType.FREE) {
          for (Trooper trooper : world.getTroopers())
            if (trooper.isTeammate()) {
              for (int stance = 0; stance < 3; ++stance) {
                if (world.isVisible(trooper.getVisionRange(), trooper.getX(), trooper.getY(), trooper.getStance(),
                        i, j, Utils.getStance(stance))) {
                  cells[i][j].setBonus(null);
                }
              }
            }
        }
  }

  public boolean hasBonus(int x, int y) {
    return cells[x][y].hasBonus();
  }

  public ArrayList<TrooperModel> getVisibleTroopers() {
    return visibleTroopers;
  }

  public ArrayList<TrooperModel> getEnemies() {
    if (visibleTroopers == null)
      getVisibleTroopers();

    if (enemies != null)
      return enemies;

    enemies = new ArrayList<>();
    for (TrooperModel trooper : visibleTroopers)
      if (!trooper.isTeammate())
        enemies.add(trooper);
    return enemies;
  }

  public void putEnemy(TrooperModel enemy) {
    assert (!enemy.isTeammate());
    visibleTroopers.add(enemy);
  }

  public PathFinder getPathFinder() {
    return pathFinder;
  }
}

class Cell {
  private CellType cellType;
//  private TrooperModel trooper = null;
  private Bonus bonus = null;
  private int timeOfLastVisit;

  public Cell(CellType cellType, int timeOfLastVisit) {
    this.cellType = cellType;
    this.timeOfLastVisit = timeOfLastVisit;
  }

  int getTimeOfLastVisit() {
    return timeOfLastVisit;
  }

//  public TrooperModel getTrooper() {
//    return trooper;
//  }
//
//  void setTrooper(TrooperModel trooper) {
//    this.trooper = trooper;
//  }

  public boolean hasBonus() {
    return bonus != null;
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
