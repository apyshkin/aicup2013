import model.TrooperStance;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 3:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class PatrolPriority implements IPriority {
  private final Environment environment;
  private final TrooperModel leader;
  private final TrooperModel trooper;
  private final int startX;
  private final int startY;
  private final Router router;
  private int[][] answer;
  private boolean[][] isCounted;
  private boolean[][] routeCells;
  private boolean[][] obstacles;
  private int[][] distancesCPToAll;
  private int[][] distancesLeaderToAll;
  private int distanceFromLeaderToCP;

  public PatrolPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.router = environment.getRouter();
    this.trooper = trooper;
    startX = trooper.getX();
    startY = trooper.getY();
    leader = environment.getMyTeam().getLeader();
    answer = new int[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
    isCounted = new boolean[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
    obstacles = initObstaclesCells(leader);
    distancesCPToAll = initCPToAllDistances();
    distancesLeaderToAll = initLeaderToAllDistances();

    MapCell checkPoint = this.router.getCheckPoint();
    distanceFromLeaderToCP = environment.getBattleMap().getPathFinder().countDistanceToCellWithObstacles(leader.getX(), leader.getY(),
            checkPoint.getY(), checkPoint.getX(), obstacles, 100);
  }

  private int[][] initCPToAllDistances() {
    int[][] distancesCPToAll = new int[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
    final PathFinder pathFinder = environment.getBattleMap().getPathFinder();
    MapCell checkPoint = router.getCheckPoint();
    pathFinder.findDistancesWithObstacles(checkPoint.getX(), checkPoint.getY(), distancesCPToAll, obstacles, Utils.INFINITY);
    return distancesCPToAll;
  }

  private int[][] initLeaderToAllDistances() {
    int[][] distancesLeaderToAll = new int[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
    if (trooper != leader) {
      final PathFinder pathFinder = environment.getBattleMap().getPathFinder();
      pathFinder.findDistancesWithObstacles(leader.getX(), leader.getY(), distancesLeaderToAll, obstacles, Utils.INFINITY);
    }
    return distancesLeaderToAll;
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    int ans = 0;
    if (isCounted[x][y])
      ans = answer[x][y];
    else {

      final BattleMap battleMap = environment.getBattleMap();
      final MapCell checkPoint = router.getCheckPoint();

      int distanceFromStartToCP = battleMap.getDistance(startX, startY, checkPoint.getX(), checkPoint.getY());
      int distanceFromHereToCP = battleMap.getDistance(x, y, checkPoint.getX(), checkPoint.getY());
      int points = distanceFromStartToCP - distanceFromHereToCP;

      int penalty = 0;
      TrooperModel leader = environment.getMyTeam().getLeader();
      if (trooper != leader) {
        penalty += countStuckPenalty(x, y, leader);
      }

      ans = points - penalty;

      isCounted[x][y] = true;
      answer[x][y] = ans;
    }
    return ans - countStancePenalty(stance);
  }

  private int countStancePenalty(int stance) {
    return (TrooperStance.STANDING.ordinal() - stance) * 2;
  }

  private int countStuckPenalty(int x, int y, TrooperModel leader) {
    final PathFinder pathFinder = environment.getBattleMap().getPathFinder();
    final MapCell checkPoint = router.getCheckPoint();

    final int distanceFromLeaderToMe = distancesLeaderToAll[x][y];
    final int distanceFromCPToMe = distancesCPToAll[x][y];
    if (!checkPointIsOnTheRoute(checkPoint, distanceFromLeaderToMe, distanceFromCPToMe))
      return 0;

    int penalty = 0;
    obstacles[x][y] = true;
    int distance = pathFinder.countDistanceToCellWithObstacles(leader.getX(), leader.getY(),
            checkPoint.getY(), checkPoint.getX(), obstacles, Utils.INFINITY);
    if (distance > distanceFromLeaderToCP) {
      penalty = 30 - distanceFromLeaderToMe;
    }
    obstacles[x][y] = false;
    return penalty;
  }

  private boolean checkPointIsOnTheRoute(MapCell checkPoint, int distanceFromLeaderToMe, int distanceFromCPToMe) {
    int distanceFromLeaderToCP = distancesLeaderToAll[checkPoint.getX()][checkPoint.getY()];
    return distanceFromLeaderToMe + distanceFromCPToMe == distanceFromLeaderToCP;
  }

  private boolean checkCandidate(int x, int y, MapCell checkPoint, int distanceFromLeaderToMe, int distanceFromCPToMe) {
    final BattleMap battleMap = environment.getBattleMap();
    return battleMap.getDistance(x, y, leader.getX(), leader.getY()) == distanceFromLeaderToMe
            && battleMap.getDistance(x, y, checkPoint.getX(), checkPoint.getY()) == distanceFromCPToMe;
  }

  private boolean[][] initObstaclesCells(TrooperModel leader) {
    final int width = environment.getWorld().getWidth();
    final int height = environment.getWorld().getHeight();

    boolean[][] obstacles = new boolean[width][height];

    for (TrooperModel myTrooper : environment.getMyTroopers())
      if (myTrooper != leader && myTrooper != trooper)
        obstacles[myTrooper.getX()][myTrooper.getY()] = true;

    return obstacles;
  }

  private boolean[][] initRouteCells(TrooperModel leader, MapCell checkPoint, int distanceFromLeaderToMe,
                                     int distanceFromCPToMe) {
    final int width = environment.getWorld().getWidth();
    final int height = environment.getWorld().getHeight();

    if (routeCells == null)
      routeCells = new boolean[width][height];

    for (int i = 0; i < width; ++i)
      for (int j = 0; j < height; ++j)
        if (environment.getCellChecker().cellIsFree(i, j))
//        if (reachableCells[i][j])
          if (checkCandidate(i, j, checkPoint, distanceFromLeaderToMe, distanceFromCPToMe))
            routeCells[i][j] = true;
          else
            routeCells[i][j] = false;
    return routeCells;
  }
}
