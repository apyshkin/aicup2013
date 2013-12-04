import model.TrooperType;
import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 4:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClosenessToHealerPriority implements IPriority {
  private static final int MAX_RADIUS = 25;
  private final PathFinder pathFinder;
  private final Environment environment;
  private final TrooperModel trooper;
  private int[][] answer;
  private boolean[][] isCounted;
  private int[][] distances;
  private boolean[][] trooperLocations;

  public ClosenessToHealerPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    pathFinder = environment.getBattleMap().getPathFinder();
    initLocations(environment, trooper);
    World world = environment.getWorld();
    answer = new int[world.getWidth()][world.getHeight()];
    isCounted = new boolean[world.getWidth()][world.getHeight()];
  }

  private void initLocations(Environment environment, TrooperModel trooper) {
    trooperLocations = new boolean[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
    for (TrooperModel myTrooper : environment.getMyTroopers())
      if (myTrooper != trooper && myTrooper.getType() != TrooperType.FIELD_MEDIC)
        trooperLocations[myTrooper.getX()][myTrooper.getY()] = true;
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    if (isCounted[x][y])
      return answer[x][y];
    else {
      if (!environment.getMyTeam().isAlive(TrooperType.FIELD_MEDIC))
        return 0;

      int ans = 0;
      TrooperModel healer = environment.getMyTeam().getMyTrooper(TrooperType.FIELD_MEDIC);

      {
        if (trooper != healer)
          trooperLocations[x][y] = true;

        int[][] distances = pathFinder.findDistancesWithObstacles(healer.getX(), healer.getY(), trooperLocations, 2 * MAX_RADIUS);

        for (TrooperModel trooper : environment.getMyTroopers()) {
          if (distances[trooper.getX()][trooper.getY()] > 10)
            ans += 8 * (10 + getMaximalHitpoints(trooper) - trooper.getHitpoints()) * distances[trooper.getX()][trooper.getY()] / 10;
          else
            ans += 2 * (5 + getMaximalHitpoints(trooper) - trooper.getHitpoints()) * distances[trooper.getX()][trooper.getY()] / 10;
        }

        if (trooper != healer)
          trooperLocations[x][y] = false;
      }

      answer[x][y] = ans;
      isCounted[x][y] = true;
      return ans;
    }

  }

  private int getMaximalHitpoints(TrooperModel trooper) {
    if (trooper.getType() == TrooperType.SOLDIER)
      return Math.max(trooper.getHitpoints(), trooper.getMaximalHitpoints());
    return trooper.getMaximalHitpoints();
  }
}


