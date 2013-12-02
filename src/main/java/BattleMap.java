import model.*;

import java.util.*;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/22/13
 * Time: 9:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class BattleMap {
  private final static Logger logger = Logger.getLogger(BattleMap.class.getName());
  final VisibilityManager visibilityManager;
  private final int width;
  private final int height;
  private final Game game;
  private CellChecker cellChecker;
  private PathFinder pathFinder;
  private DistanceCounter distanceCounter;
  private Cell[][][] cells;
  private ArrayList<TrooperModel> visibleTroopers;
  private ArrayList<TrooperModel> enemies;

  public BattleMap(World world, Game game, CellChecker cellChecker) {
    this.game = game;
    width = world.getWidth();
    height = world.getHeight();
    this.cellChecker = cellChecker;
    this.pathFinder = new PathFinder(world, cellChecker);
    this.distanceCounter = new DistanceCounter(world, cellChecker, pathFinder);
    this.visibilityManager = new VisibilityManager(world, cellChecker);
    init(world);
  }

  public CellChecker getCellChecker() {
    return cellChecker;
  }

  public DistanceCounter getDistanceCounter() {
    return distanceCounter;
  }

  private void init(World world) {
    initCellArray(world);
    visibleTroopers = new ArrayList<>();
    enemies = new ArrayList<>();
  }

  private void initCellArray(World world) {
    cells = new Cell[width][height][3];
    CellType[][] worldCells = world.getCells();
    for (int i = 0; i < worldCells.length; ++i)
      for (int j = 0; j < worldCells[0].length; ++j)
        for (int s = 0; s < 3; ++s)
          cells[i][j][s] = new Cell(worldCells[i][j], 0);
  }

  public boolean isVisibleFrom(int x, int y, TrooperModel trooper) {
    return visibilityManager.isVisibleFrom(x, y, trooper);
  }

  public boolean isVisibleBy(TrooperModel trooper, int x, int y) {
    return visibilityManager.isVisibleBy(trooper, x, y);
  }

  public int getExposure(TrooperModel trooper, int x, int y) {
    return visibilityManager.getExposure(trooper, x, y);
  }

  public boolean[][] getReachableCells(TrooperModel trooper) {
    boolean[][] answer = new boolean[width][height];
    for (int i = 0; i < width; ++i)
      for (int j = 0; j < height; ++j)
        if (cellChecker.cellIsFree(i, j) && canTrooperReachCell(trooper, i, j))
          answer[i][j] = true;

    return answer;
  }

  public boolean canTrooperReachCell(TrooperModel trooper, int x, int y) {
    int ap = trooper.getActionPoints() + (trooper.isHoldingFieldRation() ? 3 : 0);
    int stance = trooper.getStance().ordinal();
    int maxMoves = (ap - 2 * (2 - stance)) / 2;
    return getDistance(trooper.getX(), trooper.getY(), x, y) <= maxMoves;
  }

  public Cell getCell(int x, int y, int stance) {
    return cells[x][y][stance];
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
    updateTroopersWithWorld(world);
    removeInvalidTroopers(world);
    enemies = null;
  }

  private void updateTroopersWithWorld(World world) {
    ArrayList<TrooperModel> trooperToRemove = new ArrayList<>();
    ArrayList<TrooperModel> trooperToInsert = new ArrayList<>();
    for (Trooper trooper : world.getTroopers()) {
      TrooperModel newTrooper = new TrooperModel(trooper);
      boolean wasEarlier = false;
      for (int i = 0; i < visibleTroopers.size(); ++i) {
        TrooperModel oldTrooper = visibleTroopers.get(i);
        if (oldTrooper.equals(newTrooper)) {
//          visibleTroopers.set(i, newTrooper);
          oldTrooper.update(newTrooper);
          wasEarlier = true;
        }
      }
      if (!wasEarlier)
        trooperToInsert.add(newTrooper);
    }
    for (TrooperModel trooper : trooperToRemove)
      visibleTroopers.remove(trooper);

    for (TrooperModel trooper : trooperToInsert)
      visibleTroopers.add(trooper);
  }

  private void removeInvalidTroopers(World world) {
    ArrayList<TrooperModel> trooperToRemove = new ArrayList<>();
    ArrayList<TrooperModel> myTroopers = getMyTroopers();
    for (TrooperModel oldTrooper : visibleTroopers) {
      boolean canSeeItIndeed = false;
      boolean mustBeVisibleNow = checkVisibility(myTroopers, oldTrooper);
      if (mustBeVisibleNow) {
        for (Trooper trooper : world.getTroopers())
          if (trooper.getPlayerId() == oldTrooper.getPlayerId() && trooper.getType() == oldTrooper.getType())
            canSeeItIndeed = true;

        if (!canSeeItIndeed)
          trooperToRemove.add(oldTrooper);
      }
    }
    for (TrooperModel trooper : trooperToRemove) {
      logger.warning("removing invalid trooper " + trooper);
      visibleTroopers.remove(trooper);
    }
  }

  private ArrayList<TrooperModel> getMyTroopers() {
    ArrayList<TrooperModel> myTroopers = new ArrayList<>();
    for (TrooperModel trooper : visibleTroopers)
      if (trooper.isTeammate())
        myTroopers.add(trooper);
    return myTroopers;
  }

  public boolean checkVisibility(ArrayList<TrooperModel> myTroopers, int x, int y, TrooperStance stance) {
    for (TrooperModel myTrooper : myTroopers)
      if (visibilityManager.isVisible(myTrooper, x, y, stance))
        return true;

    return false;
  }

  public boolean checkVisibility(ArrayList<TrooperModel> myTroopers, TrooperModel anotherTrooper) {
    if (anotherTrooper.isTeammate())
      return true;

    for (TrooperModel myTrooper : myTroopers)
      if (visibilityManager.isVisible(myTrooper, anotherTrooper))
        return true;

    return false;
  }

  public boolean checkShootAbility(ArrayList<TrooperModel> myTroopers, TrooperModel anotherTrooper) {
    if (anotherTrooper.isTeammate())
      return false;

    for (TrooperModel myTrooper : myTroopers)
      if (ableToShoot(myTrooper, anotherTrooper))
        return true;

    return false;
  }

  private void updateBonuses(World world) {
    for (Bonus bonus : world.getBonuses()) {
      int x = bonus.getX();
      int y = bonus.getY();
      if (cells[x][y][0].hasBonus())
        logger.fine("I've seen that bonus!");
      else
        logger.fine("New bonus at " + x + "," + y + " -- cool!");
      cells[x][y][0].setBonus(bonus);
    }
  }

  private void resetVisibleArea(World world) {
    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j)
        if (world.getCells()[i][j] == CellType.FREE) {
          for (Trooper trooper : world.getTroopers())
            if (trooper.isTeammate()) {
              for (int s = 0; s < 3; ++s) {
                if (visibilityManager.isRangeVisible(trooper.getVisionRange(), trooper.getX(), trooper.getY(), trooper.getStance(), i, j, Utils.getStance(s))) {
                  cells[i][j][s].update(world.getMoveIndex());
                  if (s == 0) cells[i][j][0].setBonus(null);
                }
              }
            }
        }
  }

  public boolean hasBonus(int x, int y) {
    return cells[x][y][0].hasBonus();
  }

  public ArrayList<TrooperModel> getVisibleTroopers() {
    return visibleTroopers;
  }

  public ArrayList<TrooperModel> getEnemies() {
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
    enemies.add(enemy);
  }

  public PathFinder getPathFinder() {
    return pathFinder;
  }

  public boolean ableToShoot(TrooperModel trooper, TrooperModel anotherTrooper) {
    return visibilityManager.isAbleToShoot(trooper, anotherTrooper);
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Bonus getBonus(int tx, int ty) {
    return cells[tx][ty][0].getBonus();
  }

  public boolean ableToThrowGrenade(TrooperModel trooper, TrooperModel anotherTrooper) {
    return trooper.isHoldingGrenade() && trooper.getDistanceTo(anotherTrooper.getX(), anotherTrooper.getY()) < game.getGrenadeThrowRange();
  }

  public int getActuality(TrooperModel trooper, int currentTime) {
    return getCell(trooper.getX(), trooper.getY(), trooper.getStance().ordinal()).getActuality(currentTime);
  }
}

class Cell {
  private CellType cellType;
  private Bonus bonus = null;
  private int timeLastSeen;

  public Cell(CellType cellType, int timeLastSeen) {
    this.cellType = cellType;
    this.timeLastSeen = timeLastSeen;
  }

  int getTimeLastSeen() {
    return timeLastSeen;
  }

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
    assert time >= timeLastSeen;
    timeLastSeen = time;
  }

  public boolean isActual(int currentTime) {
    return currentTime <= timeLastSeen + 1;
  }

  public CellType getType() {
    return cellType;
  }

  public int getActuality(int currentTime) {
    return (currentTime - timeLastSeen + 1);
  }
}
