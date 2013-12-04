import model.Bonus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;

public class Router implements IRouter {
  private static final int MAX_DIST_CHECKPOINT = 8;

  private MapCell center;
  private MapCell firstCP;
  private int CPCount = 0;
  private MapCell[] checkPoints;
  private static Stack<MapCell> stackCP;
  private MapCell currentCP;
  private boolean initialized = false;

  private final MapCell[] initCheckPoints() {
    stackCP = new Stack<>();
    return new MapCell[] {firstCP, firstCP.reflectX(), center,
            firstCP.reflectXY(), firstCP.reflectY(), center};
  }

  private final BattleMap battleMap;

  public Router(BattleMap battleMap) {
    this.battleMap = battleMap;
    if (!initialized)
      init();
    updateCurrentCheckPoint();
  }

  private void init() {
    firstCP = findBestCell();
    center = findCenter();
    checkPoints = initCheckPoints();
    initialized = true;
  }

  @Override
  public MapCell getCheckPoint() {
    return currentCP;
  }

  @Override
  public void nextCheckPoint() {
    if (stackCP.isEmpty()) {
      ++CPCount;
      CPCount = CPCount % checkPoints.length;
      updateCurrentCheckPoint();
    }
    else
      currentCP = stackCP.pop();
  }

  private void updateCurrentCheckPoint() {
    assert stackCP.isEmpty();
    currentCP = checkPoints[CPCount];
  }

  private MapCell findBestCell() {
    ArrayList<TrooperModel> troopers = getMyTroopers();
    int bestX = 0;
    int bestY = 0;
    int minDist = Utils.INFINITY;

    for (int i = 0; i < battleMap.getWidth(); ++i)
      for (int j = 0; j < battleMap.getHeight(); ++j)
        if (battleMap.getCellChecker().cellIsFree(i, j)) {
          int sum = 0;
          for (TrooperModel trooper : troopers) {
            int dist = battleMap.getDistance(i, j, trooper.getX(), trooper.getY());
            sum += dist * dist;
            sum += countSafety(i, j) * countSafety(i, j);
          }

          if (minDist > sum) {
            bestX = i;
            bestY = j;
            minDist = sum;
          }
        }
    return findClosestEmptyCell(bestX, bestY);
  }

  private int countSafety(int x, int y) {
    int res = 0;
    final int radius = 3;
    for (int i = -radius; i <= radius; ++i)
      for (int j = -radius; j <= radius; ++j)
        if (battleMap.getCellChecker().cellIsWithinBoundaries(x + i, y + j)) {
          final Cell cell = battleMap.getCell(x + i, y + j, 0);
          res += cell.getType().ordinal();

          if (cell.hasBonus()) {
            Bonus bonus = cell.getBonus();
            switch (bonus.getType()) {
              case FIELD_RATION: res += 3;
                break;
              case GRENADE: res += 3;
                break;
              case MEDIKIT: res += 4;
                break;
              default:
                break;
            }
          }
        }
    return res;
  }

  private ArrayList<TrooperModel> getMyTroopers() {
    ArrayList<TrooperModel> allTroopers = battleMap.getVisibleTroopers();
    ArrayList<TrooperModel> troopers =  new ArrayList<>();
    for (TrooperModel trooper : allTroopers)
      if (trooper.isTeammate())
        troopers.add(trooper);
    return troopers;
  }

  private MapCell findClosestEmptyCell(int x, int y) {
    int minDist = Utils.INFINITY;
    MapCell answer = null;
    for (int i = battleMap.getWidth() - 1; i >= 0; --i)
      for (int j = battleMap.getHeight() - 1; j >= 0; --j)
        if (battleMap.getCellChecker().cellIsFree(i, j) && getDistance(x, y, i, j) < minDist) {
          answer = BattleMap.getMapCellFactory().createMapCell(i, j);
          minDist = getDistance(x, y, i, j);
        }
    assert answer != null;
    return answer;
  }

  private static int getDistance(int x, int y, int x1, int j) {
    return Math.abs(x1 - x) + Math.abs(j - y);
  }

  private MapCell findCenter() {
    return findClosestEmptyCell(battleMap.getWidth() >> 1,
            battleMap.getHeight() >> 1);
  }

  public boolean checkPointWasPassed(MapCell checkPoint) {
    if (!stackCP.contains(checkPoint))
      return true;

    assert (Arrays.asList(checkPoints).contains(checkPoint));
    return currentCP != checkPoint;
  }

  @Override
  public boolean checkPointWasReached() {
    final int checkPointX = currentCP.getX();
    final int checkPointY = currentCP.getY();
    int maxDistance = 0;
    int avgDistance = 0;
    for (TrooperModel trooper : getMyTroopers()) {
      final int distanceToCP = battleMap.getDistance(trooper.getX(), trooper.getY(), checkPointX, checkPointY);
      avgDistance = distanceToCP;
      maxDistance = Math.max(maxDistance, distanceToCP);
//      System.out.println("cp " + checkPointX + " " + checkPointY + " : " + trooper);
    }
    avgDistance /= getMyTroopers().size();
    return avgDistance < MAX_DIST_CHECKPOINT >> 1 && maxDistance < MAX_DIST_CHECKPOINT;
  }

  @Override
  public void setCheckPoint(MapCell checkPoint) {
    currentCP = findClosestEmptyCell(checkPoint.getX(), checkPoint.getY());
  }

  public void addCheckPoint(MapCell newCheckPoint) {
    stackCP.push(newCheckPoint);
  }
}

