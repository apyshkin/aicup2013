import model.TrooperType;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 4:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClosenessToHealerPriority implements IPriority {
  private static final int MAX_RADIUS = 6;
  private final PathFinder pathFinder;
  private final Environment environment;
  private final TrooperModel trooper;
  private int[][] distances;
  private boolean[][] trooperLocations;

  public ClosenessToHealerPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    pathFinder = environment.getBattleMap().getPathFinder();
    initLocations(environment, trooper);
  }

  private void initLocations(Environment environment, TrooperModel trooper) {
    trooperLocations = new boolean[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
    for (TrooperModel myTrooper : environment.getMyTroopers())
      if (myTrooper != trooper && myTrooper.getType() != TrooperType.FIELD_MEDIC)
        trooperLocations[myTrooper.getX()][myTrooper.getY()] = true;
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    if (!environment.getMyTeam().isAlive(TrooperType.FIELD_MEDIC))
      return 0;

    int sum = 0;
    int[][] distances = initDistances();
    TrooperModel healer = environment.getMyTeam().getMyTrooper(TrooperType.FIELD_MEDIC);

    {
      if (trooper != healer)
        trooperLocations[x][y] = true;

      pathFinder.findDistancesWithObstacles(healer.getX(), healer.getY(), distances, trooperLocations, MAX_RADIUS);
      for (TrooperModel myTrooper : environment.getMyTroopers())
        if (myTrooper != healer)
          if (distances[myTrooper.getX()][myTrooper.getY()] == 0)
            sum += 3 * MAX_RADIUS;
          else
            sum += distances[myTrooper.getX()][myTrooper.getY()];

      if (trooper != healer)
        trooperLocations[x][y] = false;
    }

    return sum / environment.getMyTroopers().size();
  }

  private int[][] initDistances() {
    if (distances == null)
      distances = new int[environment.getWorld().getWidth()][environment.getWorld().getHeight()];
    else
      for (int i = 0; i < environment.getWorld().getWidth(); ++i)
        for (int j = 0; j < environment.getWorld().getHeight(); ++j)
          distances[i][j] = 0;

    return distances;
  }
}


