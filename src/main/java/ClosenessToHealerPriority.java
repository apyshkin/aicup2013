import model.TrooperType;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 4:23 AM
 * To change this template use File | Settings | File Templates.
 */
public class ClosenessToHealerPriority implements IPriority {
  private static final int MAX_RADIUS = 6;
  private static ArrayList<MapCell> trooperLocations;
  private final Environment environment;
  private final TrooperModel trooper;

  public ClosenessToHealerPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    initLocations(environment, trooper);

  }

  private void initLocations(Environment environment, TrooperModel trooper) {
    trooperLocations = new ArrayList<>();
    for (TrooperModel myTrooper : environment.getMyTroopers())
      if (myTrooper != trooper && myTrooper.getType() != TrooperType.FIELD_MEDIC)
        trooperLocations.add(new MapCell(environment.getWorld(), myTrooper.getX(), myTrooper.getY()));
  }

  @Override
  public int getPriority(int x, int y) {
    if (!environment.getMyTeam().isAlive(TrooperType.FIELD_MEDIC))
      return 0;

    int sum = 0;

    int[][] distances;
    if (trooper.getType() != TrooperType.FIELD_MEDIC) {
      TrooperModel healer = environment.getMyTeam().getMyTrooper(TrooperType.FIELD_MEDIC);
      trooperLocations.add(new MapCell(environment.getWorld(), x, y));
      distances = environment.getBattleMap().getPathFinder().findShortestDistances(healer.getX(), healer.getY(), trooperLocations, MAX_RADIUS);
      for (MapCell location : trooperLocations)
        if (distances[location.getX()][location.getY()] == 0)
          sum += 5 * MAX_RADIUS;
        else
          sum += distances[location.getX()][location.getY()];

      trooperLocations.remove(trooperLocations.size() - 1);
    } else {
      distances = environment.getBattleMap().getPathFinder().findShortestDistances(x, y, trooperLocations, MAX_RADIUS);

      for (MapCell location : trooperLocations)
        if (distances[location.getX()][location.getY()] == 0)
          sum += 5 * MAX_RADIUS;
        else
          sum += distances[location.getX()][location.getY()];

    }

    return sum / environment.getMyTroopers().size();
  }
}
