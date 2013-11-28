import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/27/13
 * Time: 3:52 AM
 * To change this template use File | Settings | File Templates.
 */
public class PatrollingPriority implements IPriority {
  private final Environment environment;
  private final Router router;
  private final TrooperModel trooper;
  private final int startX;
  private final int startY;

  public PatrollingPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    router = new Router(environment);
    if (router.checkPointWasReached()) {

      router.nextCheckPoint();
    }

    this.trooper = trooper;
    startX = trooper.getX();
    startY = trooper.getY();
  }

  @Override
  public int getPriority(int x, int y, int stance) {
    assert (x == trooper.getX() && y == trooper.getY());
    final BattleMap battleMap = environment.getBattleMap();

    MapCell checkPoint = router.getCheckPoint();
    int distanceToCheckPoint = battleMap.getDistance(startX, startY, checkPoint.getX(), checkPoint.getY());

    int distanceFromHereToCP = battleMap.getDistance(x, y, checkPoint.getX(), checkPoint.getY());
    int points = distanceToCheckPoint - distanceFromHereToCP;

    int penalty = 0;
    TrooperModel leader = environment.getMyTeam().getLeader();
    if (trooper != leader) {
      final int distanceFromLeaderToCP = battleMap.getDistance(leader.getX(), leader.getY(), checkPoint.getX(), checkPoint.getY());
      final int distanceFromHereToLeader = battleMap.getDistance(leader.getX(), leader.getY(), x, y);
      if (distanceFromLeaderToCP == distanceFromHereToCP + distanceFromHereToLeader) //todo pathway width one
        penalty = 10 * (distanceFromLeaderToCP - distanceFromHereToCP);
    }

    return points - penalty;
  }
}
