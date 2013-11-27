import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 4:17 AM
 * To change this template use File | Settings | File Templates.
 */
public class TeamDensityPriority implements IPriority {
  private final Environment environment;
  private final TrooperModel trooper;

  public TeamDensityPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
  }

  @Override
  public int getPriority(int x, int y) {
    int maxDistance = countMaxDistance(x, y);

    int points;
    if (maxDistance >= 7)
      points = 15 - maxDistance;
    else if (maxDistance >= 2)
      points = 20 - maxDistance;
    else
      points = 10 + maxDistance;
    return points;
  }

  private int countMaxDistance(int x, int y) {
    BattleMap battleMap = environment.getBattleMap();
    ArrayList<TrooperModel> myTroopers = environment.getMyTroopers();
    int maxDistance = 0;
    for (TrooperModel myTrooper : myTroopers)
      if (myTrooper != trooper)
        maxDistance = Math.max(maxDistance, battleMap.getDistance(myTrooper.getX(), myTrooper.getY(), x, y));

    return maxDistance;
  }
}
