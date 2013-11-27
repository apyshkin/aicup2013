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

  public PatrollingPriority(Environment environment, TrooperModel trooper) {
    this.environment = environment;
    router = new Router(environment);
    this.trooper = trooper;
  }

  @Override
  public int getPriority(int x, int y) {
    if (router.checkPointWasReached())
      router.nextCheckPoint();

    MapCell checkPoint = router.getCheckPoint();
    final BattleMap battleMap = environment.getBattleMap();
    int distanceToCheckPoint = battleMap.getDistance(trooper.getX(), trooper.getY(),
            checkPoint.getX(), checkPoint.getY());

    return (distanceToCheckPoint - battleMap.getDistance(x, y, checkPoint.getX(), checkPoint.getY()));
  }
}
