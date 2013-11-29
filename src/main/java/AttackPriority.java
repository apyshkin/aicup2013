import model.TrooperStance;
import model.World;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/28/13
 * Time: 2:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class AttackPriority implements IPriority {

  private static final int MAX_RADIUS = 4;
  private final Environment environment;
  private final TrooperModel trooper;
  private final boolean[][] cellsOneCanShootFrom;
  private int[][] distances;
  private boolean[][] trooperLocations;

  public AttackPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    cellsOneCanShootFrom = getCellsFromWhichEnemyCanBeShotAt();
    initLocations(environment, trooper);
  }

  private boolean[][] getCellsFromWhichEnemyCanBeShotAt() {
    World world = environment.getWorld();
    boolean[][] result = new boolean[world.getWidth()][world.getHeight()];
    ArrayList<TrooperModel> enemies = environment.getEnemies();
    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j)
        for (TrooperModel enemy : enemies)
          if (environment.getBattleMap().isVisibleFrom(i, j, enemy))
            result[i][j] = true;

    return result;
  }

  private void initLocations(Environment environment, TrooperModel trooper) {
    trooperLocations = new boolean[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
    for (TrooperModel enemy : environment.getEnemies()) {
      trooperLocations[enemy.getX()][enemy.getY()] = true;
    }
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    final TrooperStance trooperStance = Utils.getStance(stance);

    trooperLocations[x][y] = true;
    int points = 0;
    ArrayList<TrooperModel> enemies = environment.getEnemies();
    for (TrooperModel enemy : enemies) {
      if (enemyIsVisibleFrom(x, y, trooperStance, enemy)) {
        points += 2 * countAverageShootPoints(trooper, trooperStance);
        points += countReinforcement(x, y);
        break;
      }
    }
    trooperLocations[x][y] = false;

    return points; /// environment.getMyTeam().count();
  }

  private int countReinforcement(int x, int y) {
    final PathFinder pathFinder = environment.getBattleMap().getPathFinder();
    int points = 0;

    ArrayList<Pair<TrooperModel, Integer>> troopers = new ArrayList<>();
    int maxCount = 0;
    for (TrooperModel trooperForReinforcement : environment.getMyTroopers()) {
      if (trooperForReinforcement != trooper) {
        int count = 0;
        int distances[][] = pathFinder.findDistancesWithObstacles(trooperForReinforcement.getX(),
                                                                  trooperForReinforcement.getY(),
                                                                  MAX_RADIUS, trooperLocations);
        int min = Utils.INFINITY;
        for (int i = 0; i < cellsOneCanShootFrom.length; ++i)
          for (int j = 0; j < cellsOneCanShootFrom[0].length; ++j)
            if (cellsOneCanShootFrom[i][j] && distances[i][j] <= MAX_RADIUS) {
              ++count;
              min = Math.min(min, distances[i][j]);
            }
        maxCount = Math.max(maxCount, count);
        troopers.add(new Pair<>(trooperForReinforcement, min));
      }
    }
    maxCount--;
    Collections.sort(troopers, new Comparator<Pair<TrooperModel, Integer>>() {
      @Override
      public int compare(Pair<TrooperModel, Integer> o1, Pair<TrooperModel, Integer> o2) {
        return (o1.getValue() - o2.getValue());
      }
    });
    for (Pair<TrooperModel, Integer> teammate : troopers) {
      if (maxCount == 0)
        break;
      points += countAverageShootPoints(teammate.getKey(), TrooperStance.STANDING);
      --maxCount;
    }
    return points;
  }

  private int countAverageShootPoints(TrooperModel trooper, TrooperStance stance) {
    return trooper.getDamage(stance) * trooper.getInitialActionPoints() / trooper.getShootCost();
  }

  public boolean enemyIsVisibleFrom(int x, int y, TrooperStance stance, TrooperModel enemy) {
    return environment.getWorld().isVisible(trooper.getShootingRange(), x, y, stance,
            enemy.getX(), enemy.getY(), enemy.getStance());
  }
}
