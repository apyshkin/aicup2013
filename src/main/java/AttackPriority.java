import model.TrooperStance;
import model.World;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/28/13
 * Time: 2:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class AttackPriority implements IPriority {

  private static final int MAX_RADIUS = 14;
  private final Environment environment;
  private final TrooperModel trooper;
  private int[][] distances;
  private boolean[][] trooperLocations;
  private final boolean[][] cellsOneCanShootFrom;

  public AttackPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    cellsOneCanShootFrom = getCellsFromWhichEnemyCanBeShotAt();
    initLocations(environment, trooper);
  }

  private boolean[][] getCellsFromWhichEnemyCanBeShotAt() {
    World world = environment.getWorld();
    boolean[][] result = new boolean[world.getWidth()][world.getHeight()];
    ArrayList<TrooperModel> enemies = environment.getVisibleEnemies();
    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j)
        for (TrooperModel enemy : enemies)
          if (environment.getBattleMap().isVisibleFrom(i, j, enemy))
            result[i][j] = true;

    return result;
  }

  private void initLocations(Environment environment, TrooperModel trooper) {
    trooperLocations = new boolean[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
//    for (TrooperModel myTrooper : environment.getMyTroopers())
//      if (myTrooper != trooper)
//        trooperLocations[myTrooper.getX()][myTrooper.getY()] = true;
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    final TrooperStance trooperStance = Utils.getStance(stance);

    int points = 0;
    ArrayList<TrooperModel> enemies = environment.getVisibleEnemies();
    for (TrooperModel enemy : enemies) {
      if (enemyIsVisibleFrom(x, y, trooperStance, enemy)) {
        points += countAverageShootPoints(trooper, trooperStance);
        points += countReinforcement(x, y);
      }
    }
    return points / environment.getMyTeam().count();
  }

  private int countReinforcement(int x, int y) {
    int points = 0;
    trooperLocations[x][y] = true;

    for (TrooperModel trooperForReinforcement : environment.getMyTroopers()) {
      if (trooperForReinforcement != trooper)
        if (canAttackTeammate(trooperForReinforcement))
          points += countAverageShootPoints(trooperForReinforcement, TrooperStance.STANDING);
    }

    trooperLocations[x][y] = false;

    return points;
  }

  private boolean canAttackTeammate(TrooperModel teammate) {
    final PathFinder pathFinder = environment.getBattleMap().getPathFinder();
    int distance = pathFinder.countDistanceToMarkedCellsWithObstacles(teammate.getX(), teammate.getY(), trooperLocations, cellsOneCanShootFrom, MAX_RADIUS);
    return (distance <= MAX_RADIUS);
  }

  private int countAverageShootPoints(TrooperModel trooper, TrooperStance stance) {
    return trooper.getDamage(stance) * trooper.getInitialActionPoints() / trooper.getShootCost();
  }

  public boolean enemyIsVisibleFrom(int x, int y, TrooperStance stance, TrooperModel enemy) {
    return environment.getWorld().isVisible(trooper.getShootingRange(), x, y, stance,
            enemy.getX(), enemy.getY(), enemy.getStance());
  }
}
