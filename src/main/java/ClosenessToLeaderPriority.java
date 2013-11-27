import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 11:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClosenessToLeaderPriority implements IPriority {
  private final Environment environment;
  private final TrooperModel trooper;
  private final TrooperModel leader;

  public ClosenessToLeaderPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    this.trooper = trooper;
    leader = environment.getMyTeam().getLeader();
  }

  @Override
  public int getPriority(int x, int y) {
    int distanceToLeader;
    if (leader == trooper)
      distanceToLeader = 0;
    else {
      BattleMap battleMap = environment.getBattleMap();
      distanceToLeader = battleMap.getDistance(x, y, leader.getX(), leader.getY());
    }

    int points = distanceToLeader;
    return points;
  }
}
