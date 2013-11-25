import model.*;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Environment implements Cloneable {

  public BattleMap getBattleMap() {
    return battleMap;
  }

  public TrooperModel[] getMyTroopers() {
    return myTroopers;
  }

  public World getWorld() {
    return world;
  }

  public Game getGame() {
    return game;
  }

  public int getCurrentTime() {
    return currentTime;
  }

  public TrooperModel[] getAllVisibleTroopers() {
    if (this.visibleTroopers != null)
      return this.visibleTroopers;

    Trooper[] troopers = world.getTroopers();
    ArrayList<TrooperModel> visibleTroopers = new ArrayList<>();
    for (Trooper trooper : troopers)
      visibleTroopers.add(new TrooperModel(trooper));
    this.visibleTroopers = visibleTroopers.toArray(new TrooperModel[0]);
    return this.visibleTroopers;
  }


  public int getCellNotVisitTime(int x, int y) {
    Cell cell = battleMap.getCell(x, y);
    return currentTime - cell.timeOfLastVisit;
  }

  private BattleMap battleMap;

  private final World world;
  private final Game game;
  private final TrooperModel[] myTroopers;
  private TrooperModel[] visibleTroopers;

  private final int currentTime;

  public Environment(BattleMap battleMap, World world, Game game, TrooperModel[] myTroopers, int currentTime) {
    this.battleMap = battleMap;
    this.myTroopers = myTroopers;
    this.world = world;
    this.game = game;
    this.currentTime = currentTime;
  }
  public boolean enemyIsVisible(TrooperModel self, TrooperModel enemyTrooper) {
    return world.isVisible(self.getShootingRange(), self.getX(), self.getY(), self.getStance(),
            enemyTrooper.getX(), enemyTrooper.getY(), enemyTrooper.getStance());
  }

  public Environment clone() {
    World worldClone = Utils.copyOfTheWorld(world);
    TrooperModel[] troopers = new TrooperModel[myTroopers.length];
    System.arraycopy(myTroopers, 0, troopers, 0, myTroopers.length);
    return new Environment(battleMap, worldClone, game, troopers, currentTime);
  }

  public void visitCell(int x, int y) {
    battleMap.visitCell(x, y, currentTime);
  }

  public boolean cellsAreTheSame(int x, int y, int x1, int y1) {
    return (x == x1) && (y == y1);
  }

  public boolean cellIsWithinBoundaries(int x, int y) {
    if (x < 0 || x >= world.getWidth())
      return false;
    if (y < 0 || y >= world.getHeight())
      return false;
    return true;
  }

  public boolean cellIsFree(int x, int y) {
    return (world.getCells()[x][y] == CellType.FREE);
  }

  public boolean cellsAreNeighbours(int x, int y, int x1, int y1) {
    return Math.abs(x-x1) + Math.abs(y-y1) == 1;
  }
}
