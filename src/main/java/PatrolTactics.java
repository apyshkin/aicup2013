import model.TrooperType;
import model.World;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/21/13
 * Time: 10:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class PatrolTactics implements ITactics {
  private final Environment environment;
//  private final Router router;

  public PatrolTactics(Environment environment) {
    this.environment = environment;
//    router = new Router(environment);
  }

  @Override
  public void setAction(ITrooperStrategy trooper) {
    trooper.setActionUnderTactics(this);
  }

//  @Override
//  public CellPriorities generateCellPriorities(TrooperModel trooper) {
//    World world = environment.getWorld();
//    boolean[][] reachableCells = environment.getReachableCells(trooper);
//
//    int[][] priorities = new int[world.getWidth()][world.getHeight()];
//    if (router.checkPointWasReached())
//      router.nextCheckPoint();
//
//    MapCell checkPoint = router.getCheckPoint();
//    int distanceToCheckPoint = environment.getBattleMap().getDistance(trooper.getX(), trooper.getY(),
//            checkPoint.getX(), checkPoint.getY());
//
//    for (int i = 0; i < world.getWidth(); ++i)
//      for (int j = 0; j < world.getHeight(); ++j)
//        if (reachableCells[i][j]) {
//          priorities[i][j] += 4 * (distanceToCheckPoint - environment.getBattleMap().getDistance(i, j,
//                  checkPoint.getX(), checkPoint.getY()));
//        }
//
//
//    for (int i = 0; i < world.getHeight(); ++i) {
//      for (int j = 0; j < world.getWidth(); ++j)
//        System.out.format("%3d ", priorities[j][i]);
//      System.out.println();
//    }
//
//    return new CellPriorities(environment, priorities);
//  }
}

class CellFunctions {
  public static int ExploringFunction(int cellNotVisitTime) {
    return cellNotVisitTime;
  }

  public static int GlueTogetherFunction(TrooperType myTrooperType, int maxDistance) {

    int points;
    if (maxDistance > 7)
      points = 15 - maxDistance;
    else if (maxDistance >= 2)
      points = 25 - maxDistance;
    else
      points = 20 - maxDistance;

    if (myTrooperType == TrooperType.COMMANDER)
      points /= 1.5;
    else if (myTrooperType == TrooperType.FIELD_MEDIC)
      points *= 2;

    return points;
  }
}
