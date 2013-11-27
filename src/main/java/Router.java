import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: alexeyka
 * Date: 11/25/13
 * Time: 11:07 AM
 * To change this template use File | Settings | File Templates.
 */
interface IRouter {
  public MapCell getCheckPoint();
  public void nextCheckPoint();
  public boolean checkPointWasReached();
}

public class Router implements IRouter {
  private static final int MAX_DIST_CHECKPOINT = 10;

  private static MapCell center;
  private static MapCell myCorner;
  private static int checkPointCount = 0;
  private static MapCell[] corners;
  private static MapCell currentCheckPoint;
  private static boolean initialized = false;

  private static MapCell[] initCorners() {
    return new MapCell[] {myCorner, myCorner.reflectX(),
            myCorner.reflectXY(), myCorner.reflectY(), center};
  }

  private final Environment environment;

  public Router(Environment environment) {
    this.environment = environment;
    if (!initialized)
      init();
    updateCurrentCheckPoint();
  }

  private void init() {
    myCorner = findMyCorner();
    center = findCenter();
    corners = initCorners();
    initialized = true;
  }

  @Override
  public MapCell getCheckPoint() {
    return currentCheckPoint;
  }

  @Override
  public void nextCheckPoint() {
    ++checkPointCount;
    checkPointCount &= 3;
    updateCurrentCheckPoint();
  }

  private void updateCurrentCheckPoint() {
    currentCheckPoint = corners[checkPointCount];
  }

  private MapCell findMyCorner() {
    ArrayList<TrooperModel> troopers = environment.getMyTroopers();
    return new MapCell(environment.getWorld(), troopers.get(0).getX(), troopers.get(0).getY());
  }

  private MapCell findCenter() {
    for (int i = environment.getWorld().getWidth() >> 1; i >= 0; --i)
      for (int j = environment.getWorld().getHeight() >> 1; j >= 0; --j)
        if (environment.getCellChecker().cellIsFree(i, j) && environment.getBattleMap().getDistance(myCorner.getX(), myCorner.getY(), i, j) != 0)
          return new MapCell(environment.getWorld(), i, j);

    assert false;
    return null;
  }


  @Override
  public boolean checkPointWasReached() {
    final int checkPointX = currentCheckPoint.getX();
    final int checkPointY = currentCheckPoint.getY();
    boolean oneHasReached = false;
    int maxDistance = 0;
    for (TrooperModel trooper : environment.getMyTroopers()) {
      if (environment.getCellChecker().cellsAreTheSame(trooper.getX(), trooper.getY(), checkPointX, checkPointY))
        oneHasReached = true;
      maxDistance = Math.max(maxDistance, environment.getBattleMap().getDistance(trooper.getX(), trooper.getY(), checkPointX, checkPointY));
    }
    return oneHasReached && maxDistance < MAX_DIST_CHECKPOINT;
  }
}

