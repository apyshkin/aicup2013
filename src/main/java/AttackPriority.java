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
  private int[][] answer;
  private boolean[][] isCounted;
  private int[][] possibleGrenadeDamage;

  public AttackPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    cellsOneCanShootFrom = getCellsFromWhichEnemyCanBeShotAt();
    initLocations(environment, trooper);
    initGrenade(environment, trooper);
    BattleMap battleMap = environment.getBattleMap();
    answer = new int[battleMap.getWidth()][battleMap.getHeight()];
    isCounted = new boolean[battleMap.getWidth()][battleMap.getHeight()];
  }

  private void initGrenade(Environment environment, TrooperModel trooper) {
    BattleMap battleMap = environment.getBattleMap();
    possibleGrenadeDamage = new int[battleMap.getWidth()][battleMap.getHeight()];
    for (int i = 0; i < battleMap.getWidth(); ++i)
      for (int j = 0; j < battleMap.getHeight(); ++j)
        possibleGrenadeDamage[i][j] = countPossibleGrenadeDamage(i, j);
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
    int minPoints = Utils.INFINITY;
    ArrayList<TrooperModel> enemies = environment.getEnemies();
    for (TrooperModel enemy : enemies) {
      if (enemyIsShootableFrom(x, y, 2, enemy)) {
        int stanceFromWhichCanShoot = 0;
        while (!enemyIsShootableFrom(x, y, stanceFromWhichCanShoot, enemy))
          ++stanceFromWhichCanShoot;

        assert stanceFromWhichCanShoot < 3;

        int points = countAverageShootPoints(trooper, trooperStance);
        if (!isCounted[x][y]) {
          answer[x][y] = countReinforcement(stanceFromWhichCanShoot);
          isCounted[x][y] = true;
        }
        points += answer[x][y];
        minPoints = Math.min(minPoints, points);
      }
    }
    minPoints += possibleGrenadeDamage[x][y] >> 1;
    trooperLocations[x][y] = false;

    if (minPoints < Utils.INFINITY)
      return minPoints / environment.getMyTeam().count();

    return 0;
  }

  private int countPossibleGrenadeDamage(int x, int y) {
    if (!trooper.isHoldingGrenade())
      return 0;

    BattleMap battleMap = environment.getBattleMap();
    int maxPoints = 0;
    for (int i = 0; i < battleMap.getWidth(); ++i)
      for (int j = 0; j < battleMap.getHeight(); ++j) {
        ArrayList<TrooperModel> enemiesNearby = getActualEnemiesNearby(i, j);
        int points = 0;
        for (TrooperModel enemy : enemiesNearby) {
          if (enemy.isTeammate())
            break;
          if (trooper.getDistanceTo(enemy) > environment.getGame().getGrenadeThrowRange() + Utils.EPS)
            continue;

          int eX = enemy.getX();
          int eY = enemy.getY();
          if (eX == x && eY == y)
            points += environment.getGame().getGrenadeDirectDamage();
          else if (Math.abs(eX - x) + Math.abs(eY - y) == 1)
            points += environment.getGame().getGrenadeCollateralDamage();
        }
        maxPoints = Math.max(points, maxPoints);
      }
    return maxPoints;
  }

  private ArrayList<TrooperModel> getActualEnemiesNearby(int x, int y) {
    ArrayList<TrooperModel> troopers = environment.getActualEnemies();
    ArrayList<TrooperModel> answer = new ArrayList<>();
    for (TrooperModel trooper : troopers)
      if ((Math.abs(trooper.getX() - x) + Math.abs(trooper.getY() - y)) <= 1)
        answer.add(trooper);
    return answer;
  }

  private int countReinforcement(int stanceFromWhichCanShoot) {
    final PathFinder pathFinder = environment.getBattleMap().getPathFinder();
    int points = 0;

    ArrayList<Pair<TrooperModel, Integer>> troopers = new ArrayList<>();
    int maxCount = 0;
    for (TrooperModel trooperForReinforcement : environment.getMyTroopers()) {
//      if (trooperForReinforcement != trooper) {
        int count = 0;
        int distances[][] = pathFinder.findDistancesWithObstacles(trooperForReinforcement.getX(),
                trooperForReinforcement.getY(),
                trooperLocations, MAX_RADIUS);
        int min = Utils.INFINITY;
        for (int i = 0; i < cellsOneCanShootFrom.length; ++i)
          for (int j = 0; j < cellsOneCanShootFrom[0].length; ++j)
            if (cellsOneCanShootFrom[i][j] && distances[i][j] <= MAX_RADIUS) {
              ++count;
              min = Math.min(min, distances[i][j]);
            }
        maxCount = Math.max(maxCount, count);
        troopers.add(new Pair<>(trooperForReinforcement, min));
//      }
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
      points += countAverageShootPoints(teammate.getKey(), Utils.getStance(stanceFromWhichCanShoot));
      --maxCount;
    }
    return points;
  }

  private int countAverageShootPoints(TrooperModel trooper, TrooperStance stanceFromWhichCanShoot) {
    final int stanceChangePoints = 4;
    return trooper.getDamage(stanceFromWhichCanShoot) * (trooper.getInitialActionPoints() - stanceChangePoints) / trooper.getShootCost();
  }

  public boolean enemyIsShootableFrom(int x, int y, int stance, TrooperModel enemy) {
    return environment.getWorld().isVisible(trooper.getShootingRange(), x, y, Utils.getStance(stance),
            enemy.getX(), enemy.getY(), enemy.getStance());
  }
}
