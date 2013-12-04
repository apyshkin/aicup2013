import model.*;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 6:34 PM
 * To change this template use File | Settings | File Templates.
 */
public final class Environment implements Cloneable {
  private final static Logger logger = Logger.getLogger(Environment.class.getName());

  private final World world;
  private final Game game;
  private final Team myTeam;
  private final CellChecker cellChecker;
  private final PathFinder pathFinder;
  private final int currentTime;

  private final BattleMap battleMap;
  private final Router router;
  private final TrooperModel currentTrooper;
  private final BattleState battleState;

  public Environment(TrooperModel trooper, BattleMap battleMap, BattleState battleState, World world, Game game, Router router, PathFinder pathFinder, int currentTime) {
    this.battleMap = battleMap;
    this.battleState = battleState;
    this.world = world;
    this.game = game;
    this.router = router;
    this.myTeam = new Team(findMyTroopers());
    this.cellChecker = new CellChecker(world);
    this.pathFinder = pathFinder;
    this.currentTime = currentTime;
    currentTrooper = myTeam.getMyTrooper(trooper.getType());
  }

  public boolean canTrooperReachCell(TrooperModel trooper, int x, int y) {
    return battleMap.canTrooperReachCell(trooper, x, y);
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

  public int getCellNotVisitTime(int x, int y, int stance) {
    Cell cell = battleMap.getCell(x, y, stance);
    return currentTime - cell.getTimeLastSeen();
  }

  public boolean enemyIsVisible(TrooperModel self, TrooperModel enemyTrooper) {
    return world.isVisible(self.getShootingRange(), self.getX(), self.getY(), self.getStance(),
            enemyTrooper.getX(), enemyTrooper.getY(), enemyTrooper.getStance());
  }

  public Environment clone() {
    World worldClone = Utils.copyOfTheWorld(world);
    return new Environment(currentTrooper, battleMap, battleState, worldClone, game, router, pathFinder, currentTime);
  }

  public CellChecker getCellChecker() {
    return cellChecker;
  }

  public void putEnemy(TrooperModel enemy) {
//    logger.fine("adding enemy " + enemy);
    battleMap.putEnemy(enemy);
  }

  boolean[][] getReachableCells(TrooperModel trooper) {
    return battleMap.getReachableCells(trooper);
  }

  public Team getMyTeam() {
    return myTeam;
  }

  public int getCurrentTime() {
    return currentTime;
  }

  public ArrayList<TrooperModel> getActualEnemies() {
    ArrayList<TrooperModel> result = new ArrayList<>();
    for (TrooperModel enemy : getEnemies()) {
      if (battleMap.getCell(enemy.getX(), enemy.getY(), enemy.getStance().ordinal()).isActual(currentTime)) {
        result.add(enemy);
//        logger.fine("Enemy is actual " + enemy);
      }
//      else
//        logger.fine("Enemy is not actual " + enemy);
    }
    return result;
  }

  public Router getRouter() {
    return router;
  }

  public TrooperModel getCurrentTrooper() {
    return currentTrooper;
  }

  public BattleState getBattleState() {
    return battleState;
  }

  public int getMoveIndex() {
    return currentTime;
  }
}
