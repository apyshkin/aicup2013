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

  public PatrolTactics(Environment environment) {
    this.environment = environment;
  }
  @Override
  public void setAction(ITrooperStrategy trooper) {
    trooper.setActionUnderTactics(this);
  }

  @Override
  public CellPriorities generateCellPriorities() {
    World world = environment.getWorld();
    int[][] priorities = new int[world.getWidth()][world.getHeight()];
    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j)
        priorities[i][j] = CellFunctions.ExploringFunction(environment.getCellNotVisitTime(i, j));
//                            CellFunctions.FarFromCenter(i, j, world.getWidth(), world.getHeight());

    return new CellPriorities(environment, priorities);
  }
}

class CellFunctions {
  public static int FarFromCenter(int i, int j, int width, int height) {
    return Math.abs(i - (width / 2)) + Math.abs(j - (height / 2));
  }

  public static int ExploringFunction(int cellNotVisitTime) {
    if (cellNotVisitTime >= 40)
      return 20;
    return 1;
  }

  public static int GlueTogetherFunction(TrooperType myTrooperType, TrooperModel[] myTroopers) {
    double maxDistance = 0;
    for (TrooperModel trooper1 : myTroopers)
      for (TrooperModel trooper2 : myTroopers) {
        maxDistance = Math.max(maxDistance, trooper1.getDistanceTo(trooper2));
      }

    int points = 0;
    if (maxDistance > 6)
      points = 0;
    else if (maxDistance >= 3)
      points = 30;
    else if (maxDistance < 3)
      points = 15;

    if (myTrooperType == TrooperType.COMMANDER)
      points /= 1.5;

    return points;
  }
}
