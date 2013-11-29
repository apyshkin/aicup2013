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

  private final World world;
  private final Game game;
  private final Team myTeam;
  private final CellChecker cellChecker;
  private final int currentTime;
  private final PathFinder pathFinder;
  private BattleMap battleMap;

  public Environment(BattleMap battleMap, World world, Game game, PathFinder pathFinder, int currentTime) {
    this.battleMap = battleMap;
    this.world = world;
    this.game = game;
    this.myTeam = new Team(findMyTroopers());
    this.cellChecker = new CellChecker(world);
    this.pathFinder = pathFinder;
    this.currentTime = currentTime;
  }

  public BattleMap getBattleMap() {
    return battleMap;
  }

  private ArrayList<TrooperModel> findMyTroopers() {
    assert (myTeam == null);
    ArrayList<TrooperModel> myTroopers = new ArrayList<>();
    for (TrooperModel trooper : getVisibleTroopers())
      if (trooper.isTeammate())
        myTroopers.add(trooper);

    return myTroopers;
  }

  public ArrayList<TrooperModel> getMyTroopers() {
    assert myTeam != null;
    return myTeam.getMyTroopers();
  }

  public World getWorld() {
    return world;
  }

  public Game getGame() {
    return game;
  }

  public ArrayList<TrooperModel> getVisibleTroopers() {
    return battleMap.getVisibleTroopers();
  }

  public ArrayList<TrooperModel> getEnemies() {
    return battleMap.getEnemies();
  }

  public int getCellNotVisitTime(int x, int y) {
    Cell cell = battleMap.getCell(x, y);
    return currentTime - cell.getTimeOfLastVisit();
  }

  public boolean enemyIsVisible(TrooperModel self, TrooperModel enemyTrooper) {
    return world.isVisible(self.getShootingRange(), self.getX(), self.getY(), self.getStance(),
            enemyTrooper.getX(), enemyTrooper.getY(), enemyTrooper.getStance());
  }

  public Environment clone() {
    World worldClone = Utils.copyOfTheWorld(world);
    return new Environment(battleMap, worldClone, game, pathFinder, currentTime);
  }

  public void visitCell(int x, int y) {
    battleMap.visitCell(x, y, currentTime);
  }

  public CellChecker getCellChecker() {
    return cellChecker;
  }

  public boolean canTrooperReachCell(TrooperModel trooper, int x, int y) {
    int ap = trooper.getActionPoints();
    int maxMoves = ap / game.getStandingMoveCost();
    return battleMap.getDistance(trooper.getX(), trooper.getY(), x, y) <= maxMoves;
  }

  public void putEnemy(TrooperModel enemy) {
    battleMap.putEnemy(enemy);
  }

  boolean[][] getReachableCells(TrooperModel trooper) {
    World world = getWorld();
    boolean[][] answer = new boolean[world.getWidth()][world.getHeight()];
    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j)
        if (cellChecker.cellIsFree(i, j) && canTrooperReachCell(trooper, i, j))
          answer[i][j] = true;

    return answer;
  }

  public Team getMyTeam() {
    return myTeam;
  }
}

