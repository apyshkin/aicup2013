import model.TrooperStance;
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
  public CellPriorities generateCellPriorities(TrooperModel trooper) {
    World world = environment.getWorld();
    int[][] priorities = new int[world.getWidth()][world.getHeight()];
    int maxPriority = 0;
    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j) {
        int count = 0;
        for (int k = 0; k < world.getWidth(); ++k)
          for (int l = 0; l < world.getHeight(); ++l)
            if (world.isVisible(trooper.getVisionRange(), i, j, TrooperStance.STANDING, k, l, TrooperStance.STANDING)) {
              priorities[i][j] += CellFunctions.ExploringFunction(environment.getCellNotVisitTime(k, l));
              ++count;
            }
        priorities[i][j] /= count;
        maxPriority = Math.max(maxPriority, priorities[i][j]);
      }
    for (int i = 0; i < world.getWidth(); ++i)
      for (int j = 0; j < world.getHeight(); ++j) {
        priorities[i][j] *= 3. / maxPriority;
        ++priorities[i][j];
        priorities[i][j] *= Math.sqrt(trooper.getDistanceTo(i, j));
      }

//    for (int i = 0; i < world.getHeight(); ++i) {
//      for (int j = 0; j < world.getWidth(); ++j)
//        System.out.print(priorities[j][i] + " ");
//      System.out.println();
//    }

    return new CellPriorities(environment, priorities);
  }
}

class CellFunctions {
  public static int ExploringFunction(int cellNotVisitTime) {
    return cellNotVisitTime;
  }

  public static int GlueTogetherFunction(TrooperType myTrooperType, int maxDistance) {

    int points = 0;
    if (maxDistance > 8)
      points = 20 - (maxDistance);
    else if (maxDistance >= 5)
      points = 30;
    else if (maxDistance < 2)
      points = 25;

    if (myTrooperType == TrooperType.COMMANDER)
      points /= 2;
    else if (myTrooperType == TrooperType.FIELD_MEDIC)
      points *= 2;

    return points;
  }
}
