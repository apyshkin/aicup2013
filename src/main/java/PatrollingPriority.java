import model.TrooperStance;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 3:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class PatrollingPriority implements IPriority {
  private final Environment environment;
  private final TrooperModel leader;
  private Router router;
  private final TrooperModel trooper;
  private final int startX;
  private final int startY;
  private final boolean[][] reachableCells;
  private boolean[][] routeCells;
  private boolean[][] obstacles;
  private int[][] distancesCPToAll;
  private int[][] distancesLeaderToAll;

  public PatrollingPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    startX = trooper.getX();
    startY = trooper.getY();
    leader = environment.getMyTeam().getLeader();
    initRouter(environment);
    reachableCells = environment.getReachableCells(leader);
    obstacles = initObstaclesCells(leader);
    distancesCPToAll = initCPToAllDistances();
    distancesLeaderToAll = initLeaderToAllDistances();
  }

  private int[][] initCPToAllDistances() {
    int[][] distancesCPToAll = new int[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
    final PathFinder pathFinder = environment.getBattleMap().getPathFinder();
    MapCell checkPoint = router.getCheckPoint();
    pathFinder.findDistancesWithObstacles(checkPoint.getX(), checkPoint.getY(), Utils.INFINITY, obstacles, distancesCPToAll);
    return distancesCPToAll;
  }

  private int[][] initLeaderToAllDistances() {
    int[][] distancesLeaderToAll = new int[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
    if (trooper != leader) {
      final PathFinder pathFinder = environment.getBattleMap().getPathFinder();
      pathFinder.findDistancesWithObstacles(leader.getX(), leader.getY(), Utils.INFINITY, obstacles, distancesLeaderToAll);
    }
    return distancesLeaderToAll;
  }

  private void initRouter(Environment environment) {
    router = new Router(environment);
    if (router.checkPointWasReached()) {
      router.nextCheckPoint();
    }
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    assert (x == trooper.getX() && y == trooper.getY());
    final BattleMap battleMap = environment.getBattleMap();
    final MapCell checkPoint = router.getCheckPoint();

    int distanceToCheckPoint = battleMap.getDistance(startX, startY, checkPoint.getX(), checkPoint.getY());
    int distanceFromHereToCP = battleMap.getDistance(x, y, checkPoint.getX(), checkPoint.getY());
    int points = distanceToCheckPoint - distanceFromHereToCP;

    int penalty = (TrooperStance.STANDING.ordinal() - stance) * 10;
    TrooperModel leader = environment.getMyTeam().getLeader();
    if (trooper != leader) {
      penalty += countStuckPenalty(x, y, leader);
    }

    return points - penalty;
  }

  private int countStuckPenalty(int x, int y, TrooperModel leader) {
    final PathFinder pathFinder = environment.getBattleMap().getPathFinder();
    final MapCell checkPoint = router.getCheckPoint();
    int penalty = 0;

    obstacles[x][y] = true;
    final int distanceFromLeaderToMe = distancesLeaderToAll[x][y];
    final int distanceFromCPToMe = distancesCPToAll[x][y];
    boolean[][] routeCells = initRouteCells(leader, checkPoint, distanceFromLeaderToMe, distanceFromCPToMe);

    int distance = pathFinder.countDistanceToMarkedCellsWithObstacles(leader.getX(), leader.getY(), obstacles, routeCells, distanceFromLeaderToMe + 1);
    if (distance > distanceFromLeaderToMe + 1) {
      penalty = 20 - distanceFromLeaderToMe;
    }
    obstacles[x][y] = false;
    return penalty;
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

  private boolean[][] initRouteCells(TrooperModel leader, MapCell checkPoint, int distanceFromHereToLeader, int distanceFromHereToCP) {
    final BattleMap battleMap = environment.getBattleMap();
    final int width = environment.getWorld().getWidth();
    final int height = environment.getWorld().getHeight();

    if (routeCells == null)
      routeCells = new boolean[width][height];

    for (int i = 0; i < width; ++i)
      for (int j = 0; j < height; ++j)
        if (environment.getCellChecker().cellIsFree(i, j))
//        if (reachableCells[i][j])
          if (battleMap.getDistance(i, j, leader.getX(), leader.getY()) == distanceFromHereToLeader + 1 &&
                  battleMap.getDistance(i, j, checkPoint.getX(), checkPoint.getY()) == distanceFromHereToCP - 1)
            routeCells[i][j] = true;
          else
            routeCells[i][j] = false;
    return routeCells;
  }
}
